package com.unibet.worktest.bank.dao.impl;

import java.util.Set;

import javax.persistence.EntityManager;

import com.unibet.worktest.BankSession;
import com.unibet.worktest.bank.dao.MoneyTransactionDao;
import com.unibet.worktest.bank.model.MoneyTransaction;
import com.unibet.worktest.bank.model.MoneyTransactionLeg;

public class MoneyTransactionDaoImpl extends BaseDaoImpl<MoneyTransaction> implements MoneyTransactionDao {
	
	public MoneyTransactionDaoImpl() {
		super(MoneyTransaction.class);
	}

	@Override
	public void transferFunds(MoneyTransaction moneyTransaction) {
		Set<MoneyTransactionLeg> transactionlegs = moneyTransaction.getMoneyTransactionLegs();
		try {
			EntityManager em = BankSession.getEntityManager();
			BankSession.beginTransaction();

			for (MoneyTransactionLeg moneyTransactionLeg : transactionlegs) {
				System.out.println("transferFunds: merging account" + moneyTransactionLeg.getAccount());
				em.merge(moneyTransactionLeg.getAccount());
			}
		
			em.persist(moneyTransaction);
			BankSession.commit();
		} catch (RuntimeException e) {
			BankSession.rollback();
			
			throw e;
		}
	}

	@Override
	public MoneyTransaction getTransactionByReference(String transactionRef) {
		return getEntityByReference(transactionRef, "reference");
	}

}
