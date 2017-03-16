package com.zcbspay.platform.payment.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zcbspay.platform.payment.commons.dao.impl.HibernateBaseDAOImpl;
import com.zcbspay.platform.payment.dao.OrderPaymentBatchDAO;
import com.zcbspay.platform.payment.pojo.OrderCollectBatchDO;
import com.zcbspay.platform.payment.pojo.OrderPaymentBatchDO;
@Repository
public class OrderPaymentBatchDAOImpl extends HibernateBaseDAOImpl<OrderPaymentBatchDO> implements
		OrderPaymentBatchDAO {

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public OrderPaymentBatchDO savePaymentBatchOrder(OrderPaymentBatchDO orderPaymentBatch) {
		return merge(orderPaymentBatch);
	}

	

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public OrderPaymentBatchDO getPaymentBatchOrder(String merchNo,
			String batchNo, String txndate) {
		Criteria criteria = getSession().createCriteria(OrderPaymentBatchDO.class);
		criteria.add(Restrictions.eq("merid", merchNo));
		criteria.add(Restrictions.eq("batchno", batchNo));
		criteria.add(Restrictions.eq("txndate", txndate));
		OrderPaymentBatchDO uniqueResult = (OrderPaymentBatchDO) criteria.uniqueResult();
		return uniqueResult;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public OrderPaymentBatchDO getPaymentBatchOrderByTn(String tn) {
		Criteria criteria = getSession().createCriteria(OrderPaymentBatchDO.class);
		criteria.add(Restrictions.eq("tn", tn));
		OrderPaymentBatchDO uniqueResult = (OrderPaymentBatchDO) criteria.uniqueResult();
		return uniqueResult;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public void updateOrderToStartPay(String tn) {
		// TODO Auto-generated method stub
		Session session = getSession();
		Query query = session
				.createQuery("update OrderPaymentBatchDO set status = ? where tn = ? and (status=? or status = ?)");

		Object[] paramaters = new Object[] { "02", tn, "01", "03" };
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
