package com.zcbspay.platform.payment.order.service.test;

import org.junit.Test;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.zcbspay.platform.member.merchant.bean.MerchantBean;
import com.zcbspay.platform.member.merchant.service.MerchService;
import com.zcbspay.platform.payment.exception.PaymentOrderException;
import com.zcbspay.platform.payment.order.bean.InsteadPayOrderBean;
import com.zcbspay.platform.payment.order.bean.SimpleOrderBean;
import com.zcbspay.platform.payment.order.service.OrderService;
import com.zcbspay.platform.payment.utils.DateUtil;


public class OrderServiceTest extends BaseTest{

	@Reference(version="1.0")
	private OrderService orderService;
	@Reference(version="1.0")
	public MerchService merchService;
	//@Test
	public void test_consume_order(){
		SimpleOrderBean orderBean = new SimpleOrderBean();
		orderBean.setBizType("000201");
		orderBean.setTxnType("01");
		orderBean.setTxnSubType("00");
		orderBean.setCoopInstiId("300000000000004");
		orderBean.setCurrencyCode("156");
		orderBean.setMerId("200000000000610");
		orderBean.setTxnTime(DateUtil.getCurrentDateTime());
		orderBean.setTxnAmt("2");
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
	//@Test
	public void test_query_merch(){
		MerchantBean merchantBean = merchService.getMerchBymemberId("200000000000610");
		System.out.println(JSON.toJSONString(merchantBean));
	}
	@Test
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
