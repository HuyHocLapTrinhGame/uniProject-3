<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="i18n.label" />

<style>
    .navbar-nav, .nav-link {
        height: 40px;
        width: 450px;
    }
    .custom-hover {
        position: relative;
        text-decoration: none;
        color: white !important;
        z-index: 1;
        display: flex !important;
        align-items: center;
        justify-content: center;
        height: 100%;
        width: 100%;
    }
    .custom-hover:hover {
        color: #e0e0e0 !important;
    }
    .custom-hover::after {
        content: '';
        position: absolute;
        bottom: 0px;
        left: 0;
        width: 0%;
        height: 5px;
        background-color: cyan;
        transition: width 0.3s ease-in-out;
        z-index: -1;
    }
    .custom-hover:hover::after {
        width: 100%;
    }
    .lang-btn {
        min-width: 50px;
        text-align: center;
    }
</style>

<!-- Main Content -->
<div class="m-0 w-100" style="background-color: #111827; color: white; padding: 0 80px">
    <c:if test="${not empty sessionScope.currentUser}">
        <h2 class="pt-3" style="text-align: center">
            <fmt:message key="welcome" />, <c:out value="${sessionScope.currentUser.fullName}" />
        </h2>
    </c:if>

    <!-- Navigation Bar -->
    <nav class="navbar navbar-expand-lg navbar-light p-0">
        <div class="container-fluid d-flex justify-content-between align-items-center">
            <!-- Left nav items -->
            <div class="navbar-nav" style="min-width: 70%">
                <a class="nav-link custom-hover py-0" href="${pageContext.request.contextPath}/main/product">
                    <fmt:message key="product" />
                </a>
                <a class="nav-link custom-hover py-0" href="${pageContext.request.contextPath}/main/category">
                    <fmt:message key="category" />
                </a>
                <a class="nav-link custom-hover py-0" href="${pageContext.request.contextPath}/main/promotion">
                    <fmt:message key="promotion" />
                </a>
                <a class="nav-link custom-hover py-0" href="${pageContext.request.contextPath}/main/priceHistory">
                    <fmt:message key="price.history" />
                </a>

                <c:if test="${sessionScope.currentUser.role == 'BUYER'}">
                    <a class="nav-link custom-hover py-0" href="${pageContext.request.contextPath}/main/cart">
                        <fmt:message key="cart" />
                    </a>
                    <a class="nav-link custom-hover py-0" href="${pageContext.request.contextPath}/main/invoice">
                        <fmt:message key="invoice" />
                    </a>
                </c:if>

                <c:if test="${sessionScope.currentUser.role == 'BUYER' or sessionScope.currentUser.role == 'CUSTOMER_SUPPORT'}">
                    <a class="nav-link custom-hover py-0" style="min-width: 200px"
                       <c:if test="${sessionScope.currentUser.role == 'BUYER'}">
                           href="${pageContext.request.contextPath}/main/customerCare"
                       </c:if>
                       <c:if test="${sessionScope.currentUser.role == 'CUSTOMER_SUPPORT'}">
                           href="${pageContext.request.contextPath}/main/customerCare/getAllViewModel"
                       </c:if>
                    >
                        <fmt:message key="customer.care" />
                    </a>
                </c:if>

                <c:if test="${sessionScope.currentUser.role == 'DELIVERY'}">
                    <a class="nav-link custom-hover py-0" href="${pageContext.request.contextPath}/main/delivery">
                        <fmt:message key="delivery" />
                    </a>
                </c:if>

                <c:if test="${sessionScope.currentUser.role == 'ADMIN'}">
                    <a class="nav-link custom-hover py-0" href="${pageContext.request.contextPath}/main/user">
                        <fmt:message key="user" />
                    </a>
                    <a class="nav-link custom-hover py-0" href="${pageContext.request.contextPath}/main/return">
                        <fmt:message key="return" />
                    </a>
                </c:if>
            </div>

            <!-- Right auth buttons -->
            <div class="d-flex align-items-center">
                <c:if test="${empty sessionScope.currentUser}">
                    <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-primary btn-sm fw-bold">
                        <fmt:message key="login" />
                    </a>
                </c:if>
                <c:if test="${not empty sessionScope.currentUser}">
                    <form action="${pageContext.request.contextPath}/main/auth/logout" method="POST" class="m-0">
                        <button type="submit" name="action" value="Logout" class="btn btn-danger btn-sm fw-bold">
                            <fmt:message key="logout" />
                        </button>
                    </form>
                    <form 
                        action="${pageContext.request.contextPath}/main/user/update-profile" 
                        method="GET" class="ms-3 mb-0">
                        <button type="submit" name="userID" value="${sessionScope.currentUser.userID}" class="btn btn-sm btn-warning fw-bold">
                            <fmt:message key="update.profile" />
                        </button>
                    </form>
                </c:if>
                <div class="d-inline-block">
                    <a href="?lang=vi" class="btn btn-sm lang-btn
                       ${sessionScope.locale.language == 'vi' ? 'btn-danger fw-bold' : 'btn-outline-danger'} ms-3">
                        vi
                    </a>
                    <a href="?lang=en" class="btn btn-sm lang-btn
                       ${sessionScope.locale.language == 'en' ? 'btn-primary fw-bold' : 'btn-outline-primary'}">
                        en
                    </a>
                </div>
            </div>
        </div>
    </nav>
</div>
