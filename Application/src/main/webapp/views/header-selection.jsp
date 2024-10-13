<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Select Columns for Report</title>
    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.11.5/css/jquery.dataTables.min.css">
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
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
        .checkbox-container {
            margin-top: 20px;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
        }
        #loading {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5);
            z-index: 9999;
        }

        .spinner {
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            border: 5px solid #f3f3f3;
            border-top: 5px solid #3498db;
            border-radius: 50%;
            width: 50px;
            height: 50px;
            animation: spin 1s linear infinite;
        }

        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }

        /* Styling for radio buttons and checkboxes */
        .form-check-label {
            margin-right: 15px;
        }

        .form-check-input {
            margin-right: 10px;
        }

        .form-group {
            margin-bottom: 20px;
        }

        .radio-label, .checkbox-label {
            font-size: 16px;
            padding: 5px 10px;
            display: inline-block;
            border: 1px solid #ccc;
            border-radius: 4px;
            margin-bottom: 5px;
        }

        .radio-label:hover, .checkbox-label:hover {
            background-color: #f1f1f1;
            cursor: pointer;
        }

        .btn {
            margin-right: 10px;
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

           .form-check-group {
                display: flex;
                flex-wrap: wrap; /* Allows wrapping if there's not enough space */
                gap: 15px; /* Space between items */
            }

            .custom-radio, .custom-checkbox {
                accent-color: #007bff; /* Custom color for radio buttons and checkboxes */
            }

            .custom-label {
                color: black; /* Custom text color */
                font-size: 16px; /* Adjust font size */
            }

            .card-header {
                font-size: 1.25rem; /* Adjust header font size */
            }

            .card-body {
                padding: 1.25rem; /* Adjust body padding */
            }

            .form-check-inline {
                display: flex;
                align-items: center;
            }
    </style>
</head>
<body>
    <%@ include file="navbar.jsp" %>
    <br>
    <br>
    <div class="container mt-3">
        <div class="checkbox-container">
            <h2>Select Columns for Report</h2>
            <hr>
            <form action="${pageContext.request.contextPath}/admin/generateReport" method="post" onsubmit="return validateColumnSelection()">
                <c:forEach var="trainee" items="${trainees}">
                    <input type="hidden" name="trainees" value="${trainee}"/>
                </c:forEach>
                <input type="hidden" name="batchId" value="${batchId}"/>


              <div class="container mt-4">
                  <!-- Radio Buttons Section -->
                  <div class="card mb-3">
                      <div class="card-header bg-primary text-white">
                          <h5 class="mb-0">Select Topic</h5>
                      </div>
                      <div class="card-body">
                          <div class="form-check-group">
                              <c:forEach var="topic" items="${availableTopics}">
                                  <div class="form-check form-check-inline">
                                      <input class="form-check-input custom-radio" type="radio" name="selectedTopic" id="topic${topic}" value="${topic}">
                                      <label class="form-check-label custom-label" for="topic${topic}">${topic}</label>
                                  </div>
                              </c:forEach>
                          </div>
                      </div>
                  </div>

                  <!-- Checkboxes Section -->
                  <div class="card">
                      <div class="card-header bg-primary text-white d-flex justify-content-between align-items-center mb-4">
                          <h5 class="mb-0">Select Attribute</h5>
                          <div>
                            <input type="checkbox" id="selectAll"> Select All
                          </div>
                      </div>
                      <div class="card-body">
                          <div class="form-check-group">
                              <c:forEach var="column" items="${availableColumns}">
                                  <div class="form-check form-check-inline">
                                      <input type="checkbox" name="selectedColumns" value="${column}" class="form-check-input custom-checkbox"/>
                                      <label class="form-check-label custom-label" for="column${column}">${column}</label>
                                  </div>
                              </c:forEach>
                          </div>
                      </div>
                  </div>
              </div>
              <br>

                <div class="form-group">
                    <button type="submit" class="btn btn-primary">Generate Report</button>
                    <button type="button" class="btn btn-secondary" id="cancelButton">Cancel</button>
                </div>
            </form>
        </div>
    </div>

    <!-- Spinner for form submission -->
    <div id="loading">
        <div class="spinner"></div>
    </div>

    <!-- Bootstrap Modal for Alerts -->
    <div class="modal fade" id="alertModal" tabindex="-1" role="dialog" aria-labelledby="alertModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header custom-modal-header">
                    <h5 class="modal-title" id="alertModalLabel">Error</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body" id="alertMessage">
                    <!-- Alert message will be inserted here -->
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" data-dismiss="modal" style="background-color: #2382d7; color: black; font-weight:bold;">Close</button>
                </div>
            </div>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.datatables.net/1.11.5/js/jquery.dataTables.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <script>

      $(document).ready(function() {
                $('#selectAll').on('change', function() {
                    var checked = $(this).prop('checked');
                    $('input[name="selectedColumns"]').prop('checked', checked);
                });
            });
        var cancelButton = document.getElementById('cancelButton');
        var loading = document.getElementById('loading');
        var alertModal = new bootstrap.Modal(document.getElementById('alertModal'));
        var alertMessage = document.getElementById('alertMessage');

        cancelButton.addEventListener('click', function() {
            loading.style.display = 'block'; // Show the spinner
            setTimeout(function() {
                window.history.back(); // Navigate back after delay
            }, 100); // 100 milliseconds delay to ensure spinner is visible
        });

        // Function to show spinner on form submission
        function validateColumnSelection() {
            var checkboxes = document.querySelectorAll('input[name="selectedColumns"]:checked');
            var radioButtons = document.querySelectorAll('input[name="selectedTopic"]:checked');
            if (radioButtons.length === 0) {
                alertMessage.innerHTML = 'Please select a topic for the report.';
                alertModal.show(); // Show the modal
                return false;
            }
            if (checkboxes.length === 0) {
                alertMessage.innerHTML = 'Please select at least one column for the report.';
                alertModal.show(); // Show the modal
                return false;
            }

            return true;
        }
    </script>
</body>
</html>
