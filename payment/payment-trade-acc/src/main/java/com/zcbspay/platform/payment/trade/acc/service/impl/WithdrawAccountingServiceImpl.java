/* 
 * WithdrawAccountingServiceImpl.java  
 * 
 * version TODO
 *
 * 2016年11月14日 
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
import com.zcbspay.platform.payment.trade.acc.enums.BusinessCodeEnum;
import com.zcbspay.platform.payment.trade.acc.service.WithdrawAccountingService;
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
 * @date 2016年11月14日 下午4:16:27
 * @since 
 */
@Service("withdrawAccountingService")
public class WithdrawAccountingServiceImpl implements WithdrawAccountingService {

	@Reference(version="1.0")
	private AccEntryService accEntryService;
	@Autowired
	private TxnsLogDAO txnsLogDAO;
	/**
	 *
	 * @param txnseqno
	 */
	@Override
	public ResultBean withdrawApply(String txnseqno) {
		ResultBean resultBean = null;
		try {
			PojoTxnsLog txnsLog = txnsLogDAO.getTxnsLogByTxnseqno(txnseqno);
			TradeInfo tradeInfo = new TradeInfo();
			tradeInfo.setPayMemberId(txnsLog.getAccmemberid());
			tradeInfo.setPayToMemberId(txnsLog.getAccmemberid());
			tradeInfo.setAmount(new BigDecimal(txnsLog.getAmount()));
			tradeInfo.setCharge(new BigDecimal(txnsLog.getTxnfee()));
			tradeInfo.setTxnseqno(txnsLog.getTxnseqno());
			tradeInfo.setBusiCode(BusinessCodeEnum.WITHDRAWALS.getBusiCode());
			tradeInfo.setAccess_coopInstCode(txnsLog.getAccfirmerno());
			tradeInfo.setCoopInstCode(txnsLog.getAcccoopinstino());
			// 记录分录流水
			accEntryService.accEntryProcess(tradeInfo,EntryEvent.AUDIT_APPLY);
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
