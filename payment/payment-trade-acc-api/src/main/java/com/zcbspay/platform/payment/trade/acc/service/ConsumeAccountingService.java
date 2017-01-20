package com.zcbspay.platform.payment.trade.acc.service;

import com.zcbspay.platform.payment.trade.acc.bean.ResultBean;
import com.zcbspay.platform.payment.trade.acc.bean.TxnsLogBean;

/**
 * 消费类交易账务接口
 * @author guojia
 *
 */
public interface ConsumeAccountingService {

	public ResultBean consumeAccounting(TxnsLogBean txnsLogBean);
}
