package com.zcbspay.platform.payment.dao;

import com.zcbspay.platform.payment.commons.dao.BaseDAO;
import com.zcbspay.platform.payment.pojo.OrderCollectDetaDO;

public interface OrderCollectDetaDAO extends BaseDAO<OrderCollectDetaDO> {

	/**
	 * 保存代收订单明细
	 * @param orderCollectDetaDO
	 */
	public void saveCollectOrderDeta(OrderCollectDetaDO orderCollectDetaDO);
}
