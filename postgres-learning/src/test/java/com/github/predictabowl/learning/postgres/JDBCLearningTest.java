package com.github.predictabowl.learning.postgres;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JDBCLearningTest {
	
	
	private static final String POSTGRES_URI = "jdbc:postgresql://localhost:5432/testdb";
	private Connection connection;

	@BeforeEach
	public void setUp() throws SQLException {
		Properties props = new Properties();
		props.setProperty("user", "mario");
		props.setProperty("password", "segreta");
		connection = DriverManager.getConnection(POSTGRES_URI,props);
		createTable();
	}
	
	@AfterEach
	public void tearDown() throws SQLException {
		Statement statement = null;
		try {
		statement = connection.createStatement();
		statement.execute("DROP TABLE sometable");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(statement != null) statement.close();
		}
		connection.close();
	}
	
	@Test
	public void test_connection(){
		assertThat(connection).isNotNull();
	}
	
	@Test
	public void test_SELECT_query() throws SQLException {
		populateTable();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT * FROM sometable WHERE foo = 500");
		LinkedList<String> list = new LinkedList<String>();
		while(resultSet.next()) {
			System.out.print("Column returned ");
			list.add(resultSet.getString(1));
			System.out.println(resultSet.getString(1));
		}
		assertThat(list).containsExactly("47");
		resultSet.close();
		statement.close();
	}
	
	private void createTable() throws SQLException {
		
		Statement statement=null;
		try {
			statement = connection.createStatement();
			statement.execute("CREATE TABLE sometable "+
					"(some_ID integer NOT NULL, "+
					"foo integer,"+
					"some_name varchar(40) NOT NULL, "+
					"PRIMARY KEY (some_ID))");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(statement != null) statement.close();	
		}
		
	}
	
	private void populateTable() throws SQLException {
		Statement statement = null;
		try {
			statement = connection.createStatement();
			statement.executeUpdate("INSERT into sometable VALUES("+
					"47, 500, 'Tizio')");
			statement.executeUpdate("INSERT into sometable VALUES("+
					"27, 450, 'Caio')");
			statement.executeUpdate("INSERT into sometable VALUES("+
					"16, 245, 'Sempronio')");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if(statement != null) statement.close();
		}
	}

}
