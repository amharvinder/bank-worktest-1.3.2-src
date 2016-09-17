package com.unibet.worktest.bank.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.core.GenericTypeResolver;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class BaseDao<T> {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	private final Class<T> genericType;
	
	@SuppressWarnings("unchecked")
	public BaseDao() {
		this.genericType = (Class<T>) GenericTypeResolver.resolveTypeArgument(getClass(), BaseDao.class);
	}
	
	public void save(T t) {
		entityManager.persist(t);
	}

	@Transactional(readOnly=true)
	public T getEntityByReference(String entityRef, String fieldName) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
	    CriteriaQuery<T> criteria = builder.createQuery(genericType);
	    Root<T> account = criteria.from(genericType);
	    criteria.select(account);
	    criteria.where(builder.equal(account.get(fieldName), entityRef));
	    TypedQuery<T> query = entityManager.createQuery(criteria);
	    try {
	        return query.getSingleResult();
	    } catch (final NoResultException nre) {
	        return null;
	    }
	}
}
