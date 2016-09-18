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
	@JoinColumn(name="debit_account_id")
	private Account debitAccount;
	
	@ManyToOne
	@JoinColumn(name="credit_account_id")
	private Account creditAccount;
	
	@Column(name="type")
	@Enumerated(EnumType.STRING)
	private TransactionType type;
	
	@Column(name="reference")
	private String reference;
	
	@OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL, mappedBy="moneyTransaction")
	private Set<MoneyTransactionLeg> moneyTransactionLegs;
	
	public MoneyTransaction() {}
	
	public MoneyTransaction(String reference) {
		super();
		// Since we aren't getting Transaction type for now, setting default value
		this.type = TransactionType.RTGS;
		this.reference = reference;
	}

	public Account getDebitAccount() {
		return debitAccount;
	}

	public void setDebitAccount(Account debitAccount) {
		this.debitAccount = debitAccount;
	}

	public Account getCreditAccount() {
		return creditAccount;
	}

	public void setCreditAccount(Account creditAccount) {
		this.creditAccount = creditAccount;
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
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((reference == null) ? 0 : reference.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		MoneyTransaction other = (MoneyTransaction) obj;
		if (reference == null) {
			if (other.reference != null)
				return false;
		} else if (!reference.equals(other.reference))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MoneyTransaction [id=" + id + ", debitAccount=" + debitAccount.getId() 
				+ ", creditAccount=" + creditAccount.getId() + ", type=" + type
				+ ", reference=" + reference + ", transactionlegs" + moneyTransactionLegs +"]";
	}
	
}
