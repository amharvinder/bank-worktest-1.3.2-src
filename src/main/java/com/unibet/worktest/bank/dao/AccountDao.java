package com.unibet.worktest.bank.dao;

import com.unibet.worktest.bank.Money;
import com.unibet.worktest.bank.model.Account;

public interface AccountDao {

	void createAccount(String accountRef, Money amount);

	Account getAccountByReference(String accountRef);

}
