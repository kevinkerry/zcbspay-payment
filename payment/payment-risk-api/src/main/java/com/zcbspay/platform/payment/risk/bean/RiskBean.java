/* 
 * RiskBean.java  
 * 
 * version TODO
 *
 * 2016年11月14日 
 * 
 * Copyright (c) 2016;zlebank.All rights reserved.
 * 
 */
package com.zcbspay.platform.payment.risk.bean;

import java.io.Serializable;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年11月14日 下午5:24:20
 * @since 
 */
public class RiskBean implements Serializable{
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -7655403924011339201L;
	/**
	 * 交易序列号
	 */
	private String txnseqno;
	/**
	 * 合作机构号
	 */
	private String coopInstId;
	/**
	 * 商户号
	 */
	private String merchId;
	/**
	 * 会员号
	 */
	private String memberId;
	/**
	 * 业务代码
	 */
	private String busiCode;
	/**
	 * 交易金额
	 */
	private String txnAmt;
	/**
	 * 银行卡类型
	 */
	private String cardType;
	/**
	 * 卡号/账号
	 */
	private String cardNo;
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
	 * @return the coopInstId
	 */
	public String getCoopInstId() {
		return coopInstId;
	}
	/**
	 * @param coopInstId the coopInstId to set
	 */
	public void setCoopInstId(String coopInstId) {
		this.coopInstId = coopInstId;
	}
	/**
	 * @return the merchId
	 */
	public String getMerchId() {
		return merchId;
	}
	/**
	 * @param merchId the merchId to set
	 */
	public void setMerchId(String merchId) {
		this.merchId = merchId;
	}
	/**
	 * @return the memberId
	 */
	public String getMemberId() {
		return memberId;
	}
	/**
	 * @param memberId the memberId to set
	 */
	public void setMemberId(String memberId) {
		this.memberId = memberId;
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
	/**
	 * @return the cardNo
	 */
	public String getCardNo() {
		return cardNo;
	}
	/**
	 * @param cardNo the cardNo to set
	 */
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	
	
}
