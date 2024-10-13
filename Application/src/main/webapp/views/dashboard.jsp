<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard</title>

    <style>
     .dash {
         display: flex;
         justify-content: space-between; /* Align items with space between them */
         align-items: center; /* Center items vertically */
     }

     .centered-image {
         height: 360px; /* Adjust height as needed */
         width: 400px; /* Adjust width as needed */
         margin: 0 auto; /* Center the image horizontally */
     }

    </style>
</head>
<body>
    <%@ include file="navbar.jsp"%>

    <div class="dash">
        <%@ include file="sidebar.jsp"%>
        <img src="${pageContext.request.contextPath}/static/img.png" alt="Logo" height="3" width="40" class="centered-image">

    </div>


    <!-- Other content of dashboard -->

    <%@ include file="footer.jsp"%>
</body>
</html>