<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="i18n.label" />

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><fmt:message key="invoice.list" /></title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    </head>
    <body>
        <jsp:include page="/header.jsp" flush="true" />
        <div class="container bg-white p-4 shadow-sm" style="min-height: 80vh">
            <jsp:include page="navbarInvoice.jsp" />
            
            <h2 class="mt-3"><fmt:message key="invoice.list" /></h2>
            <c:if test="${not empty invoiceViewModels}">
                <table class="table table-bordered table-hover mt-3">
                    <thead class="table-light">
                        <tr>
                            <th><fmt:message key="invoice.id" /></th>
                            <th><fmt:message key="total.amount" /></th>
                            <th><fmt:message key="created.date" /></th>
                            <th><fmt:message key="action" /></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="invoice" items="${invoiceViewModels}">
                            <tr>
                                <td>${invoice.invoiceID}</td>
                                <td>$<fmt:formatNumber type="number" maxFractionDigits="2" value="${invoice.totalAmount}" /></td>
                                <td>${invoice.createdDate}</td>
                                <td class="table-actions">
                                    <form action="${pageContext.request.contextPath}/main/invoice/getInvoiceInformation" method="get">
                                        <input type="hidden" name="invoiceID" value="${invoice.invoiceID}" />
                                        <button type="submit" class="btn btn-sm btn-primary w-100">
                                            <fmt:message key="view.detail" />
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:if>

            <c:if test="${empty invoiceViewModels}">
                <div class="alert alert-warning"><fmt:message key="invoice.list.empty" /></div>
            </c:if>
        </div>
        <jsp:include page="/footer.jsp" flush="true" />
    </body>
</html>
