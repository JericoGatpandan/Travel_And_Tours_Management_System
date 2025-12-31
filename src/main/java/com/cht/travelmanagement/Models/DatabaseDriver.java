package com.cht.travelmanagement.Models;



import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseDriver {
    private static final Logger logger = Logger.getLogger(DatabaseDriver.class.getName());
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/cht_updated";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "password";

    public DatabaseDriver() {}

    public static Connection getConnection() throws SQLException {
        Connection connection = null;
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        try {
            connection = java.sql.DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw e;
        }
        return connection;
    }


}
