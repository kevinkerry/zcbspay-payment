/* 
 * InsteadPayRealtimeDAOImpl.java  
 * 
 * version TODO
 *
 * 2016年10月21日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zcbspay.platform.payment.dao.impl;

import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zcbspay.platform.payment.commons.dao.impl.HibernateBaseDAOImpl;
import com.zcbspay.platform.payment.dao.InsteadPayRealtimeDAO;
import com.zcbspay.platform.payment.pojo.PojoInsteadPayRealtime;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年10月21日 上午11:49:04
 * @since 
 */
@Repository("insteadPayRealtimeDAO")
public class InsteadPayRealtimeDAOImpl extends HibernateBaseDAOImpl<PojoInsteadPayRealtime> implements InsteadPayRealtimeDAO {
	private static final Logger log = LoggerFactory.getLogger(InsteadPayRealtimeDAOImpl.class);
	

	/**
	 *
	 * @param tn
	 * @return
	 */
	@Override
	@Transactional(readOnly=true)
	public PojoInsteadPayRealtime queryOrderByTN(String tn) {
		Criteria criteria = getSession().createCriteria(PojoInsteadPayRealtime.class);
		criteria.add(Restrictions.eq("tn", tn));
		return (PojoInsteadPayRealtime) criteria.uniqueResult();
	}

	/**
	 *
	 * @param txnseqno
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public void updateOrderToStartPay(String txnseqno) {
		PojoInsteadPayRealtime orderinfo = getWaitPayOrderinfo(txnseqno);
		if (orderinfo == null) {
			//throw new PaymentQuickPayException("T010");
		} else {
			Session session = getSession();
			Query query = session
					.createQuery("update PojoInsteadPayRealtime set status = ? where txnseqno = ? and (status=? or status = ?)");

			Object[] paramaters = new Object[] { "02", txnseqno, "01", "03" };
			if (paramaters != null) {
				for (int i = 0; i < paramaters.length; i++) {
					query.setParameter(i, paramaters[i]);
				}
			}
			int rows = query.executeUpdate();
			if (rows != 1) {
				//throw new PaymentQuickPayException("T011");
			}
		}
		
	}
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public PojoInsteadPayRealtime getWaitPayOrderinfo(String txnseqno) {
		Session session = getSession();
		Query query = session
				.createQuery("from PojoInsteadPayRealtime where txnseqno = ? and (status=? or status = ?)");
		Object[] paramaters = new Object[] { txnseqno, "01", "03" };
		if (paramaters != null) {
			for (int i = 0; i < paramaters.length; i++) {
				query.setParameter(i, paramaters[i]);
			}
		}
		PojoInsteadPayRealtime orderinfo = (PojoInsteadPayRealtime) query.uniqueResult();
		if (orderinfo != null) {
			session.evict(orderinfo);
		}
		return orderinfo;
	}

	/**
	 *
	 * @param merchNo
	 * @param orderNo
	 * @return
	 */
	@Override
	@Transactional(readOnly=true)
	public PojoInsteadPayRealtime getOrderinfoByOrderNoAndMerchNo(
			String merchNo, String orderNo) {
		Criteria criteria = getSession().createCriteria(PojoInsteadPayRealtime.class);
		criteria.add(Restrictions.eq("merId", merchNo));
		criteria.add(Restrictions.eq("orderno", orderNo));
		return (PojoInsteadPayRealtime) criteria.uniqueResult();
	}
	/**
	 *
	 * @param bean
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public void saveInsteadTrade(PojoInsteadPayRealtime bean) {
		// TODO Auto-generated method stub
		saveEntity(bean);
	}

	/**
	 *
	 * @param txnseqno
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public PojoInsteadPayRealtime getInsteadByTxnseqno(String txnseqno) {
		Criteria criteria = getSession().createCriteria(
				PojoInsteadPayRealtime.class);
		criteria.add(Restrictions.eq("txnseqno", txnseqno));
		return (PojoInsteadPayRealtime) criteria.uniqueResult();
	}

	

	/**
	 *
	 * @param txnseqno
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public void updateInsteadSuccess(String txnseqno) {
		// TODO Auto-generated method stub
		String hql = "update PojoInsteadPayRealtime set status = ?,retCode = ?,retInfo = ?,updateTime = ? where txnseqno = ?";
		Query query = getSession().createQuery(hql);
		query.setParameter(0, txnseqno);
		query.setParameter(1, "0000");
		query.setParameter(2, "交易成功");
		query.setParameter(3, new Date());
		query.setParameter(4, txnseqno);
		int rows = query.executeUpdate();
		log.info("updateInsteadSuccess() effect rows:"+rows);
	}

	/**
	 *
	 * @param txnseqno
	 * @param retCode
	 * @param retMsg
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public void updateInsteadFail(String txnseqno, String retCode, String retMsg) {
		// TODO Auto-generated method stub
		String hql = "update PojoInsteadPayRealtime set status = ?,retCode = ?,retInfo = ?,updateTime = ? where txnseqno = ?";
		Query query = getSession().createQuery(hql);
		query.setParameter(0, txnseqno);
		query.setParameter(1, retCode);
		query.setParameter(2, retMsg);
		query.setParameter(3, new Date());
		query.setParameter(4, txnseqno);
		int rows = query.executeUpdate();
		log.info("updateInsteadSuccess() effect rows:"+rows);
	}

	/**
	 *
	 * @param orderNo
	 * @param merchNo
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public PojoInsteadPayRealtime queryInsteadPayOrder(String orderNo,
			String merchNo) {
		Criteria criteria = getSession().createCriteria(PojoInsteadPayRealtime.class);
		criteria.add(Restrictions.eq("merId", merchNo));
		criteria.add(Restrictions.eq("orderno", orderNo));
		return (PojoInsteadPayRealtime) criteria.uniqueResult();
	}

}
