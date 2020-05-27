package com.github.predictabowl.learning.postgres;

import static org.assertj.core.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.predictabowl.learning.model.Employee;

public class HibernateJPATest {

	private EntityManager entityManager;
	private EntityManagerFactory emFactory;

	@BeforeEach
	public void setUp() {
		emFactory = Persistence.createEntityManagerFactory("com.github.predictabowl.learning.hibernatejpa");
		entityManager = emFactory.createEntityManager();
	}
	
	@AfterEach
	public void tearDown() {
		if(entityManager != null)
			entityManager.close();
		emFactory.close();
	}
	
	@Test
	public void test_read_write_into_db() {
		Employee employee = new Employee(null, "Carmelo", 1250);
		entityManager.getTransaction().begin();
		entityManager.persist(employee);
		entityManager.getTransaction().commit();
		
//		entityManager.getTransaction().begin();
//		Employee employee2 = entityManager.find(Employee.class, 10L);
//		
//		assertThat(employee2).isEqualTo(employee);
	}
}
