<%@page import="java.io.PrintWriter"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
  <!DOCTYPE html>
  <html>

  <head>
    <meta charset="UTF-8">
    <title>Insert title here</title>
    <style>
      select {
        width: 200px;
        padding: 5px;
        font-size: 16px;
      }

      button {
        padding: 10px 20px;
        font-size: 16px;
        cursor: pointer;
      }
    </style>
  </head>

  <body>
  	
    <h2>Select Your Database</h2>

    <form action="sql.jsp" method="POST">
      <label for="database">Choose a database:</label>
      <select id="database" name="database">
        <option value="mysql">MySQL</option>
        <option value="postgresql">PostgreSQL</option>
      </select>
      <br><br>
      <button type="submit">Submit</button>
    </form>
    <%!
    	String name = "sai";
    	String password = "erenxnaruto";
    	String psql_url = "jdbc:postgresql://localhost:5432/jan23";
    	String msql_url = "jdbc:mysql://localhost:3306/jan23";
    %>
    <%
      String url = "";
      String which = request.getParameter("database");
      if(which == null){
    	  return;
      }
      if(which.equals("mysql")){
    	  try{
    		  Class.forName("com.mysql.cj.jdbc.Driver");
    		  
    	  }
    	  catch(Exception e){
    		  e.printStackTrace();
    	  }
    	  url = msql_url;
      }
      else{
    	  try{
    		  Class.forName("org.postgresql.Driver");
    		  
    	  }
    	  catch(Exception e){
    		  e.printStackTrace();
    	  }
    	  url = psql_url;
      }
      try(
          Connection connect = DriverManager.getConnection(url,name,password );
    		  PreparedStatement ps = connect.prepareStatement("select * from user_details");){ 
    	 
    	  ResultSet result = ps.executeQuery();
    	  out.println("<br><br><br>");
    	  while(result.next()){
    		  out.println("Id : "+result.getInt("U_no")+"<br>");
				out.println("Name : "+result.getString("Name")+"<br>");
				out.println("Email : "+result.getString("Email")+"<br>");
				out.println("<br><br><br>");
    	  }
    	  
      }
    %>

  </body>

  </html>