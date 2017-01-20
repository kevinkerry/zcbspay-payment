/* 
 * FeeBean.java  
 * 
 * version TODO
 *
 * 2016年11月16日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zcbspay.platform.payment.fee.bean;

import java.io.Serializable;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年11月16日 上午9:08:52
 * @since 
 */
public class FeeBean implements Serializable{
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 2687548389727959837L;
	//交易序列号，
	private String txnseqno;
	//扣率版本，
	private String feeVer;
	//业务类型，
	private String busiCode;
	//交易金额，
	private String txnAmt;
	//会员号，
	private String merchNo;
	//原交易序列号，
	private String txnseqnoOg;
	//卡类型 
	private String cardType;
	/**
	 * @return the txnseqno
	 */
	public String getTxnseqno() {
		return txnseqno;
	}
	/**
	 * @param txnseqno the txnseqno to set
	 */
	public void setTxnseqno(String txnseqno) {
		this.txnseqno = txnseqno;
	}
	/**
	 * @return the feeVer
	 */
	public String getFeeVer() {
		return feeVer;
	}
	/**
	 * @param feeVer the feeVer to set
	 */
	public void setFeeVer(String feeVer) {
		this.feeVer = feeVer;
	}
	/**
	 * @return the busiCode
	 */
	public String getBusiCode() {
		return busiCode;
	}
	/**
	 * @param busiCode the busiCode to set
	 */
	public void setBusiCode(String busiCode) {
		this.busiCode = busiCode;
	}
	/**
	 * @return the txnAmt
	 */
	public String getTxnAmt() {
		return txnAmt;
	}
	/**
	 * @param txnAmt the txnAmt to set
	 */
	public void setTxnAmt(String txnAmt) {
		this.txnAmt = txnAmt;
	}
	/**
	 * @return the merchNo
	 */
	public String getMerchNo() {
		return merchNo;
	}
	/**
	 * @param merchNo the merchNo to set
	 */
	public void setMerchNo(String merchNo) {
		this.merchNo = merchNo;
	}
	/**
	 * @return the txnseqnoOg
	 */
	public String getTxnseqnoOg() {
		return txnseqnoOg;
	}
	/**
	 * @param txnseqnoOg the txnseqnoOg to set
	 */
	public void setTxnseqnoOg(String txnseqnoOg) {
		this.txnseqnoOg = txnseqnoOg;
	}
	/**
	 * @return the cardType
	 */
	public String getCardType() {
		return cardType;
	}
	/**
	 * @param cardType the cardType to set
	 */
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	
	
	
}
