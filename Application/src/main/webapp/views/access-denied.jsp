<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Access Denied</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f8f9fa; /* Light gray background */
        }

        .container-access {
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            height: 100vh;
            padding: 20px;
            text-align: center;
        }

        .illustration {
            max-width: 150px; /* Adjust the maximum width */
            margin-bottom: 30px;
        }

        h1 {
            color: #333; /* Dark gray */
        }

        p {
            color: #666; /* Gray */
            margin-bottom: 20px;
        }

        .cta-button {
            display: inline-block;
            padding: 12px 24px;
            background-color: #007bff; /* Blue */
            color: #fff; /* White */
            text-decoration: none;
            border-radius: 25px;
            transition: background-color 0.3s;
        }

        .cta-button:hover {
            background-color: #0056b3; /* Dark blue */
            color:white;
            text-decoration:none;
        }
    </style>
</head>

<body>
    <%@ include file="navbar.jsp"%>
    <div class="container-access">
        <img class="illustration" src="${pageContext.request.contextPath}/static/access-denied.png" alt="Lock Icon">
        <h1>Access Denied</h1>
        <p>Sorry, you do not have permission to access this page.</p>
        <a class="cta-button" href="${pageContext.request.contextPath}/login">Login</a>
    </div>
</body>

</html>
