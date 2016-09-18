package com.unibet.worktest.bank.dao;

import com.unibet.worktest.bank.model.MoneyTransaction;

public interface MoneyTransactionDao extends BaseDao<MoneyTransaction> {

	void transferFunds(MoneyTransaction moneyTransaction);

	MoneyTransaction getTransactionByReference(String transactionRef);

}
