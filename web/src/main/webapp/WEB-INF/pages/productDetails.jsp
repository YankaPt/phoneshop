<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!doctype html>
<html>
<head>
    <script src="<c:url value="/resources/scripts/PLP_scripts.js"/>"></script>
    <title>Product list</title>
</head>
<body>
<jsp:include page="header.jsp"/>
<p>
    Hello from product page!
</p>
<form action="<c:url value="cart"/>">
    <button>to Cart</button>
</form>
<form action="<c:url value="productList"/>">
    <button>Back to Product List</button>
</form>
<div class="row">
    <div class="col-md-5">
        <p><h4>${phone.brand} ${phone.model}</h4></p>
        <img src="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${phone.imageUrl}">
        <p>${phone.description}</p>
            <p>Price: ${phone.price}</p>
            <input type="text" id="addToCartField" value="1">
            <button type="button" onclick="addToCart(${phone.id}, document.getElementById('addToCartField').value, '${pageContext.request.locale.toString()}')">
                Add
                to
            </button>
            <p><label id="label${phone.id}"></label></p>
    </div>
    <div class="col-md-5">
        <h5>Display</h5>
        <table class="table table-bordered table-hover" cellspacing="0"
               width="100%">
            <tbody>
            <tr>
                <td>Size</td>
                <td>${phone.displaySizeInches}"</td>
            </tr>
            <tr>
                <td>Resolution</td>
                <td>${phone.displayResolution}</td>
            </tr>
            <tr>
                <td>Technology</td>
                <td>${phone.displayTechnology}</td>
            </tr>
            <tr>
                <td>Pixel density</td>
                <td>${phone.pixelDensity}</td>
            </tr>
            </tbody>
        </table>
        <h5>Dimensions & weight</h5>
        <table class="table table-bordered table-hover" cellspacing="0"
               width="100%">
            <tbody>
            <tr>
                <td>Length</td>
                <td>${phone.lengthMm}mm</td>
            </tr>
            <tr>
                <td>Width</td>
                <td>${phone.widthMm}mm</td>
            </tr>
            <tr>
                <td>Height</td>
                <td>${phone.heightMm}mm</td>
            </tr>
            <tr>
                <td>Weight</td>
                <td>${phone.weightGr}</td>
            </tr>
            </tbody>
        </table>
        <h5>Camera</h5>
        <table class="table table-bordered table-hover" cellspacing="0"
               width="100%">
            <tbody>
            <tr>
                <td>Front</td>
                <td>${phone.frontCameraMegapixels} megapixels</td>
            </tr>
            <tr>
                <td>Back</td>
                <td>${phone.backCameraMegapixels} megapixels</td>
            </tr>
            </tbody>
        </table>
        <h5>Battery</h5>
        <table class="table table-bordered table-hover" cellspacing="0"
               width="100%">
            <tbody>
            <tr>
                <td>Talk time</td>
                <td>${phone.talkTimeHours} hours</td>
            </tr>
            <tr>
                <td>Stand by time</td>
                <td>${phone.standByTimeHours} hours</td>
            </tr>
            <tr>
                <td>Battery capacity</td>
                <td>${phone.batteryCapacityMah}mAh</td>
            </tr>
            <tr>
                <td>Pixel density</td>
                <td>${phone.pixelDensity}</td>
            </tr>
            </tbody>
        </table>
        <h5>Other</h5>
        <table class="table table-bordered table-hover" cellspacing="0"
               width="100%">
            <tbody>
            <tr>
                <td>Colors</td>
                <td><c:forEach var="color" items="${phone.colors}">
                    ${color}
                </c:forEach></td>
            </tr>
            <tr>
                <td>Device type</td>
                <td>${phone.deviceType}</td>
            </tr>
            <tr>
                <td>Bluetooth</td>
                <td>${phone.bluetooth}</td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>