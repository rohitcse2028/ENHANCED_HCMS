package com.hospital;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/items")
public class ItemServlet extends HttpServlet {

    private LostAndFoundManager manager = new LostAndFoundManager();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<Item> items = manager.getAllItems();
        req.setAttribute("items", items);
        req.getRequestDispatcher("items.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String id = req.getParameter("itemId");
        String name = req.getParameter("name");
        String category = req.getParameter("category");
        String location = req.getParameter("location");
        String type = req.getParameter("type");
        String contact = req.getParameter("contact");

        Item item = new Item(id, name, category, location, type, contact);
        manager.addItem(item);

        resp.sendRedirect("items");
    }
}
