<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Invalid Data Entry</title>
    <title>Invalid Data Entry</title>
    </head>
    <body>
        <h1>Invalid Data Entry</h1>

        <%= request.getParameter("id") %>

        <% if (request.getParameter("account") != null) { %>
          Welcome <%= request.getParameter("account") %>!

          <br />

          Your account number is <%= request.getParameter("id") %>
        <% } %>

        <br />

        <% if (request.getParameter("amount") != null) { %>
          Your balance is: <%= request.getParameter("amount") %>
        <% } %>
</html>
