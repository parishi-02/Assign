<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page isELIgnored="false" contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html lang="en">

<head>
    <title>Login</title>
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Open+Sans:wght@400;600&display=swap" rel="stylesheet">

    <style>
        html,
        body {
            height: 100%;
            font-family: 'Open Sans', sans-serif;
            background-color: #f5f5f5;
        }

        body {
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 20px;
        }

        .form-signin {
            width: 100%;
            max-width: 400px;
            padding: 30px;
            border-radius: 10px;
            background-color: #fff;
            box-shadow: 0px 0px 20px rgba(0, 0, 0, 0.1);
            animation: slide-in 0.5s ease;
        }

        .form-signin .form-control {
            margin-bottom: 20px;
            border: 1px solid #ced4da;
            border-radius: 4px;
            padding: 15px;
            font-size: 16px;
            transition: border-color 0.3s ease;
        }

        .form-signin .form-control:focus {
            outline: none;
            border-color: #007bff;
        }

        .form-signin .form-control::placeholder {
            color: #a8a8a8;
        }

        .form-signin .form-control:focus::placeholder {
            color: transparent;
        }

        .form-signin label {
            font-weight: 600;
            color: #333;
        }

        .form-signin .btn-login {
            background-color: #007bff;
            color: #fff;
            border: none;
            border-radius: 4px;
            padding: 15px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }

        .form-signin .btn-login:hover {
            background-color: #0056b3;
        }

        .message-container {
            margin-top: 20px;
        }

        .message {
            border-radius: 5px;
            padding: 10px;
            margin-bottom: 10px;
            display: flex;
            align-items: center;
        }

        .error-message {
            background-color: #ffe4e1;
            border: 1px solid #ff7f7f;
        }

        .error-text {
            color: #c62828;
            font-weight: bold;
        }

        .success-message {
            background-color: #d4edda;
            border: 1px solid #c3e6cb;
        }

        .success-text {
            color: #155724;
            font-weight: bold;
        }

        .icon {
            margin-right: 10px;
            font-size: 20px;
        }

        /* Centering the logo */
        .logo-container {
            text-align: center;
            margin-bottom: 20px;
        }

        @keyframes slide-in {
            from {
                opacity: 0;
                transform: translateY(-50px);
            }

            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        .corporate-title {
            font-size: 24px;
            font-weight: 600;
            text-align: center;
            margin-bottom: 30px;
            color: #333;
            text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.1);
        }
    </style>
</head>

<body>
    <form class="form-signin" method="post" action="${pageContext.request.contextPath}/login">
        <div class="logo-container">
            <img src="${pageContext.request.contextPath}/static/logo.jpg" alt="Logo" width="100" height="100">
        </div>
        <h1 class="corporate-title">EdProgress Tracker</h1>

        <div class="message-container">
            <c:if test="${not empty param.error}">
                <div class="message error-message">
                    <div id="error-message" class="error-text">Bad Credentials. Please try again.</div>
                </div>
            </c:if>

            <c:if test="${not empty param.logout}">
                <div class="message success-message">
                    <span class="icon">&#10003;</span>
                    <span class="success-text">You have been successfully logged out.</span>
                </div>
            </c:if>
        </div>

        <label for="username">Enter your username</label>
        <input type="text" id="username" name="username" class="form-control" placeholder="Enter your username" required autofocus>

        <label for="password">Enter your password</label>
        <input type="password" id="password" name="password" class="form-control" placeholder="Enter your password" required>

        <button class="btn btn-login btn-block" type="submit">Login</button>
    </form>
</body>

</html>
