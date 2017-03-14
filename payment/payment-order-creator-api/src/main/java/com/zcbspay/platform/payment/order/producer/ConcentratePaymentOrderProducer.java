package com.zcbspay.platform.payment.order.producer;

import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.google.common.base.Charsets;
import com.zcbspay.platform.payment.order.producer.bean.ResultBean;
import com.zcbspay.platform.payment.order.producer.enums.OrderTagsEnum;
import com.zcbspay.platform.payment.order.producer.interfaces.Producer;
import com.zcbspay.platform.payment.order.producer.redis.RedisFactory;

public class ConcentratePaymentOrderProducer implements Producer{
	private final static Logger logger = LoggerFactory.getLogger(ConcentratePaymentOrderProducer.class);
	private static final String KEY = "CONCENTRATEPAYMENTORDER:";
	private static final  ResourceBundle RESOURCE = ResourceBundle.getBundle("producer_order");
	//RocketMQ消费者客户端
	private DefaultMQProducer producer;
	//主题
	private String topic;
	
	public ConcentratePaymentOrderProducer(String namesrvAddr) throws MQClientException{
		logger.info("【初始化ConcentratePaymentOrderProducer】");
		logger.info("【namesrvAddr】"+namesrvAddr);          
		producer = new DefaultMQProducer(RESOURCE.getString("concentrate.payment.order.consumer.group"));
		producer.setNamesrvAddr(namesrvAddr);
		Random random = new Random();
        producer.setInstanceName(RESOURCE.getString("concentrate.payment.order.instancename")+random.nextInt(9999));
        topic = RESOURCE.getString("concentrate.payment.order.subscribe");
        logger.info("【初始化ConcentratePaymentOrderProducer结束】");
	}
	
	@Override
	public SendResult sendJsonMessage(String message, OrderTagsEnum tags)
			throws MQClientException, RemotingException, InterruptedException,
			MQBrokerException {
		if(producer==null){
			throw new MQClientException(-1,"ConcentratePaymentOrderProducer为空");
		}
		producer.start();
		Message msg = new Message(topic, tags.getCode(), message.getBytes(Charsets.UTF_8));
		SendResult sendResult = producer.send(msg);
		return sendResult;
	}

	@Override
	public void closeProducer() {
		producer.shutdown();
		producer = null;
		
	}

	@Override
	public ResultBean queryReturnResult(SendResult sendResult) {
		logger.info("【ConcentrateCollectionOrderProducer receive Result message】{}",JSON.toJSONString(sendResult));
		logger.info("msgID:{}",sendResult.getMsgId());
		String tn = getTnByCycle(sendResult.getMsgId());
		logger.info("从redis中取得key【{}】值为{}",KEY+sendResult.getMsgId(),tn);
		if(StringUtils.isNotEmpty(tn)){
			ResultBean resultBean = JSON.parseObject(tn, ResultBean.class);
			
			logger.info("msgID:{},结果数据:{}",sendResult.getMsgId(),JSON.toJSONString(resultBean));
			return resultBean;
		}else{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		logger.info("end time {}",System.currentTimeMillis());
		return null;
	}
	private String getTnByCycle(String msgId){
		Jedis jedis = RedisFactory.getInstance().getRedis();
		List<String> brpop = jedis.brpop(40000, KEY+msgId);
		String tn = brpop.get(1);
		jedis.close();
		if(StringUtils.isNotEmpty(tn)){
			return tn;
		}
		return null;
	}

}
