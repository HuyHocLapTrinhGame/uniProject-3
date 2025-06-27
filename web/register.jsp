<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="i18n.label" />

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8" />
        <title><fmt:message key="register" /></title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    </head>
    <body>
        <div class="login-container">
            <h2 class="login-title"><fmt:message key="register" /></h2>
            
            <c:if test="${not empty MSG}">
                <p class="error-msg">${MSG}</p>
            </c:if>

            <form action="${pageContext.request.contextPath}/main/auth/register" method="POST">
                <label for="userID"><fmt:message key="user.id" /></label>
                <input type="text" id="userID" name="userID" placeholder="<fmt:message key="user.enter.id" />" required class="form-control" />

                <label for="fullName"><fmt:message key="fullname" /></label>
                <input type="text" id="fullName" name="fullName" placeholder="<fmt:message key="user.enter.fullName" />" required class="form-control" />

                <label for="phone"><fmt:message key="phone" /></label>
                <input type="text" id="phone" name="phone" placeholder="<fmt:message key="user.enter.phone" />" required class="form-control" />

                <label for="password"><fmt:message key="password" /></label>
                <input type="password" id="password" name="password" placeholder="<fmt:message key="user.enter.password" />" required class="form-control" />

                <label for="confirmPassword"><fmt:message key="confirm.password" /></label>
                <input type="password" id="confirmPassword" name="confirmPassword" placeholder="<fmt:message key="user.enter.confirmPassword" />" required class="form-control" />

                <button type="submit" class="btn btn-primary w-100"><fmt:message key="register" /></button>
            </form>

            <a href="${pageContext.request.contextPath}/login.jsp" class="back-link"><fmt:message key="login" /></a>
        </div>
    </body>
</html>
