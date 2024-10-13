<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>

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

    </style>
</head>

<body>
<%@ include file="navbar.jsp"%>
<%@ include file="common-imports.jsp"%>
<c:set var="username" value="${pageContext.request.userPrincipal.name}" />
    <c:choose>
    <c:when test="${username == 'admin'}">
        <c:set var="formAction" value="${pageContext.request.contextPath}/admin/saveTraineeAttendance" />
    </c:when>
    <c:otherwise>
        <c:set var="formAction" value="${pageContext.request.contextPath}/trainer/saveTraineeAttendance" />
    </c:otherwise>
    </c:choose>
<div class="container">
    <div class="table-container">
        <form action="${formAction}" method="POST" modelAttribute="trainerDto">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2 class="mb-0">Trainee List</h2>
                <div>
                    <button type="submit" class="btn btn-primary">Save</button>
                    <button type="button" id="cancelButton" class="btn btn-secondary">Cancel</button>
                </div>
            </div>
            <hr>
            <input type="hidden" name="attendanceDate" value = "${attendanceDate}"/>
            <input type="hidden" name="batchId" value = "${batchId}"/>
            <table id="traineeTable" class="table table-striped table-bordered" style="width:100%; font-size: small;">
                <thead class="thead-dark">
                    <tr>
                        <th>Trainee Id</th>
                        <th>Trainee Name</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${traineeList}" var="parameter">
                        <tr>
                            <td>
                                <a href="#"
                                   class="trainee-link"
                                   data-toggle="modal"
                                   data-target="#traineeModal"
                                   data-id="${parameter.employeeId}"
                                   data-name="${parameter.traineeName}"
                                   data-gender="${parameter.gender}"
                                   data-contact="${parameter.cellNo}"
                                   data-college="${parameter.college}"
                                   data-branch="${parameter.branch}"
                                   data-doj="${parameter.doj}"
                                   data-email="${parameter.officeEmailId}">
                                   ${parameter.employeeId}
                                </a>
                            </td>
                            <td>${parameter.traineeName}</td>
                            <td>
                                <button type="button" class="btn btn-success mark-present" data-id="${parameter.employeeId}" data-state="absent">Present</button>
                                <button type="button" class="btn btn-danger mark-absent" data-id="${parameter.employeeId}" data-state="present">Absent</button>
                                <button type="button" class="btn btn-primary mark-halfDay" data-id="${parameter.employeeId}" data-state="halfDay">Half Day</button>

                                <input type="hidden" name="traineeStates[${parameter.employeeId}]" class="trainee-state" value="false">
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </form>
    </div>
</div>


<!-- Trainee Details Modal -->
<div class="modal fade" id="traineeModal" tabindex="-1" role="dialog" aria-labelledby="modalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="modalLabel">Trainee Details</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <p><strong>Trainee ID:</strong> <span id="modalTraineeId"></span></p>
                <p><strong>Trainee Name:</strong> <span id="modalTraineeName"></span></p>
                <p><strong>Gender:</strong> <span id="modalGender"></span></p>
                <p><strong>Contact No:</strong> <span id="modalContact"></span></p>
                <p><strong>College:</strong> <span id="modalCollege"></span></p>
                <p><strong>Branch:</strong> <span id="modalBranch"></span></p>
                <p><strong>Date of Joining:</strong> <span id="modalDoj"></span></p>
                <p><strong>Official Email Id:</strong> <span id="modalOfficeEmailId"></span></p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            </div>
        </div>
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
<script src="https://cdn.datatables.net/1.11.5/js/jquery.dataTables.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>

<script>
    $(document).ready(function() {
        $('#traineeTable').DataTable({
            "scrollX": true,
            "ordering": false
        });

        $('#traineeModal').on('show.bs.modal', function(event) {
            var button = $(event.relatedTarget); // Button that triggered the modal
            var employeeId = button.data('id'); // Extract info from data-* attributes
            var name = button.data('name');
            var gender = button.data('gender');
            var contact = button.data('contact');
            var college = button.data('college');
            var branch = button.data('branch');
            var doj = button.data('doj');
            var email = button.data('email');

            // Update the modal's content.
            var modal = $(this);
            modal.find('#modalTraineeId').text(employeeId);
            modal.find('#modalTraineeName').text(name);
            modal.find('#modalGender').text(gender);
            modal.find('#modalContact').text(contact);
            modal.find('#modalCollege').text(college);
            modal.find('#modalBranch').text(branch);
            modal.find('#modalDoj').text(doj);
            modal.find('#modalOfficeEmailId').text(email);
        });

     $(document).ready(function() {
         // Handle button clicks for Present
         $('.mark-present').click(function() {
             var $button = $(this);
             var traineeId = $button.data('id');

             // Reset all buttons for the same traineeId
             resetButtons(traineeId);

             // Change current button color
             $button.addClass('btn-secondary').removeClass('btn-success');

             // Update hidden input value
             $('input[name="traineeStates[' + traineeId + ']"]').val('present');
         });

         // Handle button clicks for Absent
         $('.mark-absent').click(function() {
             var $button = $(this);
             var traineeId = $button.data('id');

             // Reset all buttons for the same traineeId
             resetButtons(traineeId);

             // Change current button color
             $button.addClass('btn-secondary').removeClass('btn-danger');

             // Update hidden input value
             $('input[name="traineeStates[' + traineeId + ']"]').val('absent');
         });

         // Handle button clicks for Half Day
         $('.mark-halfDay').click(function() {
             var $button = $(this);
             var traineeId = $button.data('id');

             // Reset all buttons for the same traineeId
             resetButtons(traineeId);

             // Change current button color
             $button.addClass('btn-secondary').removeClass('btn-primary');

             // Update hidden input value
             $('input[name="traineeStates[' + traineeId + ']"]').val('halfDay');
         });

         // Function to reset button colors for a given traineeId
         function resetButtons(traineeId) {
             // Reset the color of all buttons for this traineeId
             $('.mark-present[data-id="' + traineeId + '"]').removeClass('btn-secondary').addClass('btn-success');
             $('.mark-absent[data-id="' + traineeId + '"]').removeClass('btn-secondary').addClass('btn-danger');
             $('.mark-halfDay[data-id="' + traineeId + '"]').removeClass('btn-secondary').addClass('btn-primary');
         }
     });

          // Show spinner on form submit
        $('form').on('submit', function(event) {
        let hasFalseValue = false;
         let errorMessage = '';

         // Get all hidden inputs with name starting with `traineeStates`
         const traineeStateInputs = document.querySelectorAll('input[name^="traineeStates"]');

         // Iterate over each hidden input
         traineeStateInputs.forEach(function(input) {
             // Extract the trainee ID from the name attribute
             const name = input.getAttribute('name');
             const match = name.match(/\d+/); // Extract digits from the name

             // Ensure match was found
             if (match) {
                 const traineeId = match[0];

                 // Check if the value is 'false'
                 if (input.value === 'false') {
                     // Append the trainee ID to the error message
                     errorMessage += (errorMessage ? ', ' : '') + traineeId;
                     hasFalseValue = true;
                 }
             }
         });

         // If any `traineeStates` value is `false`, handle the situation
         if (hasFalseValue) {
             // Example action: alert the user with the IDs that have 'false' value
              $('#validationModal .modal-title').text('Error'); // Set the title of the modal
               $('#validationModal .modal-body').text('Please mark the attendance for IDs : ' + errorMessage); // Set the message in the body of the modal
               $('#validationModal').modal('show'); // Show the modal

             // Prevent form submission
             event.preventDefault();
         } else {
             // Show the loading spinner
             document.getElementById('loading').style.display = 'block';
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
