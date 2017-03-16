package com.zcbspay.platform.payment.dao;

import com.zcbspay.platform.payment.commons.dao.BaseDAO;
import com.zcbspay.platform.payment.pojo.OrderPaymentBatchDO;

public interface OrderPaymentBatchDAO extends BaseDAO<OrderPaymentBatchDO> {

	/**
	 * 保存代付批次信息
	 * @param orderPaymentBatch
	 * @return
	 */
	public OrderPaymentBatchDO savePaymentBatchOrder(OrderPaymentBatchDO orderPaymentBatch);
	
	/**
	 * 查询代付批次数据
	 * @param merchNo 商户号
	 * @param batchNo 批次号
	 * @param txndate 交易日期
	 * @return
	 */
	public OrderPaymentBatchDO getPaymentBatchOrder(String merchNo,String batchNo,String txndate);
	/**
	 * 通过tn获取代付批次数据
	 * @param tn
	 * @return
	 */
	public OrderPaymentBatchDO getPaymentBatchOrderByTn(String tn);
	
	/**
	 *  更新订单状态为开始支付
	 * @param tn
	 */
	public void updateOrderToStartPay(String tn);
}
