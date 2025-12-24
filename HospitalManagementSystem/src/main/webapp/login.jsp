<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login - Hospital Management</title>
</head>
<body>

<h2>Login</h2>

<form action="login" method="post">
    <label>Username:</label>
    <input type="text" name="username" required> <br><br>

    <label>Password:</label>
    <input type="password" name="password" required> <br><br>

    <button type="submit">Login</button>
</form>

<% String error = (String) request.getAttribute("error");
   if (error != null) { %>
       <p style="color:red;"><%= error %></p>
<% } %>

</body>
</html>
