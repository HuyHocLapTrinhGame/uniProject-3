<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="i18n.label" />

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title><fmt:message key="customercare.view.title" /></title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
        <style>
            .table td, .table th {
                white-space: normal !important;
                word-break: break-word;
            }

            /* Giữ cho nút Update không bị xuống dòng */
            .btn-sm {
                white-space: nowrap;       /* Ngăn chữ xuống dòng */
                min-width: 70px;           /* Đảm bảo nút đủ rộng */
                text-align: center;        /* Căn giữa */
                padding: 0.375rem 0.75rem; /* Giữ cho padding ổn định */
            }
        </style>
    </head>
    <body>
        <jsp:include page="/header.jsp" flush="true" />
        <div class="container bg-white p-4 shadow-sm" style="min-height: 80vh">
            <h2><fmt:message key="customercare.full.info" /></h2>

            <!-- Notification -->
            <c:if test="${not empty requestScope.MSG}">
                <div class="alert alert-info">${requestScope.MSG}</div>
            </c:if>

            <!-- No result -->
            <c:if test="${empty customerCareView}">
                <div class="alert alert-warning"><fmt:message key="customercare.detail.not.found" /></div>
            </c:if>

            <!-- Table Responsive for long text -->
            <c:if test="${not empty customerCareView}">
                <div class="table-responsive">
                    <table class="table table-bordered table-hover">
                        <thead class="table-light">
                            <tr>
                                <th><fmt:message key="no" /></th>
                                <th><fmt:message key="ticket.id" /></th>
                                <th><fmt:message key="user.id" /></th>
                                <th><fmt:message key="fullname" /></th>
                                <th><fmt:message key="subject" /></th>
                                <th><fmt:message key="content" /></th>
                                <th><fmt:message key="status" /></th>
                                <th><fmt:message key="reply" /></th>
                                <th><fmt:message key="action" /></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="care" items="${customerCareView}" varStatus="st">
                                <tr>
                                    <td>${st.count}</td>
                                    <td>${care.ticketID}</td>
                                    <td>${care.userID}</td>
                                    <td>${care.fullName}</td>
                                    <td>${care.subject}</td>
                                    <td>${care.content}</td>
                                    <td>${care.status}</td>
                                    <td>${care.reply}</td>
                                    <td>
                                        <c:if test="${sessionScope.currentUser.role == 'CUSTOMER_SUPPORT'}">
                                            <form action="${pageContext.request.contextPath}/main/customerCare/update" method="GET" class="d-inline">
                                                <input type="hidden" name="keySearch" value="${care.ticketID}" />
                                                <button type="submit" class="btn btn-sm btn-warning"><fmt:message key="update" /></button>
                                            </form>
                                            <form action="${pageContext.request.contextPath}/main/customerCare/delete" 
                                                  method="POST" class="d-inline"
                                                  onsubmit="return confirm('<fmt:message key="confirm.delete.customer.care" />');">
                                                <input type="hidden" name="ticketID" value="${care.ticketID}" />
                                                <button type="submit" class="btn btn-sm btn-danger"><fmt:message key="delete" /></button>
                                            </form>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:if>
        </div>
        <jsp:include page="/footer.jsp" flush="true" />
    </body>
</html>
