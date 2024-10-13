<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Create Batch</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
    <style>
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

        .btn-custom-position {
            float: left;
            margin-bottom: 10px; /* Optional: space between the button and table */
        }

        .table td,
        .table th {
            vertical-align: middle; /* Center-align content vertically */
        }

        #holidaysTable {
            margin-top: 20px; /* Space above the table */
        }

         .d-flex-between {
                    display: flex;
                    justify-content: space-between;
                    align-items: center;
                    margin-bottom: 20px; /* Space below the heading and button */
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

    <%@ include file="navbar.jsp" %>

    <div class="container mt-5" style="max-width: 1200px;">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card shadow">
                    <form:form action="${pageContext.request.contextPath}/admin/createNewBatch" method="POST" enctype="multipart/form-data" modelAttribute="batchDto" id="batchCreationForm">
                        <div class="card-body" id="innerContainer">
                            <div class="d-flex justify-content-between align-items-center mb-4">
                                <h2 class="mb-0">Create Batch</h2>
                                <div>
                                   <button type="submit" class="btn btn-primary mr-2">Save</button>
                                   <button type="button" id="cancelButton" class="btn btn-secondary">Cancel</button>
                                </div>
                            </div>
                            <hr>
                            <div class="row mb-3">
                               <div class="col-md-6">
                                   <div class="form-group">
                                       <label for="startDate">Enter Start Date<span class="text-danger">*</span>:</label>
                                       <form:input type="date" class="form-control" id="startDate" path="creationDate" name="creationDate" required="true" />
                                       <div id="startDateError" class="text-danger" style="display: none;">Start date must not be in the past.</div>
                                       <div id="weekendError" class="text-danger" style="display: none;">Start date cannot be on a Saturday or Sunday.</div>
                                   </div>
                               </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="coursePlan">Upload Course Plan<span class="text-danger">*</span>:</label>
                                        <input type="file" class="form-control-file" id="coursePlan" name="coursePlan" accept=".xlsx, .xls" required="true" />
                                        <div id="coursePlanError" class="text-danger" style="display: none;">Please upload a valid file (Excel format).</div>
                                    </div>
                                </div>
                            </div>
                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="trainer">Select Trainer<span class="text-danger">*</span>:</label>
                                        <form:select class="form-control" id="trainer" path="trainerDto.trainerId" required="true">
                                            <option value="" disabled selected>Select Trainer</option>
                                            <c:forEach var="trainer" items="${trainerList}">
                                                <form:option value="${trainer.trainerId}">${trainer.trainerName}-${trainer.trainerId}</form:option>
                                            </c:forEach>
                                        </form:select>
                                        <div id="trainerError" class="text-danger" style="display: none;">Please select a trainer.</div>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group">
                                <div  class="d-flex-between">
                                    <h5 class="mb-0">Add Holidays</h5>
                                    <button class="btn btn-primary btn-custom-position" type="button" onclick="addHolidayRow();">Create New Holiday</button>
                                </div>
                                <table class="table table-bordered table-striped" id="holidaysTable">
                                    <thead class="thead-dark">
                                        <tr>
                                            <th>Holiday Type</th>
                                            <th>Holiday Name</th>
                                            <th>Holiday Date</th>
                                            <th>Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <!-- Initial Row -->
                                        <tr>
                                            <td>
                                                <select name="holidayType" class="form-control">
                                                    <option value="">Select Type</option>
                                                    <option value="eventual">Eventual</option>
                                                    <option value="optional">Optional</option>

                                                </select>
                                            </td>
                                            <td>
                                                <input type="text" name="holidayName" class="form-control"  />
                                            </td>
                                            <td>
                                                <input type="date" name="holidayDate" class="form-control" />
                                            </td>
                                            <td>
                                                <button type="button" class="btn btn-secondary remove-row-btn" onclick="removeHolidayRow(this)">Remove</button>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
    </div>

    <!-- Spinner HTML -->
    <div id="loading">
        <div class="spinner"></div> <!-- Loading spinner -->
    </div>
    <script>

    document.addEventListener('DOMContentLoaded', function() {
        var form = document.getElementById('batchCreationForm');
        var spinner = document.getElementById('loading');
        var cancelButton = document.getElementById('cancelButton');
        var startDateInput = document.getElementById('startDate');
        var coursePlanInput = document.getElementById('coursePlan');

        var trainerSelect = document.getElementById('trainer');
        var startDateError = document.getElementById('startDateError');
        var weekendError = document.getElementById('weekendError');
        var coursePlanError = document.getElementById('coursePlanError');
        var noOfDaysError = document.getElementById('noOfDaysError');
        var trainerError = document.getElementById('trainerError');


        // Function to validate start date
        function validateStartDate() {
            var today = new Date().toISOString().split('T')[0];
            var selectedDate = new Date(startDateInput.value);

            // Clear both errors initially
            startDateError.style.display = 'none';
            weekendError.style.display = 'none';

            // Check if date is in the past
            if (startDateInput.value < today) {
                startDateError.style.display = 'block';
                return false;
            }

            // Check if date is on a weekend
            var dayOfWeek = selectedDate.getDay(); // 0 = Sunday, 6 = Saturday
            if (dayOfWeek === 0 || dayOfWeek === 6) {
                weekendError.style.display = 'block';
                return false;
            }
            return true;
        }

// check holiday date, holiday type and holiday name
        function validateHolidayRows() {
            var rows = document.querySelectorAll('#holidaysTable tbody tr');
            var valid = true;

            rows.forEach(row => {
                var holidayType = row.querySelector('select[name="holidayType"]').value;
                var holidayName = row.querySelector('input[name="holidayName"]').value.trim();
                var holidayDate = row.querySelector('input[name="holidayDate"]').value;

                var isHolidayTypeFilled = holidayType !== '';
                var isHolidayNameFilled = holidayName !== '';
                var isHolidayDateFilled = holidayDate !== '';

                if(isHolidayDateFilled && !isHolidayNameFilled && !isHolidayTypeFilled)
                {
                    $('#validationModal .modal-body').text('Holiday name and holiday date are compulsory to filled.');
                    $('#validationModal').modal('show');
                    alert('Holiday name and holiday date is compulsory to filled');
                     valid = false;
                }
                else if(isHolidayNameFilled && !isHolidayDateFilled && !isHolidayTypeFilled)
                {
                    $('#validationModal .modal-body').text('Holiday Type and holiday date are compulsory to filled');
                    $('#validationModal').modal('show');
                     valid = false;
                }
                else if(isHolidayTypeFilled && !isHolidayDateFilled && !isHolidayNameFilled)
                {
                    $('#validationModal .modal-body').text('Holiday Date and holiday name are compulsory to filled.');
                    $('#validationModal').modal('show');
                     valid = false;
                }
                else if(isHolidayTypeFilled && isHolidayDateFilled && !isHolidayNameFilled)
                {
                     $('#validationModal .modal-body').text('Holiday Name is compulsory to filled');
                     $('#validationModal').modal('show');
                     valid = false;
                }
                else if(!isHolidayTypeFilled && isHolidayDateFilled && isHolidayNameFilled)
                {
                    $('#validationModal .modal-body').text('Holiday Type is compulsory to filled.');
                    $('#validationModal').modal('show');
                    valid=false;
                }
                else if(isHolidayTypeFilled && !isHolidayDateFilled && isHolidayNameFilled)
                {
                    $('#validationModal .modal-body').text('Holiday date is compulsory to filled.');
                    $('#validationModal').modal('show');
                    valid=false;
                }
            });
            return valid;
        }

         function checkHolidayNameAndType() {
            var rows = document.querySelectorAll('#holidaysTable tbody tr');
            var valid = true;

            rows.forEach(row => {
                var holidayType = row.querySelector('select[name="holidayType"]').value;
                var holidayName = row.querySelector('input[name="holidayName"]').value.trim();
                var holidayDate = row.querySelector('input[name="holidayDate"]').value;
                var startDate = new Date(startDateInput.value);
                var alphabetOnlyRegex = /^[A-Za-z\s]+$/; // Allows only letters and spaces

                 var isHolidayTypeFilled = holidayType !== '';
                var isHolidayNameFilled = holidayName !== '';
                var isHolidayDateFilled = holidayDate !== '';

                if(!isHolidayNameFilled && !isHolidayTypeFilled && !isHolidayDateFilled)
                    valid=true;
                else
                {
                   // Check if the holiday name matches the regex
                   if (!alphabetOnlyRegex.test(holidayName)) {
                        $('#validationModal .modal-body').text('Holiday name must contain only alphabets and spaces.'); // Set the message in the body of the modal
                        $('#validationModal').modal('show');
                       valid = false;
                   }

                    var holidayDateObj = new Date(holidayDate);
                    if (holidayDateObj <= startDate) {
                     $('#validationModal .modal-body').text('Holiday date must be greater than batch start date'); // Set the message in the body of the modal
                      $('#validationModal').modal('show');
                        valid = false;
                    }
                }

            });
            return valid;
        }





        // Validate start date on change
        startDateInput.addEventListener('change', validateStartDate);

       form.addEventListener('submit', function(event) {
             let valid = true;

             if (!validateStartDate()) {
                 valid = false;
             }

             if (!validateHolidayRows()) {
                 valid = false;
             }
             else
             {
                if(!checkHolidayNameAndType())
                 {
                     valid = false;
                 }
             }



             const file = coursePlanInput.files[0];
             if (!file) {
                 coursePlanError.style.display = 'block';
                 valid = false;
             } else if (!file.name.match(/\.xlsx$|\.xls$/)) {
                 coursePlanError.style.display = 'block';
                 valid = false;
             } else {
                 coursePlanError.style.display = 'none';
             }

             if (!trainerSelect.value) {
                 trainerError.style.display = 'block';
                 valid = false;
             } else {
                 trainerError.style.display = 'none';
             }


             if (!valid) {
                 event.preventDefault(); // Prevent form submission if validation fails
             } else {
                 // Show the spinner
                 spinner.style.display = 'block';
             }
         });
    });

    function addHolidayRow() {
        var table = document.getElementById("holidaysTable").getElementsByTagName('tbody')[0];
        var newRow = table.insertRow();

        // Holiday Type
        var cell1 = newRow.insertCell(0);
        cell1.innerHTML = `<select name="holidayType" class="form-control" required>
                               <option value="" disabled selected>Select Type</option>
                               <option value="eventual">Eventual</option>
                               <option value="optional">Optional</option>

                           </select>`;

        // Holiday Name
        var cell2 = newRow.insertCell(1);
        cell2.innerHTML = `<input type="text" name="holidayName" class="form-control" />`;

        // Holiday Date
        var cell3 = newRow.insertCell(2);
        cell3.innerHTML = `<input type="date" name="holidayDate" class="form-control"  />`;

        // Remove Button
        var cell4 = newRow.insertCell(3);
        cell4.innerHTML = `<button type="button" class="btn btn-secondary remove-row-btn" onclick="removeHolidayRow(this)">Remove</button>`;
    }

    function removeHolidayRow(button) {
        var row = button.parentNode.parentNode;
        row.parentNode.removeChild(row);
    }

    </script>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            var form = document.querySelector('form');
            var spinner = document.getElementById('loading');
            var cancelButton = document.getElementById('cancelButton');

            cancelButton.addEventListener('click', function() {
                // Show the spinner before navigating away
                spinner.style.display = 'block';
                // Adding a delay to ensure the spinner is shown before redirect
                window.history.back();

            });
        });
    </script>

    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
</body>
</html>
