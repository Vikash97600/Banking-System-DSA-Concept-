package com.dsa.project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

    private static final String URL_ROOT = "jdbc:mysql://localhost:3306/";
    private static final String URL_DB = "jdbc:mysql://localhost:3306/BankingDSA";
    private static final String USER = "root";
    private static final String PASS = "root";

    static {
        createDatabaseAndTables();
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL_DB, USER, PASS);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
    }

    private static void createDatabaseAndTables() {
        try (Connection con = DriverManager.getConnection(URL_ROOT, USER, PASS);
             Statement stmt = con.createStatement()) {

            stmt.execute("CREATE DATABASE IF NOT EXISTS BankingDSA");
            stmt.execute("USE BankingDSA");

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS Customers (
                    AccountNo INT AUTO_INCREMENT PRIMARY KEY,
                    Name VARCHAR(50) NOT NULL,
                    Email VARCHAR(50),
                    Phone VARCHAR(15) NOT NULL,
                    Balance DOUBLE NOT NULL DEFAULT 0.0
                ) AUTO_INCREMENT = 1001
                """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS Transactions (
                    TransactionID INT AUTO_INCREMENT PRIMARY KEY,
                    AccountNo INT,
                    Message VARCHAR(255) NOT NULL,
                    FOREIGN KEY (AccountNo) REFERENCES Customers(AccountNo) ON DELETE CASCADE
                )
                """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS Manager_Login (
                    Username VARCHAR(50) PRIMARY KEY,
                    Password VARCHAR(50) NOT NULL
                )
                """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS Customer_Login (
                    AccountNo INT PRIMARY KEY,
                    Password VARCHAR(50) NOT NULL,
                    FOREIGN KEY (AccountNo) REFERENCES Customers(AccountNo) ON DELETE CASCADE
                )
                """);

            stmt.execute("INSERT IGNORE INTO Manager_Login VALUES ('admin', 'admin123')");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}