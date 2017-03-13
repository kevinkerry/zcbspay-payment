package com.zcbspay.platform.payment.order.service.concentrate.impl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zcbspay.platform.member.coopinsti.service.CoopInstiProductService;
import com.zcbspay.platform.member.coopinsti.service.CoopInstiService;
import com.zcbspay.platform.member.individual.bean.MemberBean;
import com.zcbspay.platform.member.individual.bean.enums.MemberType;
import com.zcbspay.platform.member.individual.service.MemberInfoService;
import com.zcbspay.platform.member.merchant.bean.MerchantBean;
import com.zcbspay.platform.member.merchant.service.MerchService;
import com.zcbspay.platform.payment.bean.ResultBean;
import com.zcbspay.platform.payment.commons.utils.DateUtil;
import com.zcbspay.platform.payment.commons.utils.ValidateLocator;
import com.zcbspay.platform.payment.order.bean.BaseOrderBean;
import com.zcbspay.platform.payment.order.consume.bean.ConcentrateSingleOrderBean;
import com.zcbspay.platform.payment.order.consumer.enums.TradeStatFlagEnum;
import com.zcbspay.platform.payment.order.dao.OrderCollectSingleDAO;
import com.zcbspay.platform.payment.order.dao.OrderPaymentSingleDAO;
import com.zcbspay.platform.payment.order.dao.ProdCaseDAO;
import com.zcbspay.platform.payment.order.dao.TxncodeDefDAO;
import com.zcbspay.platform.payment.order.dao.pojo.OrderCollectSingleDO;
import com.zcbspay.platform.payment.order.dao.pojo.OrderPaymentSingleDO;
import com.zcbspay.platform.payment.order.dao.pojo.PojoProdCase;
import com.zcbspay.platform.payment.order.dao.pojo.PojoTxncodeDef;
import com.zcbspay.platform.payment.order.enums.BusiTypeEnum;
import com.zcbspay.platform.payment.order.exception.OrderException;
import com.zcbspay.platform.payment.order.sequence.SerialNumberService;
import com.zcbspay.platform.payment.order.service.CommonOrderService;
import com.zcbspay.platform.payment.order.service.concentrate.ConcentrateOrderService;
import com.zcbspay.platform.payment.order.service.consume.AbstractConsumeOrderService;
import com.zcbspay.platform.payment.pojo.PojoTxnsLog;
@Service
public class ConcentrateOrderServiceImpl implements ConcentrateOrderService {
	
	private static final Logger logger = LoggerFactory.getLogger(AbstractConsumeOrderService.class);
	@Autowired
	private SerialNumberService serialNumberService;
	@Autowired
	private CommonOrderService commonOrderService;
	@Autowired
	private CoopInstiService coopInstiService;
	@Autowired
	private MerchService merchService;
	@Autowired
	private MemberInfoService memberInfoService;
	@Autowired
	private TxncodeDefDAO txncodeDefDAO;
	@Autowired
	private CoopInstiProductService coopInstiProductService;
	
