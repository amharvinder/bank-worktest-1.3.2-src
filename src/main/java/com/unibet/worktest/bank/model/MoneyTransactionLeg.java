package com.unibet.worktest.bank.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="money_transaction_leg")
@DiscriminatorValue("mtl")
public class MoneyTransactionLeg extends BaseModel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8466940961129580547L;
	
	@Column(name="status")
	@Enumerated(EnumType.STRING)
	private TransactionStatus status;
	
	@ManyToOne
	@JoinColumn(name="transaction_id")
	private MoneyTransaction moneyTransaction;
	
	@Column(name="amount")
	private BigDecimal amount;
	
	@Column(name="currency")
	private String currency;
	
	@ManyToOne
	@JoinColumn(name="account_id")
	private Account account;
	
	public MoneyTransactionLeg() {}

	public MoneyTransactionLeg(TransactionStatus status, MoneyTransaction moneyTransaction, BigDecimal amount,
			String currency, Account account) {
		super();
		this.status = status;
		this.moneyTransaction = moneyTransaction;
		this.amount = amount;
		this.currency = currency;
		this.account = account;
	}

	public TransactionStatus getStatus() {
		return status;
	}

	public void setStatus(TransactionStatus status) {
		this.status = status;
	}

	public MoneyTransaction getMoneyTransaction() {
		return moneyTransaction;
	}

	public void setMoneyTransaction(MoneyTransaction moneyTransaction) {
		this.moneyTransaction = moneyTransaction;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}
	
}
