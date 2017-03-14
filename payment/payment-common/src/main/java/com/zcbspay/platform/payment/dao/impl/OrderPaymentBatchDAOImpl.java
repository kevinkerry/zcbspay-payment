package com.zcbspay.platform.payment.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zcbspay.platform.payment.commons.dao.impl.HibernateBaseDAOImpl;
import com.zcbspay.platform.payment.dao.OrderPaymentBatchDAO;
import com.zcbspay.platform.payment.pojo.OrderCollectBatchDO;
import com.zcbspay.platform.payment.pojo.OrderPaymentBatchDO;

public class OrderPaymentBatchDAOImpl extends HibernateBaseDAOImpl<OrderPaymentBatchDO> implements
		OrderPaymentBatchDAO {

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public OrderPaymentBatchDO savePaymentBatchOrder(OrderPaymentBatchDO orderPaymentBatch) {
		return merge(orderPaymentBatch);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public OrderPaymentBatchDO getCollectBatchOrder(String merchNo,
			String batchNo, String txndate) {
		Criteria criteria = getSession().createCriteria(OrderPaymentBatchDO.class);
		criteria.add(Restrictions.eq("merid", merchNo));
		criteria.add(Restrictions.eq("batchno", batchNo));
		criteria.add(Restrictions.eq("txndate", txndate));
		OrderPaymentBatchDO uniqueResult = (OrderPaymentBatchDO) criteria.uniqueResult();
		return uniqueResult;
	}
	

}
