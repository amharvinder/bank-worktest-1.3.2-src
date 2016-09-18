package com.unibet.worktest.bank.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.unibet.worktest.BankSession;
import com.unibet.worktest.bank.dao.BaseDao;

public class BaseDaoImpl<T> implements BaseDao<T> {
	
	private final Class<T> genericType;
	
	public BaseDaoImpl(Class<T> subClass) {
		this.genericType = subClass;
	}

	public void save(T t) {
		try {
			BankSession.beginTransaction();
			BankSession.getEntityManager().persist(t);
			BankSession.commit();
		} catch (RuntimeException e) {
        	BankSession.rollback();
            throw e;
        } 
	}
	
	public T update(T t) {
		try {
			BankSession.beginTransaction();
			T mergedT = BankSession.getEntityManager().merge(t);
			BankSession.commit();
			return mergedT;
		} catch (RuntimeException e) {
        	BankSession.rollback();
            throw e;
        } 
	}
	
	public void delete(T t) {
		try {
			BankSession.beginTransaction();
			BankSession.getEntityManager().remove(t);
			BankSession.commit();
		} catch (RuntimeException e) {
        	BankSession.rollback();
            throw e;

        } 
	}
	
	public void deleteAll() {
		try {
			BankSession.beginTransaction();
			BankSession.getEntityManager().createQuery("Delete from " + genericType.getName()).executeUpdate();
			BankSession.commit();
		} catch (RuntimeException e) {
        	BankSession.rollback();
            throw e;

        }
	}
	
	public void refresh(T t) {
		try {
			BankSession.beginTransaction();
			BankSession.getEntityManager().refresh(t);
			BankSession.commit();
		} catch (RuntimeException e) {
        	BankSession.rollback();
            throw e;

        }
	}

	public T getEntityByReference(String entityRef, String fieldName) {
		EntityManager em = BankSession.getEntityManager();
		
		System.out.println("============>>>> genericType" + genericType);
		CriteriaBuilder builder = em.getCriteriaBuilder();
	    CriteriaQuery<T> criteria = builder.createQuery(genericType);
	    Root<T> account = criteria.from(genericType);
	    criteria.select(account);
	    criteria.where(builder.equal(account.get(fieldName), entityRef));
	    TypedQuery<T> query = em.createQuery(criteria);
	    try {
	    	T t = query.getSingleResult();
	    	em.refresh(t);
	        return t;
	    } catch (final NoResultException nre) {
	        return null;
	    }
	}
}
