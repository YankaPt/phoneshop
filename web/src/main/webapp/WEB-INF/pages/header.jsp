<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="<c:url value="/resources/styles/header.css"/>">
</head>
<body>
<div id="cart" class="minicart">My cart: ${cartItemsAmount} items $${cartItemsPrice}</div>
</body>
</html>
