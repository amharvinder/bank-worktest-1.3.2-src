package com.unibet.worktest.bank.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unibet.worktest.bank.AccountNotFoundException;
import com.unibet.worktest.bank.InfrastructureException;
import com.unibet.worktest.bank.Money;
import com.unibet.worktest.bank.Transaction;
import com.unibet.worktest.bank.TransactionLeg;
import com.unibet.worktest.bank.TransferRequest;
import com.unibet.worktest.bank.TransferService;
import com.unibet.worktest.bank.UnbalancedLegsException;
import com.unibet.worktest.bank.dao.AccountDao;
import com.unibet.worktest.bank.dao.TransferDao;
import com.unibet.worktest.bank.model.Account;
import com.unibet.worktest.bank.model.MoneyTransaction;
import com.unibet.worktest.bank.model.MoneyTransactionLeg;
import com.unibet.worktest.bank.model.TransactionStatus;
import com.unibet.worktest.bank.model.TransactionType;

@Service
public class TransferServiceImpl implements TransferService {

	@Autowired
	private TransferDao transferDao;
	
	@Autowired
	private AccountDao accountDao;
	
	@Override
	public void transferFunds(TransferRequest transferRequest) {
		try{
			verifyTransferRequest(transferRequest);

			MoneyTransaction moneyTransaction = new MoneyTransaction(
					account,
					TransactionType.valueOf(transferRequest.getType()), transferRequest.getReference());
			
			List<TransactionLeg> transactionLegs = transferRequest.getTransactionLegs();
			
			Set<MoneyTransactionLeg> moneyTransactionLegs = new HashSet<>();
			
			BigDecimal totalTransferFund = new BigDecimal(0);
			String transferCurrency  = null;
			
			for (TransactionLeg transactionLeg : transactionLegs) {
				Account account = accountDao.getAccountByReference(transactionLeg.getAccountRef());
				if (account == null) {
					throw new AccountNotFoundException(transactionLeg.getAccountRef());
				}
				
				Money money = transactionLeg.getAmount();
				
				if (transferCurrency == null) {
					transferCurrency = money.getCurrency().getCurrencyCode();
				} else if (StringUtils.isEmpty(transferCurrency) || !transferCurrency.equals(money.getCurrency().getCurrencyCode())){
					throw new IllegalArgumentException("Currencies are not same for transferReference: " + transferRequest.getReference());
				}
				
				
				totalTransferFund.add(money.getAmount());
				
				moneyTransactionLegs.add(
				new MoneyTransactionLeg(TransactionStatus.COMPLETED, moneyTransaction,
						money.getAmount(), money.getCurrency().getCurrencyCode(), account));
				
				
			}
			
			if (!totalTransferFund.equals(new BigDecimal(0))) {
				throw new UnbalancedLegsException("Transaction legs are unbalanced for transferReference: " + transferRequest.getReference());
			}
			
			moneyTransaction.setMoneyTransactionLegs(moneyTransactionLegs);
			
			transferDao.transferFunds(moneyTransaction);
		}  catch (Exception e) {
			throw new InfrastructureException("Some error occured while transfering funds for transactionRef: "
					+ transferRequest.getReference());
		}	
	}

	private void verifyTransferRequest(TransferRequest transferRequest) {
		String errorMessage = null;
		
		if (transferRequest.getType() != null) {
			if (StringUtils.isNotEmpty(transferRequest.getReference())) {
				List<TransactionLeg> transactionLegs = transferRequest.getTransactionLegs();
				
				if (transactionLegs == null || transactionLegs.size() < 2) {
					errorMessage = "Expected at least two account legs";
				}
				
			} else {
				errorMessage = "Transaction reference found null";
			}
			
		} else {
			errorMessage = "Transaction type found null";
		}
		
		if (StringUtils.isNotEmpty(errorMessage)) {
			throw new IllegalArgumentException(errorMessage);
		}
	}

	@Override
	public List<Transaction> findTransactions(String accountRef) {
		try {
			Account account = accountDao.getAccountByReference(accountRef);
			Set<MoneyTransaction> moneyTransactions = account.getTransactions();
			
			List<Transaction> transactions = new ArrayList<>(); 
			
			for (MoneyTransaction moneyTransaction : moneyTransactions) {
				transactions.add(adapt(moneyTransaction));
			}
			return transactions;
		} catch (Exception e) {
			throw new InfrastructureException("Some error occured while retrieving account transactions for accountRef: " + accountRef);
		}
	}

	@Override
	public Transaction getTransaction(String transactionRef) {
		try {
			return adapt(transferDao.getTransactionByReference(transactionRef));
		} catch (Exception e) {
			throw new InfrastructureException("Some error occured while retrieving transactions for transactionRef: " + transactionRef);
		}
	}
	
	private Transaction adapt(MoneyTransaction moneyTransaction) {
		List<TransactionLeg> transactionLegs = new ArrayList<>();
		
		Set<MoneyTransactionLeg> moneyTransactionLegs = moneyTransaction.getMoneyTransactionLegs();
		for (MoneyTransactionLeg moneyTransactionLeg : moneyTransactionLegs) {
			transactionLegs.add(new TransactionLeg(moneyTransaction.getAccount().getReference(),
					new Money(moneyTransactionLeg.getAmount(),
							Currency.getInstance(moneyTransactionLeg.getCurrency()))));
		}
		
		return new Transaction(moneyTransaction.getReference(),
				moneyTransaction.getType().name(), moneyTransaction.getCreatedOn(), transactionLegs);
	}

}
