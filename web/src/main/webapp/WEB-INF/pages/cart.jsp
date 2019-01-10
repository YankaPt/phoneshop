<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!doctype html>
<html>
<head>
    <link rel="stylesheet" href="webjars/bootstrap/4.1.3/css/bootstrap.min.css"/>
    <script src="webjars/jquery/3.0.0/jquery.min.js"></script>
    <script src="webjars/bootstrap/4.1.3/js/bootstrap.min.js"></script>
    <script src="<c:url value="/resources/scripts/CartPage_scripts.js"/>"></script>
    <title>Product list</title>
</head>
<body>
<div id="cart" style="text-align: right">My cart: ${cartItemsAmount} items $${cartItemsPrice}</div>
<p>
    Hello from Cart!
</p>
<form action="<c:url value="productList"/>">
    <button>Back to Product List</button>
</form>
<p>

<form method="POST">
    <div class="table-responsive">
        <table id="table" class="table table-striped table-bordered table-hover table-sm sortable" cellspacing="0"
               width="100%">
            <thead>
            <tr>
                <th>Brand</th>
                <th>Model</th>
                <th>Color</th>
                <th>Display size</th>
                <th>Price</th>
                <th>Quantity</th>
                <th>Action</th>
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
                    <td>$ ${phone.price}</td>
                    <td>
                        <input type="text" class="quantityField" id=quantity${phone.id} name="quantity${phone.id}"
                               style="text-align: right" value="${cartItems.get(status.index).quantity}"/>
                        <br>
                        <label for=quantity${phone.id} id=label${phone.id}></label>
                    </td>
                    <td>
                        <input type="hidden" disabled id="delete${phone.id}" name="delete">
                        <input type="submit" onclick="deleteItem(${phone.id})" value="Delete"/>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</form>
<div class="row">
    <div class="col-md-11"></div>
    <div class="col-md-1">
        <form:form method="PUT">
            <input type="submit" id="updateSubmit" onclick="update()" value="update"/>
        </form:form>
        <form action="<c:url value="order"/>">
            <button>To Order!</button>
        </form>
    </div>
</div>
</p>
</body>
</html>