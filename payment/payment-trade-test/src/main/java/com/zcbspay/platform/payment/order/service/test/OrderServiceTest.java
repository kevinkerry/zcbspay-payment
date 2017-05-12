package com.zcbspay.platform.payment.order.service.test;

import java.util.List;

import org.junit.Test;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.common.collect.Lists;
import com.zcbspay.platform.payment.exception.PaymentOrderException;
import com.zcbspay.platform.payment.order.bean.ConcentrateBatchOrderBean;
import com.zcbspay.platform.payment.order.bean.ConcentrateOrderDetaBean;
import com.zcbspay.platform.payment.order.bean.InsteadPayOrderBean;
import com.zcbspay.platform.payment.order.bean.RealTimeCollectionChargesBean;
import com.zcbspay.platform.payment.order.bean.RealTimepaymentByAgencyBean;
import com.zcbspay.platform.payment.order.bean.SimpleOrderBean;
import com.zcbspay.platform.payment.order.service.ConcentrateOrderService;
import com.zcbspay.platform.payment.order.service.OrderService;
import com.zcbspay.platform.payment.utils.DateUtil;


public class OrderServiceTest extends BaseTest{

	@Reference(version="1.0")
	private OrderService orderService;
	//@Reference(version="1.0")
	//public MerchService merchService;
	@Reference(version="1.0")
	public ConcentrateOrderService concentrateOrderService;
	@Test
	public void testAll() throws PaymentOrderException{
		long currentTime = System.currentTimeMillis();
		
		//集中代收
		//test_collect_order();
		//集中代付
		//test_payment_order();
		//集中代收-批量
		test_collect_order_batch();
		//集中代付-批量
		test_payment_order_batch();
		System.out.println("excute time:"+(System.currentTimeMillis()-currentTime));
	}
	
