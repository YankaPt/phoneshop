<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!doctype html>
<html>
<head>
    <link rel="stylesheet" href="webjars/bootstrap/4.1.3/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="<c:url value="/resources/styles/orderPage_styles.css"/>">
    <script src="webjars/jquery/3.0.0/jquery.min.js"></script>
    <script src="webjars/bootstrap/4.1.3/js/bootstrap.min.js"></script>
    <title>Product list</title>
</head>
<body>
<p>
    Hello from Order!
</p>
<form action="<c:url value="cart"/>">
    <button>Back to Cart</button>
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
            <c:forEach var="phone" items="${phones}" varStatus="status">
                <tr>
                    <td>${phone.brand}</td>
                    <td>
                            ${phone.model}
                        <br>
                        <a href="productDetails?phoneId=${phone.id}">More...</a>
                    </td>
                    <td>
                        <c:forEach var="color" items="${phone.colors}">
                            ${color}
                        </c:forEach>
                    </td>
                    <td>${phone.displaySizeInches}"</td>
                    <td>${cartItems.get(status.index).quantity}</td>
                    <td>$ ${phone.price.multiply(cartItems.get(status.index).quantity)}</td>
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
                <td>${cartItemsPrice}</td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
<form:form method="post" modelAttribute="customer">
    <table>
        <tr>
            <td><form:label path="firstName">First name*</form:label></td>
            <td><form:input path="firstName"/></td>
            <td><c:if test='${errors.hasFieldErrors("firstName")}'><span class="error-message">${errors.getFieldError("firstName").code}</span></c:if></td>
        </tr>
        <tr>
            <td><form:label path="lastName">Second name*</form:label></td>
            <td><form:input path="lastName"/></td>
            <td><c:if test='${errors.hasFieldErrors("lastName")}'><span class="error-message">${errors.getFieldError("lastName").code}</span></c:if></td>
        </tr>
        <tr>
            <td><form:label path="address">Address*</form:label></td>
            <td><form:input path="address"/></td>
            <td><c:if test='${errors.hasFieldErrors("address")}'><span class="error-message">${errors.getFieldError("address").code}</span></c:if></td>
        </tr>
        <tr>
            <td><form:label path="contactNumber">
                Contact Number*</form:label></td>
            <td><form:input path="contactNumber"/></td>
            <td><c:if test='${errors.hasFieldErrors("contactNumber")}'><span class="error-message">${errors.getFieldError("contactNumber").code}</span></c:if></td>
        </tr>
        <tr>
            <td><form:label path="additionalInformation">
                Additional information</form:label></td>
            <td><form:textarea path="additionalInformation"/></td>
        </tr>
        <tr>
            <td><input type="submit" value="Submit"/></td>
        </tr>
    </table>
</form:form>
</p>
</body>
</html>