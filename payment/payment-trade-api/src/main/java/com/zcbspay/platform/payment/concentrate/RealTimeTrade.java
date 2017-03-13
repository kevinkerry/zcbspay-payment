package com.zcbspay.platform.payment.concentrate;

import com.zcbspay.platform.payment.bean.ResultBean;
import com.zcbspay.platform.payment.concentrate.bean.RealTimeCollectionChargesBean;
import com.zcbspay.platform.payment.concentrate.bean.RealTimepaymentByAgencyBean;

/**
 * 集中代收付实时交易接口
 *
 * @author guojia
 * @version
 * @date 2017年3月10日 下午3:10:53
 * @since
 */
public interface RealTimeTrade {

	/**
	 * 实时代收交易接口
	 * @param realTimeCollectionVChargesBean
	 * @return
	 */
	public ResultBean collectionCharges(RealTimeCollectionChargesBean realTimeCollectionVChargesBean);
	
	/**
	 * 实时代付交易接 口
	 * @param realTimepaymentByAgencyBean
	 * @return
	 */
	public ResultBean paymentByAgency(RealTimepaymentByAgencyBean realTimepaymentByAgencyBean);
}
