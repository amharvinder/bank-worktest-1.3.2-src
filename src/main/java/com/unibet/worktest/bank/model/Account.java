package com.unibet.worktest.bank.model;

import java.math.BigDecimal;
import java.util.Set;

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
@Table(name="account")
@DiscriminatorValue("ac")
public class Account extends BaseModel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 716424638232504588L;

	@Column(name="status")
	@Enumerated(EnumType.STRING)
	private AccountStatus accountStatus;
	
	@Column(name="reference")
	private String reference;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id")
	private User user;
	
	@Column(name="balance")
	private BigDecimal balance;
	
	@Column(name="currency")
	private String currency;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="account")
	private Set<MoneyTransaction> transactions;
	
	public Account() {}
	
	public Account(String reference, BigDecimal balance, String currency) {
		super();
		this.accountStatus = AccountStatus.ACTIVE;
		this.reference = reference;
		this.balance = balance;
		this.currency = currency;
	}

	public AccountStatus getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(AccountStatus accountStatus) {
		this.accountStatus = accountStatus;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<MoneyTransaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(Set<MoneyTransaction> transactions) {
		this.transactions = transactions;
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
		Account other = (Account) obj;
		if (reference == null) {
			if (other.reference != null)
				return false;
		} else if (!reference.equals(other.reference))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", accountStatus=" + accountStatus + ", accountReference=" + reference + ", balance="
				+ balance + ", currency=" + currency + "]";
	}
	
}
