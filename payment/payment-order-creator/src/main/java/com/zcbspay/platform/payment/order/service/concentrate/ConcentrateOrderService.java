package com.zcbspay.platform.payment.order.service.concentrate;

import com.zcbspay.platform.payment.order.consume.bean.ConcentrateBatchOrderBean;
import com.zcbspay.platform.payment.order.consume.bean.ConcentrateSingleOrderBean;
import com.zcbspay.platform.payment.order.exception.OrderException;

/**
 * 集中代收付订单创建接口
 *
 * @author guojia
 * @version
 * @date 2017年3月13日 上午11:44:48
 * @since
 */
public interface ConcentrateOrderService {

	/**
	 * 创建实时代收订单
	 * @param orderBean
	 * @return
	 */
	public String createRealTimeCollectionOrder(ConcentrateSingleOrderBean orderBean) throws OrderException;
	
	/**
	 * 创建实时代付订单
	 * @param orderBean
	 * @return
	 */
	public String createRealTimePaymentOrder(ConcentrateSingleOrderBean orderBean) throws OrderException;
	
	/**
	 * 创建代收批次订单
	 * @param orderBean
	 * @return
	 * @throws OrderException 
	 */
	public String createBatchCollectionOrder(ConcentrateBatchOrderBean orderBean) throws OrderException;
	
	/**
	 * 创建代付批次订单
	 * @param orderBean
	 * @return
	 */
	public String createBatchPaymentOrder(ConcentrateBatchOrderBean orderBean) throws OrderException;
}
