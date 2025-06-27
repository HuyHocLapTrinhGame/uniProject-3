<%-- 
    Document   : navbarReturn
    Created on : Jun 23, 2025, 2:03:03 PM
    Author     : ngogi
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="i18n.label" />

<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/navbarStyle.css">
</head>
<body>
    <div class="status-navbar">
        <form class="nav-form" action="${pageContext.request.contextPath}/main/return/getReturnByStatus" method="GET">
            <input type="hidden" name="status" value="Pending" />
            <button type="submit" class="nav-button" id="Pending" name="action"><fmt:message key="pending" /></button>
        </form>
        <form class="nav-form" action="${pageContext.request.contextPath}/main/return/getReturnByStatus" method="GET">
            <input type="hidden" name="status" value="Approve" />
            <button type="submit" class="nav-button" id="Approve" name="action"><fmt:message key="approve" /></button>
        </form>
        <form class="nav-form" action="${pageContext.request.contextPath}/main/return/getReturnByStatus" method="GET">
            <input type="hidden" name="status" value="Rejected" />
            <button type="submit" class="nav-button" id="Rejected" name="action"><fmt:message key="rejected" /></button>
        </form>
        <form class="nav-form" action="${pageContext.request.contextPath}/main/return" method="get">
            <input type="hidden" name="status" value="all" />
            <button type="submit" class="nav-button" id="all"><fmt:message key="all" /></button>
        </form>
    </div>
</body>



