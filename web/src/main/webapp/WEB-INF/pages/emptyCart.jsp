<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Cart</title>
</head>
<body>
<jsp:include page="header.jsp"/>
Sorry, your cart is empty :(
<form action="<c:url value="productList"/>">
    <button>Back to Product List</button>
</form>
</body>
</html>
