package com.unibet.worktest.bank.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.unibet.worktest.bank.AccountNotFoundException;
import com.unibet.worktest.bank.InfrastructureException;
import com.unibet.worktest.bank.InsufficientFundsException;
import com.unibet.worktest.bank.Money;
import com.unibet.worktest.bank.Transaction;
import com.unibet.worktest.bank.TransactionLeg;
import com.unibet.worktest.bank.TransferRequest;
import com.unibet.worktest.bank.TransferService;
import com.unibet.worktest.bank.UnbalancedLegsException;
import com.unibet.worktest.bank.dao.AccountDao;
import com.unibet.worktest.bank.dao.MoneyTransactionDao;
import com.unibet.worktest.bank.dto.TransactionAccountDTO;
import com.unibet.worktest.bank.model.Account;
import com.unibet.worktest.bank.model.MoneyTransaction;
import com.unibet.worktest.bank.model.MoneyTransactionLeg;
import com.unibet.worktest.bank.model.TransactionStatus;

/*@Service*/
public class TransferServiceImpl implements TransferService {

//	@Autowired
	private MoneyTransactionDao transferDao;
	
//	@Autowired
	private AccountDao accountDao;
	
	public TransferServiceImpl() {}
	
	public TransferServiceImpl(MoneyTransactionDao transferDao, AccountDao accountDao) {
		super();
		this.transferDao = transferDao;
		this.accountDao = accountDao;
	}

