package com.zcbspay.platform.payment.dao.impl;

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

}
