<%@page isELIgnored = "false" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Error page</title>
</head>
<body>
<%@ include file="navbar.jsp" %>
<c:choose>
   <c:when test="${errorMessage!=null}">
       ${errorMessage}
   </c:when>
   <c:otherwise>
        An Unexpected Error Occured. Please Contact your System Administrator
   </c:otherwise>
</c:choose>
</body>
</html>