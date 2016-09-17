package com.unibet.worktest.bank.dao;

import com.unibet.worktest.bank.model.MoneyTransaction;

public interface TransferDao {

	void transferFunds(MoneyTransaction moneyTransaction);

	MoneyTransaction getTransactionByReference(String transactionRef);

}
