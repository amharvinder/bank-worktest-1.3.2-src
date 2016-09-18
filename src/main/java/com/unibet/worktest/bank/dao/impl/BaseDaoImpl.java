package com.unibet.worktest.bank.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.core.GenericTypeResolver;

import com.unibet.worktest.Application;
import com.unibet.worktest.bank.dao.BaseDao;

public class BaseDaoImpl<T> implements BaseDao<T> {
	
	/*private static final String PERSISTENCE_UNIT_NAME = "bank-worktest";
	
	PersistenceProvider provider = new HibernatePersistenceProvider();
	//EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
	
	EntityManagerFactory factory = provider.createEntityManagerFactory(
			PERSISTENCE_UNIT_NAME, new HashMap<String, String>());*/
	
	/*ApplicationContext ctx = new ClassPathXmlApplicationContext(
			"META-INF/application-context.xml");
			new AnnotationConfigApplicationContext(HibernateConfiguration.class);*/
	
	/*@PersistenceUnit*/
//	EntityManagerFactory factory = (EntityManagerFactory) ctx.getBean("entityManagerFactory");
	
	/*@PersistenceContext
	EntityManager entityManager;*/
	
//	private final ThreadLocal<EntityManager> threadSafeEntityManager = ThreadLocal.withInitial(() -> factory.createEntityManager());
	
	private final Class<T> genericType;
	
	@SuppressWarnings("unchecked")
	public BaseDaoImpl() {
		this.genericType = (Class<T>) GenericTypeResolver.resolveTypeArgument(getClass(), BaseDao.class);
	}
	
	public BaseDaoImpl(Class<T> subClass) {
		this.genericType = subClass;
//		factory = (EntityManagerFactory) Application.getApplicationContext().getBean("entityManagerFactory");
	}

	/*protected EntityManager getEntityManager() {
//		return entityManager;
		return threadSafeEntityManager.get();
	}*/
	
	public void save(T t) {
//		EntityManager em = getEntityManager();
		try {
//			em.getTransaction().begin();
			Application.beginTransaction();
			Application.getEntityManager().persist(t);
			Application.commit();
//			em.getTransaction().commit();
		} catch (RuntimeException e) {
        	Application.rollback();
            throw e;

        } finally {
//        	Application.closeEntityManager();
        }
	}
	
	public T update(T t) {
		/*EntityManager em = getEntityManager();
		try {
			em.getTransaction().begin();
			T mergedT = em.merge(t);
			em.getTransaction().commit();
			
			return mergedT;
		} finally {
//			em.close();
		}*/
		
		try {
			Application.beginTransaction();
			T mergedT = Application.getEntityManager().merge(t);
			Application.commit();
			return mergedT;
		} catch (RuntimeException e) {
        	Application.rollback();
            throw e;

        } finally {
//        	Application.closeEntityManager();
        }
	}
	
	public void delete(T t) {
		/*EntityManager em = getEntityManager();
		try {
			em.getTransaction().begin();
			em.remove(t);
			em.getTransaction().commit();
		} finally {
//			em.close();
		}*/
		
		try {
			Application.beginTransaction();
			Application.getEntityManager().remove(t);
			Application.commit();
		} catch (RuntimeException e) {
        	Application.rollback();
            throw e;

        } finally {
//        	Application.closeEntityManager();
        }
	}
	
	public void deleteAll() {
		/*EntityManager em = getEntityManager();
		try {
			em.getTransaction().begin();
			em.createQuery("Delete from " + genericType.getName()).executeUpdate();
			em.getTransaction().commit();
		} finally {
//			em.close();
		}*/
		
		try {
			Application.beginTransaction();
			Application.getEntityManager().createQuery("Delete from " + genericType.getName()).executeUpdate();
			Application.commit();
		} catch (RuntimeException e) {
        	Application.rollback();
            throw e;

        } finally {
//        	Application.closeEntityManager();
        }
	}
	
	public void refresh(T t) {
		/*EntityManager em = getEntityManager();
		try {
			em.getTransaction().begin();
			em.refresh(t);
			em.getTransaction().commit();
		} finally {
//			em.close();
		}*/
		
		try {
			Application.beginTransaction();
			Application.getEntityManager().refresh(t);
			Application.commit();
		} catch (RuntimeException e) {
        	Application.rollback();
            throw e;

        } finally {
//        	Application.closeEntityManager();
        }
	}

	public T getEntityByReference(String entityRef, String fieldName) {
		EntityManager em = Application.getEntityManager();
		
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
