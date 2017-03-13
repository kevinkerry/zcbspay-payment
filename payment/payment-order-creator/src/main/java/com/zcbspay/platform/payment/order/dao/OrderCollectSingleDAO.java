package com.zcbspay.platform.payment.order.dao;

import com.zcbspay.platform.payment.commons.dao.BaseDAO;
import com.zcbspay.platform.payment.order.dao.pojo.OrderCollectSingleDO;

public interface OrderCollectSingleDAO extends BaseDAO<OrderCollectSingleDO>{

	/**
	 * 根据订单号和商户号查询代收订单
	 * @param orderNo
	 * @param merchNo
	 * @return
	 */
	public OrderCollectSingleDO getOrderinfoByOrderNoAndMerchNo(String orderNo,String merchNo);
	
	/**
	 * 保存代收订单
	 * @param orderCollectSingle
	 */
	public void saveSingleCollectOrder(OrderCollectSingleDO orderCollectSingle);
}
