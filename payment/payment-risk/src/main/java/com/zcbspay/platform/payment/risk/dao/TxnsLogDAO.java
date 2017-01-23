/* 
 * TxnsLogDAO.java  
 * 
 * version TODO
 *
 * 2016年9月13日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zcbspay.platform.payment.risk.dao;

import java.util.Map;

import com.zcbspay.platform.payment.commons.dao.BaseDAO;
import com.zcbspay.platform.payment.risk.pojo.PojoTxnsLog;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年9月13日 下午5:31:41
 * @since 
 */
public interface TxnsLogDAO extends BaseDAO<PojoTxnsLog>{
	/**
	 * 根据交易流水号获取交易流水数据
	 * @param txnseqno 交易流水号
	 * @return 交易流水数据pojo
	 */
	public PojoTxnsLog getTxnsLogByTxnseqno(String txnseqno);
	/**
	 * 获取银行卡信息
	 * @param cardNo 银行卡号
	 * @return
	 */
	public Map<String, Object> getCardInfo(String cardNo);
}
