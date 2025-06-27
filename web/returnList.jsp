<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="i18n.label" />

<html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><fmt:message key="return.list" /></title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    </head>

    <body>
        <jsp:include page="/header.jsp" flush="true" />
        <div class="container bg-white p-4 shadow-sm" style="min-height: 80vh">
            <h2><fmt:message key="return.list" /></h2>
            <jsp:include page="navbarReturn.jsp" />

            <c:if test="${not empty requestScope.MSG}">
                <div class="alert alert-success">${requestScope.MSG} </div>
            </c:if>

            <c:if test="${not empty returns}">
                <table class="table table-bordered table-hover mt-3">
                    <thead class="table-light">
                        <tr>
                            <th><fmt:message key="return.id" /></th>
                            <th><fmt:message key="invoice.id" /></th>
                            <th><fmt:message key="reason" /></th>
                            <th><fmt:message key="status" /></th>
                            <th><fmt:message key="action" /></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="rt" items="${returns}">
                            <tr>
                                <td>${rt.returnID}</td>
                                <td>${rt.invoiceID}</td>
                                <td>${rt.reason}</td>
                                <td>${rt.status}</td>
                                <td class="table-actions">
                                    <c:if test="${rt.status eq 'pending'}">
                                        <form action="${pageContext.request.contextPath}/main/return/update" method="POST"
                                              class="mb-0 w-100">
                                            <input type="hidden" name="returnID" value="${rt.returnID}" />
                                            <div class="d-flex align-items-center gap-2">
                                                <select name="status" class="form-select form-select-sm mb-0">
                                                    <option value="Approved"><fmt:message key="approve" /></option>
                                                    <option value="Rejected"><fmt:message key="rejected" /></option>
                                                </select>
                                                <button type="submit" class="btn btn-sm btn-warning"><fmt:message key="update" /></button>
                                            </div>
                                        </form>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:if>

            <c:if test="${empty returns}">
                <p><fmt:message key="return.not.found" /></p>
            </c:if>
        </div>
        <jsp:include page="/footer.jsp" flush="true" />
    </body>
</html>