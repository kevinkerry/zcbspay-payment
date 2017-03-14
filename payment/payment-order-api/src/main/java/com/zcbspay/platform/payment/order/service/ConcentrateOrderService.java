package com.zcbspay.platform.payment.order.service;

import com.zcbspay.platform.payment.exception.PaymentOrderException;
import com.zcbspay.platform.payment.order.bean.ConcentrateBatchOrderBean;
import com.zcbspay.platform.payment.order.bean.RealTimeCollectionChargesBean;
import com.zcbspay.platform.payment.order.bean.RealTimepaymentByAgencyBean;

/**
 * 集中代收付订单生成接口
 *
 * @author guojia
 * @version
 * @date 2017年3月14日 下午2:49:32
 * @since
 */
public interface ConcentrateOrderService {
	/**
	 * 实时代收订单易接口
	 * @param realTimeCollectionVChargesBean
	 * @return
	 * @throws PaymentOrderException 
	 */
	public String createCollectionChargesOrder(RealTimeCollectionChargesBean realTimeCollectionChargesBean) throws PaymentOrderException;
	
	/**
	 * 实时代付订单接口
	 * @param realTimepaymentByAgencyBean
	 * @return
	 * @throws PaymentOrderException 
	 */
	public String createPaymentByAgencyOrder(RealTimepaymentByAgencyBean realTimepaymentByAgencyBean) throws PaymentOrderException;
	
	/**
	 * 批量代收订单接口
	 * @param concentrateBatchOrderBean
	 * @return
	 * @throws PaymentOrderException
	 */
	public String createCollectionChargesBatchOrder(ConcentrateBatchOrderBean concentrateBatchOrderBean) throws PaymentOrderException;
	/**
	 * 批量代付订单接口
	 * @param concentrateBatchOrderBean
	 * @return
	 * @throws PaymentOrderException
	 */
	public String createPaymentByAgencyBatchOrder(ConcentrateBatchOrderBean concentrateBatchOrderBean) throws PaymentOrderException;
}
