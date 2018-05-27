<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">


<%@ page import="java.util.*" %>

<%
	int accountId = Integer.parseInt(request.getParameter("account"));
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Account Not Found</title>
    </head>
    <body>
        <h1>@Todo</h1>
        <p>Show which account was not found</p>
        <p><a href="index.html">Home</a></p>
    </body>
</html>