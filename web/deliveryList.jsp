<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="i18n.label" />

<html>

    <head>
        <meta charset="UTF-8">
        <title><fmt:message key="delivery.title" /></title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    </head>

    <body>
        <jsp:include page="/header.jsp" flush="true" />
        <div class="container bg-white p-4 shadow-sm" style="min-height: 80vh">
            <h2><fmt:message key="delivery.list" /></h2>

            <form action="${pageContext.request.contextPath}/main/delivery/getDeliveryByStatus" method="GET" class="row g-2 mb-4">
                <div class="col-md-4">
                    <select name="status" class="form-select">
                        <option value="pending"><fmt:message key="pending" /></option>
                        <option value="delivering"><fmt:message key="delivering" /></option>
                        <option value="delivered"><fmt:message key="delivered" /></option>
                    </select>
                </div>
                <div class="col-md-2">
                    <button type="submit" class="btn btn-primary w-100"><fmt:message key="search" /></button>
                </div>
            </form>

            <c:if test="${not empty requestScope.MSG}">
                <div>${requestScope.MSG} </div>
            </c:if>

            <c:if test="${empty delivery}">
                <div><fmt:message key="delivery.not.found" /></div>
            </c:if>        

            <c:if test="${not empty delivery}">
                <table class="table table-bordered table-hover">
                    <thead class="table-light">
                        <tr>
                            <th><fmt:message key="delivery.id" /></th>
                            <th><fmt:message key="invoice.id" /></th>
                            <th><fmt:message key="address" /></th>
                            <th><fmt:message key="delivery.date" /></th>
                            <th><fmt:message key="status" /></th> 
                            <th><fmt:message key="action" /></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="delivery" items="${requestScope.delivery}">
                            <tr>
                                <td>${delivery.deliveryID}</td>
                                <td>${delivery.invoiceID}</td>
                                <td>${delivery.address}</td>
                                <td>${delivery.deliveryDate}</td>
                                <td>${delivery.status}</td>
                                <td class="table-actions">
                                    <form action="${pageContext.request.contextPath}/main/delivery/update" method="POST"
                                          class="mb-0 w-100">
                                        <input type="hidden" name="deliveryID" value="${delivery.deliveryID}" />
                                        <input type="hidden" name="invoiceID" value="${delivery.invoiceID}" />
                                        <c:if test="${delivery.status == 'pending'}">
                                            <button type="submit" class="btn btn-sm btn-warning w-100"
                                                    name="status" value="delivering">
                                                <fmt:message key="delivering" />
                                            </button>
                                        </c:if>
                                        <c:if test="${delivery.status == 'delivering'}">
                                            <button type="submit" class="btn btn-sm btn-warning w-100"
                                                    name="status" value="delivered">
                                                <fmt:message key="delivered" />
                                            </button>
                                        </c:if>
                                    </form>     
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>          
                </table>
            </c:if>
        </div>
        <jsp:include page="/footer.jsp" flush="true" />
    </body>
</html>