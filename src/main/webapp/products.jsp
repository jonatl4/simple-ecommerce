<%@page import="javax.servlet.http.HttpServlet" %>
<%@page import="javax.servlet.http.HttpServletRequest" %>
<%@page import="javax.servlet.http.HttpServletResponse" %>
<%@page import="java.io.IOException" %>
<%@page import="java.io.PrintWriter" %>
<%@page import="java.sql.Connection" %>
<%@page import="java.sql.ResultSet" %>
<%@page import="java.sql.SQLException" %>
<%@page import="java.sql.Statement" %>
<%@page import="javax.servlet.RequestDispatcher" %>
<%@page import="javax.servlet.ServletException" %>
<%@ page import="org.glassfish.jersey.client.ClientConfig" %>
<%@ page import="javax.ws.rs.client.Client" %>
<%@ page import="javax.ws.rs.client.ClientBuilder" %>
<%@ page import="javax.ws.rs.client.WebTarget" %>
<%@ page import="java.net.URI" %>
<%@ page import="javax.ws.rs.core.UriBuilder" %>
<%@ page import="javax.ws.rs.core.MediaType" %>
<%@ page import="org.codehaus.jackson.map.ObjectMapper" %>
<%@ page import="org.codehaus.jackson.type.TypeReference" %>

<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="utf-8">
		<title>Soccer Jerseys</title>
		<link href= "css/styles.css" rel="stylesheet" type="text/css">
		<script type="text/javascript" src="js/qty.js"></script>
	</head>
	
	<body>
		<div class="header">
		  <img src="img/top_header_general.png" width = "100%"/>  
		</div>
		<div class="nav">
			<ul>
			  <li><a href="index.jsp">Home</a></li>
			  <li><a href="products.jsp">Online Store</a></li>
			</ul>
		</div>
		<div class="itemContainer">
			<table border= 3 cellspacing= 3 bgcolor=#333 width= 1000 align='center'>
    			<jsp:include page="/products"/>
    		</table>
    	</div>
    </body>
</html>