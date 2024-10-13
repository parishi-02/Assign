<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>Trainer List</title>
    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.11.5/css/jquery.dataTables.min.css">
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/common.css">
</head>
<body>
    <%@ include file="navbar.jsp"%>
    <%@ include file="common-imports.jsp" %>

<div class="container mt-4">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="card shadow">
                <form:form modelAttribute="trainerDTO" method="post" action="${pageContext.request.contextPath}/admin/updatingTrainer" enctype="multipart/form-data">
                    <div class="card-body" id="innerContainer">
                        <div class="d-flex justify-content-between align-items-center mb-4">
                            <h2 class="mb-0">Update Trainer</h2>
                            <div>
                                <button id = "saveButton" type="submit" class="btn btn-primary mr-2">Update</button>
                                <button type="button" class="btn btn-secondary" onclick="goBack()">Cancel</button>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="trainerType">Trainer Type<span class="text-danger">*</span>:</label>
                            <form:input path="trainerType" class="form-control" readonly="true"/>
                        </div>
                        <div class="form-group">
                            <label for="trainerId">Trainer ID:</label>
                            <form:input path="trainerId" class="form-control" readonly="true"/>
                        </div>
                        <div class="form-group">
                            <label for="trainerName">Enter Trainer Name <span class="text-danger">*</span>:</label>
                            <form:input type="text" class="form-control" id="trainerName" path="trainerName" required="true" placeholder="Enter Trainer Name"/>
                            <form:errors path="trainerName" cssClass="text-danger" />
                        </div>
                        <div class="form-group">
                            <label for="trainerEmail">Enter Email<span class="text-danger">*</span>:</label>
                            <c:if test="${trainerDTO.trainerType == 'internal'}">
                                <form:input type="email" class="form-control" id="trainerEmail" path="trainerEmail" readonly="true"/>
                            </c:if>
                            <c:if test="${trainerDTO.trainerType != 'internal'}">
                                <form:input type="email" class="form-control" id="trainerEmail" path="trainerEmail" required="true" placeholder="Enter Email"/>
                            </c:if>
                            <form:errors path="trainerEmail" cssClass="text-danger" />
                        </div>
                        <div class="form-group">
                            <label for="trainerContactNo">Contact No.<span class="text-danger">*</span>:</label>
                            <form:input type="text" class="form-control" id="trainerContactNo" path="trainerContactNo" required="true" placeholder="Enter Contact No."/>
                            <form:errors path="trainerContactNo" cssClass="text-danger" />
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
        $('form').submit(function() {///;/
            return validateForm();
        });
    });
</script>
</body>
</html>
