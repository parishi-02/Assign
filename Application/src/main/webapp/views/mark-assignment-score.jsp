<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Assign Scores to Trainees</title>
    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.11.5/css/jquery.dataTables.min.css">
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/common.css">
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

        .marks-input {
            display: none; /* Hide by default */
            width: 100px;
        }

        .action-buttons {
            display: none; /* Hide by default */
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
    <c:set var="username" value="${pageContext.request.userPrincipal.name}" />
            <c:choose>
            <c:when test="${username == 'admin'}">
                <c:set var="formAction" value="${pageContext.request.contextPath}/admin/saveAssignmentScore" />
            </c:when>
            <c:otherwise>
                <c:set var="formAction" value="${pageContext.request.contextPath}/trainer/saveAssessmentScore" />
            </c:otherwise>
            </c:choose>
    <div class="container">
        <div class="table-container">
            <form method="post" action="${formAction}">
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h2>Assign Scores to Trainees</h2>
                    <div>
                        <button type="submit" class="btn btn-primary">Save All Trainee Marks</button>
                        <button type="button" class="btn btn-secondary" id="cancelButton">Cancel</button>
                    </div>
                </div>
                <hr>
                <table id="traineeTable" class="table table-striped table-bordered">
                    <thead>
                        <tr>
                            <th>Employee Id</th>
                            <th>Trainee Name</th>
                            <th>Marks Obtained</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${trainees}" var="trainee">
                            <tr>
                                <td>${trainee.employeeId}</td>
                                <td>${trainee.traineeName}</td>
                                <td id="marksValue">
                                    <!-- Placeholder for dynamic input field -->
                                </td>
                                <td>
                                    <button type="button" class="btn btn-info assign-score" data-id="${trainee.employeeId}">Assign Score</button>
                                    <div class="action-buttons" data-id="${trainee.employeeId}">
                                        <button type="button" class="btn btn-success save-score" data-id="${trainee.employeeId}">Save</button>
                                        <button type="button" class="btn btn-danger cancel-score" data-id="${trainee.employeeId}">Cancel</button>
                                        <input type="hidden" name="marks[${trainee.employeeId}]" value="-1">
                                        <input type="hidden" name="assignmentId" value="${assignmentId}">

                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </form>
        </div>
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

    <!-- Spinner for form submission -->
    <div id="loading">
        <div class="spinner"></div>
    </div>

    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.datatables.net/1.11.5/js/jquery.dataTables.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <script>



     $(document).ready(function() {
         $('#traineeTable').DataTable({
             "ordering": false
         });

       var marksValue = "";

         // Handle "Assign Score" button click
         $(document).on('click', '.assign-score', function() {
             var traineeId = $(this).data('id');
             var row = $(this).closest('tr');
             var marksCell = row.find('td:eq(2)'); // Target "Marks Obtained" cell
             var inputField = marksCell.find('input.marks-input');
             var actionButtons = row.find('.action-buttons');

             // Retrieve the existing value from the "Marks Obtained" cell (if any)
             var existingValue = marksCell.text().trim();
             marksValue = marksCell.text().trim();

             // Create and insert the input field if it doesn't exist
             if (inputField.length === 0) {
                // Create a new input field
                 inputField = $('<input type="text" class="marks-input form-control" name="marks[' + traineeId + ']" placeholder="Enter marks"/>');
                 marksCell.html(inputField); // Replace cell content with input field
             } else {
                 // If the input field already exists, just show it
                 inputField.show().prop('disabled', false).focus();
             }

             // Set the value of the input field to the existing value, if any
             inputField.val(existingValue);

             // Show the action buttons (Save and Cancel)
             actionButtons.show();

             // Hide the "Assign Score" button
             $(this).hide();

              unsavedChanges = false;
         });

         // Handle "Save" button click
         $(document).on('click', '.save-score', function() {
             var traineeId = $(this).data('id');
             var row = $(this).closest('tr');
             var marksInput = row.find('input.marks-input');
             var marks = marksInput.val().trim();
             var actionButtons = row.find('.action-buttons');

             // Optional: Add validation for marks if needed
             if (!marks) {
                 $('#validationModal .modal-body').text('Please enter a valid mark'); // Set the message in the body of the modal
                 $('#validationModal').modal('show');
                 return;
             }
             if(marks<0)
             {
                $('#validationModal .modal-body').text('Please enter a positive mark'); // Set the message in the body of the modal
                $('#validationModal').modal('show');
                 return;
             }
             if(marks>${assignmentPlanDto.totalScore})
             {
                   $('#validationModal .modal-body').text('Marks should be less than or equal to Total score (' +${assignmentPlanDto.totalScore} + ')');
                   $('#validationModal').modal('show');
                   return;
             }

             // Update the cell with the entered marks
             var marksCell = row.find('td:eq(2)');
             marksCell.text(marks); // Replace cell content with marks value


             $('input[name="marks[' + traineeId + ']"]').val(marks);

             // Hide the "Save" and "Cancel" buttons, and show the "Assign Score" button again
             actionButtons.hide();
             row.find('.assign-score').show();
         });

              // Handle "Cancel" button click
                  $(document).on('click', '.cancel-score', function() {
                      var row = $(this).closest('tr');
                      var marksCell = row.find('td:eq(2)');
                      var inputField = row.find('input.marks-input');
                      var actionButtons = row.find('.action-buttons');

                      // Restore the cell to its previous state
                      var existingMarks = marksCell.text().trim() || inputField.val().trim();
                      marksCell.text(marksValue); // Restore existing marks or keep the same value

                      // Hide the "Cancel" button and "Save" button, and show the "Assign Score" button again
                      actionButtons.hide();
                      row.find('.assign-score').show();

                  });


             $('form').on('submit', function(event) {
                     var valid = true;
                     var errorMsg = '';
                     var hasMarks = false;

                     // Check if any action buttons are visible
                     if ($('.action-buttons:visible').length > 0) {
                         valid = false;
                         errorMsg = 'Please save or cancel all pending actions before submitting.';
                     } else {
                       // Iterate through all rows in the table
                       $('#traineeTable tbody tr').each(function() {
                           var row = $(this);
                           var marksCell = row.find('td:eq(2)');
                           var marks = marksCell.text().trim();


                           // Check if any input field has a non-empty value
                           if (marks!=='' && marks !== '-1') {

                               hasMarks = true;
                               return false; // Exit loop early if a valid mark is found
                           }
                       });

                       // If no marks are present, set error message
                       if (!hasMarks) {
                           valid = false;
                           errorMsg = 'At least one trainee must have a marks value entered.';
                       }
                   }
                   if (!valid) {
                       $('#validationModal .modal-body').text(errorMsg);
                       $('#validationModal').modal('show');
                       event.preventDefault(); // Prevent form submission
                   }
                   if(valid)
                        $('#loading').show();

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
