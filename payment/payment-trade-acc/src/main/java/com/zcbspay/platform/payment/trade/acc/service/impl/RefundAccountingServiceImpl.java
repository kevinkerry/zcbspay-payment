/* 
 * RefundAccountingServiceImpl.java  
 * 
 * version TODO
 *
 * 2016年11月11日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zcbspay.platform.payment.trade.acc.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zcbspay.platform.payment.dao.TxnsLogDAO;
import com.zcbspay.platform.payment.pojo.PojoTxnsLog;
import com.zcbspay.platform.payment.trade.acc.bean.ResultBean;
import com.zcbspay.platform.payment.trade.acc.service.RefundAccountingService;
import com.zlebank.zplatform.acc.bean.TradeInfo;
import com.zlebank.zplatform.acc.bean.enums.EntryEvent;
import com.zlebank.zplatform.acc.exception.AbstractBusiAcctException;
import com.zlebank.zplatform.acc.exception.AccBussinessException;
import com.zlebank.zplatform.acc.exception.IllegalEntryRequestException;
import com.zlebank.zplatform.acc.service.AccEntryService;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年11月11日 下午4:43:55
 * @since 
 */
@Service("refundAccountingService")
public class RefundAccountingServiceImpl implements RefundAccountingService {

	@Autowired
	private TxnsLogDAO txnsLogDAO;
	@Reference(version="1.0")
	private AccEntryService accEntryService;
	/**
	 *
	 * @param txnseqno
	 * @return
	 */
	@Override
	public ResultBean refundApply(String txnseqno) {
		ResultBean resultBean = null;
		PojoTxnsLog txnsLog = txnsLogDAO.getTxnsLogByTxnseqno(txnseqno);
		if(txnsLog==null){
			resultBean = new ResultBean("T000", "交易流水不存在");
			return resultBean;
		}
		
		TradeInfo tradeInfo = new TradeInfo();
		tradeInfo.setPayMemberId(txnsLog.getAccmemberid());
		tradeInfo.setPayToMemberId(txnsLog.getAccsecmerno());
		tradeInfo.setAmount(new BigDecimal(txnsLog.getAmount()));
		tradeInfo.setCharge(new BigDecimal(txnsLog.getTxnfee()));
		tradeInfo.setTxnseqno(txnsLog.getTxnseqno());
		tradeInfo.setCoopInstCode(txnsLog.getAccfirmerno());
		tradeInfo.setBusiCode(txnsLog.getBusicode());
		// 记录分录流水
		try {
			accEntryService.accEntryProcess(tradeInfo, EntryEvent.AUDIT_APPLY);
			resultBean = new ResultBean("success");
		} catch (AccBussinessException | IllegalEntryRequestException
				| AbstractBusiAcctException | NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resultBean = new ResultBean("T000", e.getMessage());
		}
		return resultBean;
	}

}
