package com.zcbspay.platform.payment.order.consume.bean;

import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.zcbspay.platform.payment.order.bean.BaseOrderBean;

public class ConcentrateBatchOrderBean extends BaseOrderBean{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -3135033063873877889L;
	@Length(max = 6, message = "param.error.version")
	private String version;// 版本
	@Length(max = 1, message = "param.error.encoding")
	private String encoding;// 编码方式
	@NotEmpty(message = "param.empty.merId")
	@Length(max = 15, message = "param.error.merId")
	private String merId;//商户代码
	@Length(max = 128, message = "param.error.backUrl")
	private String backUrl;//通知地址
	@NotEmpty(message = "param.empty.batchNo")
	@Length(max = 32, message = "param.error.batchNo")
	private String batchNo;//批次号
	@NotEmpty(message = "param.empty.txnTime")
	@Length(max = 14, message = "param.error.txnTime")
	private String txnTime;//订单发送时间
	@NotEmpty(message = "param.empty.totalQty")
	@Length(max = 12, message = "param.error.totalQty")
	private String totalQty;//总笔数
	@NotEmpty(message = "param.empty.totalAmt")
	@Length(max = 12, message = "param.error.totalAmt")
	private String totalAmt;//总金额 
	@Length(max = 256, message = "param.error.reserved")
	private String reserved;//保留域
	
	private List<ConcentrateOrderDetaBean> detaList;

	private String coopinstiId;
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

	public String getMerId() {
		return merId;
	}

	public void setMerId(String merId) {
		this.merId = merId;
	}

	public String getBackUrl() {
		return backUrl;
	}

	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getTxnTime() {
		return txnTime;
	}

	public void setTxnTime(String txnTime) {
		this.txnTime = txnTime;
	}

	public String getTotalQty() {
		return totalQty;
	}

	public void setTotalQty(String totalQty) {
		this.totalQty = totalQty;
	}

	public String getTotalAmt() {
		return totalAmt;
	}

	public void setTotalAmt(String totalAmt) {
		this.totalAmt = totalAmt;
	}

	public String getReserved() {
		return reserved;
	}

	public void setReserved(String reserved) {
		this.reserved = reserved;
	}

	public List<ConcentrateOrderDetaBean> getDetaList() {
		return detaList;
	}

	public void setDetaList(List<ConcentrateOrderDetaBean> detaList) {
		this.detaList = detaList;
	}

	public String getCoopinstiId() {
		return coopinstiId;
	}

	public void setCoopinstiId(String coopinstiId) {
		this.coopinstiId = coopinstiId;
	}
	
	
}
