package com.zcbspay.platform.payment.dao;

import com.zcbspay.platform.payment.commons.dao.BaseDAO;
import com.zcbspay.platform.payment.pojo.OrderPaymentSingleDO;

public interface OrderPaymentSingleDAO extends BaseDAO<OrderPaymentSingleDO> {

	/**
	 * 保存代付订单欣
	 * @param orderPaymentSingle
	 */
	public void savePaymentSingleOrder(OrderPaymentSingleDO orderPaymentSingle);
	
	/**
	 * 通过tn获取代付订单信息
	 * @param tn
	 * @return
	 */
	public OrderPaymentSingleDO getOrderinfoByTn(String tn);
	
	/**
	 * 根据订单号和商户号查询代收订单
	 * @param orderNo
	 * @param merchNo
	 * @return
	 */
	public OrderPaymentSingleDO getOrderinfoByOrderNoAndMerchNo(String orderNo,String merchNo);
	
	/**
	 * 更新订单状态为开始支付
	 * @param tn
	 */
	public void updateOrderToStartPay(String tn);
}
