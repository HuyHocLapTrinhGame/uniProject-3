<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="i18n.label" />

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title><fmt:message key="create.promotion.title" /></title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    </head>
    <body>
        <jsp:include page="/header.jsp" flush="true" />
        <div class="container bg-white p-4 shadow-sm" style="min-height: 80vh">
            <h2><fmt:message key="create.promotion" /></h2>

            <c:if test="${not empty requestScope.MSG}">
                <div class="alert alert-info">${requestScope.MSG}</div>
            </c:if>

            <form action="${pageContext.request.contextPath}/main/promotion/create" method="POST">

                <div class="mb-3">
                    <label for="name" class="form-label"><fmt:message key="promotion.name" /></label>
                    <input type="text" id="name" name="name" class="form-control" required />
                </div>

                <div class="mb-3">
                    <label for="discount" class="form-label"><fmt:message key="discount.percent" /> (%)</label>
                    <input type="number" step="0.01" min="0" max="100" id="discount" name="discount" class="form-control" required />
                </div>

                <div class="mb-3">
                    <label for="startDate" class="form-label"><fmt:message key="start.date" /></label>
                    <input type="date" id="startDate" name="startDate" class="form-control" required />
                </div>

                <div class="mb-3">
                    <label for="endDate" class="form-label"><fmt:message key="end.date" /></label>
                    <input type="date" id="endDate" name="endDate" class="form-control" required />
                </div>

                <div class="mb-3">
                    <label for="status" class="form-label"><fmt:message key="status" /></label>
                    <select name="status" id="status" class="form-select" required>
                        <option value="active"><fmt:message key="active" /></option>
                        <option value="inactive"><fmt:message key="inactive" /></option>
                    </select>
                </div>

                <button type="submit" class="btn btn-success"><fmt:message key="create" /></button>
            </form>
        </div>
        <jsp:include page="/footer.jsp" flush="true" />
    </body>
</html>
