package com.zcbspay.platform.payment.order.consumer.listener;

import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.google.common.base.Charsets;
import com.zcbspay.platform.payment.bean.ResultBean;
import com.zcbspay.platform.payment.order.consume.bean.ConcentrateBatchOrderBean;
import com.zcbspay.platform.payment.order.consume.bean.ConcentrateSingleOrderBean;
import com.zcbspay.platform.payment.order.consumer.enums.OrderTagsEnum;
import com.zcbspay.platform.payment.order.service.OrderCacheResultService;
import com.zcbspay.platform.payment.order.service.concentrate.ConcentrateOrderService;

/**
 * 集中代付监听器
 *
 * @author guojia
 * @version
 * @date 2017年3月13日 上午11:30:53
 * @since
 */
@Service("concentrateCollectionLinstener")
public class ConcentrateCollectionLinstener implements MessageListenerConcurrently{
	private static final Logger log = LoggerFactory.getLogger(InsteadPayOrderListener.class);
	private static final ResourceBundle RESOURCE = ResourceBundle.getBundle("consumer_order");
	private static final String KEY = "CONCENTRATECOLLECTIONORDER:";
	
	@Autowired
	private OrderCacheResultService orderCacheResultService;
	@Autowired
	private ConcentrateOrderService concentrateOrderService;
	
	@Override
	public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
			ConsumeConcurrentlyContext context) {
		for (MessageExt msg : msgs) {
			if (msg.getTopic().equals(RESOURCE.getString("concentrate.collection.order.subscribe"))) {
				OrderTagsEnum orderTagsEnum = OrderTagsEnum.fromValue(msg.getTags());
				if(orderTagsEnum==OrderTagsEnum.CONCENTRATE_COLLECTION_CHARGES_REALTIME) {//实时代付
						String json = new String(msg.getBody(), Charsets.UTF_8);
						log.info("接收到的MSG:{}", json);
						log.info("接收到的MSGID:{}", msg.getMsgId());
						ConcentrateSingleOrderBean orderBean = JSON.parseObject(json,
								ConcentrateSingleOrderBean.class);
						if (orderBean == null) {
							log.warn("MSGID:{}JSON转换后为NULL,无法生成订单数据,原始消息数据为{}",
									msg.getMsgId(), json);
							break;
						}
						ResultBean resultBean = null;
						try {
							String tn = concentrateOrderService.createRealTimeCollectionOrder(orderBean);
							resultBean = new ResultBean(tn);
						}catch (Throwable e) {
							e.printStackTrace();
							resultBean = new ResultBean("T000",e.getMessage());
						}
						orderCacheResultService.saveConsumeOrderOfTN(KEY
								+ msg.getMsgId(), JSON.toJSONString(resultBean));
				}else if(orderTagsEnum==OrderTagsEnum.CONCENTRATE_COLLECTION_CHARGES_BATCH){//批量代付订单处理
					String json = new String(msg.getBody(), Charsets.UTF_8);
					log.info("接收到的MSG:{}", json);
					log.info("接收到的MSGID:{}", msg.getMsgId());
					ConcentrateBatchOrderBean orderBean = JSON.parseObject(json,
							ConcentrateBatchOrderBean.class);
					if (orderBean == null) {
						log.warn("MSGID:{}JSON转换后为NULL,无法生成订单数据,原始消息数据为{}",
								msg.getMsgId(), json);
						break;
					}
					ResultBean resultBean = null;
					try {
						String tn = concentrateOrderService.createBatchCollectionOrder(orderBean);
						resultBean = new ResultBean(tn);
					}catch (Throwable e) {
						e.printStackTrace();
						resultBean = new ResultBean("T000",e.getMessage());
					}
					orderCacheResultService.saveConsumeOrderOfTN(KEY
							+ msg.getMsgId(), JSON.toJSONString(resultBean));
				}
			}
			log.info(Thread.currentThread().getName()
					+ " Receive New Messages: " + msgs);
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}
	
	
}
