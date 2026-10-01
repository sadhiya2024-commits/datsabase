package com.aids.chat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://db01.dbhost.dev:5051/db_454v6rdrt";

    private static final String USER =
            "user_454v6rdrt";

    private static final String PASSWORD =
            "p454v6rdrt";

    public static Connection getConnection() {

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                    URL,
                    USER,
                    PASSWORD
            );

            System.out.println("Database connected successfully!");

            return con;

        } catch (ClassNotFoundException e) {

            System.out.println("MySQL Driver not found!");
            e.printStackTrace();

        } catch (SQLException e) {

            System.out.println("Database connection failed!");
            e.printStackTrace();
        }

        return null;
    }
}