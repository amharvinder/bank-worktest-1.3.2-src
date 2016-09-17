package com.unibet.worktest.bank.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.unibet.worktest.bank.Money;
import com.unibet.worktest.bank.dao.AccountDao;
import com.unibet.worktest.bank.model.Account;

@Repository
@Transactional
public class AccountDaoImpl extends BaseDao<Account> implements AccountDao {
	
	@Override
	public void createAccount(String accountRef, Money amount) {
		save(new Account(accountRef, amount.getAmount(), amount.getCurrency().getCurrencyCode()));
	}

	@Override
	public Account getAccountByReference(String accountRef) {
	    return getEntityByReference(accountRef, "accountReference");
	}

}
