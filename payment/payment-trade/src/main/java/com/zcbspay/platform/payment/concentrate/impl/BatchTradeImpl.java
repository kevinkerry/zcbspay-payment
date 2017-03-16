package com.zcbspay.platform.payment.concentrate.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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
import com.zcbspay.platform.payment.dao.OrderPaymentBatchDAO;
import com.zcbspay.platform.payment.exception.ConcentrateTradeException;
import com.zcbspay.platform.payment.pojo.OrderCollectBatchDO;
import com.zcbspay.platform.payment.pojo.OrderPaymentBatchDO;

@Service("batchTrade")
public class BatchTradeImpl implements BatchTrade {
	private static final Logger logger = LoggerFactory.getLogger(BatchTradeImpl.class);
	@Autowired
	private OrderCollectBatchDAO orderCollectBatchDAO;
	@Autowired
	private OrderPaymentBatchDAO orderPaymentBatchDAO;
	@Autowired
	@Qualifier("cmbcInsteadPayProducer")
	private Producer producer_cmbc_instead_pay;
	@Autowired
	@Qualifier("cmbcWithholdingProducer")
	private Producer producer_cmbc_withhold;
	
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
