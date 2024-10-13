<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page isELIgnored="false" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Update Topic Planned Date</title>
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
             <c:set var="formAction" value="${pageContext.request.contextPath}/admin/saveUpdatedPlannedDate" />
         </c:when>
         <c:otherwise>
             <c:set var="formAction" value="${pageContext.request.contextPath}/trainer/saveUpdatedPlannedDate" />
         </c:otherwise>
         </c:choose>

    <div class="container mt-4" style="max-width: 1000px;">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card shadow">
                    <form:form  action="${formAction}" method="POST" id="updatePlannedDate">
                        <div class="card-body" id="innerContainer">
                            <div class="d-flex justify-content-between align-items-center mb-4">
                                <h2 class="mb-0">Update Planned Date</h2>
                                <div>
                                    <button type="submit" class="btn btn-primary mr-2">Save</button>
                                    <button type="button" id="cancelButton" class="btn btn-secondary">Cancel</button>
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="topicName">Topic Name:</label>
                                <input type="text" class="form-control" id="topicName" name="topicName" value="${topicName}" readonly />
                            </div>
                            <input type="hidden" name="batchId" value="${batchid}" />
                            <input type="hidden" name="planId" value="${planId}" />
                            <div class="form-group">
                                <label for="plannedDate">Enter new Planned Date<span class="text-danger">*</span>:</label>
                                <input type="date" class="form-control" id="plannedDate" name="plannedDate" required="true" value="${plannedDate}" readonly/>
                            </div>
                            <div class="form-group">
                                <label for="actualDate">Enter Actual Date<span class="text-danger">*</span>:</label>
                                <input type="date" class="form-control" id="actualDate" name="actualDate" required="true"/>

                            </div>
                            <div class="form-group">
                               <label for="remarks">Enter Remarks<span class="text-danger">*</span>:</label>
                               <input type="text" class="form-control" id="remarks" name="remarks" required="true" placeholder="Enter remarks"/>

                            </div>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
    </div>

    <!-- Modal for error messages -->
        <div class="modal fade" id="errorModal" tabindex="-1" role="dialog" aria-labelledby="errorModalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header custom-modal-header">
                        <h5 class="modal-title" id="errorModalLabel">Validation Error</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body" id="errorMessage">
                        <!-- Error message will be injected here -->
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
            var form = document.getElementById('updatePlannedDate');
            var plannedDateInput = document.getElementById('plannedDate');
            var actualDateInput = document.getElementById('actualDate');
            var remarksInput = document.getElementById('remarks');
            var dateError = document.getElementById('dateError');
            var remarksError = document.getElementById('remarksError');
            var spinner = document.getElementById('loading');
            var formValid = false;
            var errorMessage = document.getElementById('errorMessage');

            function validateDates() {
                var newPlannedDate = new Date(plannedDateInput.value);
                var actualDate = new Date(actualDateInput.value);
                 var currentDateObj = new Date();
                 currentDateObj.setHours(0,0,0,0);
                 actualDate.setHours(0,0,0,0);
                 newPlannedDate.setHours(0,0,0,0);

                // Convert to YYYY-MM-DD for comparison
                newPlannedDate = new Date(newPlannedDate.toISOString().split('T')[0]);
                actualDate = new Date(actualDate.toISOString().split('T')[0]);

                if (actualDate < newPlannedDate) {
                    errorMessage.innerHTML = 'The actual date cannot be earlier than the planned date ${plannedDate}.';
                    $('#errorModal').modal('show');
                    setTimeout(function() {
                        actualDateInput.value='' ;// Actually submit the form
                   }, 1500);
                    formValid = false;
                } else if (isWeekend(actualDate)) {
                     errorMessage.innerHTML = 'The actual date cannot be on a Saturday or Sunday.';
                     $('#errorModal').modal('show');
                     setTimeout(function() {
                        actualDateInput.value='' ;// Actually submit the form
                     }, 1500);
                    formValid = false;
                }
                else if(actualDate>currentDateObj)
                 {
                     errorMessage.innerHTML = 'Actual date cannot be greater than current date.';
                      $('#errorModal').modal('show');
                      setTimeout(function() {
                         actualDateInput.value='' ;// Actually submit the form
                      }, 1500);
                     formValid = false;
                 }

                else {
                    $('#errorModal').modal('hide');
                    formValid = true;
                }
            }

            function validateRemarks() {
                var remarksValue = remarksInput.value;
                var valid = /^[A-Za-z\s]*$/.test(remarksValue); // Regex to allow only letters and spaces

                if (!valid) {
                     errorMessage.innerHTML = 'Remarks should contain only alphabets and spaces';
                        $('#errorModal').modal('show');
                        setTimeout(function() {
                            remarksInput.value='' ;// Actually submit the form
                       }, 1500);
                    formValid = false;
                } else {
                    $('#errorModal').modal('hide');
                    formValid = true;
                }
            }

            // Function to check if a date is a weekend
            function isWeekend(date) {
                var day = new Date(date).getDay();
                return day === 0 || day === 6; // 0 for Sunday, 6 for Saturday
            }

            // Add input event listener for the Actual Date field
            actualDateInput.addEventListener('input', function() {
                validateDates();
                validateForm();
            });

            // Add input event listener for the Remarks field
            remarksInput.addEventListener('input', function() {
                validateRemarks();
                validateForm();
            });

            function validateForm() {
                var newPlannedDate = new Date(plannedDateInput.value);
                var actualDate = new Date(actualDateInput.value);
                var remarksValue = remarksInput.value;
                var validRemarks = /^[A-Za-z\s]*$/.test(remarksValue);

                // Convert to YYYY-MM-DD for comparison
                newPlannedDate = new Date(newPlannedDate.toISOString().split('T')[0]);
                actualDate = new Date(actualDate.toISOString().split('T')[0]);

                if (actualDate >= newPlannedDate && validRemarks) {
                    formValid = true;
                } else {
                    formValid = false;
                }
            }

            // Add form submit event listener
            form.addEventListener('submit', function(event) {
                validateDates();
                validateRemarks();
                validateForm();

                if (!formValid) {
                    event.preventDefault(); // Prevent form submission
                } else {
                    // Show the spinner
                    spinner.style.display = 'block';
                    // Simulate form submission delay
                    setTimeout(function() {
                        form.submit(); // Actually submit the form
                    }, 100); // 100 milliseconds delay to ensure spinner is visible
                }
            });

            cancelButton.addEventListener('click', function() {
                spinner.style.display = 'block'; // Show the spinner
                setTimeout(function() {

                    window.history.back(); // Navigate back after delay
                }, 100); // 100 milliseconds delay to ensure spinner is visible
            });

        });
    </script>
</body>
</html
