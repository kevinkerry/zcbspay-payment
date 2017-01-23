/* 
 * RiskTradeLogDAOImpl.java  
 * 
 * version TODO
 *
 * 2016年11月14日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zcbspay.platform.payment.risk.dao.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zcbspay.platform.payment.commons.dao.impl.HibernateBaseDAOImpl;
import com.zcbspay.platform.payment.risk.bean.RiskBean;
import com.zcbspay.platform.payment.risk.dao.RiskTradeLogDAO;
import com.zcbspay.platform.payment.risk.pojo.PojoRiskTradeLog;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年11月14日 下午5:40:47
 * @since 
 */
@Repository("riskTradeLogDAO")
public class RiskTradeLogDAOImpl extends HibernateBaseDAOImpl<PojoRiskTradeLog> implements RiskTradeLogDAO {

	/**
	 *
	 * @param riskTradeLog
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public void saveRiskTradeLo(PojoRiskTradeLog riskTradeLog) {
		saveEntity(riskTradeLog);
	}

	/**
	 *
	 * @param riskBean
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly=true)
	public List<Map<String, Object>> oracleFunRiskControl(RiskBean riskBean) {
		String sql = "SELECT FNC_GETRISK(?,?,?,?,?,?,?) AS RISK FROM DUAL";
		Object[] paramaters = new Object[]{riskBean.getCoopInstId(),riskBean.getMerchId(),riskBean.getMemberId(),riskBean.getBusiCode(),riskBean.getTxnAmt(),riskBean.getCardType(),riskBean.getCardNo()};
		SQLQuery query = (SQLQuery) getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		for(int i=0;i<paramaters.length;i++){
			query.setParameter(i, paramaters[i]);
		}
		return query.list();
	}
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly=true)
    public List<Map<String,String>> getRiskStrategy(int orders){
        String sql = "select * from T_RISK_LIST where ORDERS = ?";
        SQLQuery query = (SQLQuery) getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        Object[] paramaters = new Object[]{orders};
		for(int i=0;i<paramaters.length;i++){
			query.setParameter(i, paramaters[i]);
		}
        return query.list();
    }
	
}
