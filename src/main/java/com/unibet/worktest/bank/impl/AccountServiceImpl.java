package com.unibet.worktest.bank.impl;

import java.util.Currency;

import org.springframework.transaction.annotation.Transactional;

import com.unibet.worktest.bank.AccountNotFoundException;
import com.unibet.worktest.bank.AccountService;
import com.unibet.worktest.bank.InfrastructureException;
import com.unibet.worktest.bank.Money;
import com.unibet.worktest.bank.dao.AccountDao;
import com.unibet.worktest.bank.model.Account;

public class AccountServiceImpl implements AccountService {
	
	private AccountDao accountDao;
	
	public AccountServiceImpl() {}
	
	public AccountServiceImpl(AccountDao accountDao) {
		this.accountDao = accountDao;
	}

	@Override
	public void createAccount(String accountRef, Money amount) {
		try {
			accountDao.createAccount(accountRef, amount);
		} catch (Exception e) {
			e.printStackTrace();
			throw new InfrastructureException("Some error occured while creating account");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Money getAccountBalance(String accountRef) {
		try {
			Account account = accountDao.getAccountByReference(accountRef);
			
			if (account == null) {
				throw new AccountNotFoundException(accountRef);
			}
			
			return new Money(account.getBalance(), Currency.getInstance(account.getCurrency()));
		} catch (Exception e) {
			e.printStackTrace();
			throw new InfrastructureException("Some error occured while retrieving account balance");
		}	
	}

}
