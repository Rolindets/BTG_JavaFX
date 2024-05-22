

package com.mycompany.testingjavafx;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Database URL
    private static final String URL = "jdbc:mysql://localhost:3306/builders_trust_group";
    // Database credentials
    private static final String USER = "root";
    private static final String PASSWORD = "Lebron06!";

    public static Connection getConnection() 
    {
        Connection connection = null;
        try 
        {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establish the connection
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connection established successfully!");
        } catch (ClassNotFoundException e) 
        {
            System.out.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) 
        {
            System.out.println("Failed to establish connection.");
            e.printStackTrace();
        }
        return connection;
    }
}