<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="i18n.label" />

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title><fmt:message key="welcome" /></title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            body {
                text-align: center;
                margin-top: 100px;
                font-family: 'Roboto', sans-serif;
            }
        </style>
    </head>
    <body>
        <h1><fmt:message key="welcome.string" /></h1>
        <a href="${pageContext.request.contextPath}/main/product" class="btn btn-primary">
            <fmt:message key="start.shopping" />
        </a>
    </body>
</html>
