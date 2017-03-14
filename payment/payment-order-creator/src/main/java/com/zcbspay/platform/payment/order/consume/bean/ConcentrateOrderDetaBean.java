package com.zcbspay.platform.payment.order.consume.bean;

import java.io.Serializable;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class ConcentrateOrderDetaBean implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -988695978558441039L;
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
	
	@NotEmpty(message = "param.empty.orderId")
	@Length(max = 64, message = "param.error.orderId")
	private String orderId;//	商户订单号
	@NotEmpty(message = "param.empty.currencyCode")
	@Length(max = 3, message = "param.error.currencyCode")
	private String currencyCode;//	交易币种
	@NotEmpty(message = "param.empty.txnamt")
	@Length(max = 12, message = "param.error.txnamt")
	private String amt;//	单笔金额
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
	public String getAmt() {
		return amt;
	}
	public void setAmt(String amt) {
		this.amt = amt;
	}
	
	
}
