<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!doctype html>
<html>
<head>
    <link rel="stylesheet" href="webjars/bootstrap/4.1.3/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="webjars/bootstrap-sortable/1.11.1/Contents/bootstrap-sortable.css">
    <link rel="stylesheet" href="<c:url value="/resources/styles/PLP_styles.css"/>">
    <script src="webjars/jquery/3.0.0/jquery.min.js"></script>
    <script src="webjars/bootstrap-sortable/1.11.1/Scripts/bootstrap-sortable.js"></script>
    <script src="webjars/bootstrap/4.1.3/js/bootstrap.min.js"></script>
    <script src="<c:url value="/resources/scripts/PLP_scripts.js"/>"></script>
    <c:set var="cartItemsAmount" scope="session" value="'My cart: 0 items'"/>
    <title>Product list</title>
</head>
<body>
<div id="cart" class="minicart">My cart: ${cartItemsAmount} items $${cartItemsPrice}</div>
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
                <input type="text"  class="form-control noEnterSubmit" name="search" id="search" placeholder="search">
                <span class="glyphicon glyphicon-search form-control-feedback"></span>
                <button type="button"
                        onclick="doSearch(document.getElementById('search').value)">Search!
                </button>
                <script>
                    $('.noEnterSubmit').keypress(function(e){
                    if ( e.which == 13 ) e.preventDefault();
                });
                </script>
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
                       href="?pageNumber=${empty pageNumber ? pageContext.request.getParameter("pageNumber") : pageNumber}&previousPage=true&orderBy=${orderBy}&isAscend=${isAscend}">Previous</a>
                </li>
                <li class="page-item"><a class="page-link" onclick="doSort(1, '${orderBy}', '${isAscend}')">1</a></li>
                <li class="page-item"><a class="page-link" onclick="doSort(2, '${orderBy}', '${isAscend}')">2</a></li>
                <li class="page-item"><a class="page-link" onclick="doSort(3, '${orderBy}', '${isAscend}')">3</a></li>
                <li class="page-item"><a class="page-link" onclick="doSort(4, '${orderBy}', '${isAscend}')">4</a></li>
                <li class="page-item"><a class="page-link" onclick="doSort(5, '${orderBy}', '${isAscend}')">5</a></li>
                <li class="page-item"><a class="page-link" onclick="doSort(6, '${orderBy}', '${isAscend}')">6</a></li>
                <li class="page-item"><a class="page-link" onclick="doSort(7, '${orderBy}', '${isAscend}')">7</a></li>
                <li class="page-item"><a class="page-link" onclick="doSort(8, '${orderBy}', '${isAscend}')">8</a></li>
                <li class="page-item"><a class="page-link" onclick="doSort(9, '${orderBy}', '${isAscend}')">9</a></li>
                <li class="page-item"><a class="page-link disabled">...</a></li>
                <li class="page-item"><a class="page-link" onclick="doSort(${maxPageNumber}, '${orderBy}', '${isAscend}')">${maxPageNumber}</a></li>
                <li class="page-item">
                    <a class="page-link"
                       href="?pageNumber=${empty pageNumber ? pageContext.request.getParameter("pageNumber") : pageNumber}&nextPage=true&orderBy=${orderBy}&isAscend=${isAscend}">Next</a>
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
                <th><a onclick='doSort(${pageNumber}, "brand", ${isAscend}, true)'>Brand</a></th>
                <th><a onclick='doSort(${pageNumber}, "model", ${isAscend}, true)'>Model</a></th>
                <th>Color</th>
                <th><a onclick='doSort(${pageNumber}, "displaySizeInches", ${isAscend}, true)'>Display size</a></th>
                <th><a onclick='doSort(${pageNumber}, "price", ${isAscend}, true)'>Price</a></th>
                <th>Quantity</th>
                <th>Action</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="phone" items="${phones}">
                <tr>
                    <td>
                        <a href="productDetails?phoneId=${phone.id}"><img src="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${phone.imageUrl}"></a>
                    </td>
                    <td>${phone.brand}</td>
                    <td>${phone.model}</td>
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