<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Update Price History</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<jsp:include page="/header.jsp" flush="true" />

<div class="container mt-5">
    <h2>Update Price History</h2>

    <c:if test="${not empty MSG}">
        <div class="alert alert-info">${MSG}</div>
    </c:if>

    <c:if test="${not empty history}">
        <form action="${pageContext.request.contextPath}/main/priceHistory/update" method="POST" class="bg-light p-4 rounded shadow-sm">
            <input type="hidden" name="action" value="update" />
            <input type="hidden" name="historyID" value="${history.historyID}" />

            <div class="mb-3">
                <label class="form-label">Product ID</label>
                <input type="text" class="form-control" value="${history.productID}" readonly />
            </div>

            <div class="mb-3">
                <label class="form-label">Old Price</label>
                <input type="text" class="form-control" value="${history.oldPrice}" readonly />
            </div>

            <div class="mb-3">
                <label class="form-label">New Price</label>
                <input type="text" class="form-control" value="${history.newPrice}" readonly />
            </div>

            <div class="mb-3">
                <label for="note" class="form-label">Note</label>
                <textarea class="form-control" id="note" name="note" rows="3" required>${history.note}</textarea>
            </div>

            <div class="d-flex justify-content-between">
                <a href="${pageContext.request.contextPath}/main/priceHistory/getAll" class="btn btn-secondary">
                    Back
                </a>
                <button type="submit" class="btn btn-success">
                    Update
                </button>
            </div>
        </form>
    </c:if>

    <c:if test="${empty history}">
        <div class="alert alert-danger">Price history not found!</div>
    </c:if>
</div>

<jsp:include page="/footer.jsp" flush="true" />
</body>
</html>
