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
<p>
<div class="minicart">
    <c:if test="${userName == null}">
        <form action="<c:url value='/login'/>">
            <button>login</button>
        </form>
    </c:if>
    <c:if test="${userName != null}">
        ${userName}
        <form action="<c:url value='/security_logout'/>">
            <button>logout</button>
        </form>
        <c:if test="${userName == 'admin'}">
            <form action="<c:url value='/admin/orders'/>">
                <button>admin</button>
            </form>
        </c:if>
    </c:if>
</div>

</p>
<div id="cart" class="minicart">My cart: ${cartItemsAmount} items $${cartItemsPrice}</div>
</body>
</html>
