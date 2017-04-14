package com.zcbspay.platform.payment.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zcbspay.platform.payment.commons.dao.impl.HibernateBaseDAOImpl;
import com.zcbspay.platform.payment.dao.OrderPaymentDetaDAO;
import com.zcbspay.platform.payment.pojo.OrderPaymentDetaDO;
@Repository
public class OrderPaymentDetaDAOImpl extends HibernateBaseDAOImpl<OrderPaymentDetaDO> implements
		OrderPaymentDetaDAO {

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public void savePaymentDetaOrder(OrderPaymentDetaDO orderPaymentDeta) {
		// TODO Auto-generated method stub
		saveEntity(orderPaymentDeta);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public List<OrderPaymentDetaDO> getDetaListByBatchtid(Long batchId){
		Criteria criteria = getSession().createCriteria(OrderPaymentDetaDO.class);
		criteria.add(Restrictions.eq("batchtid", batchId));
		return criteria.list();
	}

	@Override
	public void updatePaymenyOrderDeta(OrderPaymentDetaDO deta) {
		// TODO Auto-generated method stub
		update(deta);
	}
}
