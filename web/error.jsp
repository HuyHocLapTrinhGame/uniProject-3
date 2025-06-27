<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="i18n.label" />

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title><fmt:message key="error.title" /></title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    </head>
    <body>
        <c:if test="${empty requestScope.MSG}">
            <c:set var="error" value="<fmt:message key='unknown.error' />" />
        </c:if>

        <jsp:include page="/header.jsp" flush="true" />
        <div class="error-container" style="min-height: 80vh">
            <h2><fmt:message key="error" /></h2>
            <p class="error-msg">${requestScope.MSG}</p>
        </div>
        <jsp:include page="/footer.jsp" flush="true" />
    </body>
</html>
