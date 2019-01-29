<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>404</title>
</head>
<body>
Ooops, page not found
<form action="<c:url value="productList"/>">
    <button>Back to Product List</button>
</form>
</body>
</html>
