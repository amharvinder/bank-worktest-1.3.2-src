package com.unibet.worktest.bank.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="money_transaction")
@DiscriminatorValue("mt")
public class MoneyTransaction extends BaseModel{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2745611270233240167L;

	@ManyToOne
	@JoinColumn(name="account_id")
	private Account account;
	
	@Column(name="type")
	@Enumerated(EnumType.STRING)
	private TransactionType type;
	
	@Column(name="reference")
	private String reference;
	
	@OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL, mappedBy="moneyTransaction")
	private Set<MoneyTransactionLeg> moneyTransactionLegs;
	
	public MoneyTransaction() {}
	
	public MoneyTransaction(Account account, TransactionType type, String reference) {
		super();
		this.account = account;
		this.type = type;
		this.reference = reference;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public TransactionType getType() {
		return type;
	}

	public void setType(TransactionType type) {
		this.type = type;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}
	
	public Set<MoneyTransactionLeg> getMoneyTransactionLegs() {
		return moneyTransactionLegs;
	}

	public void setMoneyTransactionLegs(Set<MoneyTransactionLeg> moneyTransactionLegs) {
		this.moneyTransactionLegs = moneyTransactionLegs;
	}

	@Override
	public String toString() {
		return "MoneyTransaction [id=" + id + ", account=" + account + ", type=" + type
				+ ", reference=" + reference + "]";
	}
	
}
