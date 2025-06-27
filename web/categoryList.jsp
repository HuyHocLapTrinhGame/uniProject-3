<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="i18n.label" />

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title><fmt:message key="category.title" /></title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    </head>
    <body>
        <jsp:include page="/header.jsp" flush="true" />
        <div class="container bg-white p-4 shadow-sm" style="min-height: 80vh">
            <h2 class="mb-3"><fmt:message key="category.list" /></h2>

            <c:if test="${sessionScope.currentUser.role == 'ADMIN'}">
                <form action="${pageContext.request.contextPath}/main/category/create" method="GET" class="mb-3">
                    <button type="submit" class="btn btn-success"><fmt:message key="create.category" /></button>
                </form>
            </c:if>

            <!-- Search Form -->
            <form action="${pageContext.request.contextPath}/main/category" method="GET" class="row g-2 mb-4">
                <div class="col-md-4">
                    <select name="action" class="form-select">
                        <option value="findByID" ${param.action=='findByID'?'selected':''}><fmt:message key="search.id" /></option>
                        <option value="findByName" ${param.action=='findByName'?'selected':''}><fmt:message key="search.name" /></option>
                    </select>
                </div>
                <div class="col-md-4">
                    <input type="text" class="form-control" name="keySearch" required
                           placeholder="<fmt:message key="search" />..."
                           value="${param.keySearch}" autofocus />
                </div>
                <div class="col-md-4">
                    <button type="submit" class="btn btn-primary w-100"><fmt:message key="search" /></button>
                </div>
            </form>

            <!-- Notification -->
            <c:if test="${not empty requestScope.MSG}">
                <div class="alert alert-info">${requestScope.MSG}</div>
            </c:if>

            <!-- No result -->
            <c:if test="${empty requestScope.categories}">
                <div class="alert alert-warning"><fmt:message key="no.match.categoty" /></div>
            </c:if>

            <!-- Table -->
            <c:if test="${not empty requestScope.categories}">
                <table class="table table-bordered table-hover">
                    <thead class="table-light">
                        <tr>
                            <th><fmt:message key="no" /></th>
                            <th><fmt:message key="category.id" /></th>
                            <th><fmt:message key="category.name" /></th>
                            <th><fmt:message key="description" /></th>
                            <th><fmt:message key="action" /></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="cat" items="${requestScope.categories}" varStatus="st">
                            <tr>
                                <td>${st.count}</td>
                                <td>${cat.categoryID}</td>
                                <td>${cat.categoryName}</td>
                                <td>${cat.description}</td>
                                <td class="table-actions gap-2">
                                    <c:if test="${sessionScope.currentUser.role == 'ADMIN'}">
                                        <!-- Update -->
                                        <form action="${pageContext.request.contextPath}/main/category/update" method="GET" class="d-inline">
                                            <input type="hidden" name="keySearch" value="${cat.categoryID}" />
                                            <button type="submit" class="btn btn-sm btn-warning"><fmt:message key="update" /></button>
                                        </form>
                                        <!-- Delete -->
                                        <form action="${pageContext.request.contextPath}/main/category/delete" method="POST" class="d-inline"
                                              onsubmit="return confirm('<fmt:message key="confirm.delete.category" />');">
                                            <input type="hidden" name="categoryID" value="${cat.categoryID}" />
                                            <button type="submit" class="btn btn-sm btn-danger"><fmt:message key="delete" /></button>
                                        </form>
                                    </c:if>
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
