/* 
 * TxnsLogDAOImpl.java  
 * 
 * version TODO
 *
 * 2016年9月13日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zcbspay.platform.payment.risk.dao.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.zcbspay.platform.payment.commons.dao.impl.HibernateBaseDAOImpl;
import com.zcbspay.platform.payment.risk.dao.TxnsLogDAO;
import com.zcbspay.platform.payment.risk.pojo.PojoTxnsLog;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年9月13日 下午5:33:02
 * @since
 */
@Repository("txnsLogDAO")
public class TxnsLogDAOImpl extends HibernateBaseDAOImpl<PojoTxnsLog> implements
		TxnsLogDAO {

	//private static final Logger log = LoggerFactory.getLogger(TxnsLogDAOImpl.class);

	public void saveTxnsLog(PojoTxnsLog txnsLog) {
		super.saveEntity(txnsLog);
	}

	/**
	 *
	 * @param txnseqno
	 * @return
	 */
	@Override
	@Transactional(readOnly=true)
	public PojoTxnsLog getTxnsLogByTxnseqno(String txnseqno) {
		Criteria criteria = getSession().createCriteria(PojoTxnsLog.class);
		criteria.add(Restrictions.eq("txnseqno", txnseqno));
		return (PojoTxnsLog) criteria.uniqueResult();
	}

	/**
	 *
	 * @param cardNo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly=true)
	public Map<String, Object> getCardInfo(String cardNo){
		StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("SELECT type,bankcode,bankname ");
        sqlBuffer.append("FROM (SELECT t.TYPE,t.BANKCODE,b.bankname ");
        sqlBuffer.append("FROM t_card_bin t, t_bank_insti b ");
        sqlBuffer.append("WHERE t.bankcode = b.bankcode ");
        sqlBuffer.append("AND ? LIKE t.cardbin || '%' ");
        sqlBuffer.append("AND t.cardlen = ? ");
        sqlBuffer.append("ORDER BY t.cardbin DESC) ");
        sqlBuffer.append("WHERE ROWNUM = 1 ");
        
        SQLQuery sqlQuery = (SQLQuery) getSession().createSQLQuery(sqlBuffer.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        sqlQuery.setParameter(0, cardNo);
        sqlQuery.setParameter(1, cardNo.trim().length());
        List<Map<String, Object>> routList =  (List<Map<String, Object>>)sqlQuery.list();
       
        if(routList.size()>0){
            return routList.get(0);
        }
		return null;
	}
}
