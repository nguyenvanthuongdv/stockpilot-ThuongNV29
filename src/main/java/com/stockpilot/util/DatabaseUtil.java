package com.stockpilot.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DatabaseUtil {
    private static final String URL = "jdbc:h2:./data/stockpilot;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            String schemaSql = new String(Files.readAllBytes(Paths.get("src/main/resources/schema.sql")));
            stmt.execute(schemaSql);
        } catch (Exception e) {
            System.err.println("Failed to initialize database schema: " + e.getMessage());
        }
    }
}
