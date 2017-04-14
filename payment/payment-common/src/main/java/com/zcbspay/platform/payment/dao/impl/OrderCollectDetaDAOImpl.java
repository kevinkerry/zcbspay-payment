package com.zcbspay.platform.payment.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zcbspay.platform.payment.commons.dao.impl.HibernateBaseDAOImpl;
import com.zcbspay.platform.payment.dao.OrderCollectDetaDAO;
import com.zcbspay.platform.payment.pojo.OrderCollectDetaDO;

@Repository
public class OrderCollectDetaDAOImpl extends HibernateBaseDAOImpl<OrderCollectDetaDO> implements
		OrderCollectDetaDAO {

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public void saveCollectOrderDeta(OrderCollectDetaDO orderCollectDetaDO) {
		// TODO Auto-generated method stub
		saveEntity(orderCollectDetaDO);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public List<OrderCollectDetaDO> getDetaListByBatchtid(Long batchId){
		Criteria criteria = getSession().createCriteria(OrderCollectDetaDO.class);
		criteria.add(Restrictions.eq("batchtid", batchId));
		return criteria.list();
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public void updateCollectOrderDeta(OrderCollectDetaDO orderCollectDetaDO) {
		// TODO Auto-generated method stub
		merge(orderCollectDetaDO);
	}
}
