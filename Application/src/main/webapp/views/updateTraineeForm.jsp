<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored = "false" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Update Employee Details</title>
</head>

<body>
<%@ include file="navbar.jsp" %>
<%@ include file="common-imports.jsp" %>

<form:form action="${pageContext.request.contextPath}/admin/updatingTrainee" modelAttribute="traineeDTO" method="post" >
    <div class="container mt-5 card shadow">
        <div class="d-flex justify-content-between align-items-center mt-4">
            <h2>Update Trainee Details</h2>
            <div>
                 <button type="submit" id="updateButton" class="btn btn-primary">Save</button>
                 <button type="button" class="btn btn-secondary" onclick="goBack()">Cancel</button>
            </div>
        </div>
        <div class="row mt-4">
            <div class="col-md-6">
                <div class="form-group">
                   <label for="employeeId">Trainee ID <span class="text-danger">*</span>:</label>
                   <form:input path="employeeId" name="employeeId" type="text" id="employeeId" class="form-control" disabled="true" />
                   <form:errors path="employeeId" cssClass="text-danger" />
                </div>
                <form:input path="employeeId" name="employeeId" type="text" id="employeeId" class="form-control" hidden="true" />

               <div class="form-group">
                   <label for="officeEmailId">Update Office Email Id <span class="text-danger">*</span>:</label>
                   <form:input path="officeEmailId" type="email" id="officeEmailId" class="form-control" placeholder="Enter office email id" disabled="true"/>
                   <form:errors path="officeEmailId" cssClass="text-danger" />
                   <form:input path="officeEmailId" name="officeEmailId" type="text" id="officeEmailId" class="form-control" hidden="true" />
               </div>

               <div class="form-group">
                  <label for="doj">Update Date Of Joining <span class="text-danger">*</span>:</label>
                  <form:input path="doj" id="doj" type="date" class="form-control" placeholder="Enter date of joining" />
                  <form:errors path="doj" cssClass="text-danger" />
               </div>

               <div class="form-group">
                   <label for="college">Update College <span class="text-danger">*</span>:</label>
                   <form:input path="college" id="college" type="text" class="form-control" placeholder="Enter college" />
                   <form:errors path="college" cssClass="text-danger" />
               </div>
           </div>
           <div class="col-md-6">
                <div class="form-group">
                   <label for="traineeName">Update Trainee Name <span class="text-danger">*</span>:</label>
                   <form:input path="traineeName" name="traineeName" type="text" id="traineeName" class="form-control" placeholder="Enter trainee name" />
                   <form:errors path="traineeName" cssClass="text-danger" />
                </div>
               <div class="form-group">
                   <label for="gender">Update Gender <span class="text-danger">*</span>:</label>
                   <form:select path="gender" id="gender" class="form-control">
                       <form:option value="">${traineeDTO.getGender()}</form:option>
                       <form:option value="Male">Male</form:option>
                       <form:option value="Female">Female</form:option>
                       <form:option value="Other">Other</form:option>
                   </form:select>
                   <form:errors path="gender" cssClass="text-danger" />
               </div>
               <div class="form-group">
                   <label for="cellNo">Update Contact No <span class="text-danger">*</span>:</label>
                   <form:input path="cellNo" type="number" id="cellNo" class="form-control" placeholder="Enter cell no" />
                   <form:errors path="cellNo" cssClass="text-danger" />
               </div>
               <div class="form-group">
                   <label for="branch">Update Branch <span class="text-danger">*</span>:</label>
                   <form:input path="branch" type="text" id="branch" class="form-control" placeholder="Enter branch"/>
                   <form:errors path="branch" cssClass="text-danger" />
               </div>
           </div>
        </div>
    </div>
</form:form>
</body>
</html>