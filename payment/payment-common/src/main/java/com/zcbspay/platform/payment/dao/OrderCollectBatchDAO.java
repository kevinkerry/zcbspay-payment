package com.zcbspay.platform.payment.dao;

import com.zcbspay.platform.payment.commons.dao.BaseDAO;
import com.zcbspay.platform.payment.pojo.OrderCollectBatchDO;

public interface OrderCollectBatchDAO extends BaseDAO<OrderCollectBatchDO> {

	/**
	 * 保存批量代收批次数据
	 * @param collectBatchDO
	 */
	public OrderCollectBatchDO saveCollectBatchOrder(OrderCollectBatchDO collectBatchDO);
	
	/**
	 * 查询代收批次数据
	 * @param merchNo 商户号
	 * @param batchNo 批次号
	 * @param txndate 交易日期
	 * @return
	 */
	public OrderCollectBatchDO getCollectBatchOrder(String merchNo,String batchNo,String txndate);
	
	/**
	 * 通过tn获取代收批次数据
	 * @param tn
	 * @return
	 */
	public OrderCollectBatchDO getCollectBatchOrderByTn(String tn);
	
	/**
	 * 更新订单状态为开始支付
	 * @param tn
	 */
	public void updateOrderToStartPay(String tn);
}
