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
	public OrderPaymentBatchDO getCollectBatchOrder(String merchNo,String batchNo,String txndate);
}
