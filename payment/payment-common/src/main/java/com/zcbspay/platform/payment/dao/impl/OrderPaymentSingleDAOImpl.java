package com.zcbspay.platform.payment.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zcbspay.platform.payment.commons.dao.impl.HibernateBaseDAOImpl;
import com.zcbspay.platform.payment.dao.OrderPaymentSingleDAO;
import com.zcbspay.platform.payment.pojo.OrderCollectSingleDO;
import com.zcbspay.platform.payment.pojo.OrderPaymentSingleDO;
@Repository
public class OrderPaymentSingleDAOImpl extends HibernateBaseDAOImpl<OrderPaymentSingleDO>
		implements OrderPaymentSingleDAO {

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public void savePaymentSingleOrder(OrderPaymentSingleDO orderPaymentSingle) {
		// TODO Auto-generated method stub
		saveEntity(orderPaymentSingle);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public OrderPaymentSingleDO getOrderinfoByOrderNoAndMerchNo(String orderNo,
			String merchNo) {
		Criteria criteria = getSession().createCriteria(OrderPaymentSingleDO.class);
		criteria.add(Restrictions.eq("orderid", orderNo));
		criteria.add(Restrictions.eq("merid", merchNo));
		OrderPaymentSingleDO uniqueResult = (OrderPaymentSingleDO) criteria.uniqueResult();
		return uniqueResult;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public OrderPaymentSingleDO getOrderinfoByTn(String tn) {
		Criteria criteria = getSession().createCriteria(OrderPaymentSingleDO.class);
		criteria.add(Restrictions.eq("tn", tn));
		OrderPaymentSingleDO uniqueResult = (OrderPaymentSingleDO) criteria.uniqueResult();
		return uniqueResult;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public void updateOrderToStartPay(String tn) {
		// TODO Auto-generated method stub
		Session session = getSession();
		Query query = session
				.createQuery("update OrderPaymentSingleDO set status = ? where tn = ? and (status=? or status = ?)");

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
