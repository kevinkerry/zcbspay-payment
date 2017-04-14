package com.zcbspay.platform.payment.dao;

import java.util.List;

import com.zcbspay.platform.payment.commons.dao.BaseDAO;
import com.zcbspay.platform.payment.pojo.OrderPaymentDetaDO;

public interface OrderPaymentDetaDAO extends BaseDAO<OrderPaymentDetaDO> {

	/**
	 * 保存代付订单明细
	 * @param orderPaymentDeta
	 */
	public void savePaymentDetaOrder(OrderPaymentDetaDO orderPaymentDeta);

	/**
	 * 通过批次标示获取批次明细数据集合
	 * @param batchId
	 * @return
	 */
	public List<OrderPaymentDetaDO> getDetaListByBatchtid(Long batchId);

	public void updatePaymenyOrderDeta(OrderPaymentDetaDO deta);
}
