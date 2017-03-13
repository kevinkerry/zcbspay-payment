package com.zcbspay.platform.payment.order.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zcbspay.platform.payment.commons.dao.impl.HibernateBaseDAOImpl;
import com.zcbspay.platform.payment.order.dao.OrderCollectSingleDAO;
import com.zcbspay.platform.payment.order.dao.pojo.OrderCollectSingleDO;

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

	

}
