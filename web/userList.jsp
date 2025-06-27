<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="i18n.label" />

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title><fmt:message key="user.list" /></title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    </head>
    <body>
        <jsp:include page="/header.jsp" flush="true" />
        <div class="container bg-white p-4 shadow-sm" style="min-height: 80vh">
            <form action="${pageContext.request.contextPath}/main/user" method="GET" class="mb-3">
                <button type="submit" name="action" value="create" class="btn btn-success"><fmt:message key="create.user" /></button>
            </form>

            <form action="${pageContext.request.contextPath}/main/user" method="GET" class="row g-2 mb-4">           
                <div class="col-md-4">
                    <select name="action" class="form-select">
                        <option value="getUsersByID"><fmt:message key="search.id" /></option>
                        <option value="getUsersByName"><fmt:message key="search.name" /></option>
                    </select>
                </div>
                <div class="col-md-4">
                    <input type="text" class="form-control" id="keySearch" name="keySearch" placeholder="<fmt:message key="search" />..." required />
                </div>
                <div class="col-md-4">
                    <button type="submit" class="btn btn-primary w-100"><fmt:message key="search" /></button>
                </div>
            </form>
                
            <c:if test="${!empty requestScope.MSG}">
                <div class="alert alert-success">${requestScope.MSG}</div>
            </c:if>

            <c:if test="${empty users}">
                <div class="alert alert-warning"><fmt:message key="" /></div>
            </c:if>

            <c:if test="${not empty users}">
                <table class="table table-bordered table-hover">
                    <thead class="table-light">
                        <tr>
                            <th><fmt:message key="no" /></th>
                            <th><fmt:message key="user.id" /></th>
                            <th><fmt:message key="fullname" /></th>
                            <th><fmt:message key="phone" /></th>
                            <th><fmt:message key="role" /></th>
                            <th><fmt:message key="action" /></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="user" items="${requestScope.users}" varStatus="st">
                            <tr>
                                <td>${st.count}</td>
                                <td>${user.userID}</td>
                                <td>${user.fullName}</td>
                                <td>${user.phone}</td>
                                <td>${user.role}</td>
                                <td class="table-actions gap-2">
                                    <form 
                                        action="${pageContext.request.contextPath}/main/user/update" 
                                        method="GET"
                                        >
                                        <button type="submit" name="userID" value="${user.userID}" class="btn btn-sm btn-warning">
                                            <fmt:message key="update" />
                                        </button>
                                    </form>
                                    <form 
                                        action="${pageContext.request.contextPath}/main/user/delete" 
                                        method="POST" 
                                        onsubmit="return confirm('<fmt:message key="confirm.delete.user" />');"
                                        >
                                        <button type="submit" name="userID" value="${user.userID}" class="btn btn-sm btn-danger">
                                            <fmt:message key="delete" />
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
