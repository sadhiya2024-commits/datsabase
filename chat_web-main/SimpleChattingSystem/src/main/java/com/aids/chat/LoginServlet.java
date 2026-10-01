package com.aids.chat;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Set<String> ALLOWED_USERS =
            new HashSet<String>(Arrays.asList(
                    "Sandhya", "shivani", "rahu", "motvani", "shariya"
            ));

    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || password == null ||
                !ALLOWED_USERS.contains(username)) {
            response.sendRedirect(
                    "login.jsp?error=Please select a valid username");
            return;
        }

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) {
                response.sendRedirect(
                        "login.jsp?error=Database connection failed");
                return;
            }

            // Keep the demo self-contained: create the users table if needed
            // and add the five requested login names when they are missing.
            ensureUsersTable(con);
            provisionUserIfMissing(con, username);

            String sql =
                    "SELECT * FROM users WHERE username = ? AND password = ?";

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, username);
                ps.setString(2, password);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        HttpSession session = request.getSession();
                        session.setAttribute("username", username);
                        response.sendRedirect("chat.jsp");
                    } else {
                        response.sendRedirect(
                                "login.jsp?error=Invalid password. For this demo, use the username as the password.");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(
                    "login.jsp?error=Database error");
        }
    }

    private void ensureUsersTable(Connection con) throws Exception {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "username VARCHAR(100) PRIMARY KEY, " +
                "password VARCHAR(255) NOT NULL" +
                ")";
        try (Statement st = con.createStatement()) {
            st.executeUpdate(sql);
        }
    }

    private void provisionUserIfMissing(Connection con, String username) throws Exception {
        String sql = "INSERT IGNORE INTO users (username, password) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, username);
            ps.executeUpdate();
        }
    }
}
