/* 
 * TradeRiskException.java  
 * 
 * version TODO
 *
 * 2016年11月15日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zcbspay.platform.payment.trade.route.exception;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年11月15日 下午4:45:46
 * @since 
 */
public class TradeRouteException extends AbstractDescException{
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 4634586182669271478L;
	private String code;
	/**
	 *
	 * @return
	 */
	@Override
	public String getCode() {
		// TODO Auto-generated method stub
		return code;
	}
	
	public TradeRouteException(String code,Object ... para ) {
        this.params = para;
        this.code = code;
    }
	
	public TradeRouteException(String code) {
        this.code = code;
    }
	/**
	 * 
	 */
	public TradeRouteException() {
		super();
		// TODO Auto-generated constructor stub
	}
}
