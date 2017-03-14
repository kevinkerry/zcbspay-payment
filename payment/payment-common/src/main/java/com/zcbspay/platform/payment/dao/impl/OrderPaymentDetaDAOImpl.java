package com.zcbspay.platform.payment.dao.impl;

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

	
}
