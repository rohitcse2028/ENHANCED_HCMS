package com.hospital;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String name = req.getParameter("name");
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String role = req.getParameter("role");

        try {
            UserDAO dao = new UserDAO();
            boolean inserted = dao.createUser(name, username, email, password, role);

            if (inserted) {
                resp.sendRedirect("login.jsp?success=1");
            } else {
                req.setAttribute("error", "Registration Failed");
                req.getRequestDispatcher("register.jsp").forward(req, resp);
            }

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Server Error!");
            req.getRequestDispatcher("register.jsp").forward(req, resp);
        }
    }
}
