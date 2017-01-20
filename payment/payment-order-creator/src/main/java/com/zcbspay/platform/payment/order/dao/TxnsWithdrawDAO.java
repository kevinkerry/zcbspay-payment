/* 
 * TxnsWithdrawDAO.java  
 * 
 * version TODO
 *
 * 2016年11月14日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zcbspay.platform.payment.order.dao;

import com.zcbspay.platform.payment.commons.dao.BaseDAO;
import com.zcbspay.platform.payment.order.dao.pojo.PojoTxnsWithdraw;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年11月14日 下午4:10:25
 * @since 
 */
public interface TxnsWithdrawDAO extends BaseDAO<PojoTxnsWithdraw>{

	/**
	 * 保存提现订单数据
	 * @param txnsWithdraw 提现订单pojo
	 */
	public void saveTxnsWithdraw(PojoTxnsWithdraw txnsWithdraw);
}
