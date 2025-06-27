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
        <form class="nav-form" action="${pageContext.request.contextPath}/main/invoice" method="get">
            <input type="hidden" name="status" value="pending" />
            <button type="submit" class="nav-button" id="pending"><fmt:message key="pending" /></button>
        </form>
        <form class="nav-form" action="${pageContext.request.contextPath}/main/invoice" method="get">
            <input type="hidden" name="status" value="paid" />
            <button type="submit" class="nav-button" id="paid"><fmt:message key="paid" /></button>
        </form>
        <form class="nav-form" action="${pageContext.request.contextPath}/main/invoice" method="get">
            <input type="hidden" name="status" value="cancel" />
            <button type="submit" class="nav-button" id="cancel"><fmt:message key="cancel" /></button>
        </form>
        <form class="nav-form" action="${pageContext.request.contextPath}/main/invoice" method="get">
            <input type="hidden" name="status" value="delivering" />
            <button type="submit" class="nav-button" id="delivering">
                <fmt:message key="delivering" />
            </button>
        </form>
        <form class="nav-form" action="${pageContext.request.contextPath}/main/invoice" method="get">
            <input type="hidden" name="status" value="delivered" />
            <button type="submit" class="nav-button" id="delivered">
                <fmt:message key="delivered" />
            </button>
        </form>
        <form class="nav-form" action="${pageContext.request.contextPath}/main/invoice" method="get">
            <input type="hidden" name="status" value="return" />
            <button type="submit" class="nav-button" id="return"><fmt:message key="return" /></button>
        </form>
    </div>
    <c:set var="statusPage" value="${requestScope.status}" />
    <c:if test="${empty requestScope.status}">
        <c:set var="statusPage" value="${empty param.status ? 'pending' : param.status}" />
    </c:if>

    <script>
            const page = '${statusPage}';

            const activeButton = document.getElementById(page);
            if (activeButton) {
                activeButton.classList.add("active");
            }
    </script>
</body>



