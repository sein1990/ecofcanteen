<%-- 
    Document   : logout
    Created on : Mar 23, 2017, 11:07:32 PM
    Author     : Sein 90
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>MeTL</title>
    </head>
    <body>
       
        
        
               <%
session.invalidate();
response.sendRedirect("index.jsp");
%>
        
        
        
        
        
    </body>
</html>
