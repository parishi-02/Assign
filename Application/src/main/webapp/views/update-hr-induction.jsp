<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page isELIgnored="false" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Update HR Induction</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
     <style>
            .text-danger {
                color: #dc3545;
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
    <%@ include file="common-imports.jsp" %>

      <c:set var="username" value="${pageContext.request.userPrincipal.name}" />

          <c:choose>
          <c:when test="${username == 'admin'}">
              <c:set var="formAction" value="${pageContext.request.contextPath}/admin/saveUpdatedHRInduction" />
          </c:when>
          <c:otherwise>
              <c:set var="formAction" value="${pageContext.request.contextPath}/trainer/ " />
          </c:otherwise>
          </c:choose>


    <div class="container mt-4" style="max-width: 1000px;">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card shadow">
                    <form:form  action="${formAction}" method="POST" id="updateHRForm">
                        <div class="card-body" id="innerContainer">
                            <div class="d-flex justify-content-between align-items-center mb-4">
                                <h2 class="mb-0">Update Date</h2>
                                <div>
                                    <button type="submit" class="btn btn-primary mr-2">Save</button>
                                    <button type="button" id="cancelButton" class="btn btn-secondary">Cancel</button>
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="topicName">Topic Name:</label>
                                <input type="text" class="form-control" id="topicName" name="topicName" value="${topicName}" readonly />
                            </div>
                            <input type="hidden" name="batchId" value="${batchId}" />

                            <div class="form-group">
                                <label for="plannedDate">Enter new Planned Date<span class="text-danger">*</span>:</label>
                                <input type="date" class="form-control" id="plannedDate" name="plannedDate" required="true" value="${plannedDate}"/>
                            </div>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
    </div>

    <!-- Modal for alert -->
    <div class="modal fade" id="errorModal" tabindex="-1" role="dialog" aria-labelledby="errorModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header custom-modal-header">
                    <h5 class="modal-title" id="errorModalLabel">Warning</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body" id="errorMessage">
                    <!-- Error message will be displayed here -->
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal" style="background-color: #2382d7; color: black; font-weight:bold;">Close</button>
                </div>
            </div>
        </div>
    </div>

  <!-- Spinner HTML -->
    <div id="loading">
        <div class="spinner"></div> <!-- Loading spinner -->
    </div>
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
    <script>
       document.addEventListener('DOMContentLoaded', function() {
           var form = document.getElementById('updateHRForm');
           var plannedDateInput = document.getElementById('plannedDate');
           var oldPlannedDate = new Date('${plannedDate}');
             var loading = document.getElementById('loading');

           // Function to validate the date
           function validateDate() {
               var newPlannedDate = new Date(plannedDateInput.value);

               // Convert to YYYY-MM-DD for comparison
               oldPlannedDate = new Date(oldPlannedDate.toISOString().split('T')[0]);
               newPlannedDate = new Date(newPlannedDate.toISOString().split('T')[0]);


               var errors = [];

               // Check if the new planned date is earlier than the old planned date
               if (newPlannedDate < oldPlannedDate) {
                   errors.push('The new planned date cannot be earlier than the old planned date (' + oldPlannedDate.toISOString().split('T')[0] + ').');

               }

               // Check if the new planned date is on a weekend
               else if (isWeekend(newPlannedDate)) {
                   errors.push('The new planned date cannot be on a Saturday or Sunday.');
               }

               // Display errors if any
               if (errors.length > 0) {
                   showModal(errors.join(' '));
                   plannedDateInput.value = ''; // Clear the invalid input
                   return false;
               }

               return true;
           }

           // Check if a date is a weekend
           function isWeekend(date) {
               var day = new Date(date).getDay();
               return day === 0 || day === 6; // 0 for Sunday, 6 for Saturday
           }

           // Show modal with error message
           function showModal(message) {
               var modal = new bootstrap.Modal(document.getElementById('errorModal'));
               document.getElementById('errorMessage').textContent = message;
               modal.show();
           }



           // Add change event listener for real-time validation
           plannedDateInput.addEventListener('change', validateDate);

            form.addEventListener('submit', function(event) {
              if (!validateDate()) {
                  event.preventDefault(); // Prevent form submission if validation fails
              } else {
                  loading.style.display = 'block'; // Show the spinner
              }
          });

            cancelButton.addEventListener('click', function() {
                loading.style.display = 'block'; // Show the spinner
                setTimeout(function() {
                    window.history.back(); // Navigate back after delay
                }, 100); // 100 milliseconds delay to ensure spinner is visible
            });

       });

    </script>
</body>
</html>
