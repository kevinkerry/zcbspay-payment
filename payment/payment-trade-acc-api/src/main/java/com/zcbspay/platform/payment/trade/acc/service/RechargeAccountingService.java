/* 
 * RechargeAccountingService.java  
 * 
 * version TODO
 *
 * 2016年11月18日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zcbspay.platform.payment.trade.acc.service;

import com.zcbspay.platform.payment.trade.acc.bean.ResultBean;
import com.zcbspay.platform.payment.trade.acc.bean.TxnsLogBean;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年11月18日 下午4:08:57
 * @since 
 */
public interface RechargeAccountingService {

	/**
	 * 充值账务处理
	 * @param txnsLogBean 交易日志bean
	 * @return 交易结果bean
	 */
	public ResultBean rechargeAccounting(TxnsLogBean txnsLogBean);
}
