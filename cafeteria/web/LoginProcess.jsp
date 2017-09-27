<%@page import="ClassPackage.login"%>
<jsp:useBean id="UserItems" class="ClassPackage.UserClassItems" scope="session"/>
<jsp:setProperty name="UserItems" property="*"/>
<%@page import="java.io.FileReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.DriverManager"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>MeTL</title>
    </head>
    <body>

<%
        Connection conn = null;
	 Class.forName("org.mariadb.jdbc.Driver").newInstance();
	conn = DriverManager.getConnection("jdbc:mysql://192.168.12.99:3306/canteenecof", "canteenuser", "canteenpp");
        

    //STEP 3: Open a connection

          //  return connbio;
String result=login.loginCheck(UserItems);
//user is exist
if(result.equals("true"))
{
    //create   perrmission
    session.setAttribute("username", UserItems.getUsername());
    session.setAttribute("password", UserItems.getPassword());
  
   //select user type from database
//        String Usertype= (String)session.getAttribute("username");
//        ResultSet permissionrsPgin = null;
//	PreparedStatement permissionpsPgintn=null;
//        String type="";
//        String permissionsqlPgintn="SELECT * FROM `user` WHERE `username`='"+Usertype+"'";
//	permissionpsPgintn=conn.prepareStatement(permissionsqlPgintn);
//	permissionrsPgin=permissionpsPgintn.executeQuery();
//	while(permissionrsPgin.next())
//       {		            
//       type=permissionrsPgin.getString(3);
//       }
////        
//         if("manager".equals(type))
//          {
            //redirect to managerhome page
             response.sendRedirect("ManagerHome.jsp");
         // }
        
    }
 
 //if use is not exist
        if(result.equals("false"))
        {
            response.sendRedirect("index.jsp?status=false");
        }
 //if error occur
        if(result.equals("error"))
        {
            response.sendRedirect("index.jsp?status=error");
        }
 
%>
    </body>
</html>
