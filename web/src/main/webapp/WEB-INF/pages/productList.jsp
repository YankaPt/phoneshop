<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!doctype html>
<html>
<head>
    <link rel="stylesheet" href="<c:url value="/resources/styles/PLP_styles.css"/>">
    <script src="<c:url value="/resources/scripts/PLP_scripts.js"/>"></script>
    <c:set var="cartItemsAmount" scope="session" value="'My cart: 0 items'"/>
    <title>Product list</title>
</head>
<body>
<jsp:include page="header.jsp"/>
<p>
    Hello from product list!
</p>
<form action="<c:url value="cart"/>">
    <button>to Cart</button>
</form>
<div id="result"></div>
<p>
    Found
    <c:out value="${phones.size()}"/> phones.
<div class="row justify-content-end">
    <div class="col-md-2">
        <form method="post" class="search-form">
            <div class="form-group has-feedback">
                <label for="search" class="sr-only">Search</label>
                <input type="text" class="form-control" name="search" id="search" placeholder="search">
                <span class="glyphicon glyphicon-search form-control-feedback"></span>
                <button type="button"
                        onclick="doSearch(document.getElementById('search').value)">Search!
                </button>
            </div>
        </form>
    </div>
</div>
<form method="post">
    <div id="myPaginationHead">
        <nav aria-label="Page navigation">
            <ul class="pagination justify-content-end">
                <li class="page-item">
                    <a class="page-link"
                       href="?pageNumber=${empty pageNumber ? pageContext.request.getParameter("pageNumber") : pageNumber}&previousPage=true">Previous</a>
                </li>
                <li class="page-item"><a class="page-link" href="?pageNumber=1">1</a></li>
                <li class="page-item"><a class="page-link" href="?pageNumber=2">2</a></li>
                <li class="page-item"><a class="page-link" href="?pageNumber=3">3</a></li>
                <li class="page-item"><a class="page-link" href="?pageNumber=4">4</a></li>
                <li class="page-item"><a class="page-link" href="?pageNumber=5">5</a></li>
                <li class="page-item"><a class="page-link" href="?pageNumber=6">6</a></li>
                <li class="page-item"><a class="page-link" href="?pageNumber=7">7</a></li>
                <li class="page-item"><a class="page-link" href="?pageNumber=8">8</a></li>
                <li class="page-item"><a class="page-link" href="?pageNumber=9">9</a></li>
                <li class="page-item"><a class="page-link disabled">...</a></li>
                <li class="page-item"><a class="page-link" href="?pageNumber=${maxPageNumber}">${maxPageNumber}</a></li>
                <li class="page-item">
                    <a class="page-link"
                       href="?pageNumber=${empty pageNumber ? pageContext.request.getParameter("pageNumber") : pageNumber}&nextPage=true">Next</a>
                </li>
            </ul>
        </nav>
    </div>
    <div class="table-responsive">
        <table id="table" class="table table-striped table-bordered table-hover table-sm" cellspacing="0"
               width="100%">
            <thead>
            <tr>
                <th>Image</th>
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
            <c:forEach var="phone" items="${phones}">
                <tr>
                    <td>
                        <img src="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${phone.imageUrl}">
                    </td>
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
                        <input type="text" id=quantity${phone.id} class="quantity-area" value="1"/>
                        <br>
                        <label for=quantity${phone.id} id=label${phone.id}></label>
                    </td>
                    <td>
                        <button type="button"
                                onclick="addToCart(${phone.id}, document.getElementById('quantity${phone.id}').value, '${pageContext.request.locale.toString()}')">
                            Add
                            to
                        </button>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <div id="myPaginationFoot"></div>
    <script>
        document.getElementById("myPaginationFoot").appendChild(document.getElementById("myPaginationHead").cloneNode(true));
    </script>
</form>
</p>
</body>
</html>