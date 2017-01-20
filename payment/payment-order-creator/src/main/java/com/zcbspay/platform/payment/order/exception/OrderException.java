/* 
 * InsteadPayOrderException.java  
 * 
 * version TODO
 *
 * 2016年10月20日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zcbspay.platform.payment.order.exception;

import java.util.ResourceBundle;


/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年10月20日 上午10:36:29
 * @since
 */
public class OrderException extends Exception {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5825227604243819693L;
	private static final ResourceBundle RESOURCE = ResourceBundle.getBundle("customer_exception");
	private String code;
	private String message;
	public OrderException(String code,String message ) {
		this.code = code;
		this.message = message;
    }
	
	public OrderException(String code) {
        this.code = code;
        this.message = RESOURCE.getString(code);
    }
	
	public String getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
