<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Quick order entry</title>
    <link rel="stylesheet" href="<c:url value="/resources/styles/quickOrderEntryPage_styles.css"/>">
    <script src="<c:url value="/resources/scripts/quickOrderEntryPage_scripts.js"/>"></script>
</head>
<body>
<jsp:include page="header.jsp"/>
Hello from quick order entry!
<form method="post">
    <div class="table-responsive">
        <table id="table" class="table table-striped table-bordered table-hover table-sm" cellspacing="0"
               width="100%">
            <thead>
            <tr>
                <th>Code</th>
                <th>Quantity</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="row" items="${formData}" varStatus="status">
                <tr>
                    <td>
                        <input type="text" class="code-field" id=code${row.key} name="code${row.key}"
                               value=""/>
                        <br>
                        <c:if test="${errors.containsKey(row.key)}">
                            <label for=code${row.key} class="error-message" id=labelc${row.key}>${errors.get(row.key)}</label>
                        </c:if>
                    </td>
                    <td>
                        <input type="text" class="quantity-field" id=quantity${row.key} name="quantity${row.key}"
                               value=""/>
                        <br>
                        <c:if test="${errors.containsKey(row.key)}">
                            <label for=quantity${row.key} class="error-message" id=labelq${row.key}>${errors.get(row.key)}</label>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <input type="submit" value="add to Cart"/>
</form>
</body>
</html>
