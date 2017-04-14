package com.zcbspay.platform.payment.dao;

import java.util.List;

import com.zcbspay.platform.payment.commons.dao.BaseDAO;
import com.zcbspay.platform.payment.pojo.OrderCollectDetaDO;

public interface OrderCollectDetaDAO extends BaseDAO<OrderCollectDetaDO> {

	/**
	 * 保存代收订单明细
	 * @param orderCollectDetaDO
	 */
	public void saveCollectOrderDeta(OrderCollectDetaDO orderCollectDetaDO);
	
	/**
	 * 通过批次标示获取批次明细数据集合
	 * @param batchId
	 * @return
	 */
	public List<OrderCollectDetaDO> getDetaListByBatchtid(Long batchId);
	
	/**
	 * 更新代收明细订单状态
	 * @param orderCollectDetaDO
	 */
	public void updateCollectOrderDeta(OrderCollectDetaDO orderCollectDetaDO);
}
