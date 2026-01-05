package com.cht.travelmanagement.Models;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;

public class DatabaseInitializer {

    public static void initialize() {
        File scriptFile = new File("schema.sql");
        if (!scriptFile.exists()) {
            System.out.println("No schema.sql found. Skipping auto-initialization.");
            return;
        }

        System.out.println("Found schema.sql! initializing database...");

        Properties props = new Properties();
        try (FileInputStream input = new FileInputStream("config.properties")) {
            props.load(input);
        } catch (Exception e) {
            System.out.println("Could not read config.properties. Cannot init DB.");
            return;
        }

        String dbUrl = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String pass = props.getProperty("db.password");

        String serverUrl = dbUrl.substring(0, dbUrl.lastIndexOf("/"));

        try (Connection conn = DriverManager.getConnection(serverUrl, user, pass); Statement stmt = conn.createStatement()) {

            try (Scanner scanner = new Scanner(scriptFile)) {

                scanner.useDelimiter(";");

                while (scanner.hasNext()) {
                    String rawCommand = scanner.next().trim();

                    // Strip leading comment lines (lines starting with --)
                    while (rawCommand.startsWith("--")) {
                        int newlineIndex = rawCommand.indexOf('\n');
                        if (newlineIndex == -1) {
                            rawCommand = "";
                            break;
                        }
                        rawCommand = rawCommand.substring(newlineIndex + 1).trim();
                    }

                    if (rawCommand.isEmpty()) {
                        continue;
                    }

                    if (rawCommand.contains("Dump completed")) {
                        break;
                    }

                    try {

                        stmt.execute(rawCommand);

                        if (rawCommand.toUpperCase().startsWith("CREATE")) {
                            System.out.println("Executed: " + rawCommand.split("\n")[0]);
                        }
                    } catch (Exception e) {
                        if (!e.getMessage().contains("exists")) {
                            System.out.println("Warning executing command: " + e.getMessage());
                        }
                    }
                }
            }
            System.out.println("Database initialization complete.");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Critical Error during DB Init: " + e.getMessage());
        }
    }
}
