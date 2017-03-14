package com.zcbspay.platform.payment.dao;

import com.zcbspay.platform.payment.commons.dao.BaseDAO;
import com.zcbspay.platform.payment.pojo.OrderPaymentDetaDO;

public interface OrderPaymentDetaDAO extends BaseDAO<OrderPaymentDetaDO> {

	/**
	 * 保存代付订单明细
	 * @param orderPaymentDeta
	 */
	public void savePaymentDetaOrder(OrderPaymentDetaDO orderPaymentDeta);
}
