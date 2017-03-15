package com.zcbspay.platform.payment.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zcbspay.platform.payment.commons.dao.impl.HibernateBaseDAOImpl;
import com.zcbspay.platform.payment.dao.OrderCollectSingleDAO;
import com.zcbspay.platform.payment.pojo.OrderCollectSingleDO;

@Repository
public class OrderCollectSingleDAOImpl extends HibernateBaseDAOImpl<OrderCollectSingleDO> implements OrderCollectSingleDAO {

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public OrderCollectSingleDO getOrderinfoByOrderNoAndMerchNo(String orderNo,
			String merchNo) {
		Criteria criteria = getSession().createCriteria(OrderCollectSingleDO.class);
		criteria.add(Restrictions.eq("orderid", orderNo));
		criteria.add(Restrictions.eq("merid", merchNo));
		OrderCollectSingleDO uniqueResult = (OrderCollectSingleDO) criteria.uniqueResult();
		return uniqueResult;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public void saveSingleCollectOrder(OrderCollectSingleDO orderCollectSingle) {
		// TODO Auto-generated method stub
		saveEntity(orderCollectSingle);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public OrderCollectSingleDO getOrderinfoByTn(String tn) {
		Criteria criteria = getSession().createCriteria(OrderCollectSingleDO.class);
		criteria.add(Restrictions.eq("tn", tn));
		OrderCollectSingleDO uniqueResult = (OrderCollectSingleDO) criteria.uniqueResult();
		return uniqueResult;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public void updateOrderToStartPay(String tn) {
		// TODO Auto-generated method stub
		Session session = getSession();
		Query query = session
				.createQuery("update OrderCollectSingleDO set status = ? where tn = ? and (status=? or status = ?)");

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
