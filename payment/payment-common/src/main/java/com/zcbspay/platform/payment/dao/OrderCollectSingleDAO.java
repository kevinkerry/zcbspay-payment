package com.zcbspay.platform.payment.dao;

import com.zcbspay.platform.payment.commons.dao.BaseDAO;
import com.zcbspay.platform.payment.pojo.OrderCollectSingleDO;

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
	
	/**
	 * 通过tn获取代收订单信息
	 * @param tn
	 * @return
	 */
	public OrderCollectSingleDO getOrderinfoByTn(String tn);
	
	/**
	 * 更新订单状态为开始支付
	 * @param tn
	 */
	public void updateOrderToStartPay(String tn);
}
