/* 
 * TradeFeeException.java  
 * 
 * version TODO
 *
 * 2016年11月16日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zcbspay.platform.payment.fee.exception;

import com.zcbspay.platform.payment.fee.exception.base.AbstractDescException;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年11月16日 上午9:18:12
 * @since 
 */
public class TradeFeeException extends AbstractDescException{
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
	
	public TradeFeeException(String code,Object ... para ) {
        this.params = para;
        this.code = code;
    }
	
	public TradeFeeException(String code) {
        this.code = code;
    }
	/**
	 * 
	 */
	public TradeFeeException() {
		super();
		// TODO Auto-generated constructor stub
	}
}
