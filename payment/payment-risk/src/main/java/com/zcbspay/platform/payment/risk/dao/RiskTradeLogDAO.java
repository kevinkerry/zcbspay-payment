/* 
 * PojoRiskTradeLog.java  
 * 
 * version TODO
 *
 * 2016年11月14日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zcbspay.platform.payment.risk.dao;

import java.util.List;
import java.util.Map;

import com.zcbspay.platform.payment.commons.dao.BaseDAO;
import com.zcbspay.platform.payment.risk.bean.RiskBean;
import com.zcbspay.platform.payment.risk.pojo.PojoRiskTradeLog;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年11月14日 下午5:38:25
 * @since 
 */
public interface RiskTradeLogDAO extends BaseDAO<PojoRiskTradeLog>{

	/**
	 * 保存风控交易日志
	 * @param riskTradeLog
	 */
	public void saveRiskTradeLo(PojoRiskTradeLog riskTradeLog);
	
	/**
	 * 数据库交易风控
	 * @param riskBean
	 * @return
	 */
	public List<Map<String, Object>> oracleFunRiskControl(RiskBean riskBean);
	
	/**
	 * 取得风控策略
	 * @param orders
	 * @return
	 */
    public List<Map<String,String>> getRiskStrategy(int orders);
}
