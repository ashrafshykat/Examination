package singletonConnection;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class SingletonConnection {
    private static BasicDataSource dataSource;

    // Private constructor to prevent instantiation
    private SingletonConnection() {}

    // Initialize the connection pool
    static {
        dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/examination"); // Update your DB URL
        dataSource.setUsername("root"); // Update your username
        dataSource.setPassword(""); // Update your password

        // Pool configuration
        dataSource.setMinIdle(5); // Minimum idle connections
        dataSource.setMaxIdle(10); // Maximum idle connections
        dataSource.setMaxTotal(20); // Maximum total connections
        dataSource.setMaxOpenPreparedStatements(100); // Maximum open prepared statements
    }

    // Provide access to the pooled connections
    public static Connection getCon() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to get a database connection!", e);
        }
    }

    // Optional method to shut down the pool
    public static void closeConnectionPool() {
        try {
            if (dataSource != null) {
                dataSource.close();
                System.out.println("Connection pool shut down!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
