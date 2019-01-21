<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="webjars/bootstrap/4.1.3/css/bootstrap.min.css"/>
    <script src="webjars/jquery/3.0.0/jquery.min.js"></script>
    <script src="webjars/bootstrap/4.1.3/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="<c:url value="/resources/styles/header.css"/>">
</head>
<body>
<div id="cart" class="minicart">My cart: ${cartItemsAmount} items $${cartItemsPrice}</div>
</body>
</html>
