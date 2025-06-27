<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="i18n.label" />

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title><fmt:message key="customercare.list" /></title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    </head>
    <body>
        <jsp:include page="/header.jsp" flush="true" />
        <div class="container bg-white p-4 shadow-sm" style="min-height: 80vh">
            <c:if test="${sessionScope.currentUser.role == 'BUYER'}">
                <form action="${pageContext.request.contextPath}/main/customerCare/create" method="GET" class="mb-3">
                    <button type="submit" class="btn btn-success">
                        <fmt:message key="customercare.create" />
                    </button>
                </form>
            </c:if>
                   
            <!-- Search Form -->
            <form action="${pageContext.request.contextPath}/main/customerCare" method="GET" 
                  class="row g-2 mb-4">
                <div class="col-md-4">
                    <select name="action" class="form-select">
                        <option value="findByID" 
                                ${param.action=='findByID'?'selected':''}><fmt:message key="search.id" /></option>
                        <option value="findBySubject" 
                                ${param.action=='findBySubject'?'selected':''}><fmt:message key="search.subject" /></option>
                    </select>
                </div>
                <div class="col-md-4">
                    <input type="text" class="form-control" name="keySearch" 
                           placeholder="<fmt:message key="search" />..." required 
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
            <c:if test="${empty requestScope.customerCares}">
                <div class="alert alert-warning"><fmt:message key="customercare.not.found" /></div>
            </c:if>

            <!-- Table -->
            <c:if test="${not empty requestScope.customerCares}">
                <table class="table table-bordered table-hover">
                    <thead class="table-light">
                        <tr>
                            <th><fmt:message key="no" /></th>
                            <th><fmt:message key="ticket.id" /></th>
                            <th><fmt:message key="subject" /></th>
                            <th><fmt:message key="content" /></th>
                            <th><fmt:message key="status" /></th>
                            <th><fmt:message key="reply" /></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="customerCare" items="${requestScope.customerCares}" varStatus="st">
                            <tr>
                                <td>${st.count}</td>
                                <td>${customerCare.ticketID}</td>
                                <td>${customerCare.subject}</td>
                                <td>${customerCare.content}</td>
                                <td>${customerCare.status}</td>
                                <td>${customerCare.reply}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:if>
        </div>
        <jsp:include page="/footer.jsp" flush="true" />
    </body>
</html>
