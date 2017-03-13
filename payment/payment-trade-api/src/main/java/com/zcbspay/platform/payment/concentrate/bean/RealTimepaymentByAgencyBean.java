package com.zcbspay.platform.payment.concentrate.bean;

import java.io.Serializable;

public class RealTimepaymentByAgencyBean implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1880089238266231064L;
	private String version;// 版本
	private String encoding;// 编码方式
	private String txnType;// 交易类型
	private String txnSubType;// 交易子类
	private String bizType;// 产品类型
	private String txnTime;// 订单发送时间
	private String merId;// 商户代码
	private String merName;// 商户全称
	private String merAbbr;// 商户简称
	private String orderId;// 商户订单号
	private String currencyCode;// 交易币种
	private String txnAmt;// 金额
	private String reserved;// 保留域

	private String debtorBank;// 付款人银行号
	private String debtorAccount;// 付款人账号
	private String debtorName;// 付款人名称
	private String debtorConsign;// 付款合同号
	private String creditorBank;// 收款人银行号
	private String creditorAccount;// 收款人账号
	private String creditorName;// 收款人名称
	private String proprietary;// 业务种类编码
	private String summary;// 摘要
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getEncoding() {
		return encoding;
	}
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	public String getTxnType() {
		return txnType;
	}
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
	public String getTxnSubType() {
		return txnSubType;
	}
	public void setTxnSubType(String txnSubType) {
		this.txnSubType = txnSubType;
	}
	public String getBizType() {
		return bizType;
	}
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	public String getTxnTime() {
		return txnTime;
	}
	public void setTxnTime(String txnTime) {
		this.txnTime = txnTime;
	}
	public String getMerId() {
		return merId;
	}
	public void setMerId(String merId) {
		this.merId = merId;
	}
	public String getMerName() {
		return merName;
	}
	public void setMerName(String merName) {
		this.merName = merName;
	}
	public String getMerAbbr() {
		return merAbbr;
	}
	public void setMerAbbr(String merAbbr) {
		this.merAbbr = merAbbr;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getTxnAmt() {
		return txnAmt;
	}
	public void setTxnAmt(String txnAmt) {
		this.txnAmt = txnAmt;
	}
	public String getReserved() {
		return reserved;
	}
	public void setReserved(String reserved) {
		this.reserved = reserved;
	}
	public String getDebtorBank() {
		return debtorBank;
	}
	public void setDebtorBank(String debtorBank) {
		this.debtorBank = debtorBank;
	}
	public String getDebtorAccount() {
		return debtorAccount;
	}
	public void setDebtorAccount(String debtorAccount) {
		this.debtorAccount = debtorAccount;
	}
	public String getDebtorName() {
		return debtorName;
	}
	public void setDebtorName(String debtorName) {
		this.debtorName = debtorName;
	}
	public String getDebtorConsign() {
		return debtorConsign;
	}
	public void setDebtorConsign(String debtorConsign) {
		this.debtorConsign = debtorConsign;
	}
	public String getCreditorBank() {
		return creditorBank;
	}
	public void setCreditorBank(String creditorBank) {
		this.creditorBank = creditorBank;
	}
	public String getCreditorAccount() {
		return creditorAccount;
	}
	public void setCreditorAccount(String creditorAccount) {
		this.creditorAccount = creditorAccount;
	}
	public String getCreditorName() {
		return creditorName;
	}
	public void setCreditorName(String creditorName) {
		this.creditorName = creditorName;
	}
	public String getProprietary() {
		return proprietary;
	}
	public void setProprietary(String proprietary) {
		this.proprietary = proprietary;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	
}
