package com.zcbspay.platform.payment.trade.route.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zcbspay.platform.payment.commons.dao.impl.HibernateBaseDAOImpl;
import com.zcbspay.platform.payment.trade.route.dao.MerchBankAccountDAO;
import com.zcbspay.platform.payment.trade.route.exception.TradeRouteException;
import com.zcbspay.platform.payment.trade.route.pojo.MerchBankAccountDO;

@Repository
public class MerchBankAccountDAOImpl extends HibernateBaseDAOImpl<MerchBankAccountDO> implements MerchBankAccountDAO{

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public MerchBankAccountDO getTradeRoute(String merchNo,String accountno,String protocoltype) throws TradeRouteException{
		Criteria criteria = getSession().createCriteria(MerchBankAccountDO.class);
		criteria.add(Restrictions.eq("merchno", merchNo));
		criteria.add(Restrictions.eq("accountno", accountno));
		criteria.add(Restrictions.eq("protocoltype", protocoltype));
		criteria.add(Restrictions.eq("status","00"));
		MerchBankAccountDO uniqueResult = null;
		try {
			uniqueResult = (MerchBankAccountDO) criteria.uniqueResult();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new TradeRouteException("PC009");
		}
		return uniqueResult;
	}
}
