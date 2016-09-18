package com.unibet.worktest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.unibet.worktest.configuration.HibernateConfiguration;

public class Application {
	
	private static final ApplicationContext context;
	private static final EntityManagerFactory emf; 
    private static final ThreadLocal<EntityManager> threadSafeEntityManager;

	static {
		System.out.println("Allication Loaded");
		context = new AnnotationConfigApplicationContext(HibernateConfiguration.class);
		emf = (EntityManagerFactory) context.getBean("entityManagerFactory");
		threadSafeEntityManager= ThreadLocal.withInitial(() -> emf.createEntityManager());
	}
	
	public static EntityManager getEntityManager() {
        EntityManager em = threadSafeEntityManager.get();

        if (em == null) {
            em = emf.createEntityManager();
            threadSafeEntityManager.set(em);
        }
        return em;
    }

    public static void closeEntityManager() {
        EntityManager em = threadSafeEntityManager.get();
        if (em != null) {
        	if (em.isOpen()) {
	            em.close();
	            threadSafeEntityManager.set(null);
        	}
        }
    }

    public static void closeEntityManagerFactory() {
        emf.close();
    }
    
    public static EntityTransaction getTransaction() {
        return getEntityManager().getTransaction();
    }

    public static void beginTransaction() {
        getTransaction().begin();
    }

    public static void rollback() {
    	EntityTransaction transaction = getTransaction();
    	if (transaction != null && transaction.isActive()) {
    		getTransaction().rollback();
    	}
    }

    public static void commit() {
        getTransaction().commit();
    } 
	
	
}
