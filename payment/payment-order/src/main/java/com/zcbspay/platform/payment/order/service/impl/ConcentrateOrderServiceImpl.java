package com.zcbspay.platform.payment.order.service.impl;

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
import com.zcbspay.platform.payment.exception.PaymentOrderException;
import com.zcbspay.platform.payment.order.bean.ConcentrateBatchOrderBean;
import com.zcbspay.platform.payment.order.bean.RealTimeCollectionChargesBean;
import com.zcbspay.platform.payment.order.bean.RealTimepaymentByAgencyBean;
import com.zcbspay.platform.payment.order.producer.bean.ResultBean;
import com.zcbspay.platform.payment.order.producer.enums.OrderTagsEnum;
import com.zcbspay.platform.payment.order.producer.interfaces.Producer;
import com.zcbspay.platform.payment.order.service.ConcentrateOrderService;

@Service
public class ConcentrateOrderServiceImpl implements ConcentrateOrderService {

	private static final Logger logger = LoggerFactory.getLogger(ConcentrateOrderServiceImpl.class);
	@Autowired
	@Qualifier("concentrateCollectionOrderProducer")
	private Producer concentrateCollectionOrderProducer;
	@Autowired
	@Qualifier("concentratePaymentOrderProducer")
	private Producer concentratePaymentOrderProducer;
	@Override
	public String createCollectionChargesOrder(
			RealTimeCollectionChargesBean realTimeCollectionChargesBean) throws PaymentOrderException {
		try {
			SendResult sendResult = concentrateCollectionOrderProducer.sendJsonMessage(JSON.toJSONString(realTimeCollectionChargesBean), OrderTagsEnum.CONCENTRATE_COLLECTION_CHARGES_REALTIME);
			ResultBean resultBean = concentrateCollectionOrderProducer.queryReturnResult(sendResult);
			if(resultBean.isResultBool()){
				return resultBean.getResultObj().toString();
			}else{
				throw new PaymentOrderException("PC027",resultBean.getErrMsg());
			}
		} catch (MQClientException | RemotingException | InterruptedException
				| MQBrokerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new PaymentOrderException("PC013");
		}
	}

	@Override
	public String createPaymentByAgencyOrder(
			RealTimepaymentByAgencyBean realTimepaymentByAgencyBean) throws PaymentOrderException {
		try {
			SendResult sendResult = concentratePaymentOrderProducer.sendJsonMessage(JSON.toJSONString(realTimepaymentByAgencyBean), OrderTagsEnum.CONCENTRATE_PAYMENT_REALTIME);
			ResultBean resultBean = concentratePaymentOrderProducer.queryReturnResult(sendResult);
			if(resultBean.isResultBool()){
				return resultBean.getResultObj().toString();
			}else{
				throw new PaymentOrderException("PC027",resultBean.getErrMsg());
			}
		} catch (MQClientException | RemotingException | InterruptedException
				| MQBrokerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new PaymentOrderException("PC013");
		}
	}

	@Override
	public String createCollectionChargesBatchOrder(
			ConcentrateBatchOrderBean concentrateBatchOrderBean)
			throws PaymentOrderException {
		try {
			SendResult sendResult = concentrateCollectionOrderProducer.sendJsonMessage(JSON.toJSONString(concentrateBatchOrderBean), OrderTagsEnum.CONCENTRATE_COLLECTION_CHARGES_BATCH);
			ResultBean resultBean = concentrateCollectionOrderProducer.queryReturnResult(sendResult);
			if(resultBean.isResultBool()){
				return resultBean.getResultObj().toString();
			}else{
				throw new PaymentOrderException("PC027",resultBean.getErrMsg());
			}
		} catch (MQClientException | RemotingException | InterruptedException
				| MQBrokerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new PaymentOrderException("PC013");
		}
	}

	@Override
	public String createPaymentByAgencyBatchOrder(
			ConcentrateBatchOrderBean concentrateBatchOrderBean)
			throws PaymentOrderException {
		try {
			SendResult sendResult = concentratePaymentOrderProducer.sendJsonMessage(JSON.toJSONString(concentrateBatchOrderBean), OrderTagsEnum.CONCENTRATE_PAYMENT_BATCH);
			ResultBean resultBean = concentratePaymentOrderProducer.queryReturnResult(sendResult);
			if(resultBean.isResultBool()){
				return resultBean.getResultObj().toString();
			}else{
				throw new PaymentOrderException("PC027",resultBean.getErrMsg());
			}
		} catch (MQClientException | RemotingException | InterruptedException
				| MQBrokerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new PaymentOrderException("PC013");
		}
	}

}
