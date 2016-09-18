package com.unibet.worktest.bank.dao.impl;

import java.util.Set;

import org.apache.log4j.Logger;

import com.unibet.worktest.bank.Money;
import com.unibet.worktest.bank.dao.AccountDao;
import com.unibet.worktest.bank.model.Account;
import com.unibet.worktest.bank.model.MoneyTransaction;

public class AccountDaoImpl extends BaseDaoImpl<Account> implements AccountDao {
	
	private final Logger logger = Logger.getLogger(AccountDaoImpl.class);
	
	public AccountDaoImpl() {
		super(Account.class);
	}
	
	@Override
	public void createAccount(String accountRef, Money amount) {
		Account account = new Account(accountRef, amount.getAmount(), amount.getCurrency().getCurrencyCode());
		logger.info("===> Creating Account <=== with " + account);
		System.out.println("===> Creating Account <=== with " + account);
		save(new Account(accountRef, amount.getAmount(), amount.getCurrency().getCurrencyCode()));
	}

	@Override
	public Account getAccountByReference(String accountRef) {
	    return getEntityByReference(accountRef, "reference");
	}

	@Override
	public Set<MoneyTransaction> getAccountTransactions(String accountRef) {
		Account account = getAccountByReference(accountRef);
		
		Set<MoneyTransaction> moneyTransactions = account.getDebitTransactions();
		moneyTransactions.addAll(account.getCreditTransactions());
		
		System.out.println("All Transactions ==> " + moneyTransactions);
		
		return moneyTransactions;
	}
	
}
