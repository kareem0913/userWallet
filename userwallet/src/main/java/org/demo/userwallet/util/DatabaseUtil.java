package org.demo.userwallet.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseUtil {
    private static final String URL = "jdbc:postgresql://localhost:5432/userwallet";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123";

    private DatabaseUtil() {
        throw  new IllegalStateException("Utility class");
    }

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL JDBC driver not found", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}