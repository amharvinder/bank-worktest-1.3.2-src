package com.unibet.worktest.bank.dao;

public interface BaseDao<T> {
	
	public void save(T t);
	
	public T update(T t);
	
	public void delete(T t);
	
	public void deleteAll();
	
	public void refresh(T t) ;

	public T getEntityByReference(String entityRef, String fieldName);

}
