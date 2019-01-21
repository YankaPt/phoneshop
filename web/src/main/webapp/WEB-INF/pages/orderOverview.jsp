<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!doctype html>
<html>
<head>
    <title>Product list</title>
</head>
<body>
<jsp:include page="header.jsp"/>
<p>
    Thank you for your Order!
</p>
<p>
    Order number: ${order.id}
</p>
<form action="<c:url value="productList"/>">
    <button>Back to Product list</button>
</form>
<p>

<div class="table-responsive">
    <table id="table" class="table table-striped table-bordered table-hover table-sm" cellspacing="0"
           width="100%">
        <thead>
        <tr>
            <th>Brand</th>
            <th>Model</th>
            <th>Color</th>
            <th>Display size</th>
            <th>Price</th>
            <th>Quantity</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="cartItem" items="${order.orderItems}" varStatus="status">
            <tr>
                <td>${cartItem.phone.brand}</td>
                <td>
                        ${cartItem.phone.model}
                    <br>
                    <a href="productDetails?phoneId=${cartItem.phone.id}">More...</a>
                </td>
                <td>
                    <c:forEach var="color" items="${cartItem.phone.colors}">
                        ${color}
                    </c:forEach>
                </td>
                <td>${cartItem.phone.displaySizeInches}"</td>
                <td>${cartItem.quantity}</td>
                <td>$ ${cartItem.phone.price.multiply(cartItem.quantity)}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<div class="row">
    <div class="col-md-8"></div>
    <div class="col-md-3">
        <table class="table table-bordered table-hover" cellspacing="0">
            <tbody>
            <tr>
                <td>Subtotal price:</td>
                <td>${order.subtotal}</td>
            </tr>
            <tr>
                <td>Delivery price:</td>
                <td>${order.deliveryPrice}</td>
            </tr>
            <tr>
                <td>TOTAL:</td>
                <td>${order.totalPrice}</td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
    <table>
        <tr>
            <td>First name </td>
            <td>${customer.firstName}</td>
        </tr>
        <tr>
            <td>Last name </td>
            <td>${customer.lastName}</td>
        </tr>
        <tr>
            <td>Address </td>
            <td>${customer.address}</td>
        </tr>
        <tr>
            <td>Contact phone </td>
            <td>${customer.contactNumber}</td>
        </tr>
    </table>
<p>${customer.additionalInformation}</p>
</p>
</body>
</html>