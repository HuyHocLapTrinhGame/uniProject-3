x<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="i18n.label" />

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8" />
        <title><fmt:message key="update.category" /></title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    </head>
    <body>
        <jsp:include page="/header.jsp" flush="true" />
        <div class="container mt-5 bg-light p-4 shadow-sm" style="min-height: 80vh">
            <h3 class="mb-3"><fmt:message key="update.category" /></h3>

            <!-- Message -->
            <c:if test="${not empty requestScope.MSG}">
                <div class="alert alert-info">${requestScope.MSG}</div>
            </c:if>

            <!-- Update Form -->
            <form action="${pageContext.request.contextPath}/main/category/update" method="POST">
                <input type="hidden" name="categoryID" value="${requestScope.category.categoryID}" />

                <div class="mb-3">
                    <label for="categoryName" class="form-label"><fmt:message key="category.name" /></label>
                    <input type="text" id="categoryName" name="categoryName"
                           class="form-control" required
                           value="${requestScope.category.categoryName}" />
                </div>

                <div class="mb-3">
                    <label for="description" class="form-label"><fmt:message key="description" /></label>
                    <textarea id="description" name="description" class="form-control"
                              rows="4" required>${requestScope.category.description}</textarea>
                </div>

                <button type="submit" class="btn btn-success w-100"><fmt:message key="update" /></button>
            </form>
        </div>
        <jsp:include page="/footer.jsp" flush="true" />
    </body>
</html>
