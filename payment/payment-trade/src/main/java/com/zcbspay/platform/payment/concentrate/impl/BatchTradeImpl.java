package com.zcbspay.platform.payment.concentrate.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.zcbspay.platform.channel.simulation.enums.InsteadPayTagsEnum;
import com.zcbspay.platform.channel.simulation.enums.WithholdingTagsEnum;
import com.zcbspay.platform.channel.simulation.interfaces.Producer;
import com.zcbspay.platform.payment.bean.ResultBean;
import com.zcbspay.platform.payment.bean.TradeBean;
import com.zcbspay.platform.payment.concentrate.BatchTrade;
import com.zcbspay.platform.payment.dao.OrderCollectBatchDAO;
import com.zcbspay.platform.payment.dao.OrderCollectDetaDAO;
import com.zcbspay.platform.payment.dao.OrderPaymentBatchDAO;
import com.zcbspay.platform.payment.dao.OrderPaymentDetaDAO;
import com.zcbspay.platform.payment.dao.TxnsLogDAO;
import com.zcbspay.platform.payment.enums.OrderStatusEnum;
import com.zcbspay.platform.payment.exception.ConcentrateTradeException;
import com.zcbspay.platform.payment.fee.bean.FeeBean;
import com.zcbspay.platform.payment.fee.exception.TradeFeeException;
import com.zcbspay.platform.payment.fee.service.TradeFeeService;
import com.zcbspay.platform.payment.pojo.OrderCollectBatchDO;
import com.zcbspay.platform.payment.pojo.OrderCollectDetaDO;
import com.zcbspay.platform.payment.pojo.OrderPaymentBatchDO;
import com.zcbspay.platform.payment.pojo.OrderPaymentDetaDO;
import com.zcbspay.platform.payment.pojo.PojoTxnsLog;
import com.zcbspay.platform.payment.risk.bean.RiskBean;
import com.zcbspay.platform.payment.risk.exception.TradeRiskException;
import com.zcbspay.platform.payment.risk.service.TradeRiskControlService;

@Service("batchTrade")
public class BatchTradeImpl implements BatchTrade {
	private static final Logger logger = LoggerFactory.getLogger(BatchTradeImpl.class);
	@Autowired
	private OrderCollectBatchDAO orderCollectBatchDAO;
	@Autowired
	private OrderCollectDetaDAO orderCollectDetaDAO;
	@Autowired
	private OrderPaymentBatchDAO orderPaymentBatchDAO;
	@Autowired
	private TxnsLogDAO txnsLogDAO;
	@Autowired
	private OrderPaymentDetaDAO orderPaymentDetaDAO;
	@Autowired
	@Qualifier("cmbcInsteadPayProducer")
	private Producer producer_cmbc_instead_pay;
	@Autowired
	@Qualifier("cmbcWithholdingProducer")
	private Producer producer_cmbc_withhold;
	@Reference(version="1.0")
	private TradeRiskControlService tradeRiskControlService;
	@Reference(version="1.0")
	private TradeFeeService tradeFeeService;
	
