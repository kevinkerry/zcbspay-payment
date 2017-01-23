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
package com.zcbspay.platform.payment.risk.exception;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年11月15日 下午4:45:46
 * @since 
 */
public class TradeRiskException extends AbstractDescException{
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
	
	public TradeRiskException(String code,Object ... para ) {
        this.params = para;
        this.code = code;
    }
	
	public TradeRiskException(String code) {
        this.code = code;
    }
	/**
	 * 
	 */
	public TradeRiskException() {
		super();
		// TODO Auto-generated constructor stub
	}
}
