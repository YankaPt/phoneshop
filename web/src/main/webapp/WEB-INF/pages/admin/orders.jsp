<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Orders</title>
    <link rel="stylesheet" href="../webjars/bootstrap/4.1.3/css/bootstrap.min.css"/>
</head>
<body>
<H4>
    Orders
</H4>
<form action="<c:url value="../productList"/>">
    <button>Back to Product List</button>
</form>
<div class="table-responsive">
    <table id="table" class="table table-striped table-bordered table-hover table-sm" cellspacing="0"
           width="100%">
        <thead>
        <tr>
            <th>Order number</th>
            <th>Customer</th>
            <th>Phone</th>
            <th>Address</th>
            <th>Total price</th>
            <th>Status</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="order" items="${orders}">
            <tr>
                <td>
                    <a href="orders/${order.id}">${order.id}</a>
                </td>
                <td>${order.firstName} ${order.lastName}</td>
                <td>${order.contactPhoneNo}</td>
                <td>${order.deliveryAddress}</td>
                <td>$ ${order.totalPrice}</td>
                <td>${order.status}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
