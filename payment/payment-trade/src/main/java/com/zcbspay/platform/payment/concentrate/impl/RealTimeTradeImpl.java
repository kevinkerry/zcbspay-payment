package com.zcbspay.platform.payment.concentrate.impl;

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
import com.zcbspay.platform.channel.simulation.interfaces.Producer;
import com.zcbspay.platform.payment.bean.ResultBean;
import com.zcbspay.platform.payment.bean.TradeBean;
import com.zcbspay.platform.payment.commons.utils.BeanCopyUtil;
import com.zcbspay.platform.payment.concentrate.RealTimeTrade;
import com.zcbspay.platform.payment.dao.OrderCollectSingleDAO;
import com.zcbspay.platform.payment.dao.OrderPaymentSingleDAO;
import com.zcbspay.platform.payment.dao.TxnsLogDAO;
import com.zcbspay.platform.payment.enums.TradeStatFlagEnum;
import com.zcbspay.platform.payment.exception.ConcentrateTradeException;
import com.zcbspay.platform.payment.exception.PaymentQuickPayException;
import com.zcbspay.platform.payment.pojo.OrderCollectSingleDO;
import com.zcbspay.platform.payment.pojo.OrderPaymentSingleDO;
import com.zcbspay.platform.payment.pojo.PojoTxnsLog;
import com.zcbspay.platform.payment.risk.bean.RiskBean;
import com.zcbspay.platform.payment.risk.exception.TradeRiskException;
import com.zcbspay.platform.payment.risk.service.TradeRiskControlService;

@Service
public class RealTimeTradeImpl implements RealTimeTrade {
	private static final Logger logger = LoggerFactory.getLogger(RealTimeTradeImpl.class);
	@Autowired
	private OrderCollectSingleDAO orderCollectSingleDAO;
	@Autowired
	private OrderPaymentSingleDAO orderPaymentSingleDAO;
	@Autowired
	private TxnsLogDAO txnsLogDAO;
	@Autowired
	@Qualifier("cmbcInsteadPayProducer")
	private Producer producer_cmbc_instead_pay;
	@Reference(version="1.0")
	private TradeRiskControlService tradeRiskControlService;
	@Override
	public ResultBean collectionCharges(String tn) throws ConcentrateTradeException {
		ResultBean resultBean = null;
		OrderCollectSingleDO orderinfo = orderCollectSingleDAO.getOrderinfoByTn(tn);
		if(orderinfo==null){//订单不存在
			throw new ConcentrateTradeException("PC015");
		}
		if("00".equals(orderinfo.getStatus())){//订单支付中成功
			throw new ConcentrateTradeException("PC022");
		}
		if("02".equals(orderinfo.getStatus())){//订单支付中
			throw new ConcentrateTradeException("PC016");
		}
		if("04".equals(orderinfo.getStatus())){//订单过期
			throw new ConcentrateTradeException("PC017");
		}
		PojoTxnsLog txnsLog = txnsLogDAO.getTxnsLogByTxnseqno(orderinfo.getRelatetradetxn());
		if(txnsLog==null){
			throw new ConcentrateTradeException("PC008");
		}
		try {
			RiskBean riskBean = new RiskBean();
			riskBean.setBusiCode(txnsLog.getBusicode());
			riskBean.setCardNo(orderinfo.getDebtoraccount());
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
			throw new ConcentrateTradeException("PC012");
			
		}
		txnsLogDAO.initretMsg(txnsLog.getTxnseqno());
		orderCollectSingleDAO.updateOrderToStartPay(txnsLog.getTxnseqno());
		txnsLogDAO.updateTradeStatFlag(txnsLog.getTxnseqno(), TradeStatFlagEnum.READY);
		
		TradeBean tradeBean = new TradeBean();
		
		tradeBean.setTxnseqno(txnsLog.getTxnseqno());
		
		com.zcbspay.platform.channel.simulation.bean.ResultBean sendTradeMsgToCMBC;
		try {
			sendTradeMsgToCMBC = sendTradeMsgToCMBC(tradeBean);
			if(sendTradeMsgToCMBC==null){
				throw new PaymentQuickPayException("PC019");
			}
			resultBean = BeanCopyUtil.copyBean(ResultBean.class, sendTradeMsgToCMBC);
		} catch (MQClientException | RemotingException | InterruptedException
				| MQBrokerException | PaymentQuickPayException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		
		
		return resultBean;
	}
	
	private com.zcbspay.platform.channel.simulation.bean.ResultBean sendTradeMsgToCMBC(TradeBean tradeBean) throws MQClientException, RemotingException, InterruptedException, MQBrokerException{
		//Producer producer = new InsteadPayProducer(ResourceBundle.getBundle("producer_cmbc").getString("single.namesrv.addr"), InsteadPayTagsEnum.INSTEADPAY_REALTIME);
		SendResult sendResult = producer_cmbc_instead_pay.sendJsonMessage(JSON.toJSONString(tradeBean),InsteadPayTagsEnum.INSTEADPAY_REALTIME);
		com.zcbspay.platform.channel.simulation.bean.ResultBean queryReturnResult = producer_cmbc_instead_pay.queryReturnResult(sendResult);
		System.out.println(JSON.toJSONString(queryReturnResult));
		//producer.closeProducer();
		return queryReturnResult;
	}

	@Override
	public ResultBean paymentByAgency(String tn) throws ConcentrateTradeException {
		OrderPaymentSingleDO orderinfo = orderPaymentSingleDAO.getOrderinfoByTn(tn);
		
		if(orderinfo==null){//订单不存在
			throw new ConcentrateTradeException("PC015");
		}
		if("00".equals(orderinfo.getStatus())){//订单支付中成功
			throw new ConcentrateTradeException("PC022");
		}
		if("02".equals(orderinfo.getStatus())){//订单支付中
			throw new ConcentrateTradeException("PC016");
		}
		if("04".equals(orderinfo.getStatus())){//订单过期
			throw new ConcentrateTradeException("PC017");
		}
		PojoTxnsLog txnsLog = txnsLogDAO.getTxnsLogByTxnseqno(orderinfo.getRelatetradetxn());
		if(txnsLog==null){
			throw new ConcentrateTradeException("PC008");
		}
		try {
			RiskBean riskBean = new RiskBean();
			riskBean.setBusiCode(txnsLog.getBusicode());
			riskBean.setCardNo(orderinfo.getDebtoraccount());
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
			throw new ConcentrateTradeException("PC012");
			
		}
		txnsLogDAO.initretMsg(txnsLog.getTxnseqno());
		orderPaymentSingleDAO.updateOrderToStartPay(txnsLog.getTxnseqno());
		txnsLogDAO.updateTradeStatFlag(txnsLog.getTxnseqno(), TradeStatFlagEnum.READY);
		return null;
	}

	 
	

}
