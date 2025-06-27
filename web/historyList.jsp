<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Price History</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<jsp:include page="/header.jsp" flush="true" />

<div class="container bg-white p-4 shadow-sm" style="min-height: 80vh">
    <h2 class="mb-4">Price History</h2>

    <!-- Search Form -->
    <form action="${pageContext.request.contextPath}/main/priceHistory/findByID" method="GET" class="row g-2 mb-4">
        <div class="col-md-4">
            <input type="number" class="form-control" name="productID" placeholder="Search by Product ID..." required />
        </div>
        <div class="col-md-4">
            <button type="submit" class="btn btn-primary w-100">
                Search
            </button>
        </div>
        <div class="col-md-4 d-flex gap-2">
            <a href="${pageContext.request.contextPath}/main/priceHistory/getAll" class="btn btn-outline-secondary w-50">
                Load All
            </a>
            <a href="${pageContext.request.contextPath}/main/priceHistory/delete" 
               class="btn btn-danger w-50"
               onclick="return confirm('Are you sure you want to delete old history records?');">
                Delete Old Records
            </a>
        </div>
    </form>

    <!-- Notification -->
    <c:if test="${not empty MSG}">
        <div class="alert alert-info">${MSG}</div>
    </c:if>

    <!-- No Data -->
    <c:if test="${empty historyList}">
        <div class="alert alert-warning">
            No price history found!
        </div>
    </c:if>

    <!-- History Table -->
    <c:if test="${not empty historyList}">
        <table class="table table-bordered table-hover">
            <thead class="table-light">
                <tr>
                    <th>#</th>
                    <th>Product ID</th>
                    <th>Change Date</th>
                    <th>Old Price</th>
                    <th>New Price</th>
                    <th>Note</th>
                    <th>Update Note</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="h" items="${historyList}" varStatus="st">
                    <tr>
                        <td>${st.count}</td>
                        <td>${h.productID}</td>
                        <td>${h.changeDate}</td>
                        <td>${h.oldPrice}</td>
                        <td>${h.newPrice}</td>
                        <td>${h.note}</td>
                        <td>
                            <form method="post" action="${pageContext.request.contextPath}/main/priceHistory/update" class="d-flex gap-2">
                                <input type="hidden" name="historyID" value="${h.historyID}" />
                                <input type="text" name="note" class="form-control form-control-sm" 
                                       placeholder="New note..." required />
                                <button type="submit" class="btn btn-sm btn-success">
                                    Save
                                </button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:if>
</div>

<jsp:include page="/footer.jsp" flush="true" />
</body>
</html>
