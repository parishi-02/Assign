<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<html>
<head>
    <title>Trainee List</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/common.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
    <style>
     .container {
            margin-top: 20px;
        }

        .table-container {
            margin-top: 20px;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            background-color: #fff; /* White background for the container */
        }

        .table {
            width: 100% !important;
            table-layout: auto;
        }

        .table thead th {
            background-color: #343a40; /* Dark background for header */
            color: #fff; /* White text */
        }

        .btn-primary, .btn-secondary, .btn-danger, .btn-info {
            margin-right: 5px; /* Spacing between buttons */
        }
        .button-container {
            display: flex;
            gap: 10px;
            align-items: center;
        }
        .button-container form, .button-container button {
            margin: 0;
        }

        .trainee-link {
            text-decoration: none;
            color: black;
        }
        .trainee-link:hover {
            text-decoration: none;
            color: black;
            font-size: 15px;
            font-weight: bold;
        }

        #trainerLink:hover {
            color: black;
            font-size: 20px;
            font-weight: bold;
        }
        #loading {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5); /* Semi-transparent black background */
            z-index: 9999;
        }

        .spinner {
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            border: 5px solid #f3f3f3; /* Light grey */
            border-top: 5px solid #3498db; /* Blue */
            border-radius: 50%;
            width: 50px;
            height: 50px;
            animation: spin 1s linear infinite; /* Spin animation */
        }

        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }
    </style>
</head>

<body>
<%@ include file="navbar.jsp"%>
<%@ include file="common-imports.jsp"%>

<div class="container">
   <div class="table-container">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2 class="mb-0">Trainee Attendance</h2>
            <button type="button" id="cancelButton" class="btn btn-secondary">Cancel</button>
        </div>
        <hr>
        <table id="traineeTable" class="table table-striped table-bordered" style="width:100%; font-size: small;">
            <thead class="thead-dark">
                <tr>
                    <th>Date</th>
                    <th>Attendance Status</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${traineeAttendance}" var="parameter">
                <tr>
                    <td>${parameter.key}</td>
                    <td>
                     <c:choose>
                        <c:when test="${parameter.value eq 'absent'}">
                            Absent
                        </c:when>
                        <c:when test="${parameter.value eq 'present'}">
                            Present
                        </c:when>
                        <c:when test="${parameter.value eq 'halfDay'}">
                            Half Day
                        </c:when>
                     </c:choose>

                    </td>

                </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>


  <!-- Spinner for form submission -->
    <div id="loading">
        <div class="spinner"></div>
    </div>


<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.datatables.net/1.11.5/js/jquery.dataTables.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
<script>
    $(document).ready(function() {
        $('#traineeTable').DataTable({
            "scrollX": true,
            "ordering": false
        });
    });
     cancelButton.addEventListener('click', function() {
               loading.style.display = 'block'; // Show the spinner
               setTimeout(function() {
                   window.history.back(); // Navigate back after delay
               }, 100); // 100 milliseconds delay to ensure spinner is visible
            });
</script>


</body>
</html>
