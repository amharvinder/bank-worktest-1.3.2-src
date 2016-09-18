package com.unibet.worktest.bank.dto;

import java.util.Map;

import com.unibet.worktest.bank.model.Account;
import com.unibet.worktest.bank.model.MoneyTransaction;

public class TransactionAccountDTO {
	private MoneyTransaction moneyTransaction;
	private Map<String, Account> debitCreditAccount;
	
	public TransactionAccountDTO(MoneyTransaction moneyTransaction, Map<String, Account> debitCreditAccount) {
		this.moneyTransaction = moneyTransaction;
		this.debitCreditAccount = debitCreditAccount;
	}

	public MoneyTransaction getMoneyTransaction() {
		return moneyTransaction;
	}

	public void setMoneyTransaction(MoneyTransaction moneyTransaction) {
		this.moneyTransaction = moneyTransaction;
	}

	public Map<String, Account> getDebitCreditAccount() {
		return debitCreditAccount;
	}

	public void setDebitCreditAccount(Map<String, Account> debitCreditAccount) {
		this.debitCreditAccount = debitCreditAccount;
	}
}
