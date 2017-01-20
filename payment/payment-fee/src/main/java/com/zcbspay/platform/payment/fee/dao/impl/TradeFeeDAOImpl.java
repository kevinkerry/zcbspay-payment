/* 
 * TradeFeeDAOImpl.java  
 * 
 * version TODO
 *
 * 2016年11月16日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zcbspay.platform.payment.fee.dao.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.zcbspay.platform.payment.commons.dao.impl.HibernateBaseDAOImpl;
import com.zcbspay.platform.payment.fee.bean.FeeBean;
import com.zcbspay.platform.payment.fee.dao.TradeFeeDAO;
import com.zcbspay.platform.payment.fee.exception.TradeFeeException;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年11月16日 上午9:53:12
 * @since 
 */
@Repository("tradeFeeDAO")
public class TradeFeeDAOImpl extends HibernateBaseDAOImpl implements TradeFeeDAO {

	/**
	 *
	 * @param feeBean
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly=true)
	public long getTxnFee(FeeBean feeBean) throws TradeFeeException{
		String sql = "select FNC_GETFEES(?,?,?,?,?,?,?) as fee from dual";
		SQLQuery query = (SQLQuery) getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		Object[] paramaters = new Object[]{feeBean.getTxnseqno(),feeBean.getFeeVer(),feeBean.getBusiCode(),feeBean.getTxnAmt(),feeBean.getMerchNo(),feeBean.getTxnseqnoOg(),feeBean.getCardType()};
		for(int i=0;i<paramaters.length;i++){
			query.setParameter(i, paramaters[i]);
		}
		List<Map<String, Object>> feeList = query.list();
		if(feeList.size()>0){
			if(feeList.get(0).get("FEE")==null||"".equals(feeList.get(0).get("FEE"))){
				throw new TradeFeeException("TF001");
			}else{
				return Long.valueOf(feeList.get(0).get("FEE")+"");
            }
        }else{
        	throw new TradeFeeException("TF001");
        }
	}

	/**
	 *
	 * @param feeBean
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly=true)
	public long getRefundFee(FeeBean feeBean)  throws TradeFeeException{
		String sql = "select FNC_GETFEES_notxns(?,?,?,?,?,?) as fee from dual";
		SQLQuery query = (SQLQuery) getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		Object[] paramters = new Object[] { feeBean.getFeeVer(), feeBean.getBusiCode(), feeBean.getTxnAmt(),feeBean.getMerchNo(), feeBean.getTxnseqnoOg(),feeBean.getCardType() };
		for(int i=0;i<paramters.length;i++){
			query.setParameter(i, paramters[i]);
		}
		List<Map<String, Object>> feeList = query.list();
		
		if(feeList.size()>0){
			if(feeList.get(0).get("FEE")==null||"".equals(feeList.get(0).get("FEE"))){
				throw new TradeFeeException("TF001");
			}else{
				return Long.valueOf(feeList.get(0).get("FEE")+"");
            }
        }else{
        	throw new TradeFeeException("TF001");
        }
	}

}
