/* 
 * RealTimeInsteadPayServiceImpl.java  
 * 
 * version TODO
 *
 * 2016年10月21日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zcbspay.platform.payment.quickpay.service.impl;

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
import com.zcbspay.platform.payment.bean.InsteadPayTradeBean;
import com.zcbspay.platform.payment.bean.ResultBean;
import com.zcbspay.platform.payment.commons.utils.BeanCopyUtil;
import com.zcbspay.platform.payment.commons.utils.DateUtil;
import com.zcbspay.platform.payment.dao.InsteadPayRealtimeDAO;
import com.zcbspay.platform.payment.dao.TxnsLogDAO;
import com.zcbspay.platform.payment.enums.ChannelEnmu;
import com.zcbspay.platform.payment.enums.TradeStatFlagEnum;
import com.zcbspay.platform.payment.exception.PaymentInsteadPayException;
import com.zcbspay.platform.payment.exception.PaymentQuickPayException;
import com.zcbspay.platform.payment.exception.PaymentRouterException;
import com.zcbspay.platform.payment.fee.service.TradeFeeService;
import com.zcbspay.platform.payment.pojo.PojoInsteadPayRealtime;
import com.zcbspay.platform.payment.pojo.PojoTxnsLog;
import com.zcbspay.platform.payment.quickpay.bean.InsteadPayOrderBean;
import com.zcbspay.platform.payment.quickpay.service.RealTimeInsteadPayService;
import com.zcbspay.platform.payment.risk.bean.RiskBean;
import com.zcbspay.platform.payment.risk.exception.TradeRiskException;
import com.zcbspay.platform.payment.risk.service.TradeRiskControlService;
import com.zcbspay.platform.payment.router.service.RouteConfigService;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年10月21日 上午10:58:52
 * @since 
 */
@Service("realTimeInsteadPayService")
public class RealTimeInsteadPayServiceImpl implements RealTimeInsteadPayService {

