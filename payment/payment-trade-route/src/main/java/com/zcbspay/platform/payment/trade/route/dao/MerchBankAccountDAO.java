package com.zcbspay.platform.payment.trade.route.dao;

import com.zcbspay.platform.payment.commons.dao.BaseDAO;
import com.zcbspay.platform.payment.trade.route.exception.TradeRouteException;
import com.zcbspay.platform.payment.trade.route.pojo.MerchBankAccountDO;

public interface MerchBankAccountDAO extends BaseDAO<MerchBankAccountDO>{

	/**
	 * 通过商户账户和协议类型查询交易渠道
	 * @param merchNo
	 * @param accountno
	 * @param protocoltype
	 * @return
	 */
	public MerchBankAccountDO getTradeRoute(String merchNo,String accountno,String protocoltype) throws TradeRouteException;
}
