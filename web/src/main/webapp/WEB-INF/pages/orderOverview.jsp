<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!doctype html>
<html>
<head>
    <title>Order ${order.id}</title>
    <link rel="stylesheet" href="webjars/bootstrap/4.1.3/css/bootstrap.min.css"/>
</head>
<body>
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
        <c:forEach var="orderItem" items="${order.orderItems}" varStatus="status">
            <tr>
                <td>${orderItem.phone.brand}</td>
                <td>
                        ${orderItem.phone.model}
                    <br>
                    <a href="productDetails?phoneId=${orderItem.phone.id}">More...</a>
                </td>
                <td>
                    <c:forEach var="color" items="${orderItem.phone.colors}">
                        ${color}
                    </c:forEach>
                </td>
                <td>${orderItem.phone.displaySizeInches}"</td>
                <td>${orderItem.quantity}</td>
                <td>$ ${orderItem.phone.price.multiply(orderItem.quantity)}</td>
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