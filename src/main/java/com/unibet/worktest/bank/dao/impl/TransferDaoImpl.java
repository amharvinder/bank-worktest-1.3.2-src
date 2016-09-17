package com.unibet.worktest.bank.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.unibet.worktest.bank.dao.TransferDao;
import com.unibet.worktest.bank.model.MoneyTransaction;

@Repository
@Transactional
public class TransferDaoImpl extends BaseDao<MoneyTransaction> implements TransferDao {
	
	@Override
	public void transferFunds(MoneyTransaction moneyTransaction) {
		save(moneyTransaction);
	}

	@Override
	@Transactional(readOnly=true)
	public MoneyTransaction getTransactionByReference(String transactionRef) {
		return getEntityByReference(transactionRef, "reference");
	}

}
