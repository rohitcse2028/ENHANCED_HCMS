package com.hospital;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        try {
            UserDAO dao = new UserDAO();
            boolean isValid = dao.authenticate(username, password);

            if (isValid) {
                HttpSession session = req.getSession();
                session.setAttribute("username", username);
                resp.sendRedirect("dashboard.jsp");
            } else {
                req.setAttribute("error", "Invalid Username or Password");
                req.getRequestDispatcher("login.jsp").forward(req, resp);
            }

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Server Error!");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }
}
