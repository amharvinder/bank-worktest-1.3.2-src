package com.unibet.worktest.bank.impl;

import java.util.Currency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unibet.worktest.bank.AccountNotFoundException;
import com.unibet.worktest.bank.AccountService;
import com.unibet.worktest.bank.InfrastructureException;
import com.unibet.worktest.bank.Money;
import com.unibet.worktest.bank.dao.AccountDao;
import com.unibet.worktest.bank.model.Account;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {
	
	@Autowired
	private AccountDao accountDao;

	@Override
	public void createAccount(String accountRef, Money amount) {
		try {
			accountDao.createAccount(accountRef, amount);
		} catch (Exception e) {
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
			throw new InfrastructureException("Some error occured while retrieving account balance");
		}	
	}

}
