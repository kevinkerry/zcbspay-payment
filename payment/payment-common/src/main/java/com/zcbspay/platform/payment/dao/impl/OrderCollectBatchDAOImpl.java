package com.zcbspay.platform.payment.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zcbspay.platform.payment.commons.dao.impl.HibernateBaseDAOImpl;
import com.zcbspay.platform.payment.dao.OrderCollectBatchDAO;
import com.zcbspay.platform.payment.pojo.OrderCollectBatchDO;

@Repository
public class OrderCollectBatchDAOImpl extends HibernateBaseDAOImpl<OrderCollectBatchDO> implements
		OrderCollectBatchDAO {

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public OrderCollectBatchDO saveCollectBatchOrder(OrderCollectBatchDO collectBatchDO) {
		// TODO Auto-generated method stub
		//saveEntity(collectBatchDO);
		return merge(collectBatchDO);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public OrderCollectBatchDO getCollectBatchOrder(String merchNo,
			String batchNo, String txndate) {
		Criteria criteria = getSession().createCriteria(OrderCollectBatchDO.class);
		criteria.add(Restrictions.eq("merid", merchNo));
		criteria.add(Restrictions.eq("batchno", batchNo));
		criteria.add(Restrictions.eq("txndate", txndate));
		OrderCollectBatchDO uniqueResult = (OrderCollectBatchDO) criteria.uniqueResult();
		return uniqueResult;
	}

}
