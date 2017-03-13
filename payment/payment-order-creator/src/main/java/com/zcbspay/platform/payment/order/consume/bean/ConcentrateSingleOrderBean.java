package com.zcbspay.platform.payment.order.consume.bean;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.zcbspay.platform.payment.order.bean.BaseOrderBean;

/**
 * 集中代收付订单bean
 *
 * @author guojia
 * @version
 * @date 2017年3月10日 下午3:38:15
 * @since
 */
public class ConcentrateSingleOrderBean extends BaseOrderBean{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 4060822152522948998L;
	// 报文信息
	@Length(max = 6, message = "param.error.version")
	private String version;// 版本
	@Length(max = 1, message = "param.error.encoding")
	private String encoding;// 编码方式
	
	@Length(max = 128, message = "param.error.backUrl")
	private String backUrl;// 通知地址
	@NotEmpty(message = "param.empty.merId")
	@Length(max = 15, message = "param.error.merId")
	private String merchNo;// 商户号
	@Length(max = 40, message = "param.error.merName")
	private String merName;// 商户全称
	@Length(max = 40, message = "param.error.merAbbr")
	private String merAbbr;// 商户简称
	@NotEmpty(message = "param.empty.orderId")
	@Length(max = 32, message = "param.error.orderId")
	private String orderId;// 商户订单号
	@NotEmpty(message = "param.empty.txnTime")
	@Length(max = 14, message = "param.error.txnTime")
	private String txnTime;// 订单发送时间
	@Length(max = 14, message = "param.error.payTimeout")
	private String payTimeout;// 支付超时时间
	@NotEmpty(message = "param.empty.txnAmt")
	@Length(max = 12, message = "param.error.txnAmt")
	private String txnAmt;// 交易金额
	@NotEmpty(message = "param.empty.currencyCode")
	@Length(max = 3, message = "param.error.currencyCode")
	private String currencyCode;// 交易币种
	@Length(max = 256, message = "param.error.orderDesc")
	private String orderDesc;// 订单描述
	private String reserved;// 保留域
	// 代收付账户信息
	@NotEmpty(message = "param.empty.debtorBank")
	@Length(max = 14, message = "param.error.debtorBank")
	private String debtorBank;// 付款人银行号
	@NotEmpty(message = "param.empty.debtorAccount")
	@Length(max = 32, message = "param.error.debtorAccount")
	private String debtorAccount;// 付款人账号
	@NotEmpty(message = "param.empty.debtorName")
	@Length(max = 64, message = "param.error.debtorName")
	private String debtorName;// 付款人名称
	@NotEmpty(message = "param.empty.debtorConsign")
	@Length(max = 60, message = "param.error.debtorConsign")
	private String debtorConsign;// 付款合同号
	@NotEmpty(message = "param.empty.creditorBank")
	@Length(max = 14, message = "param.error.creditorBank")
	private String creditorBank;// 收款人银行号
	@NotEmpty(message = "param.empty.creditorAccount")
	@Length(max = 32, message = "param.error.creditorAccount")
	private String creditorAccount;// 收款人账号
	@NotEmpty(message = "param.empty.creditorName")
	@Length(max = 64, message = "param.error.creditorName")
	private String creditorName;// 收款人名称
	@NotEmpty(message = "param.empty.proprietary")
	@Length(max = 5, message = "param.error.proprietary")
	private String proprietary;// 业务种类编码
	@Length(max = 64, message = "param.error.summary")
	private String summary;// 摘要
	private String coopinstiId;//合作机构
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
	public String getBackUrl() {
		return backUrl;
	}
	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
	}
	public String getMerchNo() {
		return merchNo;
	}
	public void setMerchNo(String merchNo) {
		this.merchNo = merchNo;
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
	public String getTxnTime() {
		return txnTime;
	}
	public void setTxnTime(String txnTime) {
		this.txnTime = txnTime;
	}
	public String getPayTimeout() {
		return payTimeout;
	}
	public void setPayTimeout(String payTimeout) {
		this.payTimeout = payTimeout;
	}
	public String getTxnAmt() {
		return txnAmt;
	}
	public void setTxnAmt(String txnAmt) {
		this.txnAmt = txnAmt;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getOrderDesc() {
		return orderDesc;
	}
	public void setOrderDesc(String orderDesc) {
		this.orderDesc = orderDesc;
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
	public String getCoopinstiId() {
		return coopinstiId;
	}
	public void setCoopinstiId(String coopinstiId) {
		this.coopinstiId = coopinstiId;
	}
	
	
	
}
