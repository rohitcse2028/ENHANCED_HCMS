<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Register - Hospital Management</title>
</head>
<body>

<h2>Register User</h2>

<form action="register" method="post">

    <label>Name:</label>
    <input type="text" name="name" required><br><br>

    <label>Username:</label>
    <input type="text" name="username" required><br><br>

    <label>Email:</label>
    <input type="email" name="email" required><br><br>

    <label>Password:</label>
    <input type="password" name="password" required><br><br>

    <label>Role:</label>
    <select name="role">
        <option>Admin</option>
        <option>Doctor</option>
        <option>Patient</option>
    </select>
    <br><br>

    <button type="submit">Register</button>
</form>

<% String error = (String) request.getAttribute("error");
   if (error != null) { %>
       <p style="color:red;"><%= error %></p>
<% } %>

</body>
</html>
