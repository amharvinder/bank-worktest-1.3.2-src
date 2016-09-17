package com.unibet.worktest.bank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.unibet.worktest.bank.impl.AccountServiceImpl;
import com.unibet.worktest.bank.impl.TransferServiceImpl;

@Component
public class BankFactoryImpl implements BankFactory {

	@Autowired
	private AccountService accountService;
	
	@Autowired
	private TransferService transferService;

	@Override
	public AccountService getAccountService() {
		return accountService;
	}

	@Override
	public TransferService getTransferService() {
		return transferService;
	}

	@Override
	public void setupInitialData() {
		accountService = new AccountServiceImpl();
		transferService = new TransferServiceImpl();
	}

}
