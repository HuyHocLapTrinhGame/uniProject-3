<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="i18n.label" />

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8" />
        <title><fmt:message key="create.customer.care.title" /></title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
    </head>
    <body>
        <jsp:include page="/header.jsp" flush="true" />
        <div class="container mt-5 bg-light p-4 shadow-sm" style="min-height: 80vh">
            <h3 class="mb-3"><fmt:message key="create.customer.care.ticket" /></h3>

            <!-- Message -->
            <c:if test="${not empty requestScope.MSG}">
                <div class="alert alert-info">${requestScope.MSG}</div>
            </c:if>

            <!-- Create Form -->
            <form action="${pageContext.request.contextPath}/main/customerCare/create" method="POST">

                <input type="hidden" name="userID" value="${sessionScope.currentUser.userID}" />

                <div class="mb-3">
                    <label for="subject" class="form-label"><fmt:message key="subject" /></label>
                    <input type="text" id="subject" name="subject" class="form-control"
                           placeholder="<fmt:message key="enter.subject" />" required value="${param.subject}" />
                </div>

                <div class="mb-3">
                    <label for="content" class="form-label"><fmt:message key="content" /></label>
                    <textarea id="content" name="content" class="form-control" rows="4"
                              placeholder="<fmt:message key="enter.content" />" required>${param.content}</textarea>
                </div>

                <button type="submit" class="btn btn-primary w-100"><fmt:message key="create" /></button>
            </form>
        </div>
        <jsp:include page="/footer.jsp" flush="true" />
    </body>
</html>