	public void test_payment_order_batch() {
		try {
			ConcentrateBatchOrderBean concentrateBatchOrderBean = new ConcentrateBatchOrderBean();
			concentrateBatchOrderBean.setVersion("1.0");
			concentrateBatchOrderBean.setEncoding("1");
			concentrateBatchOrderBean.setTxnType("02");
			concentrateBatchOrderBean.setTxnSubType("00");
			concentrateBatchOrderBean.setBizType("000003");
			concentrateBatchOrderBean.setMerId("200000000001573");
			concentrateBatchOrderBean.setBackUrl("");
			concentrateBatchOrderBean.setBatchNo(DateUtil.getCurrentDate()+System.currentTimeMillis()+"");
			concentrateBatchOrderBean.setTxnTime(DateUtil.getCurrentDateTime());
			concentrateBatchOrderBean.setTotalQty("1");
			concentrateBatchOrderBean.setTotalAmt("100"); 
			concentrateBatchOrderBean.setReserved("");
			List<ConcentrateOrderDetaBean> detaList = Lists.newArrayList();
			for(int i=0;i<1;i++){
				ConcentrateOrderDetaBean detaBean = new ConcentrateOrderDetaBean();
				detaBean.setOrderId(System.currentTimeMillis()+"");
				detaBean.setCurrencyCode("156");
				detaBean.setAmt("100");
				
				detaBean.setDebtorBank("308290003126");//收款人开户行
				detaBean.setDebtorAccount("213582310310001");
				detaBean.setDebtorName("上海宜赋通信息技术有限公司");
				
				detaBean.setCreditorBank("103100004360");//付款人银行号
				detaBean.setCreditorAccount("6228480018543668976");
				detaBean.setCreditorName("郭佳");
				detaBean.setDebtorConsign("098765400001");
				
				detaBean.setProprietary("Y0");
				detaBean.setSummary("test"+i);
				detaList.add(detaBean);
			}
			concentrateBatchOrderBean.setDetaList(detaList);
			String tn = concentrateOrderService.createPaymentByAgencyBatchOrder(concentrateBatchOrderBean);
			System.out.println("test_payment_order_batch TN:"+tn);
		} catch (PaymentOrderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void test_collect_order_batch() {
		try {
			ConcentrateBatchOrderBean concentrateBatchOrderBean = new ConcentrateBatchOrderBean();
			concentrateBatchOrderBean.setVersion("1.0");
			concentrateBatchOrderBean.setEncoding("1");
			concentrateBatchOrderBean.setTxnType("01");
			concentrateBatchOrderBean.setTxnSubType("00");
			concentrateBatchOrderBean.setBizType("000003");
			concentrateBatchOrderBean.setMerId("200000000001573");
			concentrateBatchOrderBean.setBackUrl("");
			concentrateBatchOrderBean.setBatchNo(DateUtil.getCurrentDate()+System.currentTimeMillis()+"");
			concentrateBatchOrderBean.setTxnTime(DateUtil.getCurrentDateTime());
			concentrateBatchOrderBean.setTotalQty("1");
			concentrateBatchOrderBean.setTotalAmt("100"); 
			concentrateBatchOrderBean.setReserved("");
			List<ConcentrateOrderDetaBean> detaList = Lists.newArrayList();
			for(int i=0;i<1;i++){
				ConcentrateOrderDetaBean detaBean = new ConcentrateOrderDetaBean();
				detaBean.setOrderId(System.currentTimeMillis()+"");
				detaBean.setCurrencyCode("156");
				detaBean.setAmt("100");
				detaBean.setDebtorBank("3310110301");//付款人银行号
				detaBean.setDebtorAccount("6228480018543668976");
				detaBean.setDebtorName("郭佳");
				detaBean.setCreditorBank("3310130601");//收款人开户行
				detaBean.setCreditorAccount("213582310310001");
				detaBean.setCreditorName("上海宜赋通信息技术有限公司");
				detaBean.setDebtorConsign("09876540000000000002");
				
				detaBean.setProprietary("50");
				detaBean.setSummary("test"+i);
				detaList.add(detaBean);
			}
			concentrateBatchOrderBean.setDetaList(detaList);
			String tn = concentrateOrderService.createCollectionChargesBatchOrder(concentrateBatchOrderBean);
			System.out.println("test_collect_order_batch TN:"+tn);
		} catch (PaymentOrderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void test_payment_order(){
		try {
			RealTimepaymentByAgencyBean realTimepaymentByAgencyBean = new RealTimepaymentByAgencyBean();
			realTimepaymentByAgencyBean.setVersion("1.0");
			realTimepaymentByAgencyBean.setEncoding("1");
			realTimepaymentByAgencyBean.setTxnType("02");
			realTimepaymentByAgencyBean.setTxnSubType("00");
			realTimepaymentByAgencyBean.setBizType("000002");
			realTimepaymentByAgencyBean.setBackUrl("");
			realTimepaymentByAgencyBean.setMerchNo("200000000000610");
			realTimepaymentByAgencyBean.setMerAbbr("");
			realTimepaymentByAgencyBean.setOrderId(System.currentTimeMillis()+"");
			realTimepaymentByAgencyBean.setTxnTime(DateUtil.getCurrentDateTime());
			realTimepaymentByAgencyBean.setPayTimeout("20180202000000");
			realTimepaymentByAgencyBean.setTxnAmt("12");
			realTimepaymentByAgencyBean.setCurrencyCode("156");
			realTimepaymentByAgencyBean.setOrderDesc("集中代收实时测试");
			realTimepaymentByAgencyBean.setDebtorBank("203121000010");
			realTimepaymentByAgencyBean.setDebtorAccount("6228480018543668979");
			realTimepaymentByAgencyBean.setDebtorName("测试账户1");
			realTimepaymentByAgencyBean.setDebtorConsign("1234567");
			realTimepaymentByAgencyBean.setCreditorBank("203121000010");
			realTimepaymentByAgencyBean.setCreditorAccount("6228480018543668970");
			realTimepaymentByAgencyBean.setCreditorName("测试账户2");
			realTimepaymentByAgencyBean.setProprietary("09001");
			String tn = concentrateOrderService.createPaymentByAgencyOrder(realTimepaymentByAgencyBean );
			System.out.println("createCollectionChargesOrder TN:"+tn);
		} catch (PaymentOrderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void test_collect_order(){
		RealTimeCollectionChargesBean realTimeCollectionChargesBean = new RealTimeCollectionChargesBean();
		realTimeCollectionChargesBean.setVersion("1.0");
		realTimeCollectionChargesBean.setEncoding("1");
		realTimeCollectionChargesBean.setTxnType("01");
		realTimeCollectionChargesBean.setTxnSubType("00");
		realTimeCollectionChargesBean.setBizType("000002");
		realTimeCollectionChargesBean.setBackUrl("");
		realTimeCollectionChargesBean.setMerchNo("200000000000610");
		realTimeCollectionChargesBean.setMerName("");
		realTimeCollectionChargesBean.setMerAbbr("");
		realTimeCollectionChargesBean.setOrderId(System.currentTimeMillis()+"");
		realTimeCollectionChargesBean.setTxnTime(DateUtil.getCurrentDateTime());
		realTimeCollectionChargesBean.setPayTimeout("20180202000000");
		realTimeCollectionChargesBean.setTxnAmt("12");
		realTimeCollectionChargesBean.setCurrencyCode("156");
		realTimeCollectionChargesBean.setOrderDesc("集中代收实时测试");
		realTimeCollectionChargesBean.setDebtorBank("203121000010");
		realTimeCollectionChargesBean.setDebtorAccount("6228480018543668979");
		realTimeCollectionChargesBean.setDebtorName("测试账户1");
		realTimeCollectionChargesBean.setDebtorConsign("1234567");
		realTimeCollectionChargesBean.setCreditorBank("203121000010");
		realTimeCollectionChargesBean.setCreditorAccount("6228480018543668970");
		realTimeCollectionChargesBean.setCreditorName("测试账户2");
		realTimeCollectionChargesBean.setProprietary("09001");
		
		try {
			String tn = concentrateOrderService.createCollectionChargesOrder(realTimeCollectionChargesBean);
			System.out.println("createCollectionChargesOrder TN:"+tn);
		} catch (PaymentOrderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void test_consume_order(){
		SimpleOrderBean orderBean = new SimpleOrderBean();
		orderBean.setBizType("000201");
		orderBean.setTxnType("01");
		orderBean.setTxnSubType("00");
		orderBean.setCoopInstiId("300000000000004");
		orderBean.setCurrencyCode("156");
		orderBean.setMerId("200000000000610");
		orderBean.setTxnTime(DateUtil.getCurrentDateTime());
		orderBean.setTxnAmt("201");
		orderBean.setOrderTimeout("20170202000000");
		orderBean.setMemberId("999999999999999");
		orderBean.setOrderId(System.currentTimeMillis()+"");
		try {
			String order = orderService.createConsumeOrder(orderBean);
			System.out.println("createConsumeOrder TN:"+order);
		} catch (PaymentOrderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void test_insteadPay_order() throws PaymentOrderException{
		
		InsteadPayOrderBean insteadPayOrderBean = new InsteadPayOrderBean();
		insteadPayOrderBean.setBizType("000207");
		insteadPayOrderBean.setTxnType("70");
		insteadPayOrderBean.setTxnSubType("00");
		insteadPayOrderBean.setCoopInstiId("300000000000004");
		insteadPayOrderBean.setCurrencyCode("156");
		insteadPayOrderBean.setMerId("200000000000610");
		insteadPayOrderBean.setTxnTime(DateUtil.getCurrentDateTime());
		insteadPayOrderBean.setTxnAmt("2");
		insteadPayOrderBean.setOrderId(System.currentTimeMillis()+"");
		insteadPayOrderBean.setAccNo("6228480018543668979");
		insteadPayOrderBean.setAccName("郭佳");
		insteadPayOrderBean.setAccType("01");
		insteadPayOrderBean.setCertifId("110105198610094112");
		insteadPayOrderBean.setCertifTp("01");
		insteadPayOrderBean.setPhoneNo("18600806796");
		String createInsteadPayOrder = orderService.createInsteadPayOrder(insteadPayOrderBean);
		System.out.println("createInsteadPayOrder:"+createInsteadPayOrder);
		
	}
}
