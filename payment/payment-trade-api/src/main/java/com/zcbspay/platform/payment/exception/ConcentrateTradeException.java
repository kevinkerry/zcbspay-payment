package com.zcbspay.platform.payment.exception;

public class ConcentrateTradeException extends AbstractDescException{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7177362118678143837L;
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
	
	public ConcentrateTradeException(String code,Object ... para ) {
        this.params = para;
        this.code = code;
    }
	
	public ConcentrateTradeException(String code) {
        this.code = code;
    }
	/**
	 * 
	 */
	public ConcentrateTradeException() {
		super();
		// TODO Auto-generated constructor stub
	}
}
