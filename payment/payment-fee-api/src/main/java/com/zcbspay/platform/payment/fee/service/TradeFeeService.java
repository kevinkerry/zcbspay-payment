/* 
 * TradeFeeService.java  
 * 
 * version TODO
 *
 * 2016年11月16日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zcbspay.platform.payment.fee.service;

import com.zcbspay.platform.payment.fee.bean.FeeBean;
import com.zcbspay.platform.payment.fee.exception.TradeFeeException;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年11月16日 上午9:27:26
 * @since 
 */
public interface TradeFeeService {

	/**
	 * 获取通用交易手续费
	 * @param feeBean
	 * @return
	 * @throws TradeFeeException
	 */
	public long getCommonFee(FeeBean feeBean) throws TradeFeeException;
	
	/**
	 * 获取退款交易手续费
	 * @param feeBean
	 * @return
	 * @throws TradeFeeException
	 */
	public long getRefundFee(FeeBean feeBean) throws TradeFeeException;
}
