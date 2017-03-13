package com.zcbspay.platform.payment.order.consumer;

import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.zcbspay.platform.payment.order.consumer.enums.OrderTagsEnum;

/**
 * 集中代付订单消费者
 *
 * @author guojia
 * @version
 * @date 2017年3月13日 上午11:20:31
 * @since
 */
@Service
public class ConcentratePaymentOrderConsumer  implements  ApplicationListener<ContextRefreshedEvent>{
	private static final Logger log = LoggerFactory.getLogger(InsteadPayOrderConsumer.class);
	private static final  ResourceBundle RESOURCE = ResourceBundle.getBundle("consumer_order");
	
	@Autowired
	@Qualifier("concentratePaymentLinstener")
	private MessageListenerConcurrently  concentratePaymentLinstener;
	
	public void startConsume() throws InterruptedException, MQClientException {
		/**
		 * 当前例子是PushConsumer用法，使用方式给用户感觉是消息从RocketMQ服务器推到了应用客户端。<br>
		 * 但是实际PushConsumer内部是使用长轮询Pull方式从RocketMQ服务器拉消息，然后再回调用户Listener方法<br>
		 */
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(RESOURCE.getString("concentrate.payment.order.consumer.group"));
		consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
		consumer.setNamesrvAddr(RESOURCE.getString("single.namesrv.addr"));
		consumer.setInstanceName(RESOURCE.getString("concentrate.payment.order.instancename"));
		String subExpression = "";
		for(OrderTagsEnum tagsEnum:OrderTagsEnum.values()){
			if(StringUtils.isNotEmpty(subExpression)){
				subExpression+=" || ";
			}
			subExpression+=tagsEnum.getCode();
		}
		log.info("subExpression:{}",subExpression);
		consumer.subscribe(RESOURCE.getString("concentrate.payment.order.subscribe"), subExpression);
		consumer.registerMessageListener(concentratePaymentLinstener);//在监听器中实现创建order
		consumer.start();
		log.info("ConcentratePaymentOrderConsumer {},消费者启动",consumer.getInstanceName());
	}

	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// TODO Auto-generated method stub
		if (event.getApplicationContext().getParent() == null) {

			try {
				startConsume();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error(e.getMessage());
			} catch (MQClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error(e.getMessage());
			}

		}
	}

}
