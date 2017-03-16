package com.zcbspay.platform.payment.order.service.concentrate.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zcbspay.platform.member.coopinsti.service.CoopInstiProductService;
import com.zcbspay.platform.member.coopinsti.service.CoopInstiService;
import com.zcbspay.platform.member.individual.service.MemberInfoService;
import com.zcbspay.platform.member.merchant.bean.MerchantBean;
import com.zcbspay.platform.member.merchant.service.MerchService;
import com.zcbspay.platform.payment.bean.ResultBean;
import com.zcbspay.platform.payment.commons.utils.DateUtil;
import com.zcbspay.platform.payment.commons.utils.ValidateLocator;
import com.zcbspay.platform.payment.dao.OrderCollectBatchDAO;
import com.zcbspay.platform.payment.dao.OrderCollectDetaDAO;
import com.zcbspay.platform.payment.dao.OrderCollectSingleDAO;
import com.zcbspay.platform.payment.dao.OrderPaymentBatchDAO;
import com.zcbspay.platform.payment.dao.OrderPaymentDetaDAO;
import com.zcbspay.platform.payment.dao.OrderPaymentSingleDAO;
import com.zcbspay.platform.payment.enums.OrderStatusEnum;
import com.zcbspay.platform.payment.order.bean.BaseOrderBean;
import com.zcbspay.platform.payment.order.consume.bean.ConcentrateBatchOrderBean;
import com.zcbspay.platform.payment.order.consume.bean.ConcentrateOrderDetaBean;
import com.zcbspay.platform.payment.order.consume.bean.ConcentrateSingleOrderBean;
import com.zcbspay.platform.payment.order.consumer.enums.TradeStatFlagEnum;
import com.zcbspay.platform.payment.order.dao.ProdCaseDAO;
import com.zcbspay.platform.payment.order.dao.TxncodeDefDAO;
import com.zcbspay.platform.payment.order.dao.pojo.PojoProdCase;
import com.zcbspay.platform.payment.order.dao.pojo.PojoTxncodeDef;
import com.zcbspay.platform.payment.order.enums.BusiTypeEnum;
import com.zcbspay.platform.payment.order.enums.BusinessEnum;
import com.zcbspay.platform.payment.order.exception.OrderException;
import com.zcbspay.platform.payment.order.sequence.SerialNumberService;
import com.zcbspay.platform.payment.order.service.CommonOrderService;
import com.zcbspay.platform.payment.order.service.concentrate.ConcentrateOrderService;
import com.zcbspay.platform.payment.order.service.consume.AbstractConsumeOrderService;
import com.zcbspay.platform.payment.pojo.OrderCollectBatchDO;
import com.zcbspay.platform.payment.pojo.OrderCollectDetaDO;
import com.zcbspay.platform.payment.pojo.OrderCollectSingleDO;
import com.zcbspay.platform.payment.pojo.OrderPaymentBatchDO;
import com.zcbspay.platform.payment.pojo.OrderPaymentDetaDO;
import com.zcbspay.platform.payment.pojo.OrderPaymentSingleDO;
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
	@Autowired
	private OrderCollectBatchDAO orderCollectBatchDAO;
	@Autowired
	private OrderCollectDetaDAO orderCollectDetaDAO;
	@Autowired
	private OrderPaymentBatchDAO orderPaymentBatchDAO;
	@Autowired
	private OrderPaymentDetaDAO orderPaymentDetaDAO;
	
	
	
	
	@Override
	public String createRealTimeCollectionOrder(ConcentrateSingleOrderBean orderBean) {
		String tn = null;
		try {
			tn = checkOfSecondPay(orderBean);
			if(StringUtils.isNotEmpty(tn)){
				return tn;
			}
			checkOfOrder(orderBean);
			checkOfRepeatSubmit(orderBean);
			checkOfCollectionBusiness(orderBean);
			tn = saveCollectionOrder(orderBean);
		} catch (OrderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tn;
	}
	@Override
	public String createRealTimePaymentOrder(ConcentrateSingleOrderBean orderBean) {
		String tn = null;
		try {
			tn = checkOfSecondPay(orderBean);
			if(StringUtils.isNotEmpty(tn)){
				return null;
			}
			checkOfPaymentSecondPay(orderBean);
			checkOfPaymentRepeatSubmit(orderBean);
			checkOfPaymentBusiness(orderBean);
			tn = savePaymentOrder(orderBean);
		} catch (OrderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tn;
	}
	
	@Override
	public String createBatchCollectionOrder(ConcentrateBatchOrderBean orderBean) throws OrderException {
		checkOfBatchOrder(orderBean);
		checkOfBatchRepeatSubmit(orderBean);
		checkOfBatchCollectionBusiness(orderBean);
		String tn = saveCollectionBatchOrder(orderBean);
		return tn;
	}
	
	@Override
	public String createBatchPaymentOrder(ConcentrateBatchOrderBean orderBean) throws OrderException {
		checkOfBatchOrder(orderBean);
		checkOfPaymentBatchRepeatSubmit(orderBean);
		checkOfBatchPaymentBusiness(orderBean);
		String tn = savePaymentBatchOrder(orderBean);
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
	public void checkOfCollectionBusiness(ConcentrateSingleOrderBean orderBean) throws OrderException {
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
            BusinessEnum businessEnum = BusinessEnum.fromValue(busiModel.getBusicode());
            if(businessEnum!=BusinessEnum.CONCENTRATE_COLLECT_REALTIME){
            	  throw new OrderException("OD045");
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
		orderCollectSingle.setStatus(OrderStatusEnum.INITIAL.value());
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
		//付款方信息
		txnsLog.setPan(orderBean.getDebtorAccount());
		txnsLog.setPanName(orderBean.getDebtorName());
		txnsLog.setCardinstino(orderBean.getDebtorBank());
		//收款信息
		txnsLog.setInpan(orderBean.getCreditorAccount());
		txnsLog.setInpanName(orderBean.getCreditorName());
		txnsLog.setIncardinstino(orderBean.getCreditorBank());
		txnsLog.setAccbusicode(busiModel.getBusicode());
		return txnsLog;
	}


	


	
	
	
	
	
	
	
	
	
	
	private void checkOfPaymentBusiness(ConcentrateSingleOrderBean orderBean) throws OrderException {
		// TODO Auto-generated method stub
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
            BusinessEnum businessEnum = BusinessEnum.fromValue(busiModel.getBusicode());
            if(businessEnum!=BusinessEnum.CONCENTRATE_PAYMENT_REALTIME){
            	  throw new OrderException("OD045");
            }
        }else{
            throw new OrderException("OD045");
        }
	}


	/**
	 * 检查订单是否为二次提交
	 * @param orderBean
	 * @throws OrderException
	 */
	
	public void checkOfPaymentRepeatSubmit(ConcentrateSingleOrderBean orderBean) throws OrderException{
		OrderPaymentSingleDO orderInfo = orderPaymentSingleDAO.getOrderinfoByOrderNoAndMerchNo(orderBean.getOrderId(), orderBean.getMerchNo());
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
	 * 检查订单二次支付
	 * @param baseOrderBean
	 * @return 受理订单号 tn
	 * @throws OrderException
	 */
	public String checkOfPaymentSecondPay(ConcentrateSingleOrderBean orderBean) throws OrderException{
		OrderPaymentSingleDO orderinfo = orderPaymentSingleDAO.getOrderinfoByOrderNoAndMerchNo(orderBean.getOrderId(), orderBean.getMerchNo());
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
		orderPaymentSingle.setMername(orderBean.getMerName());
		orderPaymentSingle.setMerabbr(orderBean.getMerAbbr());
		orderPaymentSingle.setOrderid(orderBean.getOrderId());
		orderPaymentSingle.setTxntime(orderBean.getTxnTime());
		orderPaymentSingle.setPaytimeout(orderBean.getPayTimeout());
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
		orderPaymentSingle.setOrderdesc(orderBean.getOrderDesc());
		orderPaymentSingle.setReserved(orderBean.getReserved());
		orderPaymentSingle.setStatus(OrderStatusEnum.INITIAL.value());
		orderPaymentSingle.setOrdercommitime(orderBean.getTxnTime());
		return orderPaymentSingle;
	}


	
	private String saveCollectionBatchOrder(ConcentrateBatchOrderBean orderBean) {
		String TN = serialNumberService.generateTN(orderBean.getMerId());
		//保存批次订单数据
		OrderCollectBatchDO orderCollectBatch = generateCollectBatchOrderBean(orderBean);
		orderCollectBatch.setTn(TN);
		orderCollectBatch = orderCollectBatchDAO.saveCollectBatchOrder(orderCollectBatch);
		//保存批次明细数据和交易流水
		saveDetaOrder(orderCollectBatch.getTid(), orderBean);
		return TN;
	}
	
	private void saveDetaOrder(long batchId,ConcentrateBatchOrderBean orderBean) {
		//保存代收批次明细数据
		List<ConcentrateOrderDetaBean> detaList = orderBean.getDetaList();
		for(ConcentrateOrderDetaBean detaBean : detaList){
			String txnseqno = serialNumberService.generateTxnseqno();
			OrderCollectDetaDO orderCollectDeta = new OrderCollectDetaDO();
			orderCollectDeta.setBatchtid(batchId);
			orderCollectDeta.setBatchno(orderBean.getBatchNo());
			orderCollectDeta.setOrderid(detaBean.getOrderId());
			orderCollectDeta.setCurrencycode(detaBean.getCurrencyCode());
			orderCollectDeta.setAmt(detaBean.getAmt());
			orderCollectDeta.setDebtorbank(detaBean.getDebtorBank());
			orderCollectDeta.setDebtoraccount(detaBean.getDebtorAccount());
			orderCollectDeta.setDebtorname(detaBean.getDebtorName());
			orderCollectDeta.setDebtorconsign(detaBean.getDebtorConsign());
			orderCollectDeta.setCreditorbank(detaBean.getCreditorBank());
			orderCollectDeta.setCreditoraccount(detaBean.getCreditorAccount());
			orderCollectDeta.setCreditorname(detaBean.getCreditorName());
			orderCollectDeta.setProprietary(detaBean.getProprietary());
			orderCollectDeta.setSummary(detaBean.getSummary());
			orderCollectDeta.setRelatetradetxn(txnseqno);
			orderCollectDeta.setStatus(OrderStatusEnum.INITIAL.value());
			
			PojoTxnsLog txnsLog = new PojoTxnsLog();
			MerchantBean member = null;
			PojoTxncodeDef busiModel = txncodeDefDAO.getBusiCode(orderBean.getTxnType(), orderBean.getTxnSubType(),orderBean.getBizType());
			member = merchService.getMerchBymemberId(orderBean.getMerId());
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
			txnsLog.setAmount(Long.valueOf(detaBean.getAmt()));
			txnsLog.setAccordno(detaBean.getOrderId());
			txnsLog.setAccfirmerno(orderBean.getCoopinstiId());
			txnsLog.setAcccoopinstino(orderBean.getCoopinstiId());
			txnsLog.setAccsecmerno(orderBean.getMerId());
			txnsLog.setAccordcommitime(DateUtil.getCurrentDateTime());
			txnsLog.setTradestatflag(TradeStatFlagEnum.INITIAL.getStatus());// 交易初始状态
			txnsLog.setAccmemberid("999999999999999");// 匿名会员号
			//付款方信息
			txnsLog.setPan(detaBean.getDebtorAccount());
			txnsLog.setPanName(detaBean.getDebtorName());
			txnsLog.setCardinstino(detaBean.getDebtorBank());
			//收款信息
			txnsLog.setInpan(detaBean.getCreditorAccount());
			txnsLog.setInpanName(detaBean.getCreditorName());
			txnsLog.setIncardinstino(detaBean.getCreditorBank());
			txnsLog.setTxnseqno(txnseqno);
			txnsLog.setAccbusicode(busiModel.getBusicode());
			commonOrderService.saveTxnsLog(txnsLog);
			orderCollectDetaDAO.saveCollectOrderDeta(orderCollectDeta);
			
		}
		
		
	}


	private OrderCollectBatchDO generateCollectBatchOrderBean(
			ConcentrateBatchOrderBean orderBean) {
		OrderCollectBatchDO orderCollectBatchDO = new OrderCollectBatchDO();
		orderCollectBatchDO.setVersion(orderBean.getVersion());
		orderCollectBatchDO.setAccesstype(orderBean.getAccessType());
		orderCollectBatchDO.setCoopinstiid(orderBean.getCoopinstiId());
		orderCollectBatchDO.setMerid(orderBean.getMerId());
		orderCollectBatchDO.setEncoding(orderBean.getEncoding());
		orderCollectBatchDO.setTxntype(orderBean.getTxnType());
		orderCollectBatchDO.setTxnsubtype(orderBean.getTxnSubType());
		orderCollectBatchDO.setBackurl(orderBean.getBackUrl());
		orderCollectBatchDO.setBatchno(orderBean.getBatchNo());
		orderCollectBatchDO.setTxndate(orderBean.getTxnTime().substring(0,8));
		orderCollectBatchDO.setTxntime(orderBean.getTxnTime().substring(8));
		orderCollectBatchDO.setTotalqty(Long.valueOf(orderBean.getTotalQty()));
		orderCollectBatchDO.setTotalamt(Long.valueOf(orderBean.getTotalAmt()));
		orderCollectBatchDO.setReserved(orderBean.getReserved());
		orderCollectBatchDO.setStatus(OrderStatusEnum.INITIAL.value());
		orderCollectBatchDO.setOrdercommitime(DateUtil.getCurrentDateTime());
		return orderCollectBatchDO;
	}
	
	
	


	/**
	 * 检查订单业务有效性
	 * @param orderBean
	 * @throws OrderException
	 */
	public void checkOfBatchCollectionBusiness(ConcentrateBatchOrderBean orderBean) throws OrderException {
		PojoTxncodeDef busiModel = txncodeDefDAO.getBusiCode(orderBean.getTxnType(), orderBean.getTxnSubType(), orderBean.getBizType());
        if(busiModel==null){
        	throw new OrderException("OD045");
        }
        BusiTypeEnum busiTypeEnum = BusiTypeEnum.fromValue(busiModel.getBusitype());
        if(busiTypeEnum==BusiTypeEnum.CONCENTRATE){//集中代收付业务
        	if(StringUtils.isEmpty(orderBean.getMerId())){
        		 throw new OrderException("OD004");
        	}
        	MerchantBean member = merchService.getMerchBymemberId(orderBean.getMerId());//memberService.getMemberByMemberId(order.getMerId());.java
        	if(member==null){
        		throw new OrderException("OD009");
        	}
        	PojoProdCase prodCase= prodCaseDAO.getMerchProd(member.getPrdtVer(),busiModel.getBusicode());
            if(prodCase==null){
                throw new OrderException("OD005");
            }
            
            BusinessEnum businessEnum = BusinessEnum.fromValue(busiModel.getBusicode());
            if(businessEnum!=BusinessEnum.CONCENTRATE_COLLECT_BATCH){
            	throw new OrderException("OD045");
            }
        }else{
            throw new OrderException("OD045");
        }
	}
	/**
	 * 检查订单业务有效性
	 * @param orderBean
	 * @throws OrderException
	 */
	public void checkOfBatchPaymentBusiness(ConcentrateBatchOrderBean orderBean) throws OrderException {
		PojoTxncodeDef busiModel = txncodeDefDAO.getBusiCode(orderBean.getTxnType(), orderBean.getTxnSubType(), orderBean.getBizType());
        if(busiModel==null){
        	throw new OrderException("OD045");
        }
        BusiTypeEnum busiTypeEnum = BusiTypeEnum.fromValue(busiModel.getBusitype());
        if(busiTypeEnum==BusiTypeEnum.CONCENTRATE){//集中代收付业务
        	if(StringUtils.isEmpty(orderBean.getMerId())){
        		 throw new OrderException("OD004");
        	}
        	MerchantBean member = merchService.getMerchBymemberId(orderBean.getMerId());//memberService.getMemberByMemberId(order.getMerId());.java
        	if(member==null){
        		throw new OrderException("OD009");
        	}
        	PojoProdCase prodCase= prodCaseDAO.getMerchProd(member.getPrdtVer(),busiModel.getBusicode());
            if(prodCase==null){
                throw new OrderException("OD005");
            }
            BusinessEnum businessEnum = BusinessEnum.fromValue(busiModel.getBusicode());
            if(businessEnum!=BusinessEnum.CONCENTRATE_PAYMENT_BATCH){
            	throw new OrderException("OD045");
            }
        }else{
            throw new OrderException("OD045");
        }
	}
	private void checkOfBatchRepeatSubmit(ConcentrateBatchOrderBean orderBean) throws OrderException {
		// TODO Auto-generated method stub
		OrderCollectBatchDO orderInfo = orderCollectBatchDAO.getCollectBatchOrder(orderBean.getMerId(), orderBean.getBatchNo(), orderBean.getTxnTime().substring(0, 8));
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
	 * 检查代收批次数据和明细数据
	 * @param orderBean
	 * @throws OrderException
	 */
	public void checkOfBatchOrder(ConcentrateBatchOrderBean orderBean) throws OrderException{
		ResultBean resultBean = null;
		resultBean = ValidateLocator.validateBeans(orderBean);
		if(!resultBean.isResultBool()){
			throw new OrderException("OD049", resultBean.getErrMsg());
		}
		int size = orderBean.getDetaList().size();
		if(size>0){
			List<ConcentrateOrderDetaBean> detaList = orderBean.getDetaList();
			for(ConcentrateOrderDetaBean detaBean : detaList){
				resultBean = ValidateLocator.validateBeans(detaBean);
				if(!resultBean.isResultBool()){
					throw new OrderException("OD049", resultBean.getErrMsg());
				}
			}
		}else{
			throw new OrderException("");
		}
	}


	

	private void checkOfPaymentBatchRepeatSubmit(
			ConcentrateBatchOrderBean orderBean) throws OrderException {
		OrderPaymentBatchDO orderInfo = orderPaymentBatchDAO.getPaymentBatchOrder(orderBean.getMerId(), orderBean.getBatchNo(), orderBean.getTxnTime().substring(0, 8));
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
	private String savePaymentBatchOrder(ConcentrateBatchOrderBean orderBean) {
		String TN = serialNumberService.generateTN(orderBean.getMerId());
		//保存批次订单数据
		OrderPaymentBatchDO orderPaymentBatch = generatePaymentBatchOrderBean(orderBean);
		orderPaymentBatch.setTn(TN);
		orderPaymentBatch = orderPaymentBatchDAO.savePaymentBatchOrder(orderPaymentBatch);
		//保存批次明细数据和交易流水
		savePaymentDetaOrder(orderPaymentBatch.getTid(), orderBean);
		return TN;
	}
	
	private void savePaymentDetaOrder(long batchId,ConcentrateBatchOrderBean orderBean) {
		//保存代收批次明细数据
		List<ConcentrateOrderDetaBean> detaList = orderBean.getDetaList();
		for(ConcentrateOrderDetaBean detaBean : detaList){
			String txnseqno = serialNumberService.generateTxnseqno();
			OrderPaymentDetaDO orderPaymentDeta = new OrderPaymentDetaDO();
			orderPaymentDeta.setBatchtid(batchId);
			orderPaymentDeta.setBatchno(orderBean.getBatchNo());
			orderPaymentDeta.setOrderid(detaBean.getOrderId());
			orderPaymentDeta.setCurrencycode(detaBean.getCurrencyCode());
			orderPaymentDeta.setAmt(detaBean.getAmt());
			orderPaymentDeta.setDebtorbank(detaBean.getDebtorBank());
			orderPaymentDeta.setDebtoraccount(detaBean.getDebtorAccount());
			orderPaymentDeta.setDebtorname(detaBean.getDebtorName());
			orderPaymentDeta.setDebtorconsign(detaBean.getDebtorConsign());
			orderPaymentDeta.setCreditorbank(detaBean.getCreditorBank());
			orderPaymentDeta.setCreditoraccount(detaBean.getCreditorAccount());
			orderPaymentDeta.setCreditorname(detaBean.getCreditorName());
			orderPaymentDeta.setProprietary(detaBean.getProprietary());
			orderPaymentDeta.setSummary(detaBean.getSummary());
			orderPaymentDeta.setRelatetradetxn(txnseqno);
			orderPaymentDeta.setStatus(OrderStatusEnum.INITIAL.value());
			
			PojoTxnsLog txnsLog = new PojoTxnsLog();
			MerchantBean member = null;
			PojoTxncodeDef busiModel = txncodeDefDAO.getBusiCode(orderBean.getTxnType(), orderBean.getTxnSubType(),orderBean.getBizType());
			member = merchService.getMerchBymemberId(orderBean.getMerId());
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
			txnsLog.setAmount(Long.valueOf(detaBean.getAmt()));
			txnsLog.setAccordno(detaBean.getOrderId());
			txnsLog.setAccfirmerno(orderBean.getCoopinstiId());
			txnsLog.setAcccoopinstino(orderBean.getCoopinstiId());
			txnsLog.setAccsecmerno(orderBean.getMerId());
			txnsLog.setAccordcommitime(DateUtil.getCurrentDateTime());
			txnsLog.setTradestatflag(TradeStatFlagEnum.INITIAL.getStatus());// 交易初始状态
			txnsLog.setAccmemberid("999999999999999");// 匿名会员号
			//付款方信息
			txnsLog.setPan(detaBean.getDebtorAccount());
			txnsLog.setPanName(detaBean.getDebtorName());
			txnsLog.setCardinstino(detaBean.getDebtorBank());
			//收款信息
			txnsLog.setInpan(detaBean.getCreditorAccount());
			txnsLog.setInpanName(detaBean.getCreditorName());
			txnsLog.setIncardinstino(detaBean.getCreditorBank());
			txnsLog.setTxnseqno(txnseqno);
			txnsLog.setAccbusicode(busiModel.getBusicode());
			commonOrderService.saveTxnsLog(txnsLog);
			orderPaymentDetaDAO.savePaymentDetaOrder(orderPaymentDeta);
			
		}
		
		
	}

	private OrderPaymentBatchDO generatePaymentBatchOrderBean(
			ConcentrateBatchOrderBean orderBean) {
		OrderPaymentBatchDO orderPaymentSingle = new OrderPaymentBatchDO();
		orderPaymentSingle.setVersion(orderBean.getVersion());
		orderPaymentSingle.setAccesstype(orderBean.getAccessType());
		orderPaymentSingle.setCoopinstiid(orderBean.getCoopinstiId());
		orderPaymentSingle.setMerid(orderBean.getMerId());
		orderPaymentSingle.setEncoding(orderBean.getEncoding());
		orderPaymentSingle.setTxntype(orderBean.getTxnType());
		orderPaymentSingle.setTxnsubtype(orderBean.getTxnSubType());
		orderPaymentSingle.setBackurl(orderBean.getBackUrl());
		orderPaymentSingle.setBatchno(orderBean.getBatchNo());
		orderPaymentSingle.setTxndate(orderBean.getTxnTime().substring(0,8));
		orderPaymentSingle.setTxntime(orderBean.getTxnTime().substring(8));
		
		orderPaymentSingle.setTotalqty(Long.valueOf(orderBean.getTotalQty()));
		orderPaymentSingle.setTotalamt(Long.valueOf(orderBean.getTotalAmt()));
		orderPaymentSingle.setReserved(orderBean.getReserved());
		orderPaymentSingle.setStatus(OrderStatusEnum.INITIAL.value());
		orderPaymentSingle.setOrdercommitime(DateUtil.getCurrentDateTime());
		return orderPaymentSingle;
	}
}
