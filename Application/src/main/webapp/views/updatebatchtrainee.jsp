<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Update Batch Trainees</title>
    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.11.5/css/jquery.dataTables.min.css">
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .table-container {
            margin-top: 20px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            padding: 20px;
            border-radius: 5px;
        }
        .table {
            width: 100% !important;
            table-layout: auto;
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
        .custom-modal-header {
            background-color: #4895d9; /* Dark background color */
            color: white; /* Text color */
            border-bottom: 1px solid #dee2e6; /* Light border for separation */
        }

        .custom-modal-header .modal-title {
            font-weight: 600; /* Bold title */
        }

        .custom-close {
            color: white; /* White close button */
            opacity: 0.8; /* Slightly transparent */
        }

        .custom-close:hover {
            color: red; /* Slightly lighter color on hover */
            opacity: 1; /* Full opacity on hover */
        }

        /* Optional: Add some margin to the body of the modal */
        .modal-body {
            margin-bottom: 20px; /* Add margin for spacing */
        }
    </style>
</head>
<body>
    <%@ include file="navbar.jsp" %>
    <br>
    <br>
     <div class="container">
        <div class="table-container">
            <form:form method="post" action="${pageContext.request.contextPath}/admin/removeTraineesFromBatch" onsubmit="return validateSelection()" >
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h2>Update Batch Trainees</h2>
                    <div>
                        <button type="submit" class="btn btn-primary">Remove Selected Trainees</button>
                        <button type="button" class="btn btn-secondary" id="cancelButton">Cancel</button>
                    </div>
                </div>
                <hr>
                <br>
                <table id="traineeTable" class="table table-striped table-bordered">
                    <thead class="thead-dark">
                        <tr>
                            <th>Select</th>
                            <th>Employee Id</th>
                            <th>Trainee Name</th>
                            <th>Gender</th>
                            <th>Branch</th>
                            <th>Date of Joining</th>
                            <th>College Name</th>
                            <th>Email Address</th>
                            <th>Contact Number</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${trainees}" var="trainee">
                            <tr>
                                <td>
                                    <input type="checkbox" name="selectedTrainees" value="${trainee.employeeId}">
                                </td>
                                <td>${trainee.employeeId}</td>
                                <td>${trainee.traineeName}</td>
                                <td>${trainee.gender}</td>
                                <td>${trainee.branch}</td>
                                <td>${trainee.doj}</td>
                                <td>${trainee.college}</td>
                                <td>${trainee.officeEmailId}</td>
                                <td>${trainee.cellNo}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <div class="form-group mt-3">
                    <input type="hidden" name="batchId" value="${batchId}">
                </div>
            </form:form>
        </div>
    </div>

    <!-- Spinner for form submission -->
    <div id="loading">
        <div class="spinner"></div>
    </div>

     <!-- Validation Modal HTML -->
        <div class="modal fade" id="validationModal" tabindex="-1" aria-labelledby="validationModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header custom-modal-header">
                        <h5 class="modal-title">Error</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                          <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                    </div>
                     <div class="modal-footer">
                       <button type="button" class="btn btn-secondary" data-dismiss="modal" style="background-color: #2382d7; color: black; font-weight:bold;">Close</button>
                     </div>
                </div>
            </div>
        </div>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.datatables.net/1.11.5/js/jquery.dataTables.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <script>
         $(document).ready(function() {
               $('#traineeTable').DataTable({
                   "ordering": false
               });

               var cancelButton = document.getElementById('cancelButton');
               var loading = document.getElementById('loading');

               cancelButton.addEventListener('click', function() {
                   loading.style.display = 'block'; // Show the spinner
                   setTimeout(function() {
                       window.history.back(); // Navigate back after delay
                   }, 100); // 100 milliseconds delay to ensure spinner is visible
               });

               // Function to show spinner on form submission
               window.validateSelection = function() {
                   var checkboxes = document.querySelectorAll('input[name="selectedTrainees"]:checked');
                   if (checkboxes.length === 0) {
                        $('#validationModal .modal-body').text('Please select at least one trainee to remove.');
                        $('#validationModal').modal('show');
                       return false;
                   }

                   document.getElementById('loading').style.display = 'block';

                   return true;
               }
           });
       </script>
</body>
</html>
