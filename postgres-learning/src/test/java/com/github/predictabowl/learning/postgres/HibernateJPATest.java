package com.github.predictabowl.learning.postgres;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;

import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.predictabowl.learning.model.Employee;

public class HibernateJPATest {

	private EntityManager entityManager;
	private static EntityManagerFactory emFactory;

	@BeforeAll
	public static void setUpClass() {
		emFactory = Persistence.createEntityManagerFactory("com.github.predictabowl.learning.hibernatejpa");
	}
	
	@BeforeEach
	public void setUp() {
		entityManager = emFactory.createEntityManager();
	}
	
	@AfterEach
	public void tearDown() {
		if(entityManager != null)
			entityManager.close();
		
	}
	
	@AfterAll
	public static void tearDownClass() {
		emFactory.close();
	}
	
	/**
	 * For now we rely on persistance.xml setting that create a new table
	 * every time and entityManager is created.
	 * That should not be the case.
	 */
	@Test
	public void test_write_into_db() {
		Employee employee = new Employee(null, "Sergio", 1350);
		Employee employee3 = new Employee(null, "Luigi", 1550);
		entityManager.getTransaction().begin();
		entityManager.persist(employee);
		entityManager.persist(employee3);
		entityManager.getTransaction().commit();
		
		entityManager.getTransaction().begin();
		Employee employee2 = entityManager.find(Employee.class,1L);
		entityManager.getTransaction().commit();
		
		assertThat(employee2).isEqualTo(employee);
		
		entityManager.getTransaction().begin();
		employee.setName("Giulio");
		entityManager.getTransaction().commit();
		
		entityManager.getTransaction().begin();
		employee2 = entityManager.find(Employee.class,1L);
		entityManager.getTransaction().commit();
		
		assertThat(employee2).isEqualTo(new Employee(1L,"Giulio",1350));
		
	}
	
	/**
	 * This is not how it should be done, on every test the DB should be cleared,
	 * here we rely on previous data, just to see how it behave. 
	 */
	@Test
	public void test_read_into_db() {
//		TypedQuery<Employee> query = entityManager.createQuery("SELECT c FROM employee c",Employee.class);
//		query.getResultList().stream().peek(e -> System.out.println(e));		
	}
}
