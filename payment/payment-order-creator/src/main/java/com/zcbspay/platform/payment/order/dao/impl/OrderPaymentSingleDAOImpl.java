package com.zcbspay.platform.payment.order.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zcbspay.platform.payment.commons.dao.impl.HibernateBaseDAOImpl;
import com.zcbspay.platform.payment.order.dao.OrderPaymentSingleDAO;
import com.zcbspay.platform.payment.order.dao.pojo.OrderPaymentSingleDO;
@Repository
public class OrderPaymentSingleDAOImpl extends HibernateBaseDAOImpl<OrderPaymentSingleDO>
		implements OrderPaymentSingleDAO {

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public void savePaymentSingleOrder(OrderPaymentSingleDO orderPaymentSingle) {
		// TODO Auto-generated method stub
		saveEntity(orderPaymentSingle);
	}

	

}
