<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Trainee Registration Form</title>
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
   </style>
<body>
    <%@ include file="navbar.jsp" %>
    <%@ include file="common-imports.jsp" %>

    <form:form action="${pageContext.request.contextPath}/admin/registerTrainee" method="post" modelAttribute="traineeDTO" >
        <div class="container mt-5 card shadow">
            <div class="d-flex justify-content-between align-items-center mt-4">
                <h2>Trainee Registration Form</h2>
                <div>
                    <button type="submit" id="saveButton" class="btn btn-primary">Save</button>
                    <button type="button"  class="btn btn-primary" data-toggle="modal" data-target="#bulkUploadModal">Bulk Upload</button>
                    <button type="button" class="btn btn-secondary" onclick="goBack()">Cancel</button>
                </div>
            </div>
            <div class="row mt-4">
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="employeeId">Enter Employee Id <span class="text-danger">*</span>:</label>
                        <form:input path="employeeId" type="number" id="employeeId" class="form-control" placeholder="Enter employee id" required="true" min="10000"/>
                        <form:errors path="employeeId" cssClass="text-danger" />
                    </div>
                    <div class="form-group">
                        <label for="officeEmailId">Enter Office Email Id <span class="text-danger">*</span>:</label>
                        <form:input path="officeEmailId" type="email" id="officeEmailId" class="form-control" placeholder="Enter office email id" required="true" />
                        <form:errors path="officeEmailId" cssClass="text-danger" />
                    </div>
                     <div class="form-group">
                        <label for="doj">Enter Date Of Joining <span class="text-danger">*</span>:</label>
                        <form:input path="doj" id="doj" type="date" class="form-control" placeholder="Enter date of joining" required="true" />
                        <form:errors path="doj" cssClass="text-danger" />
                    </div>
                    <div class="form-group">
                        <label for="college">Enter College <span class="text-danger">*</span>:</label>
                        <form:input path="college" id="college" type="text" class="form-control" placeholder="Enter college" required="true" />
                        <form:errors path="college" cssClass="text-danger" />
                    </div>
                    <div class="form-group">
                         <label for="tenthMarks">Enter Tenth Marks (out of 100) <span class="text-danger">*</span>:</label>
                         <form:input path="tenthMarks" id="tenthMarks" type="text" class="form-control" placeholder="Enter tenth marks" required="true" min="1"/>

                     </div>
                     <div class="form-group">
                         <label for="remarks">Enter Remarks <span class="text-danger">*</span>:</label>
                         <form:input path="remarks" id="remarks" type="text" class="form-control" placeholder="Enter remarks" required="true"/>
                         <form:errors path="remarks" cssClass="text-danger" />
                     </div>
                </div>

                <div class="col-md-6">
                    <div class="form-group">
                        <label for="traineeName">Enter Trainee Name <span class="text-danger">*</span>:</label>
                        <form:input path="traineeName" name="traineeName" type="text" id="traineeName" class="form-control" placeholder="Enter trainee name" required="true" />
                        <form:errors path="traineeName" cssClass="text-danger" />
                    </div>
                    <div class="form-group">
                        <label for="gender">Enter Gender <span class="text-danger">*</span>:</label>
                        <form:select path="gender" id="gender" class="form-control" required="true">
                            <form:option value="">Select gender</form:option>
                            <form:option value="Male">Male</form:option>
                            <form:option value="Female">Female</form:option>
                            <form:option value="Other">Other</form:option>
                        </form:select>
                        <form:errors path="gender" cssClass="text-danger" />
                    </div>
                    <div class="form-group">
                        <label for="cellNo">Enter Contact No <span class="text-danger">*</span>:</label>
                        <form:input path="cellNo" type="number" id="cellNo" class="form-control" placeholder="Enter contact no" required="true" />
                        <form:errors path="cellNo" cssClass="text-danger" />
                    </div>
                    <div class="form-group">
                        <label for="branch">Enter Branch <span class="text-danger">*</span>:</label>
                        <form:input path="branch" type="text" id="branch" class="form-control" placeholder="Enter branch" required="true" />
                        <form:errors path="branch" cssClass="text-danger" />
                    </div>
                    <div class="form-group">
                        <label for="twelvethMarks">Enter Twelveth Marks (out of 100) <span class="text-danger">*</span>:</label>
                        <form:input path="twelvethMarks" id="twelvethMarks" type="text" class="form-control" placeholder="Enter twelveth marks" required="true" min="1"/>

                    </div>
                </div>
            </div>
        </div>
    </form:form>

    <!-- Bulk Upload Modal -->
    <div class="modal fade" id="bulkUploadModal" tabindex="-1" aria-labelledby="bulkUploadModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="bulkUploadModalLabel">Bulk Upload Trainees</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form action="bulkUpload" method="POST" enctype="multipart/form-data" onsubmit="showPopup()">
                        <div class="form-group">
                            <label for="file">Choose Excel File</label>
                            <input type="file" class="form-control-file" id="file" name="file" accept=".xlsx, .xls" required>
                        </div>
                        <button type="submit" class="btn btn-primary" id="upload">Upload</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
     <!-- Spinner for form submission -->
        <div id="loading">
            <div class="spinner"></div>
        </div>

    <script>
        var today = new Date().toISOString().split('T')[0];
        document.getElementById("doj").setAttribute('max', today);

    </script>

</body>
</html>
