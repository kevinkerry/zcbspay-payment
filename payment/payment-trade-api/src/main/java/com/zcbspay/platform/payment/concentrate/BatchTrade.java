package com.zcbspay.platform.payment.concentrate;

import com.zcbspay.platform.payment.bean.ResultBean;
import com.zcbspay.platform.payment.exception.ConcentrateTradeException;

/**
 * 集中代收付批量交易接口
 *
 * @author guojia
 * @version
 * @date 2017年3月10日 下午3:10:32
 * @since
 */
public interface BatchTrade {
	/**
	 * 批量代收交易接口
	 * @param realTimeCollectionVChargesBean
	 * @return
	 * @throws ConcentrateTradeException 
	 */
	public ResultBean collectionCharges(String tn) throws ConcentrateTradeException;
	
	/**
	 * 批量代付交易接口
	 * @param realTimepaymentByAgencyBean
	 * @return
	 * @throws ConcentrateTradeException 
	 */
	public ResultBean paymentByAgency(String tn) throws ConcentrateTradeException;
}
