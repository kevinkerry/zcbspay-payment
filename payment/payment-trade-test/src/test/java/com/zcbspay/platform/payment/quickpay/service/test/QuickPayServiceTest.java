package com.zcbspay.platform.payment.quickpay.service.test;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.zcbspay.platform.payment.bean.ResultBean;
import com.zcbspay.platform.payment.exception.PaymentInsteadPayException;
import com.zcbspay.platform.payment.exception.PaymentQuickPayException;
import com.zcbspay.platform.payment.exception.PaymentRouterException;
import com.zcbspay.platform.payment.quickpay.bean.InsteadPayOrderBean;
import com.zcbspay.platform.payment.quickpay.bean.PayBean;
import com.zcbspay.platform.payment.quickpay.service.QuickPayService;
import com.zcbspay.platform.payment.quickpay.service.RealTimeInsteadPayService;
import com.zcbspay.platform.payment.utils.DateUtil;

public class QuickPayServiceTest extends BaseTest{

	private static final Logger logger = LoggerFactory.getLogger(QuickPayServiceTest.class);
	@Reference(version="1.0")
	private QuickPayService quickPayService;
	@Reference(version="1.0")
	private RealTimeInsteadPayService realTimeInsteadPayService;
	
	@Test
	public void testAll(){
		//实时代收
		test_pay("170222061000000028");
		//实时代付
		//test_instead_pay("170220061000000009");
	}
	
	
	
	//@Test
	public void test_pay(String tn){
		PayBean payBean = new PayBean();
		payBean.setCardNo("6228480018543668979");
		payBean.setCardKeeper("郭佳");
		payBean.setCardType("1");
		payBean.setPhone("18600806796");
		payBean.setCertNo("110105198610094112");
		payBean.setTn(tn);
		payBean.setTxnAmt("201");
		try {
			ResultBean pay = quickPayService.pay(payBean);
			logger.info(JSON.toJSONString(pay));
		} catch (PaymentQuickPayException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PaymentRouterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//@Test
	public void test_instead_pay(String tn){
		com.zcbspay.platform.payment.quickpay.bean.InsteadPayOrderBean insteadPayOrderBean = new InsteadPayOrderBean();
		insteadPayOrderBean.setBizType("000207");
		insteadPayOrderBean.setTxnType("70");
		insteadPayOrderBean.setTxnSubType("00");
		insteadPayOrderBean.setCoopInstiId("300000000000004");
		insteadPayOrderBean.setCurrencyCode("156");
		insteadPayOrderBean.setMerId("200000000000610");
		insteadPayOrderBean.setTxnTime(DateUtil.getCurrentDateTime());
		insteadPayOrderBean.setTxnAmt("2");
		insteadPayOrderBean.setAccNo("6228480018543668979");
		insteadPayOrderBean.setAccName("郭佳");
		insteadPayOrderBean.setAccType("01");
		insteadPayOrderBean.setCertifId("110105198610094112");
		insteadPayOrderBean.setCertifTp("01");
		insteadPayOrderBean.setPhoneNo("18600806796");
		insteadPayOrderBean.setTn(tn);
		insteadPayOrderBean.setOrderId("1485068751913");
		try {
			ResultBean singleInsteadPay = realTimeInsteadPayService.singleInsteadPay(insteadPayOrderBean);
			logger.info(JSON.toJSONString(singleInsteadPay));
		} catch (PaymentInsteadPayException | PaymentQuickPayException
				| PaymentRouterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
