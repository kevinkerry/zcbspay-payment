/* 
 * TradeRiskControlService.java  
 * 
 * version TODO
 *
 * 2016年11月14日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zcbspay.platform.payment.risk.service;

import com.zcbspay.platform.payment.risk.bean.RiskBean;
import com.zcbspay.platform.payment.risk.exception.TradeRiskException;

/**
 * 交易风控service
 *
 * @author guojia
 * @version
 * @date 2016年11月14日 下午4:44:51
 * @since 
 */
public interface TradeRiskControlService {

	/**
	 * 实时交易风控
	 * @param riskBean
	 */
	public void realTimeTradeRiskControl(RiskBean riskBean) throws TradeRiskException;
}
