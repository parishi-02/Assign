<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>



<html>
<head>
    <title>Batch List</title>
    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.11.5/css/jquery.dataTables.min.css">
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/common.css">
    <style>
        #batch-btn-container {
            display: flex;
            flex-wrap: wrap; /* Allow wrapping if there's not enough space */
            justify-content: center; /* Center buttons horizontally */
            margin-top: 20px;
        }

        #batch-btn-container .btn {
            margin: 5px; /* Adjust spacing between buttons */
        }

        .toggle-btn {
            width: 200px;
            height: 50px;
            font-size: 18px;
        }

        .card-container {
            display: none;
            margin-top: 20px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            padding: 20px;
            border-radius: 5px;
        }

        .info-line {
            display: flex;
            flex-wrap: wrap;
            align-items: center;
            margin-bottom: 10px;
            font-size: 18px;
        }

        .info-line h5 {
            margin: 0;
            margin-right: 10px; /* Space between label and value */
        }

        .info-line .info-value {
            margin-right: 20px;
            color: black;
        }

        #trainerLink
        {
             text-decoration:none;
        }

        .trainee-link
        {
            text-decoration:none;
            color:black;
        }
        .trainee-link:hover
        {
            text-decoration:none;
            color:black;
            font-size: 15px;
            font-weight:bold;
        }

        #trainerLink:hover {
            color: black;
            font-size: 20px;
            font-weight:bold;
        }

        .innerBtns {
            display: flex;
            justify-content: center;
            margin-top: 20px;
        }

        .innerBtns .btn {
            margin: 0 10px;
        }

        .coursePlan-container, .trainee-container, .mentoring-container, .assessment-container ,.assignment-container ,.attendance-container{
            display: none; /* Initially hide tables */
            margin-top: 20px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            padding: 20px;
            border-radius: 5px;
        }

        .table {
            width: 100% !important; /* Ensure the table takes full width */
            table-layout: auto; /* Allow table to adjust column widths based on content */
        }

        .highlight-row {
            color: green;
            font-weight: bold;
        }

        .default-row {
            background-color: green;
        }

        .highlight-yellow {
            color: red;
            font-weight: bold;
        }

        .disabled-button {
            background-color: gray;
            cursor: not-allowed;
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

        /* Optional: Add some margin to the body of the modal */
        .modal-body {
            margin-bottom: 20px; /* Add margin for spacing */
        }

        /* Ensure modal-dialog uses the full viewport height */
        .show-info-modal {
            height: 80vh; /* Adjust height as needed */
            margin: 0 auto;
            display: flex;
            align-items: center; /* Center vertically */
        }

        /* Make modal-content fill the modal-dialog */
        .show-info-modal-content {
            height: 100%;
            display: flex;
            flex-direction: column;
        }

        /* Make modal-body scrollable if content is too large */
        .modal-body {
            overflow-y: auto; /* Add vertical scrolling */
            max-height: calc(80vh - 120px); /* Adjust based on modal-header and modal-footer height */
        }



    </style>
</head>
<body>

    <%@ include file="navbar.jsp" %>

    <div class="container mt-5" style="display: flex; justify-content: space-between; align-items: baseline; flex-wrap: wrap;">
        <h2>Batch List</h2>
    </div>
    <hr>

    <div class="container" id="batch-btn-container">
        <!-- Batch buttons -->
        <c:forEach items="${BatchDto}" var="batch">
            <button class="btn btn-primary toggle-btn" data-course-plan-id="${batch.batchId}" style="width: 300px;">
                ${batch.batchId}
            </button>
        </c:forEach>
    </div>

    <div class="modal fade" id="trainerModal" tabindex="-1" role="dialog" aria-labelledby="trainerModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header custom-modal-header">
                    <h5 class="modal-title" id="trainerModalLabel">Trainer Details</h5>
                    <button type="button" class="close custom-close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <p><strong>Trainer ID:</strong> <span id="trainerId"></span></p>
                    <p><strong>Name:</strong> <span id="trainerName"></span></p>
                    <p><strong>Email:</strong> <span id="trainerEmail"></span></p>
                    <p><strong>Type:</strong> <span id="trainerType"></span></p>
                    <p><strong>Contact No.:</strong> <span id="trainerContact"></span></p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary mod" data-dismiss="modal" style="background-color: #2382d7; color: black; font-weight:bold;">Close</button>
                </div>
            </div>
        </div>
    </div>


    <!-- Trainee Modal -->
    <div id="traineeModal" class="modal fade" tabindex="-1" role="dialog">
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <div class="modal-header custom-modal-header">
            <h5 class="modal-title">Trainee Details</h5>
            <button type="button" class="close custom-close" data-dismiss="modal" aria-label="Close">
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div class="modal-body">
            <p><strong>ID:</strong> <span id="modalEmployeeId"></span></p>
            <p><strong>Name:</strong> <span id="modalTraineeName"></span></p>
            <p><strong>Gender:</strong> <span id="modalGender"></span></p>
            <p><strong>Contact No:</strong> <span id="modalContact"></span></p>
            <p><strong>College:</strong> <span id="modalCollege"></span></p>
            <p><strong>Branch:</strong> <span id="modalBranch"></span></p>
            <p><strong>Date of Joining:</strong> <span id="modalDoj"></span></p>
            <p><strong>Official Email Id:</strong> <span id="modalOfficeEmailId"></span></p>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-dismiss="modal" style="background-color: #2382d7; color: black; font-weight:bold;">Close</button>
          </div>
        </div>
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

    <!-- Attendance for trainee Modal -->
            <!-- Modal -->
            <div class="modal fade" id="attendanceDetailsModal" tabindex="-1" role="dialog" aria-labelledby="attendanceDetailsModalLabel" aria-hidden="true">
              <div class="modal-dialog show-info-modal" role="document">
                <div class="modal-content show-info-modal-content">
                  <div class="modal-header custom-modal-header">
                    <h5 class="modal-title" id="attendanceDetailsModalLabel">Attendance Details</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                      <span aria-hidden="true">&times;</span>
                    </button>
                  </div>
                  <div class="modal-body">
                    <p><strong>Trainee Id:</strong> <span id="traineeId"></span></p>
                    <p><strong>Trainee Name:</strong> <span id="traineeName"></span></p>

                    <!-- Table for displaying marks -->
                    <table class="table table-striped">
                      <thead>
                        <tr>
                          <th scope="col">Date</th>
                          <th scope="col">Status</th>
                        </tr>
                      </thead>
                      <tbody id="attendanceStatusByName">

                        <!-- Rows will be populated dynamically -->
                      </tbody>
                    </table>
                  </div>
                  <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal" style="background-color: #2382d7; color: black; font-weight:bold;">Close</button>
                  </div>
                </div>
              </div>
            </div>

    <!-- Assignment Marks Modal -->
        <!-- Modal -->
        <div class="modal fade" id="assignmentDetailsModal" tabindex="-1" role="dialog" aria-labelledby="assignmentDetailsModalLabel" aria-hidden="true">
          <div class="modal-dialog show-info-modal" role="document">
            <div class="modal-content show-info-modal-content">
              <div class="modal-header custom-modal-header">
                <h5 class="modal-title" id="assignmentDetailsModalLabel">Attendance Details</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                  <span aria-hidden="true">&times;</span>
                </button>
              </div>
              <div class="modal-body">
                <p><strong>Trainee Id:</strong> <span id="traineeId1"></span></p>
                <p><strong>Trainee Name:</strong> <span id="traineeName1"></span></p>

                <!-- Table for displaying marks -->
                <table class="table table-striped">
                  <thead>
                    <tr>
                      <th scope="col">Assignment Name</th>
                      <th scope="col">Total Marks</th>
                      <th scope="col">Score</th>
                    </tr>
                  </thead>
                  <tbody id="assignmentMarksByTraineeId">

                    <!-- Rows will be populated dynamically -->
                  </tbody>
                </table>
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal" style="background-color: #2382d7; color: black; font-weight:bold;">Close</button>
              </div>
            </div>
          </div>
        </div>

         <!-- Assessment Marks Modal -->
        <!-- Modal -->
        <div class="modal fade" id="assessmentDetailsModal" tabindex="-1" role="dialog" aria-labelledby="assessmentDetailsModalLabel" aria-hidden="true">
          <div class="modal-dialog show-info-modal" role="document">
            <div class="modal-content show-info-modal-content">
              <div class="modal-header custom-modal-header">
                <h5 class="modal-title" id="assessmentDetailsModalLabel">Attendance Details</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                  <span aria-hidden="true">&times;</span>
                </button>
              </div>
              <div class="modal-body">
                <p><strong>Trainee Id:</strong> <span id="traineeId2"></span></p>
                <p><strong>Trainee Name:</strong> <span id="traineeName2"></span></p>

                <!-- Table for displaying marks -->
                <table class="table table-striped">
                  <thead>
                    <tr>
                      <th scope="col">Assessment Name</th>
                      <th scope="col">Total Marks</th>
                      <th scope="col">Score</th>
                    </tr>
                  </thead>
                  <tbody id="assessmentMarksByTraineeId">

                    <!-- Rows will be populated dynamically -->
                  </tbody>
                </table>
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal" style="background-color: #2382d7; color: black; font-weight:bold;">Close</button>
              </div>
            </div>
          </div>
        </div>

        <!-- Assignment View Marks Modal -->
        <div class="modal fade" id="assignmentDetailsModal1" tabindex="-1" role="dialog" aria-labelledby="assignmentDetailsModalLabel1" aria-hidden="true">
          <div class="modal-dialog show-info-modal" role="document">
            <div class="modal-content show-info-modal-content">
              <div class="modal-header  custom-modal-header">
                <h5 class="modal-title" id="assignmentDetailsModalLabel1">Assignment Details</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                  <span aria-hidden="true">&times;</span>
                </button>
              </div>
              <div class="modal-body">
                <p><strong>Description:</strong> <span id="assignmentDescription"></span></p>
                <p><strong>Total Score:</strong> <span id="totalScore"></span></p>

                <!-- Table for displaying marks -->
                <table class="table table-striped">
                  <thead>
                    <tr>
                      <th scope="col">Name</th>
                      <th scope="col">Score</th>
                    </tr>
                  </thead>
                  <tbody id="marksObtainedByName">
                    <!-- Rows will be populated dynamically -->
                  </tbody>
                </table>
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal" style="background-color: #2382d7; color: black; font-weight:bold;">Close</button>
              </div>
            </div>
          </div>
        </div>

        <!-- Assessment View Marks Modal -->
          <div class="modal fade" id="assessmentDetailsModal1" tabindex="-1" role="dialog" aria-labelledby="assessmentDetailsModalLabel1" aria-hidden="true">
            <div class="modal-dialog show-info-modal" role="document">
              <div class="modal-content show-info-modal-content">
                <div class="modal-header custom-modal-header">
                  <h5 class="modal-title" id="assessmentDetailsModalLabel1">Assessment Details</h5>
                  <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                  </button>
                </div>
                <div class="modal-body">
                  <p><strong>Description:</strong> <span id="assessmentDescription"></span></p>
                  <p><strong>Total Score:</strong> <span id="totalScore1"></span></p>

                  <!-- Table for displaying marks -->
                  <table class="table table-striped">
                    <thead>
                      <tr>
                        <th scope="col">Name</th>
                        <th scope="col">Score</th>
                      </tr>
                    </thead>
                    <tbody id="marksObtainedByName1">
                      <!-- Rows will be populated dynamically by AJAX -->
                    </tbody>
                  </table>
                </div>
                <div class="modal-footer">
                  <button type="button" class="btn btn-secondary" data-dismiss="modal" style="background-color: #2382d7; color: black; font-weight:bold;">Close</button>
                </div>
              </div>
            </div>
          </div>

          <!--  Attendance View Modal -->
        <div class="modal fade" id="attendanceDetailsModalPerDay" tabindex="-1" role="dialog" aria-labelledby="attendanceDetailsModalPerDayLabel1" aria-hidden="true">
          <div class="modal-dialog show-info-modal" role="document">
            <div class="modal-content show-info-modal-content">
              <div class="modal-header custom-modal-header">
                <h5 class="modal-title" id="attendanceDetailsModalPerDayLabel1">Attendance Details</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                  <span aria-hidden="true">&times;</span>
                </button>
              </div>
              <div class="modal-body">
                <p><strong>Description:</strong> <span id="attendanceDate"></span></p>

                <!-- Table for displaying marks -->
                <table class="table table-striped">
                  <thead>
                    <tr>
                      <th scope="col">Trainee Id</th>
                      <th scope="col">Trainee Name</th>
                      <th scope="col">Status</th>
                    </tr>
                  </thead>
                  <tbody id="attendanceStatusByDate">
                    <!-- Rows will be populated dynamically by AJAX -->
                  </tbody>
                </table>
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal" style="background-color: #2382d7; color: black; font-weight:bold;">Close</button>
              </div>
            </div>
          </div>
        </div>
         <!-- Attendance download Modal HTML -->
          <div class="modal fade" id="dateRangeModal" tabindex="-1" aria-labelledby="dateRangeModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
              <div class="modal-content">
                <div class="modal-header custom-modal-header">
                  <h5 class="modal-title" id="dateRangeModalLabel">Select Date Range</h5>
                  <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                  <span aria-hidden="true">&times;</span>
                  </button>
                </div>
                <div class="modal-body">
                <div id="error-message" class="alert alert-danger d-none" role="alert">
                                    <!-- Error message will be inserted here -->
                                </div>
                  <form id="dateRangeForm">
                    <div class="mb-3">
                      <label for="fromDate" class="form-label">From Date</label>
                      <input type="date" class="form-control" id="fromDate" required>
                    </div>
                    <div class="mb-3">
                      <label for="toDate" class="form-label">To Date</label>
                      <input type="date" class="form-control" id="toDate" required>
                    </div>
                  </form>
                </div>
                <div class="modal-footer">
                  <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>

                  <button type="button" class="btn btn-primary" id="downloadBtn">Download</button>
                </div>
              </div>
            </div>
          </div>



    <c:forEach items="${BatchDto}" var="batch">
        <div class="container mt-3 card-container" data-course-plan-id="${batch.batchId}">
            <div class="info-line">
                <h5>Batch ID:</h5>
                <span class="info-value">${batch.batchId}</span>
            </div>
            <div class="info-line">
                <h5>Creation Date:</h5>
                <span class="info-value">${batch.creationDate}</span>
            </div>
            <div class="info-line">
                <h5>Trainer Name:</h5>
                <a id="trainerLink" href="#" class="info-value trainer-link"
                   data-trainer-id="${batch.trainerDto.trainerId}"
                   data-trainer-name="${batch.trainerDto.trainerName}"
                   data-trainer-email="${batch.trainerDto.trainerEmail}"
                   data-trainer-type="${batch.trainerDto.trainerType}"
                   data-trainer-contact="${batch.trainerDto.trainerContactNo}">
                    ${batch.trainerDto.trainerName}
                </a>
            </div>

            <div class="innerBtns">
                <button class="btn btn-primary toggle-btn" data-course-plan-id="${batch.batchId}" data-target="coursePlan">Course Plan</button>
                <button class="btn btn-primary toggle-btn" data-course-plan-id="${batch.batchId}" data-target="traineeList">Trainee List</button>
                <button class="btn btn-primary toggle-btn" data-course-plan-id="${batch.batchId}" data-target="attendance">Attendance</button>
                <button class="btn btn-primary toggle-btn" data-course-plan-id="${batch.batchId}" data-target="mentoring">Mentoring Session</button>
                <button class="btn btn-primary toggle-btn" data-course-plan-id="${batch.batchId}" data-target="assignment">Assignments</button>
                <button class="btn btn-primary toggle-btn" data-course-plan-id="${batch.batchId}" data-target="assessment">Assessment</button>
            </div>
        </div>

        <div class="container mt-3 coursePlan-container" data-course-plan-id="${batch.batchId}">
            <div class="container mt-3" style="display: flex; justify-content: space-between; align-items: baseline; flex-wrap: wrap;">
               <h2>Course Plan</h2>
               <button class="btn btn-primary fixbutton" onclick="redirectWithAdditionalParams('${pageContext.request.contextPath}/trainer/downloadCoursePlan?batchId=${batch.batchId}','${batch.creationDate}','','download');">
                               Download Course Plan
               </button>
            </div>
            <hr>
            <table id="coursePlanTable-${batch.batchId}" class="table table-striped table-bordered" style="width:100%; font-size: small;">
                <thead class="thead-dark">
                    <tr>
                        <th>S.No</th>
                        <th>Day</th>
                        <th>Topic</th>
                        <th>SubTopic</th>
                        <th>Planned Date</th>
                        <th>Actual Date</th>
                        <th>Remarks</th>
                        <th>Assignment</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${batch.coursePlanDto.dayWisePlanDTOS}" var="parameter">
                        <tr class="<c:choose>
                                    <c:when test='${fn:contains(parameter.topics.topicName, "HR INDUCTION")}'>highlight-row</c:when>
                                    <c:when test='${fn:contains(parameter.topics.topicName, "Holiday")}'>highlight-yellow</c:when>
                                    <c:otherwise>default-row</c:otherwise>
                                  </c:choose>">
                            <td>${parameter.serialNumber}</td>
                            <td>${parameter.day}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${fn:contains(parameter.topics.topicName, 'Holiday')}">
                                        ${fn:replace(parameter.topics.topicName, 'Holiday', '')}
                                    </c:when>
                                    <c:otherwise>
                                        ${parameter.topics.topicName}
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:forEach items="${parameter.topics.subTopicsDto}" var="sub">
                                    ${sub.subtopicName}<br>
                                </c:forEach>
                            </td>
                            <td style="width: 75px;">${parameter.plannedDate}</td>
                            <td style="width: 75px;">${parameter.actualDate}</td>
                            <td>${parameter.remarks}</td>
                            <td>${parameter.assignmentPlanDTO.description}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${fn:contains(parameter.topics.topicName, 'HR INDUCTION')}">
                                        <div class="btn-group" role="group">
                                            <button class="btn btn-primary fixbutton" onclick="redirectWithAdditionalParams('${pageContext.request.contextPath}/trainer/updateHRInduction?batchid=${batch.batchId}&topicName=${parameter.topics.topicName}&plannedDate=${parameter.plannedDate}','${batch.creationDate}');" style="color:white; text-decoration:none;">
                                                Update
                                            </button>
                                        </div>
                                    </c:when>
                                    <c:when test="${fn:contains(parameter.topics.topicName, 'Holiday')}">
                                        <div class="btn-group" role="group">
                                            <button class="btn btn-primary fixbutton disabled-button" disabled style="color:white; text-decoration:none;">
                                                Update
                                            </button>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                       <div class="btn-group" role="group">
                                           <button class="btn btn-primary fixbutton" onclick="checkPlannedDate('${parameter.plannedDate}','${pageContext.request.contextPath}/trainer/updatePlannedDate?batchid=${batch.batchId}&topicName=${parameter.topics.topicName}&planId=${parameter.planId}&plannedDate=${parameter.plannedDate}','${batch.creationDate}');" style="color:white; text-decoration:none;">
                                              Update
                                           </button>
                                       </div>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <div class="container mt-3 mentoring-container" data-course-plan-id="${batch.batchId}">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2>Mentoring List</h2>
                <div>

                    <button class="btn btn-primary fixbutton" onclick="redirectWithAdditionalParams('${pageContext.request.contextPath}/trainer/downloadMentoringSession?batchId=${batch.batchId}','${batch.creationDate}','','download');">
                          Download Mentoring Session List
                    </button>
                </div>
            </div>
            <hr>
            <table id="mentoringSessionTable-${batch.batchId}" class="table table-striped table-bordered" style="width:100%; font-size: small;">
                <thead class="thead-dark">
                    <tr>
                        <th>Session Description</th>
                        <th>Session Time</th>
                        <th>Planned Date</th>
                        <th>Actual Date</th>
                        <th>Remarks</th>

                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${batch.mentoringSessionDtos}" var="parameter">
                        <tr>
                            <td>${parameter.sessionDescription}</td>
                            <td>${parameter.plannedTime}</td>
                            <td>${parameter.plannedDate}</td>
                            <td>${parameter.actualDate}</td>
                            <td>${parameter.remarks}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>


        <div class="container mt-3 attendance-container" data-course-plan-id="${batch.batchId}">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2>Attendance</h2>
                 <button class="btn btn-primary fixbutton"
                      onclick="attendanceDownload('${pageContext.request.contextPath}/trainer/downloadAttendance?batchId=${batch.batchId}', '${batch.creationDate}', '', 'download');">
                      Download Attedance Marks
                  </button>
            </div>
            <hr>
            <table id="attendanceTable-${batch.batchId}" class="table table-striped table-bordered" style="width:100%; font-size: small;">
                <thead class="thead-dark">
                    <tr>
                        <th>Date</th>
                        <th>Day</th>
                        <th>Action</th>
                    </tr>
                </thead>
                    <tbody>
                          <c:forEach items="${batch.coursePlanDto.dayWisePlanDTOS}" var="parameter">
                              <c:set var="plannedDate" value="${parameter.plannedDate}" />
                              <c:set var="plannedDay" value="${parameter.day}" />

                              <!-- Check if plannedDate is before the currentDate -->
                              <c:choose>
                                  <c:when test="${plannedDate lt currentDate}">

                                      <c:choose>
                                          <c:when test="${plannedDay ne 'SATURDAY' && plannedDay ne 'SUNDAY'}">
                                              <tr>
                                                  <td>${parameter.plannedDate}</td>
                                                  <td>${parameter.day}</td>
                                                  <td>
                                                      <c:choose>
                                                          <c:when test="${fn:contains(parameter.topics.topicName, 'Holiday')}">
                                                              <button type="button" class="btn btn-primary" disabled>${parameter.topics.topicName}</button>
                                                          </c:when>
                                                          <c:otherwise>
                                                              <button type="button" class="btn btn-secondary" onclick="redirectWithAdditionalParams('${pageContext.request.contextPath}/trainer/updateAttendanceForm?batchId=${batch.batchId}&attendanceDate=${parameter.plannedDate}','${batch.creationDate}');">Update Attendance</button>
                                                           <button type="button" class="btn btn-secondary" onclick="viewAttendance('${batch.batchId}','${parameter.plannedDate}');">View</button>
                                                          </c:otherwise>
                                                      </c:choose>
                                                  </td>
                                              </tr>
                                          </c:when>
                                          <c:otherwise>
                                              <!-- Skip weekends -->
                                          </c:otherwise>
                                      </c:choose>
                                  </c:when>

                                  <c:when test="${plannedDate eq currentDate}">
                                      <c:choose>
                                        <c:when test="${plannedDay ne 'SATURDAY' && plannedDay ne 'SUNDAY'}">
                                            <tr>
                                                <td>${parameter.plannedDate}</td>
                                                <td>${parameter.day}</td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${fn:contains(parameter.topics.topicName, 'Holiday')}">
                                                            <button type="button" class="btn btn-primary" disabled>Mark Attendance</button>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <button type="button" class="btn btn-primary" onclick="redirectWithAdditionalParams('${pageContext.request.contextPath}/trainer/updateAttendanceForm?batchId=${batch.batchId}&attendanceDate=${parameter.plannedDate}','${batch.creationDate}');">Mark Attendance</button>
                                                           <button type="button" class="btn btn-secondary" onclick="viewAttendance('${batch.batchId}','${parameter.plannedDate}');">View</button>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                            </tr>
                                      </c:when>
                                        <c:otherwise>
                                            <!-- Skip weekends -->
                                        </c:otherwise>
                                    </c:choose>
                                  </c:when>
                                  <c:otherwise>

                                  </c:otherwise>
                              </c:choose>
                          </c:forEach>
                      </tbody>
            </table>
        </div>
        <div class="container mt-3 assignment-container" data-course-plan-id="${batch.batchId}">
            <div class="container mt-3" style="display: flex; justify-content: space-between; align-items: baseline; flex-wrap: wrap;">
               <h2>Assignment List</h2>
               <button class="btn btn-primary fixbutton" onclick="redirectWithAdditionalParams('${pageContext.request.contextPath}/trainer/downloadAssignmentList?batchId=${batch.batchId}','${batch.creationDate}','','download');">
                               Download Assignment List
               </button>
            </div>
            <hr>
            <table id="assignmentTable-${batch.batchId}" class="table table-striped table-bordered" style="width:100%; font-size: small;">
                <thead class="thead-dark">
                    <tr>
                        <th>Date</th>
                        <th>Assignment Name</th>
                        <th>Topic</th>
                        <th>Total Marks</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${batch.coursePlanDto.dayWisePlanDTOS}" var="parameter">
                        <c:if test="${not empty parameter.assignmentPlanDTO.description}">
                            <tr>
                                <td style="width:70px;">${parameter.plannedDate}</td>
                                <td style="width:150px;">${parameter.assignmentPlanDTO.description}</td>
                                <td style="width:300px;">${parameter.topics.topicName}</td>
                                <td style="width:80px;">
                                    <form class="edit-form" action="${pageContext.request.contextPath}/trainer/updateAssignmentTotalMarks" method="post">

                                       <input type="hidden" name="id" id="assignmentId" value="${parameter.assignmentPlanDTO.id}"/>
                                       <input type="hidden" name="batchId" id="batchId" value="${batch.batchId}"/>
                                       <input type="hidden" name="divType" id="divType" value="assignment"/>
                                       <input type="hidden" name="batchType" id="batchType" value="${batch.batchId}"/>
                                        <span class="marks" id="mark" value="${parameter.assignmentPlanDTO.totalScore}">${parameter.assignmentPlanDTO.totalScore}</span>
                                        <input type="number" name="totalMarks" id="score-${parameter.assignmentPlanDTO.id}" class="form-control d-none" min="1" value="${parameter.assignmentPlanDTO.totalScore}"/>
                                    </form>
                                </td>
                                <td style="width:300px;">
                                    <button type="button" class="btn btn-primary update-btn" data-id="${batch.creationDate}">Update</button>
                                    <button type="button" class="btn btn-success" id="uploadMarksBtn" data-id="${parameter.assignmentPlanDTO.id}" data-batch-creation-date="${batch.creationDate}" data-planned-date="${parameter.plannedDate}">Upload Marks</button>
                                    <button type="button" class="btn btn-secondary" onclick="viewAssignmentDetails(${parameter.assignmentPlanDTO.id})">View</button>
                                    <button type="button" class="btn btn-secondary cancel-btn d-none">Cancel</button>
                                </td>
                            </tr>
                        </c:if>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <div class="container mt-3 assessment-container" data-course-plan-id="${batch.batchId}">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2>Assessment List</h2>
                <div>
                    <button class="btn btn-primary fixbutton" onclick="redirectWithAdditionalParams('${pageContext.request.contextPath}/trainer/assessment-form?batchId=${batch.batchId}&batchStartDate=${batch.creationDate}','${batch.creationDate}');">
                       Add Assessment
                    </button>
                     <button class="btn btn-primary fixbutton"
                           onclick="redirectWithAdditionalParams('${pageContext.request.contextPath}/trainer/downloadAssessments?batchId=${batch.batchId}', '${batch.creationDate}', '', 'download');">
                       Download Assessment Marks
                   </button>
                </div>

            </div>
            <hr>
            <table id="assessmentTable-${batch.batchId}" class="table table-striped table-bordered" style="width:100%; font-size: small;">
                <thead class="thead-dark">
                    <tr>
                        <th>Topic</th>
                        <th>Total Marks</th>
                        <th>Planned Date</th>
                        <th>Actual Date</th>
                        <th>Remarks</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${batch.assessmentDTOS}" var="parameter">
                        <tr>
                            <td>${parameter.subTopic}</td>
                            <td>${parameter.totalScore}</td>
                            <td>${parameter.plannedDate}</td>
                            <td>${parameter.actualDate}</td>
                            <td>${parameter.remarks}</td>

                            <td style="width:300px;">

                                   <button class="btn btn-primary" onclick="checkPlannedDate('${parameter.plannedDate}','${pageContext.request.contextPath}/trainer/updateAssessmentForm?assessmentId=${parameter.assessmentId}','${batch.creationDate}');" style="color:white; text-decoration:none;">
                                     Update
                                   </button>
                                   <button class="btn btn-success" onclick="checkActualDate('${parameter.actualDate}','${pageContext.request.contextPath}/trainer/updateAssessmentMarks?assessmentId=${parameter.assessmentId}&batchId=${batch.batchId}','${batch.creationDate}');" style="color:white; text-decoration:none;">
                                        Upload Marks
                                   </button>
                                   <button type="button" class="btn btn-secondary" onclick="viewAssessmentDetails(${parameter.assessmentId})">View</button>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <div class="container mt-3 trainee-container" data-course-plan-id="${batch.batchId}">
            <div class="container mt-3" style="display: flex; justify-content: space-between; align-items: baseline; flex-wrap: wrap;">
                <h2>Trainee List</h2>
            </div>
            <hr>
            <table id="traineeTable-${batch.batchId}" class="table table-striped table-bordered" style="width:100%; font-size: small;">
                <thead class="thead-dark">
                    <tr>
                        <th>Trainee Id</th>
                        <th>Trainee Name</th>
                        <th>Date of Joining</th>
                        <th>Email Address</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${batch.traineeList}" var="parameter">
                        <tr>
                            <td>
                                <a href="#"
                                   data-toggle="modal"
                                   data-target="#traineeModal"
                                   data-id="${parameter.employeeId}"
                                   data-name="${parameter.traineeName}"
                                   data-gender="${parameter.gender}"
                                   data-contact="${parameter.cellNo}"
                                   data-college="${parameter.college}"
                                   data-branch="${parameter.branch}"
                                   data-doj="${parameter.doj}"
                                   data-email="${parameter.officeEmailId}"
                                   class="trainee-link">
                                   ${parameter.employeeId}
                                </a>
                            </td>
                            <td>${parameter.traineeName}</td>
                            <td>${parameter.doj}</td>
                            <td>${parameter.officeEmailId}</td>
                            <td style="width:400px;">
                               <button class="btn btn-primary fixbutton" onclick="viewAttendanceDetails(${parameter.employeeId})" style="color:white; text-decoration:none;">
                                 Attendance
                               </button>
                               <button class="btn btn-primary fixbutton" onclick="viewAssignmentMarksDetails(${parameter.employeeId})" style="color:white; text-decoration:none;">
                                 Assignment
                               </button>
                               <button class="btn btn-primary fixbutton" onclick="viewAssessmentMarksDetails(${parameter.employeeId})"  style="color:white; text-decoration:none;">
                                 Assessment
                               </button>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </c:forEach>


     <!-- Spinner for form submission -->
      <div id="loading">
          <div class="spinner"></div>
      </div>


    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <!-- Latest DataTables -->
    <script src="https://cdn.datatables.net/1.12.1/js/jquery.dataTables.min.js"></script>

    <script>

        // Declare and initialize divTypeValue and batchTypeValue at the beginning
              var divTypeValue;
              var batchTypeValue;
              function showValidationModal(title, message) {
                  $('#validationModal .modal-title').text(title); // Set the title of the modal
                  $('#validationModal .modal-body').text(message); // Set the message in the body of the modal
                  $('#validationModal').modal('show'); // Show the modal
              }

              // Declare and initialize divTypeValue and batchTypeValue at the beginning
              var divTypeValue = 'defaultDivType'; // Default value for divType
              var batchTypeValue = 'defaultBatchType'; // Default value for batchType

              // Function to update URL parameters
              function updateUrlParams() {
                  var url = new URL(window.location.href);
                  url.searchParams.set('divType', encodeURIComponent(divTypeValue));
                  url.searchParams.set('batchType', encodeURIComponent(batchTypeValue));
                  window.history.replaceState(null, '', url.toString()); // Update URL without reloading
              }

              // Function to get URL parameters
              function getUrlParams() {
                  var urlParams = new URLSearchParams(window.location.search);
                  return {
                      divType: urlParams.get('divType'),
                      batchType: urlParams.get('batchType')
                  };
              }

              // Initialize page state based on URL parameters
              function initializePageState(divType, batchType) {

                  if (batchType) {
                      var cardContainer = $('.card-container[data-course-plan-id="' + batchType + '"]');
                      var coursePlanId = batchType;
                      var coursePlanContainer = $('.coursePlan-container[data-course-plan-id="' + coursePlanId + '"]');
                      var traineeContainer = $('.trainee-container[data-course-plan-id="' + coursePlanId + '"]');
                      var mentoringContainer = $('.mentoring-container[data-course-plan-id="' + coursePlanId + '"]');
                      var assessmentContainer = $('.assessment-container[data-course-plan-id="' + coursePlanId + '"]');
                      var assignmentContainer = $('.assignment-container[data-course-plan-id="' + coursePlanId + '"]');
                      var attendanceContainer = $('.attendance-container[data-course-plan-id="' + coursePlanId + '"]');
                      if (cardContainer.length > 0) {
                          cardContainer.show();
                          $('.card-container').not(cardContainer).hide();
                          $('.coursePlan-container, .trainee-container, .mentoring-container, .assessment-container, .assignment-container,.attendance-container').hide();
                          if (divType === 'coursePlan') {
                              coursePlanContainer.toggle();
                          } else if (divType === 'traineeList') {
                              traineeContainer.toggle();
                          } else if (divType === 'mentoring') {
                              mentoringContainer.toggle();
                          } else if (divType === 'assessment') {
                              assessmentContainer.toggle();
                          } else if (divType === 'assignment') {
                              assignmentContainer.toggle();
                          }
                          else if (divType === 'attendance') {
                            attendanceContainer.toggle();
                          }
                      }
                  } else {
                      // Handle the case where batchType is not set
                      $('.card-container').hide();
                      $('.coursePlan-container, .trainee-container, .mentoring-container, .assessment-container, .assignment-container,.attendance-container').hide();
                  }

                  initializeDataTables();
              }

              // Function to initialize DataTables
              function initializeDataTables() {
                  $('.coursePlan-container, .trainee-container, .mentoring-container, .assessment-container, .assignment-container, .attendance-container').each(function() {
                      const tableId = $(this).find('table').attr('id');
                      if ($('#' + tableId).length > 0 && $('#' + tableId + ' tbody tr').length > 0) {
                          if ($.fn.DataTable.isDataTable('#' + tableId)) {
                              $('#' + tableId).DataTable().destroy();
                          }

                          $('#' + tableId).DataTable({
                              "ordering": false
                          });
                      }
                  });
              }

              function checkActualDate(actualDate,basePath,batchCreationDate)
              {
                  if(actualDate==='')
                  {
                      showValidationModal('Error','You cannot upload marks until actual date is updated.');
                  }
                  else
                  {
                      // Convert plannedDate from string to Date object
                       var actualDateObj = new Date(actualDate);
                       var currentDateObj = new Date();

                       // Strip time from date for comparison
                       currentDateObj.setHours(0, 0, 0, 0);
                       actualDateObj.setHours(0, 0, 0, 0);

                       // Check if current date is less than actual date
                       if (currentDateObj < actualDateObj) {
                           showValidationModal('Error', 'Assessment Actual Date is ' + actualDate + " so you cannot upload marks before the actual date.");
                       } else {
                           // Redirect to the URL if date check passes
                           redirectWithAdditionalParams(basePath,batchCreationDate);
                       }
                  }

              }


               function checkPlannedDate(plannedDate,basePath,batchCreationDate)
                {
                  // Convert plannedDate from string to Date object
                  var plannedDateObj = new Date(plannedDate);
                  var currentDateObj = new Date();

                  // Strip time from date for comparison
                  currentDateObj.setHours(0, 0, 0, 0);
                  plannedDateObj.setHours(0, 0, 0, 0);

                  // Check if current date is less than planned date
                  if (currentDateObj < plannedDateObj) {
                      showValidationModal('Error', 'Assessment Planned Date is ' + plannedDate + " so you cannot update Actual date before the planned date.");
                  } else {
                      // Redirect to the URL if date check passes
                      redirectWithAdditionalParams(basePath,batchCreationDate);
                  }
               }

               function attendanceDownload(basePath,batchCreationDate,traineeFlag,buttonName)
                   {
                    // Store parameters for later use
                       window.attendanceDownloadParams = {
                           basePath: basePath,
                           batchCreationDate: batchCreationDate,
                           traineeFlag: traineeFlag,
                           buttonName: buttonName
                       };

                       // Show the modal
                       $('#dateRangeModal').modal('show');

                   }
                document.addEventListener('DOMContentLoaded', function() {
                     document.getElementById('downloadBtn').addEventListener('click', function() {

                         var fromDate = document.getElementById('fromDate').value;
                         var toDate = document.getElementById('toDate').value;
                         var errorMessageContainer = document.getElementById('error-message');
                          // Clear any previous error messages
                         errorMessageContainer.classList.add('d-none');
                         errorMessageContainer.textContent = '';

                         if(!fromDate || !toDate)
                         {
                           errorMessageContainer.textContent = 'Please select both dates.';
                             errorMessageContainer.classList.remove('d-none');
                             return; // Stop further execution
                         }

                       $('#dateRangeModal').modal('hide');
                          var params = window.attendanceDownloadParams;
                     // Redirect to URL with selected dates and other parameters
                          var url = params.basePath + "&fromDate=" + fromDate + "&toDate=" + toDate;
                          redirectWithAdditionalParams(url,params.batchCreationDate,'',params.buttonName);

                     });
                 });


              // Function to redirect with additional parameters
              function redirectWithAdditionalParams(basePath,batchCreationDate,traineeFlag,buttonName) {
                if(buttonName!=='download')
                   document.getElementById('loading').style.display = 'block';

                   // Convert the batchCreationDate to a Date object
                           var batchDate = new Date(batchCreationDate);
                           var currentDate = new Date();
                             var currentDate = new Date();
                           batchDate.setHours(0, 0, 0, 0);
                           currentDate.setHours(0, 0, 0, 0);

                         // Check if batchCreationDate is greater than the current date
                         if (batchDate >= currentDate) {
                             // Hide the spinner
                             document.getElementById('loading').style.display = 'none';
                             // Show alert and exit function
                             showValidationModal('Error','You cannot perform any operation until the batch starts.');
                             return; // Stop execution
                         }
                  var baseUrl = new URL(basePath, window.location.origin);
                  baseUrl.searchParams.set('divType', encodeURIComponent(divTypeValue));
                  baseUrl.searchParams.set('batchType', encodeURIComponent(batchTypeValue));
                  window.location.href = baseUrl.toString();
              }

              // Handle button click to toggle container visibility
              $('.toggle-btn[data-target]').click(function() {
                  var coursePlanId = $(this).data('course-plan-id');
                  var target = $(this).data('target');
                  var coursePlanContainer = $('.coursePlan-container[data-course-plan-id="' + coursePlanId + '"]');
                  var traineeContainer = $('.trainee-container[data-course-plan-id="' + coursePlanId + '"]');
                  var mentoringContainer = $('.mentoring-container[data-course-plan-id="' + coursePlanId + '"]');
                  var assessmentContainer = $('.assessment-container[data-course-plan-id="' + coursePlanId + '"]');
                  var assignmentContainer = $('.assignment-container[data-course-plan-id="' + coursePlanId + '"]');
                  var attendanceContainer = $('.attendance-container[data-course-plan-id="' + coursePlanId + '"]');

                  if (target === 'coursePlan') {
                      coursePlanContainer.toggle();
                      traineeContainer.hide();
                      mentoringContainer.hide();
                      assessmentContainer.hide();
                      assignmentContainer.hide();
                      attendanceContainer.hide();
                      divTypeValue = coursePlanContainer.is(':visible') ? 'coursePlan' : 'defaultDivType';
                  } else if (target === 'traineeList') {
                      traineeContainer.toggle();
                      coursePlanContainer.hide();
                      assessmentContainer.hide();
                      mentoringContainer.hide();
                      assignmentContainer.hide();
                      attendanceContainer.hide();
                      divTypeValue = traineeContainer.is(':visible') ? 'traineeList' : 'defaultDivType';
                  } else if (target === 'mentoring') {
                      mentoringContainer.toggle();
                      coursePlanContainer.hide();
                      traineeContainer.hide();
                      assessmentContainer.hide();
                      assignmentContainer.hide();
                      attendanceContainer.hide();
                      divTypeValue = mentoringContainer.is(':visible') ? 'mentoring' : 'defaultDivType';
                  } else if (target === 'assessment') {
                      assessmentContainer.toggle();
                      mentoringContainer.hide();
                      coursePlanContainer.hide();
                      traineeContainer.hide();
                      assignmentContainer.hide();
                      attendanceContainer.hide();
                      divTypeValue = assessmentContainer.is(':visible') ? 'assessment' : 'defaultDivType';
                  } else if (target === 'assignment') {
                      assignmentContainer.toggle();
                      mentoringContainer.hide();
                      coursePlanContainer.hide();
                      traineeContainer.hide();
                      assessmentContainer.hide();
                      attendanceContainer.hide();
                      divTypeValue = assignmentContainer.is(':visible') ? 'assignment' : 'defaultDivType';
                  }
                   else if (target === 'attendance') {
                        attendanceContainer.toggle();
                        assignmentContainer.hide();
                        mentoringContainer.hide();
                        coursePlanContainer.hide();
                        traineeContainer.hide();
                        assessmentContainer.hide();
                        divTypeValue = attendanceContainer.is(':visible') ? 'attendance' : 'defaultDivType';
                    }


                  updateUrlParams(); // Update URL parameters
                  initializeDataTables(); // Initialize DataTables when visible
              });

              // Handle toggle button without data-target
              $('.toggle-btn:not([data-target])').click(function() {
                  if (!batchTypeValue || batchTypeValue === 'defaultBatchType') {
                      var coursePlanId = $(this).data('course-plan-id');
                      batchTypeValue = coursePlanId;
                      var cardContainer = $('.card-container[data-course-plan-id="' + coursePlanId + '"]');

                      $('.card-container').not(cardContainer).hide();
                      $('.coursePlan-container, .trainee-container, .mentoring-container, .assessment-container, .assignment-container, .attendance-container').hide();

                      cardContainer.toggle();
                  } else {
                      var coursePlanId = batchTypeValue;
                      var cardContainer = $('.card-container[data-course-plan-id="' + coursePlanId + '"]');
                      var coursePlanId1 = $(this).data('course-plan-id');

                      console.log('Switched cardContainer ID:', coursePlanId1); // Debug log
                      var cardContainer1 = $('.card-container[data-course-plan-id="' + coursePlanId1 + '"]');

                      $('.card-container').not(cardContainer).hide();
                      $('.card-container').not(cardContainer1).hide();
                      $('.coursePlan-container, .trainee-container, .mentoring-container, .assessment-container, .assignment-container, .attendance-container').hide();

                      cardContainer1.toggle();
                      batchTypeValue = coursePlanId1;
                      divTypeValue = 'defaultDivType'; // Reset the value to default
                  }

                  updateUrlParams(); // Update URL parameters
              });

              // Function to submit form
              function submitForm(traineeId) {
                  document.getElementById('employeeId').value = traineeId;
                  document.getElementById('updateTraineeForm').submit();
              }

              // Handle form submission and URL updates on DOMContentLoaded
              document.addEventListener('DOMContentLoaded', function() {
                  const urlParams = new URLSearchParams(window.location.search);
                  const successMessage = urlParams.get('successMessage');
                  if (successMessage && successMessage.trim() !== 'null') {
                      showValidationModal('Success', successMessage);
                      urlParams.delete('successMessage');
                      const newUrl = window.location.pathname + (urlParams.toString() ? '?' + urlParams.toString() : '');
                      window.history.replaceState({}, '', newUrl);

                  }
                  var params = getUrlParams();
                  divTypeValue = params.divType || 'defaultDivType';
                  batchTypeValue = params.batchType || 'defaultBatchType';
                  initializePageState(divTypeValue, batchTypeValue);
              });

              // Handle browser navigation events
              window.addEventListener('popstate', function() {
                  var params = getUrlParams();
                  divTypeValue = params.divType || 'defaultDivType';
                  batchTypeValue = params.batchType || 'defaultBatchType';
                  initializePageState(divTypeValue, batchTypeValue);
              });

          $('.trainer-link').click(function(e) {
              e.preventDefault();

              // Get trainer details from data attributes
              var trainerId = $(this).data('trainer-id');
              var trainerName = $(this).data('trainer-name');
              var trainerEmail = $(this).data('trainer-email');
              var trainerType = $(this).data('trainer-type');
              var trainerContact = $(this).data('trainer-contact');

              // Set trainer details in the modal
              $('#trainerId').text(trainerId);
              $('#trainerName').text(trainerName);
              $('#trainerEmail').text(trainerEmail);
              $('#trainerType').text(trainerType);
              $('#trainerContact').text(trainerContact);

              // Show the modal
              $('#trainerModal').modal('show');
          });

          $('.trainee-link').on('click', function() {
              var employeeId = $(this).data('id');
              var name = $(this).data('name');
              var gender = $(this).data('gender');
              var contact = $(this).data('contact');
              var college = $(this).data('college');
              var branch = $(this).data('branch');
              var doj = $(this).data('doj');
              var email = $(this).data('email');

              $('#modalEmployeeId').text(employeeId);
              $('#modalTraineeName').text(name);
              $('#modalGender').text(gender);
              $('#modalContact').text(contact);
              $('#modalCollege').text(college);
              $('#modalBranch').text(branch);
              $('#modalDoj').text(doj);
              $('#modalOfficeEmailId').text(email);
          });

          // Handle click event for the "Update" button
          $(document).on('click', '.update-btn', function() {
              const row = $(this).closest('tr');
              const form = row.find('form.edit-form');
              const marksSpan = row.find('.marks');
              const marksInput = row.find('input[name="totalMarks"]');
              const cancelBtn = row.find('.cancel-btn');
              const errorMessage = row.find('.error-message');

              var batchCreationDate = $(this).data('id');

               var batchDate = new Date(batchCreationDate);
               var currentDate = new Date();
                 batchDate.setHours(0, 0, 0, 0);
                   currentDate.setHours(0, 0, 0, 0);


               // Check if batchCreationDate is greater than the current date
               if (batchDate >= currentDate) {
                   // Hide the spinner
                   document.getElementById('loading').style.display = 'none';
                   // Show alert and exit function
                   showValidationModal('Error','You cannot perform any operation until the batch starts.');
               }
               else
               {
                  if ($(this).text() === 'Update') {
                      // Make the "Total Marks" column editable
                      const currentValue = marksSpan.text().trim();
                      marksInput.val(currentValue);
                      marksInput.removeClass('d-none');
                      marksSpan.addClass('d-none');
                      $(this).text('Save').removeClass('btn-primary').addClass('btn-success');
                      cancelBtn.removeClass('d-none'); // Show cancel button
                  } else {
                      // Validate the input before saving
                      const marksValue = marksInput.val().trim();
                      if (marksValue === '' || parseInt(marksValue, 10) <= 0) {
                          // Show error message
                          showValidationModal('Error', 'Please enter valid total score before saving.');
                      } else {
                          // Save the updated value and submit the form
                          marksSpan.text(marksValue);
                          marksInput.addClass('d-none');
                          marksSpan.removeClass('d-none');
                          $(this).text('Update').removeClass('btn-success').addClass('btn-primary');
                          cancelBtn.addClass('d-none'); // Hide cancel button

                        var loading = document.getElementById('loading');
                        loading.style.display = 'block'; // Show the spinner

                        // Submit the form
                        form.submit();
                      }
                  }
              }
          });

          // Handle click event for the "Cancel" button
          $(document).on('click', '.cancel-btn', function() {
              const row = $(this).closest('tr');
              const form = row.find('form.edit-form');
              const marksSpan = row.find('.marks');
              const marksInput = row.find('input[name="totalMarks"]');
              const updateBtn = row.find('.update-btn');
              const errorMessage = row.find('.error-message');

              // Revert to original state
              marksInput.val(marksSpan.text().trim());
              marksInput.addClass('d-none');
              marksSpan.removeClass('d-none');
              updateBtn.text('Update').removeClass('btn-success').addClass('btn-primary');
              this.classList.add('d-none'); // Hide cancel button
              errorMessage.addClass('d-none'); // Hide error message
          });

           $(document).on('click', '#uploadMarksBtn', function(event) {
                 var button = this;
                 var batchCreationDate = button.getAttribute('data-batch-creation-date');
                 var plannedDate = button.getAttribute('data-planned-date');
                 var plannedDateObj = new Date(plannedDate);
                 plannedDateObj.setHours(0,0,0,0);
                 var batchDate = new Date(batchCreationDate);
                  var currentDate = new Date();
                    batchDate.setHours(0, 0, 0, 0);
                      currentDate.setHours(0, 0, 0, 0);


                  // Check if batchCreationDate is greater than the current date
                  if (batchDate >= currentDate) {
                      // Hide the spinner
                      document.getElementById('loading').style.display = 'none';
                      // Show alert and exit function
                      showValidationModal('Error','You cannot perform any operation until the batch starts.');
                  }
                   else if(currentDate<plannedDateObj)
                   {
                         document.getElementById('loading').style.display = 'none';
                         // Show alert and exit function
                         showValidationModal('Error','Assignemnt planned date(' + plannedDate + ") is greater than current date so you cannot upload marks.");
                   }
                  else
                  {
                        // Prevent default action
                        event.preventDefault();

                        // Get the assignment ID from the data attribute
                        var assignmentId = $(this).data('id');

                        // Construct the id for the totalMarks input field based on the assignmentId
                        var totalMarksInputId = '#score-' + assignmentId;

                        // Retrieve the value of the input field with the constructed id
                        var totalMarksValue = $(totalMarksInputId).val().trim();

                        var batchId = $('#batchId').val();

                        // Check if the totalMarksValue is empty
                        if (!totalMarksValue) {
                            // Show the modal if the input field is empty
                            showValidationModal('Error', 'Please enter a total score before uploading marks.');
                        } else {
                            // If value is present, navigate to the URL
                             var ur = "${pageContext.request.contextPath}/trainer/updateAssignmentMarks?assignmentId=" + assignmentId + "&batchId=" + batchTypeValue;
                            redirectWithAdditionalParams(ur);
                        }
                  }
            });

    </script>

     <script>
       function viewAttendanceDetails(assignmentId) {
        var loading = document.getElementById('loading');

            loading.style.display = 'block'; // Show the spinner
           $.ajax({
               url: "${pageContext.request.contextPath}/trainer/getTraineeAttendance",
               type: "GET",
               data: {traineeId: assignmentId},
               success: function(data) {

               if(data.error)
               {
                    $('#validationModal .modal-title').text('Error'); // Set the title of the modal
                      $('#validationModal .modal-body').text(data.error); // Set the message in the body of the modal
                      $('#validationModal').modal('show'); // Show the modal
               }
               else
               {
                   // Check if data contains the expected properties
                   if (data.traineeId && data.traineeName && data.traineeAttendance) {
                       // Set the assignment description and total score
                       $('#traineeId').text(data.traineeId);
                       $('#traineeName').text(data.traineeName);

                       // Clear any previous content in the attendanceStatusByName table body
                       $('#attendanceStatusByName').empty();


                       // Loop through the map and append each key-value pair as a table row
                       $.each(data.traineeAttendance, function(date, attendanceStatus){
                        let status;
                           if (attendanceStatus === 'present') {
                               status = 'PRESENT';
                           } else if (attendanceStatus === 'absent') {
                               status = 'ABSENT';
                           } else if (attendanceStatus === 'halfDay'){
                               status = 'HALF DAY'; // Handle any other cases or invalid scores
                           }

                           $('#attendanceStatusByName').append('<tr><td>' + date + '</td><td>' + status + '</td></tr>');
                       });
                       // Show the modal
                       $('#attendanceDetailsModal').modal('show');
                        loading.style.display = 'none';
                   }
                    else {
                       console.error('Data missing expected properties:', data);
                       alert('Error: Data does not contain expected properties.');
                   }
               }
           },
               error: function(xhr, status, error) {
                   // Improved error handling
                   console.error("Error fetching assignment details:", {
                       status: status,
                       error: error,
                       responseText: xhr.responseText,
                       responseStatus: xhr.status
                   });
                   alert('Error fetching assignment details. Status: ' + xhr.status + ', Error: ' + error);
               },
                complete: function() {
                   // Hide the spinner regardless of success or error
                   loading.style.display = 'none';
               }
           });
         }


          function viewAssignmentMarksDetails(assignmentId) {
           var loading = document.getElementById('loading');
            loading.style.display = 'block'; // Show the spinner
            $.ajax({
                url: "${pageContext.request.contextPath}/trainer/getTraineeAssignment",
                type: "GET",
                data: {traineeId: assignmentId},
                success: function(data) {

            if(data.error)
            {
                 $('#validationModal .modal-title').text('Error'); // Set the title of the modal
                   $('#validationModal .modal-body').text(data.error); // Set the message in the body of the modal
                   $('#validationModal').modal('show'); // Show the modal
            }
            else
            {

                // Check if data contains the expected properties
                if (data.traineeId && data.traineeName && data.traineeMarks) {
                    // Set the assignment description and total score
                    $('#traineeId1').text(data.traineeId);
                    $('#traineeName1').text(data.traineeName);

                    // Clear any previous content in the assignmentMarksByTraineeId table body
                    $('#assignmentMarksByTraineeId').empty();

                // Loop through the map and append each key-value pair as a table row
                   $.each(data.traineeMarks, function(key, value){
                  // Extract the relevant parts from the key string
                     var descriptionMatch = key.match(/description='([^']+)'/);
                     var totalScoreMatch = key.match(/totalScore=([\d.]+)/);

                     // Check if the matches were successful
                     if (descriptionMatch && totalScoreMatch) {
                         var description = descriptionMatch[1];
                         var totalScore = totalScoreMatch[1];

                         // Format the marksObtained as JSON
                         var marks = JSON.stringify(value); // 'value' is the marksObtained
                         $('#assignmentMarksByTraineeId').append('<tr><td>' + description + '</td><td>' + totalScore + '</td><td>' + marks + '</td></tr>');
                        }
                       });
                       // Show the modal
                       $('#assignmentDetailsModal').modal('show');
                   } else {
                       console.error('Data missing expected properties:', data);
                       alert('Error: Data does not contain expected properties.');
                   }
                 }
               },
                error: function(xhr, status, error) {
                    // Improved error handling
                    console.error("Error fetching assignment details:", {
                        status: status,
                        error: error,
                        responseText: xhr.responseText,
                        responseStatus: xhr.status
                    });
                    alert('Error fetching assignment details. Status: ' + xhr.status + ', Error: ' + error);
                },
                 complete: function() {
                            // Hide the spinner regardless of success or error
                            loading.style.display = 'none';
                        }
            });

          }

          function viewAssessmentMarksDetails(assignmentId) {
             var loading = document.getElementById('loading');

              loading.style.display = 'block'; // Show the spinner
              $.ajax({
                  url: "${pageContext.request.contextPath}/trainer/getTraineeAssessment",
                  type: "GET",
                  data: {traineeId: assignmentId},
                  success: function(data) {
                    if(data.error)
                    {
                         $('#validationModal .modal-title').text('Error'); // Set the title of the modal
                           $('#validationModal .modal-body').text(data.error); // Set the message in the body of the modal
                           $('#validationModal').modal('show'); // Show the modal
                    }
                    else
                    {
                            // Check if data contains the expected properties
                      if (data.traineeId && data.traineeName && data.traineeMarks) {
                          // Set the assignment description and total score
                          $('#traineeId2').text(data.traineeId);
                          $('#traineeName2').text(data.traineeName);

                          // Clear any previous content in the assessmentMarksByTraineeId table body
                          $('#assessmentMarksByTraineeId').empty();

                      // Loop through the map and append each key-value pair as a table row
                         $.each(data.traineeMarks, function(key, value){
                        // Extract the relevant parts from the key string
                           var descriptionMatch = key.match(/subTopic='([^']+)'/);
                           var totalScoreMatch = key.match(/totalScore=([\d.]+)/);

                           // Check if the matches were successful
                           if (descriptionMatch && totalScoreMatch) {
                               var description = descriptionMatch[1];
                               var totalScore = totalScoreMatch[1];

                               // Format the marksObtained as JSON
                               var marks = JSON.stringify(value); // 'value' is the marksObtained
                               $('#assessmentMarksByTraineeId').append('<tr><td>' + description + '</td><td>' + totalScore + '</td><td>' + marks + '</td></tr>');
                              }
                             });
                             // Show the modal
                             $('#assessmentDetailsModal').modal('show');
                         } else {
                             console.error('Data missing expected properties:', data);
                             alert('Error: Data does not contain expected properties.');
                         }
                    }
                  },
                  error: function(xhr, status, error) {
                      // Improved error handling
                      console.error("Error fetching assessment details:", {
                          status: status,
                          error: error,
                          responseText: xhr.responseText,
                          responseStatus: xhr.status
                      });
                      alert('Error fetching assessment details. Status: ' + xhr.status + ', Error: ' + error);
                  },
                   complete: function() {
                              // Hide the spinner regardless of success or error
                              loading.style.display = 'none';
                          }
              });

            }

            function viewAssessmentDetails(assessmentId) {
            var loading = document.getElementById('loading');
            loading.style.display = 'block'; // Show the spinner
                $.ajax({
                    url: "${pageContext.request.contextPath}/trainer/viewTraineesAssessmentMarks",
                    type: "GET",
                    data: { assessmentId: assessmentId },
                    success: function(data) {
                        // Set the assessment description and total score

                        if(data.error)
                        {
                             $('#validationModal .modal-title').text('Error'); // Set the title of the modal
                               $('#validationModal .modal-body').text(data.error); // Set the message in the body of the modal
                               $('#validationModal').modal('show'); // Show the modal
                        }

                        else
                        {
                            $('#assessmentDescription').text(data.assessmentDescription);
                            $('#totalScore1').text(data.assessmentTotalScore);


                            // Clear any previous content in the marksObtainedByName table body
                            $('#marksObtainedByName1').empty();

                            // Loop through the map and append each key-value pair as a table row
                            $.each(data.assessmentMarksObtainedByName, function(name, score) {

                                $('#marksObtainedByName1').append('<tr><td>' + name + '</td><td>' + score + '</td></tr>');
                            });

                            // Show the modal after content is populated
                            $('#assessmentDetailsModal1').modal('show');
                        }

                    },
                    error: function(xhr, status, error) {
                        console.error("Error fetching assessment details:", error);
                    },
                    complete: function() {
                      // Hide the spinner regardless of success or error
                      loading.style.display = 'none';
                    }
                });
            }

            function viewAssignmentDetails(assignmentId) {
            var loading = document.getElementById('loading');
            loading.style.display = 'block'; // Show the spinner
            $.ajax({
                url: "${pageContext.request.contextPath}/trainer/viewTraineesAssignmentMarks",
                type: "GET",
                data: { assignmentId: assignmentId },
                success: function(data) {
                    // Set the assignment description and total score

                    if(data.error)
                    {
                         $('#validationModal .modal-title').text('Error'); // Set the title of the modal
                           $('#validationModal .modal-body').text(data.error); // Set the message in the body of the modal
                           $('#validationModal').modal('show'); // Show the modal

                    }
                    else
                    {
                        $('#assignmentDescription').text(data.assignmentDescription);
                        $('#totalScore').text(data.totalScore);

                        // Clear any previous content in the marksObtainedByName table body
                        $('#marksObtainedByName').empty();

                        // Loop through the map and append each key-value pair as a table row
                        $.each(data.marksObtainedByName, function(name, score) {
                            $('#marksObtainedByName').append('<tr><td>' + name + '</td><td>' + score + '</td></tr>');
                        });

                        // Show the modal
                        $('#assignmentDetailsModal1').modal('show');
                    }
                },
                error: function(xhr, status, error) {
                    console.error("Error fetching assignment details:", error);
                },
                complete: function() {
                  // Hide the spinner regardless of success or error
                  loading.style.display = 'none';
                }
            });
        }
        </script>

        <script>
        function viewAttendance(batchId, plannedDate) {
            var loading = document.getElementById('loading');
            loading.style.display = 'block'; // Show the spinner

           $.ajax({
               url: `${pageContext.request.contextPath}/trainer/getAttendancePerDay`,
               type: "GET",
               data: {
                   batchId1: batchId,
                   plannedDate1: plannedDate // Ensure plannedDate is properly formatted
               },
               success: function(data) {
                   // Hide spinner before showing modals
                   loading.style.display = 'none';

                   // Check if data contains the expected properties
                   if (data.error) {
                       $('#validationModal .modal-title').text('Error'); // Set the title of the modal
                       $('#validationModal .modal-body').text(data.error); // Set the message in the body of the modal
                       $('#validationModal').modal('show'); // Show the modal
                   } else {
                       if (data.traineeList && data.attendanceDto && data.attendanceStatus) {
                           var key = data.attendanceDto;
                           var date = key.attendanceDate.year + '-0' + key.attendanceDate.monthValue + '-0' + key.attendanceDate.dayOfMonth;
                           $('#attendanceDate').text(date);

                           // Parse attendanceStatus into an object
                           var attendanceMap = {};
                           data.attendanceStatus.forEach(function(status) {
                               var parts = status.split(' ');
                               var employeeId = parts[0];
                               var statusValue = parts[1];
                               attendanceMap[employeeId] = statusValue;
                           });

                           // Append each trainee with their attendance status
                           $('#attendanceStatusByDate').empty(); // Clear existing content if any
                           $.each(data.traineeList, function(index, trainee) {
                               var attendanceStatus = attendanceMap[trainee.employeeId] || 'No data';
                               $('#attendanceStatusByDate').append('<tr><td>' + trainee.employeeId +'</td><td>' + trainee.traineeName + '</td><td>' + attendanceStatus + '</td></tr>');
                           });
                       }

                       // Show the modal
                       $('#attendanceDetailsModalPerDay').modal('show');
                   }
               },
               error: function(xhr, status, error) {
                   // Hide spinner on error
                   loading.style.display = 'none';

                   // Improved error handling
                   console.error("Error fetching attendance details:", {
                       status: status,
                       error: error,
                       responseText: xhr.responseText,
                       responseStatus: xhr.status
                   });
                   alert('Error fetching attendance details. Status: ' + xhr.status + ', Error: ' + error);
               }
           });

        }

        </script>





</body>
</html>
