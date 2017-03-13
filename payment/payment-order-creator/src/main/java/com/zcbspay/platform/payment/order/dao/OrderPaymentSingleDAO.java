package com.zcbspay.platform.payment.order.dao;

import com.zcbspay.platform.payment.commons.dao.BaseDAO;
import com.zcbspay.platform.payment.order.dao.pojo.OrderPaymentSingleDO;

public interface OrderPaymentSingleDAO extends BaseDAO<OrderPaymentSingleDO> {

	public void savePaymentSingleOrder(OrderPaymentSingleDO orderPaymentSingle);
}
