package com.unibet.worktest.bank.dao;

import java.util.Set;

import com.unibet.worktest.bank.Money;
import com.unibet.worktest.bank.model.Account;
import com.unibet.worktest.bank.model.MoneyTransaction;

public interface AccountDao extends BaseDao<Account>{

	void createAccount(String accountRef, Money amount);

	Account getAccountByReference(String accountRef);
	
	Set<MoneyTransaction> getAccountTransactions(String accountRef);

}
