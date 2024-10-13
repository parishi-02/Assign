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

        .card-header {
            position: relative;
        }

        .btn-custom-position {
            position: absolute;
            right: 1rem;
            top: 1rem;
        }

        .remove-row-btn {
            color: #dc3545; /* Bootstrap danger color */
            border: none;
            background: none;
            font-size: 0.9rem;
            cursor: pointer;
        }

        .remove-row-btn:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>

    <%@ include file="navbar.jsp" %>

    <div class="container mt-5" style="max-width: 1200px;">
        <div class="card shadow-lg">
            <div class="card-header">
                <h2 class="mb-0 text-center text-md-left">Add Holidays</h2>
                <button class="btn btn-primary btn-custom-position" onclick="addHolidayRow();">
                    Create New Holiday
                </button>
            </div>
            <div class="card-body">
                <form action="createHolidays" method="POST" >
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
                                    <select name="holidayType" class="form-control" required>
                                        <option value="eventual">Eventual</option>
                                        <option value="optional">Optional</option>
                                        <option value="NA">NA</option>
                                    </select>
                                </td>
                                <td>
                                    <input type="text" name="holidayName" class="form-control" required />
                                </td>
                                <td>
                                    <input type="date" name="holidayDate" class="form-control" required />
                                </td>
                                <td>
                                    <button type="button" class="btn btn-secondary remove-row-btn" onclick="removeHolidayRow(this)">Remove</button>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                    <div class="d-flex justify-content-end mt-3">
                        <button type="submit" class="btn btn-primary mr-2">Save</button>
                        <button type="button" id="cancelButton" class="btn btn-secondary">Cancel</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script>
        function addHolidayRow() {
            var table = document.getElementById("holidaysTable").getElementsByTagName('tbody')[0];
            var rowCount = table.rows.length;
            var newRow = table.insertRow();

            // Holiday Type
            var cell1 = newRow.insertCell(0);
            cell1.innerHTML = `<select name="holidayType" class="form-control" required>
                                   <option value="eventual">Eventual</option>
                                   <option value="optional">Optional</option>
                                   <option value="NA">NA</option>
                               </select>`;

            // Holiday Name
            var cell2 = newRow.insertCell(1);
            cell2.innerHTML = `<input type="text" name="holidayName" class="form-control" required />`;

            // Holiday Date
            var cell3 = newRow.insertCell(2);
            cell3.innerHTML = `<input type="date" name="holidayDate" class="form-control" required />`;

            // Remove Button
            var cell4 = newRow.insertCell(3);
            cell4.innerHTML = `<button type="button" class="btn btn-secondary remove-row-btn" onclick="removeHolidayRow(this)">Remove</button>`;
        }

        function removeHolidayRow(button) {
            var row = button.parentNode.parentNode;
            row.parentNode.removeChild(row);
        }
    </script>

    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
</body>
</html>