	@Override
	public ResultBean collectionCharges(String tn) throws ConcentrateTradeException {
		ResultBean resultBean = null;
		OrderCollectBatchDO collectBatchOrder = orderCollectBatchDAO.getCollectBatchOrderByTn(tn);
		if(collectBatchOrder==null){//订单不存在
			throw new ConcentrateTradeException("PC015");
		}
		if("00".equals(collectBatchOrder.getStatus())){//订单支付中成功
			throw new ConcentrateTradeException("PC022");
		}
		if("02".equals(collectBatchOrder.getStatus())){//订单支付中
			throw new ConcentrateTradeException("PC016");
		}
		if("04".equals(collectBatchOrder.getStatus())){//订单过期
			throw new ConcentrateTradeException("PC017");
		}
		orderCollectBatchDAO.updateOrderToStartPay(tn);
		List<OrderCollectDetaDO> collectDetaList = orderCollectDetaDAO.getDetaListByBatchtid(collectBatchOrder.getTid());
		for(OrderCollectDetaDO deta :  collectDetaList){
			PojoTxnsLog txnsLog = txnsLogDAO.getTxnsLogByTxnseqno(deta.getRelatetradetxn());
			try {
				//风控
				RiskBean riskBean = new RiskBean();
				riskBean.setBusiCode(txnsLog.getBusicode());
				riskBean.setCardNo(txnsLog.getPan());
				riskBean.setCardType("");
				riskBean.setCoopInstId(txnsLog.getAccfirmerno());
				riskBean.setMemberId(txnsLog.getAccmemberid());
				riskBean.setMerchId(txnsLog.getAccsecmerno());
				riskBean.setTxnAmt(txnsLog.getAmount()+"");
				riskBean.setTxnseqno(txnsLog.getTxnseqno());
				tradeRiskControlService.realTimeTradeRiskControl(riskBean);
			} catch (TradeRiskException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				//throw new ConcentrateTradeException("PC012");
				deta.setStatus(OrderStatusEnum.FAILED.value());
				deta.setRespcode("0040");
				deta.setRespmsg("交易有风险,交易被风控系统拒绝！");
				orderCollectDetaDAO.updateCollectOrderDeta(deta);
			}
			//计费
			//计算交易手续费
			try {
				FeeBean feeBean = new FeeBean();
				feeBean.setBusiCode(txnsLog.getBusicode());
				feeBean.setFeeVer(txnsLog.getFeever());
				feeBean.setTxnAmt(txnsLog.getAmount()+"");
				feeBean.setMerchNo(txnsLog.getAccsecmerno());
				feeBean.setCardType("1");
				feeBean.setTxnseqnoOg("");
				feeBean.setTxnseqno(txnsLog.getTxnseqno());
				long fee = tradeFeeService.getCommonFee(feeBean);
				txnsLogDAO.updateTradeFee(txnsLog.getTxnseqno(), fee);
			} catch (TradeFeeException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				//throw new ConcentrateTradeException("PC028");
				txnsLogDAO.updateTradeFee(txnsLog.getTxnseqno(), 0);
			}
		}
		TradeBean tradeBean = new TradeBean();
		tradeBean.setTn(tn);
		try {
			sendTradeMsgToCollection(tradeBean);
			
			resultBean = new ResultBean("交易已受理,请稍后查询");
		} catch (MQClientException | RemotingException | InterruptedException
				| MQBrokerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		
		
		return resultBean;
		
	}

	@Override
	public ResultBean paymentByAgency(String tn) throws ConcentrateTradeException {
		ResultBean resultBean = null;
		OrderPaymentBatchDO orderPaymentBatch = orderPaymentBatchDAO.getPaymentBatchOrderByTn(tn);
		if(orderPaymentBatch==null){//订单不存在
			throw new ConcentrateTradeException("PC015");
		}
		if("00".equals(orderPaymentBatch.getStatus())){//订单支付中成功
			throw new ConcentrateTradeException("PC022");
		}
		if("02".equals(orderPaymentBatch.getStatus())){//订单支付中
			throw new ConcentrateTradeException("PC016");
		}
		if("04".equals(orderPaymentBatch.getStatus())){//订单过期
			throw new ConcentrateTradeException("PC017");
		}
		List<OrderPaymentDetaDO> paymentDetaList = orderPaymentDetaDAO.getDetaListByBatchtid(orderPaymentBatch.getTid());
		for(OrderPaymentDetaDO deta :  paymentDetaList){
			PojoTxnsLog txnsLog = txnsLogDAO.getTxnsLogByTxnseqno(deta.getRelatetradetxn());
			try {
				//风控
				RiskBean riskBean = new RiskBean();
				riskBean.setBusiCode(txnsLog.getBusicode());
				riskBean.setCardNo(txnsLog.getPan());
				riskBean.setCardType("");
				riskBean.setCoopInstId(txnsLog.getAccfirmerno());
				riskBean.setMemberId(txnsLog.getAccmemberid());
				riskBean.setMerchId(txnsLog.getAccsecmerno());
				riskBean.setTxnAmt(txnsLog.getAmount()+"");
				riskBean.setTxnseqno(txnsLog.getTxnseqno());
				tradeRiskControlService.realTimeTradeRiskControl(riskBean);
			} catch (TradeRiskException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				//throw new ConcentrateTradeException("PC012");
				deta.setStatus(OrderStatusEnum.FAILED.value());
				deta.setRespcode("0040");
				deta.setRespmsg("交易有风险,交易被风控系统拒绝！");
				orderPaymentDetaDAO.updatePaymenyOrderDeta(deta);
			}
			//计费
			//计算交易手续费
			try {
				FeeBean feeBean = new FeeBean();
				feeBean.setBusiCode(txnsLog.getBusicode());
				feeBean.setFeeVer(txnsLog.getFeever());
				feeBean.setTxnAmt(txnsLog.getAmount()+"");
				feeBean.setMerchNo(txnsLog.getAccsecmerno());
				feeBean.setCardType("1");
				feeBean.setTxnseqnoOg("");
				feeBean.setTxnseqno(txnsLog.getTxnseqno());
				long fee = tradeFeeService.getCommonFee(feeBean);
				txnsLogDAO.updateTradeFee(txnsLog.getTxnseqno(), fee);
			} catch (TradeFeeException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				//throw new ConcentrateTradeException("PC028");
				txnsLogDAO.updateTradeFee(txnsLog.getTxnseqno(), 0);
			}
		}
		orderPaymentBatchDAO.updateOrderToStartPay(tn);
		TradeBean tradeBean = new TradeBean();
		tradeBean.setTn(tn);
		try {
			sendTradeMsgToPayment(tradeBean);
			resultBean = new ResultBean("交易已受理,请稍后查询");
		} catch (MQClientException | RemotingException | InterruptedException
				| MQBrokerException  e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return resultBean;
	}

	private void sendTradeMsgToPayment(TradeBean tradeBean) throws MQClientException, RemotingException, InterruptedException, MQBrokerException{
		SendResult sendResult = producer_cmbc_instead_pay.sendJsonMessage(JSON.toJSONString(tradeBean),InsteadPayTagsEnum.BATCH_PAYMENT_CONCENTRATE);
	}
	private void sendTradeMsgToCollection(TradeBean tradeBean) throws MQClientException, RemotingException, InterruptedException, MQBrokerException{
		SendResult sendResult = producer_cmbc_withhold.sendJsonMessage(JSON.toJSONString(tradeBean),WithholdingTagsEnum.BATCH_COLLECTION_CONCENTRATE);
		
		
	}

}