	@Override
	public void transferFunds(TransferRequest transferRequest) {
		try{
			verifyTransferRequest(transferRequest);

			List<TransactionLeg> transactionLegs = transferRequest.getTransactionLegs();
			
			Set<MoneyTransactionLeg> moneyTransactionLegs = new HashSet<>();
			
			TransactionAccountDTO transactionAccountDTO = createTransactionAccountDTO(transferRequest, transactionLegs);
			
			MoneyTransaction moneyTransaction = transactionAccountDTO.getMoneyTransaction();
			int i=0;
			
			for (TransactionLeg transactionLeg : transactionLegs) {
				Account account = transactionAccountDTO.getDebitCreditAccount().get(transactionLeg.getAccountRef());
				
				Money money = transactionLeg.getAmount();
				
				BigDecimal newBalance = account.getBalance().add(money.getAmount());
				
				System.out.println("OldBalance: " + account.getBalance() + " Augmend: " + money.getAmount());
				System.out.println("NewBalance: " + newBalance);
				
				if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
					throw new InsufficientFundsException(transactionLeg.getAccountRef());
				}
				
				account.setBalance(newBalance);
				
				/*if (money.getAmount().compareTo(BigDecimal.ZERO) < 0) {
					moneyTransaction.setDebitAccount(account);
				} else {
					moneyTransaction.setCreditAccount(account);
				}*/
				
				/*if (transferCurrency == null) {
					transferCurrency = money.getCurrency().getCurrencyCode();
				} else if (StringUtils.isEmpty(transferCurrency) || !transferCurrency.equals(money.getCurrency().getCurrencyCode())){
					throw new IllegalArgumentException("Currencies are not same for transferReference: " + transferRequest.getReference());
				}*/
				
				moneyTransactionLegs.add(
				new MoneyTransactionLeg(TransactionStatus.COMPLETED, moneyTransaction,
						money.getAmount(), money.getCurrency().getCurrencyCode(), account));
				
				i++;
			}
			
			moneyTransaction.setMoneyTransactionLegs(moneyTransactionLegs);

			System.out.println("Loop ran for" + i + " and Transactions "  + moneyTransactionLegs);
			
			transferDao.transferFunds(moneyTransaction);
		}  catch (Exception e) {
			e.printStackTrace();
			throw new InfrastructureException("Some error occured while transfering funds for transactionRef: "
					+ transferRequest.getReference());
		}	
	}

	private void verifyTransferRequest(TransferRequest transferRequest) {
		String errorMessage = null;
		
		if (StringUtils.isNotEmpty(transferRequest.getReference())) {
			List<TransactionLeg> transactionLegs = transferRequest.getTransactionLegs();
			
			if (transactionLegs != null && transactionLegs.size() >= 2) {
				
				verifyFundsTransfer(transferRequest.getReference(), transactionLegs);
				
			} else {
				errorMessage = "Expected at least two account legs";
			}
			
		} else {
			errorMessage = "Transaction reference found null";
		}
			
		if (StringUtils.isNotEmpty(errorMessage)) {
			throw new IllegalArgumentException(errorMessage);
		}
	}

	private void verifyFundsTransfer(String transferReference, List<TransactionLeg> transactionLegs) {
		Map<String, BigDecimal> totalTransferFund = new HashMap<>();
		
		for (TransactionLeg transactionLeg : transactionLegs) {
			Money money = transactionLeg.getAmount();
			updateTransferFund(totalTransferFund, money);
		}
		
		Set<Entry<String, BigDecimal>> fundEntries = totalTransferFund.entrySet();
		
		/* If one or more currency are involved in a transaction, total 
		 * transaction sum (adding all legs of one currency) should be zero for each currency.
		 */
		for (Entry<String, BigDecimal> fundEntry : fundEntries) {
			if (fundEntry.getValue().compareTo(BigDecimal.ZERO) != 0) {
				throw new UnbalancedLegsException(
						"Transaction legs are unbalanced for transferReference: " 
								+ transferReference);
			}
		}
	}

	private void updateTransferFund(Map<String, BigDecimal> totalTransferFund, Money money) {
		BigDecimal amount = totalTransferFund.get(money.getCurrency().getCurrencyCode());
		
		amount = (amount != null) ? amount.add(money.getAmount()) : money.getAmount();
		
		totalTransferFund.put(money.getCurrency().getCurrencyCode(), amount);
		
	}

	@Override
	public List<Transaction> findTransactions(String accountRef) {
		try {
			Set<MoneyTransaction> moneyTransactions = accountDao.getAccountTransactions(accountRef);
			
			List<Transaction> transactions = new ArrayList<>(); 
			
			for (MoneyTransaction moneyTransaction : moneyTransactions) {
				transactions.add(adapt(moneyTransaction));
			}
			return transactions;
		} catch (Exception e) {
			e.printStackTrace();
			throw new InfrastructureException("Some error occured while retrieving account transactions for accountRef: " + accountRef);
		}
	}

	@Override
	public Transaction getTransaction(String transactionRef) {
		try {
			return adapt(transferDao.getTransactionByReference(transactionRef));
		} catch (Exception e) {
			e.printStackTrace();
			throw new InfrastructureException("Some error occured while retrieving transactions for transactionRef: " + transactionRef);
		}
	}
	
	private Transaction adapt(MoneyTransaction moneyTransaction) {
		List<TransactionLeg> transactionLegs = new ArrayList<>();
		
		Set<MoneyTransactionLeg> moneyTransactionLegs = moneyTransaction.getMoneyTransactionLegs();
		for (MoneyTransactionLeg moneyTransactionLeg : moneyTransactionLegs) {
			if (moneyTransactionLeg.getAmount().compareTo(BigDecimal.ZERO) < 0) {
				transactionLegs.add(new TransactionLeg(moneyTransaction.getDebitAccount().getReference(),
						new Money(moneyTransactionLeg.getAmount(),
								Currency.getInstance(moneyTransactionLeg.getCurrency()))));
			} else {
				transactionLegs.add(new TransactionLeg(moneyTransaction.getCreditAccount().getReference(),
						new Money(moneyTransactionLeg.getAmount(),
								Currency.getInstance(moneyTransactionLeg.getCurrency()))));
			}
		}
		
		return new Transaction(moneyTransaction.getReference(),
				moneyTransaction.getType().name(), moneyTransaction.getCreatedOn(), transactionLegs);
	}
	
	private TransactionAccountDTO createTransactionAccountDTO(TransferRequest transferRequest, List<TransactionLeg> transactionLegs) {
		MoneyTransaction moneyTransaction = new MoneyTransaction(transferRequest.getReference());
		
		Map<String, Account> debitCreditAccount = new HashMap<>();
		
		for (TransactionLeg transactionLeg : transactionLegs) {
			String accountref = transactionLeg.getAccountRef();
			
			if (debitCreditAccount.get(accountref) == null) {
				Account account = accountDao.getAccountByReference(accountref);
				if (account == null) {
					throw new AccountNotFoundException(transactionLeg.getAccountRef());
				}
				
				Money money = transactionLeg.getAmount();
				
				if (money.getAmount().compareTo(BigDecimal.ZERO) < 0) {
					moneyTransaction.setDebitAccount(account);
				} else {
					moneyTransaction.setCreditAccount(account);
				}
				
				debitCreditAccount.put(accountref, account);
			}
		}
		
		return new TransactionAccountDTO(moneyTransaction, debitCreditAccount);
	}
	
}
