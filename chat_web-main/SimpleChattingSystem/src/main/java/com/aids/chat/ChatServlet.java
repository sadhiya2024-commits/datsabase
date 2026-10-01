package com.aids.chat;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/chat")
public class ChatServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);

        if (session == null ||
            session.getAttribute("username") == null) {

            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED
            );

            return;
        }

        String currentUser =
                (String) session.getAttribute("username");

        String receiver =
                request.getParameter("receiver");

        response.setContentType(
                "application/json"
        );

        response.setCharacterEncoding("UTF-8");

        String sql =
                "SELECT sender, receiver, message, " +
                "DATE_FORMAT(message_time, '%Y-%m-%d %H:%i:%s') AS message_time " +
                "FROM messages " +
                "WHERE (sender=? AND receiver=?) " +
                "OR (sender=? AND receiver=?) " +
                "ORDER BY message_time ASC";

        try (Connection con =
                     DBConnection.getConnection();

             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setString(1, currentUser);
            ps.setString(2, receiver);
            ps.setString(3, receiver);
            ps.setString(4, currentUser);

            ResultSet rs =
                    ps.executeQuery();

            PrintWriter out =
                    response.getWriter();

            out.print("[");

            boolean first = true;

            while (rs.next()) {

                if (!first) {
                    out.print(",");
                }

                out.print("{");

                out.print("\"sender\":\""
                        + escapeJson(rs.getString("sender"))
                        + "\",");

                out.print("\"receiver\":\""
                        + escapeJson(rs.getString("receiver"))
                        + "\",");

                out.print("\"message\":\""
                        + escapeJson(rs.getString("message"))
                        + "\",");

                out.print("\"time\":\""
                        + escapeJson(rs.getString("message_time"))
                        + "\"");

                out.print("}");

                first = false;
            }

            out.print("]");

        } catch (Exception e) {

            e.printStackTrace();

            response.setStatus(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );
        }
    }


    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);

        if (session == null ||
            session.getAttribute("username") == null) {

            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED
            );

            return;
        }

        String sender =
                (String) session.getAttribute("username");

        String receiver =
                request.getParameter("receiver");

        String message =
                request.getParameter("message");

        if (receiver == null ||
            receiver.trim().isEmpty() ||
            message == null ||
            message.trim().isEmpty()) {

            response.setStatus(
                    HttpServletResponse.SC_BAD_REQUEST
            );

            return;
        }

        String sql =
                "INSERT INTO messages " +
                "(sender, receiver, message) " +
                "VALUES (?, ?, ?)";

        try (Connection con =
                     DBConnection.getConnection();

             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setString(1, sender);
            ps.setString(2, receiver);
            ps.setString(3, message);

            ps.executeUpdate();

            response.setContentType("text/plain");

            response.getWriter().write("success");

        } catch (Exception e) {

            e.printStackTrace();

            response.setStatus(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );
        }
    }


    private String escapeJson(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}