package crud;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Crud {
	
	private Connection connection;
	private Statement statement;
	
	public Crud() {
		try {
			//Bring in the driver
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Driver loaded");
			
			//Establish a connection
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/color_database", "root", "Box-chan1");
			System.out.println("Connection Established");
			
			//Create Statement
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			System.out.println("Statement object created");
			
			
		} catch (ClassNotFoundException e) {
			System.out.println("Driver not found");
		} catch (SQLException e) {
			System.out.println("Unable to establish connection");
		}
	}
	
	public void save(String hex, String name, String path) {
		String insertStatement = "INSERT INTO hex_values VALUES(DEFAULT, '" + hex + "', '" + name + "', '" + path + "')";		
		System.out.println(insertStatement);
		
		try {
			statement.executeUpdate(insertStatement);
			System.out.println("Hex saved successful");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ResultSet load() {
		ResultSet resultSet = null;
		
		String selectStatement = "SELECT hex, name, image_path FROM hex_values WHERE color_id = 5";
		
		try {
			resultSet = statement.executeQuery(selectStatement);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resultSet;
	}
	
	public void delete() {
		String deleteStatement = "DELETE FROM student WHERE color_id = '" + 1 + "'";
		
		try {
			statement.executeUpdate(deleteStatement);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void close() {
		try {
			connection.close();
			System.out.println("Connection closed");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}