	private static final Logger logger = LoggerFactory.getLogger(RealTimeInsteadPayServiceImpl.class);
	/*@Autowired
	private OrderService orderService;*/
	@Autowired
	private InsteadPayRealtimeDAO insteadPayRealtimeDAO;
	@Autowired
	private TxnsLogDAO txnsLogDAO;
	@Autowired
	private RouteConfigService routeConfigService;
	@Autowired
	@Qualifier("cmbcInsteadPayProducer")
	private Producer producer_cmbc_instead_pay;
	@Reference(version="1.0")
	private TradeRiskControlService tradeRiskControlService;
	@Reference(version="1.0")
	private TradeFeeService tradeFeeService;
	/**
	 *
	 * @param insteadPayOrderBean
	 * @return
	 * @throws PaymentInsteadPayException 
	 * @throws PaymentQuickPayException 
	 * @throws PaymentRouterException 
	 */
	@Override
	public ResultBean singleInsteadPay(InsteadPayOrderBean insteadPayOrderBean) throws PaymentInsteadPayException, PaymentQuickPayException, PaymentRouterException {
		ResultBean resultBean = null;
		String tn = insteadPayOrderBean.getTn();//orderService.createInsteadPayOrder(insteadPayOrderBean);
		PojoInsteadPayRealtime orderinfo = insteadPayRealtimeDAO.queryOrderByTN(tn);
		if(orderinfo==null){//订单不存在
			throw new PaymentInsteadPayException("PC015");
		}
		if("00".equals(orderinfo.getStatus())){//订单支付中成功
			throw new PaymentQuickPayException("PC022");
		}
		if("02".equals(orderinfo.getStatus())){//订单支付中
			throw new PaymentQuickPayException("PC016");
		}
		if("04".equals(orderinfo.getStatus())){//订单过期
			throw new PaymentQuickPayException("PC017");
		}
		if(!insteadPayOrderBean.getTxnAmt().equals(orderinfo.getTransAmt().toString())){
			throw new PaymentInsteadPayException("PC018");
		}
		PojoTxnsLog txnsLog = txnsLogDAO.getTxnsLogByTxnseqno(orderinfo.getTxnseqno());
		if(txnsLog==null){
			throw new PaymentInsteadPayException("PC008");
		}
		String channelCode = routeConfigService.getTradeChannel(DateUtil.getCurrentDateTime(), orderinfo.getTransAmt().toString(), orderinfo.getMerId(), txnsLog.getBusicode(), txnsLog.getPan(), txnsLog.getRoutver());
		try {
			RiskBean riskBean = new RiskBean();
			riskBean.setBusiCode(txnsLog.getBusicode());
			riskBean.setCardNo(insteadPayOrderBean.getAccNo());
			riskBean.setCardType(insteadPayOrderBean.getAccType());
			riskBean.setCoopInstId(txnsLog.getAccfirmerno());
			riskBean.setMemberId(txnsLog.getAccmemberid());
			riskBean.setMerchId(txnsLog.getAccsecmerno());
			riskBean.setTxnAmt(txnsLog.getAmount()+"");
			riskBean.setTxnseqno(txnsLog.getTxnseqno());
			tradeRiskControlService.realTimeTradeRiskControl(riskBean);
		} catch (TradeRiskException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw new PaymentRouterException("PC012");
			
		}
		txnsLogDAO.initretMsg(txnsLog.getTxnseqno());
		insteadPayRealtimeDAO.updateOrderToStartPay(txnsLog.getTxnseqno());
		txnsLogDAO.updateTradeStatFlag(txnsLog.getTxnseqno(), TradeStatFlagEnum.READY);
		try {
			InsteadPayTradeBean tradeBean = new InsteadPayTradeBean();
			tradeBean.setAcc_no(txnsLog.getPan());
			tradeBean.setAcc_name(txnsLog.getPanName());
			tradeBean.setTrans_amt(txnsLog.getAmount().toString());
			tradeBean.setRemark(orderinfo.getRemark());
			tradeBean.setTxnseqno(txnsLog.getTxnseqno());
			if(ChannelEnmu.fromValue(channelCode)==ChannelEnmu.CMBCINSTEADPAY_REALTIME){
				com.zcbspay.platform.channel.simulation.bean.ResultBean sendTradeMsgToCMBC = sendTradeMsgToCMBC(tradeBean);
				if(sendTradeMsgToCMBC==null){
					throw new PaymentQuickPayException("PC019");
				}
				resultBean = BeanCopyUtil.copyBean(ResultBean.class, sendTradeMsgToCMBC);
			}
			
		} catch (MQClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getErrorMessage());
			//throw new PaymentOrderException("PC013");
		} catch (RemotingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage());
			//throw new PaymentOrderException("PC013");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage());
			//throw new PaymentOrderException("PC013");
		} catch (MQBrokerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage());
			//throw new PaymentOrderException("PC013");
		}
		if(resultBean.isResultBool()){
			return new ResultBean(tn);
		}
		return resultBean;
	}

	private com.zcbspay.platform.channel.simulation.bean.ResultBean sendTradeMsgToCMBC(InsteadPayTradeBean tradeBean) throws MQClientException, RemotingException, InterruptedException, MQBrokerException{
		//Producer producer = new InsteadPayProducer(ResourceBundle.getBundle("producer_cmbc").getString("single.namesrv.addr"), InsteadPayTagsEnum.INSTEADPAY_REALTIME);
		SendResult sendResult = producer_cmbc_instead_pay.sendJsonMessage(JSON.toJSONString(tradeBean),InsteadPayTagsEnum.INSTEADPAY_REALTIME);
		com.zcbspay.platform.channel.simulation.bean.ResultBean queryReturnResult = producer_cmbc_instead_pay.queryReturnResult(sendResult);
		System.out.println(JSON.toJSONString(queryReturnResult));
		//producer.closeProducer();
		return queryReturnResult;
	}
}
