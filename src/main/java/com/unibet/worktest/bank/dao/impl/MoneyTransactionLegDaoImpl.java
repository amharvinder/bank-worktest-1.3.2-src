package com.unibet.worktest.bank.dao.impl;

import com.unibet.worktest.bank.dao.MoneyTransactionLegDao;
import com.unibet.worktest.bank.model.MoneyTransactionLeg;

public class MoneyTransactionLegDaoImpl extends BaseDaoImpl<MoneyTransactionLeg> implements MoneyTransactionLegDao {

	public MoneyTransactionLegDaoImpl() {
		super(MoneyTransactionLeg.class);
	}

}
