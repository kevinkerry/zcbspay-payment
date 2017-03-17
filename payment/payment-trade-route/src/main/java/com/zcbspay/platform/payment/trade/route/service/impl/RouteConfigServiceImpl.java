/* 
 * RouteConfigServiceImp.java  
 * 
 * version TODO
 *
 * 2016年10月12日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zcbspay.platform.payment.trade.route.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zcbspay.platform.payment.trade.route.dao.MerchBankAccountDAO;
import com.zcbspay.platform.payment.trade.route.dao.RouteConfigDAO;
import com.zcbspay.platform.payment.trade.route.exception.TradeRouteException;
import com.zcbspay.platform.payment.trade.route.service.RouteConfigService;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年10月12日 上午9:41:39
 * @since 
 */
@Service("RouteConfigService")
public class RouteConfigServiceImpl implements RouteConfigService{

	@Autowired
	private RouteConfigDAO routeConfigDAO;
	@Autowired
	private MerchBankAccountDAO merchBankAccountDAO;
	/**
	 *
	 * @param transTime
	 * @param transAmt
	 * @param memberId
	 * @param busiCode
	 * @param cardNo
	 * @return
	 * @throws TradeRouteException 
	 */
	@Override
	public String getTradeChannel(String transTime, String transAmt,String memberId, String busiCode, String cardNo,String routeVer) throws TradeRouteException {
		return routeConfigDAO.getTradeRoute(transTime, transAmt, memberId, busiCode, cardNo, routeVer);
	}
	/**
	 *
	 * @param cardNo
	 * @return
	 */
	@Override
	public Map<String, Object> getCardInfo(String cardNo) {
		// TODO Auto-generated method stub
		return routeConfigDAO.getCardInfo(cardNo);
	}
	@Override
	public String getTradeChannel(String merchNo, String accountno,
			String protocoltype) throws TradeRouteException{
		// TODO Auto-generated method stub
		return merchBankAccountDAO.getTradeRoute(merchNo, accountno, protocoltype).getChannelcode();
	}

	
}
