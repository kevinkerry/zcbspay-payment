package com.zcbspay.platform.payment.order.service.concentrate;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zcbspay.platform.member.merchant.bean.MerchantBean;
import com.zcbspay.platform.member.merchant.service.MerchService;
import com.zcbspay.platform.payment.bean.ResultBean;
import com.zcbspay.platform.payment.commons.utils.ValidateLocator;
import com.zcbspay.platform.payment.order.bean.BaseOrderBean;
import com.zcbspay.platform.payment.order.consume.bean.ConcentrateSingleOrderBean;
import com.zcbspay.platform.payment.order.consume.bean.ConsumeOrderBean;
import com.zcbspay.platform.payment.order.dao.OrderCollectSingleDAO;
import com.zcbspay.platform.payment.order.dao.ProdCaseDAO;
import com.zcbspay.platform.payment.order.dao.TxncodeDefDAO;
import com.zcbspay.platform.payment.order.dao.pojo.OrderCollectSingleDO;
import com.zcbspay.platform.payment.order.dao.pojo.PojoProdCase;
import com.zcbspay.platform.payment.order.dao.pojo.PojoTxncodeDef;
import com.zcbspay.platform.payment.order.enums.BusiTypeEnum;
import com.zcbspay.platform.payment.order.exception.OrderException;
import com.zcbspay.platform.payment.order.service.CheckOfServcie;
import com.zcbspay.platform.payment.order.service.OrderService;
import com.zcbspay.platform.payment.order.service.consume.AbstractConsumeOrderService;
/**
 * 
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2017年3月13日 上午11:48:55
 * @since
 */
@Component
public abstract class AbstractConcentrateOrderService implements CheckOfServcie<ConcentrateSingleOrderBean>,OrderService{
	
	private static final Logger logger = LoggerFactory.getLogger(AbstractConsumeOrderService.class);
	
	@Autowired
	private OrderCollectSingleDAO orderCollectSingleDAO;
	@Autowired
	private TxncodeDefDAO txncodeDefDAO;
	@Autowired
	private MerchService merchService;
	@Autowired
	private ProdCaseDAO prodCaseDAO;
	/**
	 * 订单非空有效性检查
	 * @param baseOrderBean
	 * @throws OrderException
	 */
	@Override
	public void checkOfOrder(BaseOrderBean baseOrderBean) throws OrderException{
		ResultBean resultBean = null;
		resultBean = ValidateLocator.validateBeans(baseOrderBean);
		if(!resultBean.isResultBool()){
			throw new OrderException("OD049", resultBean.getErrMsg());
		}
	}
	
	/**
	 * 检查订单二次支付
	 * @param baseOrderBean
	 * @return 受理订单号 tn
	 * @throws OrderException
	 */
	@Override
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
	
	/**
	 * 检查订单是否为二次提交
	 * @param orderBean
	 * @throws OrderException
	 */
	@Override
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
	@Override
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

	/**
	 * 检查商户和合作机构有效性
	 * @param orderBean
	 * @throws OrderException
	 */
	@Override
	public void checkOfMerchantAndCoopInsti(ConcentrateSingleOrderBean orderBean) throws OrderException{
        
	}
	
	/**
	 * 检查商户和会员的账户状态
	 *
	 * @param orderBean
	 * @throws OrderException
	 */
	@Override
	public void checkOfBusiAcct(ConcentrateSingleOrderBean orderBean) throws OrderException{
		
	}
	
	/**
	 * 检查消费订单特殊性要求检查，如果没有可以为空
	 * @param orderBean
	 * @throws OrderException 
	 */
	@Override
	public void checkOfSpecialBusiness(ConcentrateSingleOrderBean orderBean) throws OrderException{
		
	}
	
	/**
	 * 检查所有订单有效性检查项
	 * @param baseOrderBean
	 * @throws OrderException 
	 */
	public abstract void checkOfAll(BaseOrderBean baseOrderBean) throws OrderException;
	
	/**
	 * 保存订单信息
	 * @param orderBean
	 * @throws OrderException 
	 */
	public abstract String saveConsumeOrder(BaseOrderBean baseOrderBean) throws OrderException;
	
	
}
