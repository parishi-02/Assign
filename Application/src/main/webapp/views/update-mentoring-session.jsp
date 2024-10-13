<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Update Mentoring Session</title>
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

    <div class="container mt-4" style="max-width: 1000px;">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card shadow">
                    <form id="updateMentoring" action="${pageContext.request.contextPath}/admin/saveUpdateMentoringSession" method="POST">
                        <div class="card-body" id="innerContainer">
                            <div class="d-flex justify-content-between align-items-center mb-4">
                                <h2 class="mb-0">Update Mentoring Session</h2>
                                <div>
                                    <button type="submit" class="btn btn-primary mr-2">Save</button>
                                    <button type="button" id="cancelButton" class="btn btn-secondary">Cancel</button>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="sessionDescription">Session Description:</label>
                                <input type="text" class="form-control" id="sessionDescription" name="sessionDescription" value="${mentoringSessionDto.sessionDescription}" readonly />
                            </div>
                            <input type="hidden" name="sessionId" value="${mentoringSessionDto.sessionId}" aria-hidden="true"/>

                            <div class="form-group">
                                <label for="plannedDate">Planned Date</label>
                                <input type="date" class="form-control" id="plannedDate" name="plannedDate" required value="${mentoringSessionDto.plannedDate}" readonly/>
                            </div>
                            <div class="form-group">
                                <label for="plannedTime">Session Timings:</label>
                                <input type="time" class="form-control" id="plannedTime" name="plannedTime" value="${mentoringSessionDto.plannedTime}" />
                            </div>
                            <div class="form-group">
                                <label for="actualDate">Enter Actual Date:</label>
                                <input type="date" class="form-control" id="actualDate" name="actualDate" value="${mentoringSessionDto.actualDate}"/>
                            </div>
                            <div class="form-group">
                                <label for="remarks">Enter Remarks:</label>
                                <input type="text" class="form-control" id="remarks" name="remarks" placeholder="Enter remarks" value="${mentoringSessionDto.remarks}"/>
                            </div>
                        </div>
                    </form>
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
            var plannedTimeInput = document.getElementById('plannedTime');
            var actualDateInput = document.getElementById('actualDate');
            var remarksInput = document.getElementById('remarks');
            var form = document.getElementById('updateMentoring');
            var cancelButton = document.getElementById('cancelButton');
            var loading = document.getElementById('loading');
            var errorMessage = document.getElementById('errorMessage');
             var defaultPlannedTime = plannedTimeInput.value;

            function updateFieldStates() {
                var plannedTime = plannedTimeInput.value.trim();
                var actualDateFilled = actualDateInput.value.trim() !== '';
                var remarksFilled = remarksInput.value.trim() !== '';


                // Disable or enable fields based on input
                if (plannedTime !== '' && plannedTime !== '${mentoringSessionDto.plannedTime}') {
                    actualDateInput.readOnly = true;
                    remarksInput.readOnly = true;
                } else {
                    actualDateInput.readOnly = false;
                    remarksInput.readOnly = false;
                }

                if (actualDateFilled) {
                    plannedTimeInput.readOnly = true;
                    if (!remarksFilled) {
                        errorMessage.innerHTML = 'If you enter an actual date, remarks must be filled.';
                        $('#errorModal').modal('show');
                    }
                } else {
                    plannedTimeInput.readOnly = false;
                    if (remarksFilled) {
                        errorMessage.innerHTML = 'If you enter remarks, an actual date must be filled.';
                        $('#errorModal').modal('show');
                    }
                }
            }

            function validateForm() {
                var plannedDateInput = document.getElementById('plannedDate');
                var plannedDate = new Date(plannedDateInput.value);
                var actualDate = new Date(actualDateInput.value);
                var remarks = remarksInput.value;
                var currentDateObj = new Date();
                 currentDateObj.setHours(0,0,0,0);
                 actualDate.setHours(0,0,0,0);
                 plannedDate.setHours(0,0,0,0);

                var errors = [];

                // Check if remarks contain only letters and spaces
                var remarksRegex = /^[a-zA-Z\s]*$/; // Allows letters and spaces
                if (!remarksRegex.test(remarks)) {
                    errors.push('Remarks cannot contain special characters.');
                }

                if (actualDateInput.value && !remarks) {
                    errors.push('If you enter an actual date, remarks must be filled.');
                }

                if (remarks && !actualDateInput.value) {
                    errors.push('If you enter remarks, an actual date must be filled.');
                }

                if (actualDateInput.value) {
                    if (actualDate < plannedDate) {
                        errors.push('Actual date must be greater than the planned date.');
                    }
                    else if (isWeekend(actualDateInput.value)) {
                        errors.push('Actual date cannot be on a Saturday or Sunday.');
                    }
                     else if(actualDate>currentDateObj)
                     {
                         errors.push('Actual date cannot be greater than current date');
                     }
                }

                if (errors.length > 0) {
                    errorMessage.innerHTML = errors.join('<br>');
                    $('#errorModal').modal('show');
                    return false;
                }

                return true;
            }

            function isWeekend(date) {
                var day = new Date(date).getDay();
                return day === 0 || day === 6; // 0 for Sunday, 6 for Saturday
            }

            // Attach event listeners for input changes
            plannedTimeInput.addEventListener('input', updateFieldStates);
            actualDateInput.addEventListener('input', updateFieldStates);
            remarksInput.addEventListener('input', updateFieldStates);

            // Check initial state when page loads
            updateFieldStates();

            form.addEventListener('submit', function(event) {
                if (!validateForm()) {
                    event.preventDefault(); // Prevent form submission if validation fails
                } else {
                 // Check if planned time is empty, and set it to the default planned time if needed
                            if (plannedTimeInput.value.trim() === '') {
                                plannedTimeInput.value = defaultPlannedTime;
                            }

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
