/* 
 * RouteConfigService.java  
 * 
 * version TODO
 *
 * 2016年10月12日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zcbspay.platform.payment.trade.route.service;

import java.util.Map;

import com.zcbspay.platform.payment.trade.route.exception.TradeRouteException;

/**
 * 数据库路由服务接口
 *
 * @author guojia
 * @version
 * @date 2016年10月12日 上午9:32:49
 * @since 
 */
public interface RouteConfigService {

	/**
	 * 获取交易渠道路由
	 * @param transTime 交易时间
	 * @param transAmt 交易金额
	 * @param memberId 会员号
	 * @param busiCode 业务代码
	 * @param cardNo 银行卡号
	 * @param routeVer 路由版本-商户
	 * @return 交易渠道代码
	 */
	public String getTradeChannel(String transTime,String transAmt,String memberId,String busiCode,String cardNo,String routeVer) throws TradeRouteException;
	
	/**
	 * 通过银行卡号获取银行卡bin信息
	 * @param cardNo
	 * @return
	 */
	public Map<String, Object> getCardInfo(String cardNo);
	
	/**
	 * 获取交易渠道路由
	 * @param merchNo 商户号
	 * @param accountno 账户号
	 * @param protocoltype 协议类型
	 * @return
	 */
	public String getTradeChannel(String merchNo,String accountno,String protocoltype) throws TradeRouteException;
}