	@Autowired
	private OrderCollectSingleDAO orderCollectSingleDAO;
	@Autowired
	private OrderPaymentSingleDAO orderPaymentSingleDAO;
	@Autowired
	private ProdCaseDAO prodCaseDAO;
	@Override
	public String createRealTimeCollectionOrder(
			ConcentrateSingleOrderBean orderBean) {
		/**
		 * 1.检查订单是否为二次支付
		 * 2.检查订单是否为二次提交
		 * 3.检查订单业务有效性
		 * 4.检查商户和合作机构有效性
		 * 5.检查消费订单特殊性要求检查，如果没有可以为空
		 * 6.检查消费订单特殊性要求检查，如果没有可以为空
		 * 7.保存订单信息
		 */
		String tn = null;
		try {
			tn = checkOfSecondPay(orderBean);
			if(StringUtils.isNotEmpty(tn)){
				return tn;
			}
			checkOfOrder(orderBean);
			checkOfRepeatSubmit(orderBean);
			checkOfBusiness(orderBean);
			tn = saveCollectionOrder(orderBean);
		} catch (OrderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tn;
	}
	
	
	/**
	 * 检查订单二次支付
	 * @param baseOrderBean
	 * @return 受理订单号 tn
	 * @throws OrderException
	 */
	public String checkOfSecondPay(ConcentrateSingleOrderBean orderBean) throws OrderException{
		OrderCollectSingleDO orderinfo = orderCollectSingleDAO.getOrderinfoByOrderNoAndMerchNo(orderBean.getOrderId(), orderBean.getMerchNo());
		if(orderinfo==null){
			return null;
		}
		if(orderinfo.getTxnamt().longValue()!=Long.valueOf(orderBean.getTxnAmt()).longValue()){
			logger.info("订单金额:{};数据库订单金额:{}", orderBean.getTxnAmt(),orderinfo.getTxnamt());
			throw new OrderException("OD015");
		}
		
		if(!orderinfo.getOrdercommitime().equals(orderBean.getTxnTime())){
			logger.info("订单时间:{};数据库订单时间:{}", orderBean.getTxnTime(),orderinfo.getOrdercommitime());
			throw new OrderException("OD016");
		}
		return orderinfo.getTn();
	}
	
	public void checkOfOrder(BaseOrderBean baseOrderBean) throws OrderException{
		ResultBean resultBean = null;
		resultBean = ValidateLocator.validateBeans(baseOrderBean);
		if(!resultBean.isResultBool()){
			throw new OrderException("OD049", resultBean.getErrMsg());
		}
	}
	
	

	/**
	 * 检查订单是否为二次提交
	 * @param orderBean
	 * @throws OrderException
	 */
	
	public void checkOfRepeatSubmit(ConcentrateSingleOrderBean orderBean) throws OrderException{
		OrderCollectSingleDO orderInfo = orderCollectSingleDAO.getOrderinfoByOrderNoAndMerchNo(orderBean.getOrderId(), orderBean.getMerchNo());
		if (orderInfo != null) {
			if ("00".equals(orderInfo.getStatus())) {// 交易成功订单不可二次支付
				throw new OrderException("OD001","订单交易成功，请不要重复支付");
			}
			if ("02".equals(orderInfo.getStatus())) {
				throw new OrderException("OD002","订单正在支付中，请不要重复支付");
			}
			if ("04".equals(orderInfo.getStatus())) {
				throw new OrderException("OD003","订单失效");
			}
			
		}
	}
	
	/**
	 * 检查订单业务有效性
	 * @param orderBean
	 * @throws OrderException
	 */
	public void checkOfBusiness(ConcentrateSingleOrderBean orderBean) throws OrderException {
		PojoTxncodeDef busiModel = txncodeDefDAO.getBusiCode(orderBean.getTxnType(), orderBean.getTxnSubType(), orderBean.getBizType());
        if(busiModel==null){
        	throw new OrderException("OD045");
        }
        BusiTypeEnum busiTypeEnum = BusiTypeEnum.fromValue(busiModel.getBusitype());
        if(busiTypeEnum==BusiTypeEnum.CONCENTRATE){//集中代收付业务
        	if(StringUtils.isEmpty(orderBean.getMerchNo())){
        		 throw new OrderException("OD004");
        	}
        	MerchantBean member = merchService.getMerchBymemberId(orderBean.getMerchNo());//memberService.getMemberByMemberId(order.getMerId());.java
        	if(member==null){
        		throw new OrderException("OD009");
        	}
        	PojoProdCase prodCase= prodCaseDAO.getMerchProd(member.getPrdtVer(),busiModel.getBusicode());
            if(prodCase==null){
                throw new OrderException("OD005");
            }
        }else{
            throw new OrderException("OD045");
        }
	}
	public String saveCollectionOrder(ConcentrateSingleOrderBean orderBean) throws OrderException {
		String txnseqno = serialNumberService.generateTxnseqno();
		String TN = serialNumberService.generateTN(orderBean.getMerchNo());
		OrderCollectSingleDO collectSingleOrder = generateOrderInfoBean(orderBean);
		collectSingleOrder.setTn(TN);
		collectSingleOrder.setRelatetradetxn(txnseqno);
		orderCollectSingleDAO.saveSingleCollectOrder(collectSingleOrder);
		// 保存交易流水
		PojoTxnsLog txnsLog = generateTxnsLog(orderBean);
		txnsLog.setTxnseqno(txnseqno);
		commonOrderService.saveTxnsLog(txnsLog);
		return collectSingleOrder.getTn();
	}
	
	private OrderCollectSingleDO generateOrderInfoBean(
			ConcentrateSingleOrderBean orderBean) {
		OrderCollectSingleDO orderCollectSingle = new OrderCollectSingleDO();
		orderCollectSingle.setVersion(orderBean.getVersion());
		orderCollectSingle.setAccesstype(orderBean.getAccessType());
		orderCollectSingle.setCoopinstiid(orderBean.getCoopinstiId());
		orderCollectSingle.setMerid(orderBean.getMerchNo());
		orderCollectSingle.setEncoding(orderBean.getEncoding());
		orderCollectSingle.setTxntype(orderBean.getTxnType());
		orderCollectSingle.setTxnsubtype(orderBean.getTxnSubType());
		orderCollectSingle.setBiztype(orderBean.getBizType());
		orderCollectSingle.setBackurl(orderBean.getBackUrl());
		orderCollectSingle.setMername(orderBean.getMerName());
		orderCollectSingle.setMerabbr(orderBean.getMerAbbr());
		orderCollectSingle.setOrderid(orderBean.getOrderId());
		orderCollectSingle.setTxntime(orderBean.getTxnTime());
		orderCollectSingle.setPaytimeout(orderBean.getPayTimeout());
		orderCollectSingle.setTxnamt(Long.valueOf(orderBean.getTxnAmt()));
		orderCollectSingle.setCurrencycode(orderBean.getCurrencyCode());
		orderCollectSingle.setDebtorbank(orderBean.getDebtorBank());
		orderCollectSingle.setDebtoraccount(orderBean.getDebtorAccount());
		orderCollectSingle.setDebtorname(orderBean.getDebtorName());
		orderCollectSingle.setDebtorconsign(orderBean.getDebtorConsign());
		orderCollectSingle.setCreditorbank(orderBean.getCreditorBank());
		orderCollectSingle.setCreditoraccount(orderBean.getCreditorAccount());
		orderCollectSingle.setCreditorname(orderBean.getCreditorName());
		orderCollectSingle.setProprietary(orderBean.getProprietary());
		orderCollectSingle.setSummary(orderBean.getSummary());
		orderCollectSingle.setOrderdesc(orderBean.getOrderDesc());
		orderCollectSingle.setReserved(orderBean.getReserved());
		orderCollectSingle.setStatus("01");
		orderCollectSingle.setOrdercommitime(orderBean.getTxnTime());
		return orderCollectSingle;
	}

	private PojoTxnsLog generateTxnsLog(ConcentrateSingleOrderBean orderBean) {
		PojoTxnsLog txnsLog = new PojoTxnsLog();
		MerchantBean member = null;
		PojoTxncodeDef busiModel = txncodeDefDAO.getBusiCode(orderBean.getTxnType(), orderBean.getTxnSubType(),orderBean.getBizType());
		member = merchService.getMerchBymemberId(orderBean.getMerchNo());
		txnsLog.setRiskver(member.getRiskVer());
		txnsLog.setSplitver(member.getSpiltVer());
		txnsLog.setFeever(member.getFeeVer());
		txnsLog.setPrdtver(member.getPrdtVer());
		txnsLog.setRoutver(member.getRoutVer());
		txnsLog.setAccsettledate(DateUtil.getSettleDate(Integer.valueOf(member.getSetlCycle().toString())));
		txnsLog.setTxndate(DateUtil.getCurrentDate());
		txnsLog.setTxntime(DateUtil.getCurrentTime());
		txnsLog.setBusicode(busiModel.getBusicode());
		txnsLog.setBusitype(busiModel.getBusitype());
		txnsLog.setTradcomm(0L);
		txnsLog.setAmount(Long.valueOf(orderBean.getTxnAmt()));
		txnsLog.setAccordno(orderBean.getOrderId());
		txnsLog.setAccfirmerno(orderBean.getCoopinstiId());
		txnsLog.setAcccoopinstino(orderBean.getCoopinstiId());
		txnsLog.setAccsecmerno(orderBean.getMerchNo());
		txnsLog.setAccordcommitime(DateUtil.getCurrentDateTime());
		txnsLog.setTradestatflag(TradeStatFlagEnum.INITIAL.getStatus());// 交易初始状态
		txnsLog.setAccmemberid("999999999999999");// 匿名会员号
		return txnsLog;
	}


	


	
	
	
	
	
	
	
	
	
	@Override
	public String createRealTimePaymentOrder(
			ConcentrateSingleOrderBean orderBean) {
		/**
		 * 1.检查订单是否为二次支付
		 * 2.检查订单是否为二次提交
		 * 3.检查订单业务有效性
		 * 4.检查商户和合作机构有效性
		 * 5.检查消费订单特殊性要求检查，如果没有可以为空
		 * 6.检查消费订单特殊性要求检查，如果没有可以为空
		 * 7.保存订单信息
		 */
		String tn = null;
		try {
			tn = checkOfSecondPay(orderBean);
			if(StringUtils.isNotEmpty(tn)){
				return null;
			}
			checkOfOrder(orderBean);
			checkOfRepeatSubmit(orderBean);
			checkOfBusiness(orderBean);
			tn = savePaymentOrder(orderBean);
		} catch (OrderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tn;
	}

	public String savePaymentOrder(ConcentrateSingleOrderBean orderBean) throws OrderException {
		String txnseqno = serialNumberService.generateTxnseqno();
		String TN = serialNumberService.generateTN(orderBean.getMerchNo());
		OrderPaymentSingleDO orderPaymentSingle = generatePaymentOrderInfoBean(orderBean);
		orderPaymentSingle.setTn(TN);
		orderPaymentSingle.setRelatetradetxn(txnseqno);
		orderPaymentSingleDAO.savePaymentSingleOrder(orderPaymentSingle);
		// 保存交易流水
		PojoTxnsLog txnsLog = generateTxnsLog(orderBean);
		txnsLog.setTxnseqno(txnseqno);
		commonOrderService.saveTxnsLog(txnsLog);
		return orderPaymentSingle.getTn();
	}
	
	private OrderPaymentSingleDO generatePaymentOrderInfoBean(
			ConcentrateSingleOrderBean orderBean) {
		OrderPaymentSingleDO orderPaymentSingle = new OrderPaymentSingleDO();
		orderPaymentSingle.setVersion(orderBean.getVersion());
		orderPaymentSingle.setAccesstype(orderBean.getAccessType());
		orderPaymentSingle.setCoopinstiid(orderBean.getCoopinstiId());
		orderPaymentSingle.setMerid(orderBean.getMerchNo());
		orderPaymentSingle.setEncoding(orderBean.getEncoding());
		orderPaymentSingle.setTxntype(orderBean.getTxnType());
		orderPaymentSingle.setTxnsubtype(orderBean.getTxnSubType());
		orderPaymentSingle.setBiztype(orderBean.getBizType());
		orderPaymentSingle.setBackurl(orderBean.getBackUrl());
		//orderCollectSingle.setMername(orderBean.getMerName());
		//orderCollectSingle.setMerabbr(orderBean.getMerAbbr());
		orderPaymentSingle.setOrderid(orderBean.getOrderId());
		orderPaymentSingle.setTxntime(orderBean.getTxnTime());
		//orderCollectSingle.setPaytimeout(orderBean.getPayTimeout());
		orderPaymentSingle.setTxnamt(Long.valueOf(orderBean.getTxnAmt()));
		orderPaymentSingle.setCurrencycode(orderBean.getCurrencyCode());
		orderPaymentSingle.setDebtorbank(orderBean.getDebtorBank());
		orderPaymentSingle.setDebtoraccount(orderBean.getDebtorAccount());
		orderPaymentSingle.setDebtorname(orderBean.getDebtorName());
		orderPaymentSingle.setDebtorconsign(orderBean.getDebtorConsign());
		orderPaymentSingle.setCreditorbank(orderBean.getCreditorBank());
		orderPaymentSingle.setCreditoraccount(orderBean.getCreditorAccount());
		orderPaymentSingle.setCreditorname(orderBean.getCreditorName());
		orderPaymentSingle.setProprietary(orderBean.getProprietary());
		orderPaymentSingle.setSummary(orderBean.getSummary());
		//orderCollectSingle.setOrderdesc(orderBean.getOrderDesc());
		orderPaymentSingle.setReserved(orderBean.getReserved());
		orderPaymentSingle.setStatus("01");
		orderPaymentSingle.setOrdercommitime(orderBean.getTxnTime());
		return orderPaymentSingle;
	}

}
