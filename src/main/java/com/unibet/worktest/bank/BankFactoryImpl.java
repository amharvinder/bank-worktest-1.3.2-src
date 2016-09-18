package com.unibet.worktest.bank;

import com.unibet.worktest.bank.dao.impl.AccountDaoImpl;
import com.unibet.worktest.bank.dao.impl.MoneyTransactionDaoImpl;
import com.unibet.worktest.bank.dao.impl.MoneyTransactionLegDaoImpl;
import com.unibet.worktest.bank.impl.AccountServiceImpl;
import com.unibet.worktest.bank.impl.TransferServiceImpl;

/*@Component*/
public class BankFactoryImpl implements BankFactory {
	
	/*static { 
		System.out.println("Initializing BankFactoryImpl");
		new Application();
	}*/
	
	/*@Autowired
	private AccountService accountService;*/

	@Override
	public AccountService getAccountService() {
		System.out.println("accountSrevice");
		/*if (accountService == null) {
			throw new NullPointerException("accountService object is null");
		}*/
		return new AccountServiceImpl(new AccountDaoImpl());
	}

	@Override
	public TransferService getTransferService() {
		System.out.println("TransferSrevice");
		return new TransferServiceImpl(new MoneyTransactionDaoImpl(), new AccountDaoImpl());
	}

	@Override
	public void setupInitialData() {
		System.out.println("Setting up Initial Data");
		new MoneyTransactionLegDaoImpl().deleteAll();
		new MoneyTransactionDaoImpl().deleteAll();
		new AccountDaoImpl().deleteAll();
	}

}
