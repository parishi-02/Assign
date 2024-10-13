<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>

<html>
<head>
    <title>Trainee List</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/common.css">
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
           .button-container {
               display: flex; /* Use Flexbox for alignment */
               justify-content: space-between; /* Space between buttons */
               align-items: center; /* Center items vertically */
           }
           .fixbutton {
               margin-right: 10px; /* Optional: Adjust spacing between buttons */
           }
           /* Custom Modal Header Styles */
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
    <%@ include file="navbar.jsp"%>
    <%@ include file="common-imports.jsp"%>
        <br>
        <br>
        <div class="container">
            <div class="table-container">
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h2>List of Trainee</h2>
                    <div class="button-container">
                        <button type="button" class="btn btn-primary fixbutton" onclick = "window.location.href-'${pageContext.request.contextPath}/admin/downloadTrainees'">Download List</button>
                        <button id="registerTraineeButton" class="btn btn-primary fixbutton" onclick="window.location.href='${pageContext.request.contextPath}/admin/trainee-form'" style="color:white; text-decoration:none;">Register new Trainee</button>
                    </div>
                </div>
                <hr>
                <table id="traineeTable" class="table table-striped table-bordered" style="width:100%; font-size: small;">
                    <thead class="thead-dark">
                    <tr>
                        <th>Trainee Id</th>
                        <th>Trainee Name</th>
                        <th>Gender</th>
                        <th>Branch</th>
                        <th>Date of Joining</th>
                        <th>College Name</th>
                        <th>Email Address</th>
                        <th>Contact Number</th>
                        <th>Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${trainees}" var="parameter">
                        <tr>
                            <td>${parameter.employeeId}</td>
                            <td>${parameter.traineeName}</td>
                            <td>${parameter.gender}</td>
                            <td>${parameter.branch}</td>
                            <td>${parameter.doj}</td>
                            <td>${parameter.college}</td>
                            <td>${parameter.officeEmailId}</td>
                            <td>${parameter.cellNo}</td>
                            <td>
                                <div class="btn-group" role="group">
                                    <button type="button" onclick="submitForm('${parameter.employeeId}')" class="btn btn-primary btn-sm mr-1" id="updateTrainee">Update</button>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                <form id="updateTraineeForm" action="updateTraineeForm" method="post">
                    <input type="hidden" id="employeeId" name="employeeId">
                </form>
            </div>
        </div>
    <!-- Validation Modal HTML -->
    <div class="modal fade" id="validationModal" tabindex="-1" aria-labelledby="validationModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header custom-modal-header">
                    <h5 class="modal-title"></h5>
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
      <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
      <!-- Latest DataTables -->
      <script src="https://cdn.datatables.net/1.12.1/js/jquery.dataTables.min.js"></script>
    <script>
        $(document).ready(function() {
            $('#traineeTable').DataTable({
                "scrollX": true
            });
        });
          function showValidationModal(title, message) {
              $('#validationModal .modal-title').text(title); // Set the title of the modal
              $('#validationModal .modal-body').text(message); // Set the message in the body of the modal
              $('#validationModal').modal('show'); // Show the modal
          }

        function submitForm(traineeId) {
            // Set the traineeId in the hidden input field
            document.getElementById('employeeId').value = traineeId;
            // Submit the form
            document.getElementById('updateTraineeForm').submit();
        }
       document.addEventListener('DOMContentLoaded', function() {
          const urlParams = new URLSearchParams(window.location.search);
          const successMessage = urlParams.get('successMessage');
          if (successMessage && successMessage.trim() !== 'null') {
              showValidationModal('Success', successMessage);
              urlParams.delete('successMessage');
              const newUrl = window.location.pathname + (urlParams.toString() ? '?' + urlParams.toString() : '');
              window.history.replaceState({}, '', newUrl);
          }

      });

        $(document).ready(function() {
                // Check if there's a message to display
                <c:if test="${not empty message}">
                    var message = "${message}";
                    var alertBox = `
                        <div id="alert-container" style="position: fixed; top: 70px; right: 500px; z-index: 9999; width: 320px;">
                            <div class="alert alert-success alert-dismissible fade show" role="alert">
                                ${message}
                                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                        </div>
                    `;
                    $('body').append(alertBox);
                </c:if>
            });
    </script>
</body>
</html>
