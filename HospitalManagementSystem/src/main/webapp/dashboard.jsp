<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%! String user; %>
<!DOCTYPE html>
<html>
<head>
    <title>Dashboard</title>
</head>
<body>

<h2>Welcome to Dashboard</h2>

<% 
    HttpSession session = request.getSession(false);
    if (session != null) {
        user = (String) session.getAttribute("username");
    }
%>

<p>Hello, <b><%= user %></b>!</p>

<a href="items">View Items</a>

</body>
</html>
