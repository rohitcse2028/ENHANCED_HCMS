<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, com.hospital.Item" %>

<!DOCTYPE html>
<html>
<head>
    <title>Items - Hospital Management</title>
</head>
<body>

<h2>All Lost & Found Items</h2>

<table border="1" cellpadding="5">
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Category</th>
        <th>Location</th>
        <th>Type</th>
        <th>Contact</th>
    </tr>

<%
    List<Item> items = (List<Item>) request.getAttribute("items");
    if (items != null) {
        for (Item i : items) {
%>
            <tr>
                <td><%= i.getItemId() %></td>
                <td><%= i.getName() %></td>
                <td><%= i.getCategory() %></td>
                <td><%= i.getLocation() %></td>
                <td><%= i.getType() %></td>
                <td><%= i.getContactInfo() %></td>
            </tr>
<%
        }
    }
%>

</table>

<h3>Add New Item</h3>

<form action="items" method="post">
    <label>Item ID:</label> <input type="text" name="itemId"><br><br>
    <label>Name:</label> <input type="text" name="name"><br><br>
    <label>Category:</label> <input type="text" name="category"><br><br>
    <label>Location:</label> <input type="text" name="location"><br><br>
    <label>Type:</label> <input type="text" name="type"><br><br>
    <label>Contact:</label> <input type="text" name="contact"><br><br>

    <button type="submit">Add Item</button>
</form>

</body>
</html>
