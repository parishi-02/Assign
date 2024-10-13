<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Trainer Registration Form</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">


</head>

<body>
    <%@ include file="navbar.jsp" %>
    <%@ include file="common-imports.jsp" %>

    <div class="container mt-4">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card shadow">
                    <form:form action="${pageContext.request.contextPath}/admin/registerTrainer" method="POST" modelAttribute="trainerDto">
                        <div class="card-body" id="innerContainer">
                            <div class="d-flex justify-content-between align-items-center mb-4">
                                <h2 class="mb-0">Trainer Registration Form</h2>
                                <div>
                                    <button id = "saveButton" type="submit" class="btn btn-primary mr-2">Save</button>
                                    <button type="button" class="btn btn-secondary" onclick="goBack()">Cancel</button>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="trainerType">Select Trainer Type<span class="text-danger">*</span>:</label>
                                <form:select class="form-control" id="trainerType" path="trainerType" placeholder="Select Trainer Type">
                                    <form:option value="" disabled="disabled" selected="selected">Select Trainer Type</form:option>
                                    <form:option value="internal">Internal</form:option>
                                    <form:option value="external">External</form:option>
                                </form:select>
                                <form:errors path="trainerType" cssClass="text-danger" />
                            </div>
                            <div class="form-group">
                                <label for="trainerId">Enter Trainer Id <span class="text-danger">*</span>:</label>
                                <form:input type="number" class="form-control" id="trainerId" path="trainerId" placeholder="Enter Trainer Id" required="true" min="10000"/>
                                <form:errors path="trainerId" cssClass="text-danger" />
                            </div>
                            <div class="form-group">
                                <label for="trainerName">Enter Trainer Name <span class="text-danger">*</span>:</label>
                                <form:input type="text" class="form-control" id="trainerName" path="trainerName" required="true" placeholder="Enter Trainer Name"/>
                                <form:errors path="trainerName" id="trainerNameErrors" cssClass="text-danger" />
                            </div>
                            <div class="form-group">
                                <label for="trainerEmail">Enter Email<span class="text-danger">*</span>:</label>
                                <form:input type="email" class="form-control" id="trainerEmail" path="trainerEmail" required="true" placeholder="Enter Email"/>
                                <div id="emailError" class="invalid-feedback">Enter a valid email address.</div>
                                <form:errors path="trainerEmail" cssClass="text-danger" />
                            </div>

                            <div class="form-group">
                                <label for="trainerContactNo">Contact No.<span class="text-danger">*</span>:</label>
                                <form:input type="text" class="form-control" id="trainerContactNo" path="trainerContactNo" required="true" placeholder="Enter Contact No."/>
                                <form:errors path="trainerContactNo" id="trainerContactNoErrors" cssClass="text-danger" />
                            </div>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
    </div>
    <script>
        $(document).ready(function() {
            // Function to validate form before submission
            function validateForm() {
                var isValid = true;

                // Additional validation for email based on trainerType
                var trainerType = $('#trainerType').val();
                var trainerEmail = $('#trainerEmail').val();

                if (trainerType === 'internal' && !isValidInternalEmail(trainerEmail)) {
                    $('#trainerEmail').addClass('is-invalid');
                    $('#emailError').show(); // Show error message
                    isValid = false;
                } else if (trainerType === 'external' && !isValidExternalEmail(trainerEmail)) {
                    $('#trainerEmail').addClass('is-invalid');
                    $('#emailError').show(); // Show error message
                    isValid = false;
                } else {
                    $('#emailError').hide(); // Hide error message if email is valid
                }

                return isValid;
            }

            // Function to validate internal email format
            function isValidInternalEmail(email) {
                var internalEmailRegex = /^[a-zA-Z0-9._%+-]+@gmail\.com$/;
                return internalEmailRegex.test(email);
            }

            // Function to validate external email format
            function isValidExternalEmail(email) {
                var externalEmailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
                return externalEmailRegex.test(email);
            }

            // Form submit event handler
            $('form').submit(function() {
                return validateForm();
            });
        });
    </script>

    </body>
</html>
