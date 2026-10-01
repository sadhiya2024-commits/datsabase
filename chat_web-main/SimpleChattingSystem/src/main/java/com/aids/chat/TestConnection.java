package com.aids.chat;

import java.sql.Connection;

public class TestConnection {

    public static void main(String[] args) {

        Connection con = DBConnection.getConnection();

        if (con != null) {
            System.out.println(
                "SUCCESS: Nimbus MySQL connected!"
            );
        } else {
            System.out.println(
                "FAILED: Database connection failed!"
            );
        }
    }
}