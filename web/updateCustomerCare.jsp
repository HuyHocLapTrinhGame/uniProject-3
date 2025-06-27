<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="i18n.label" />

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title><fmt:message key="update.customer.care" /></title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    </head>
    <body>
        <jsp:include page="/header.jsp" flush="true" />
        <div class="container bg-white p-4 shadow-sm" style="min-height: 80vh">
            <h2><fmt:message key="update.customer.care" /></h2>

            <c:if test="${not empty requestScope.MSG}">
                <div class="alert alert-info">${requestScope.MSG}</div>
            </c:if>

            <form action="${pageContext.request.contextPath}/main/customerCare/update" method="POST">
                <input type="hidden" name="ticketID" value="${requestScope.customerCare.ticketID}" />

                <input type="hidden" name="userID" value="${requestScope.customerCare.userID}" />
                <div class="mb-3">
                    <label class="form-label"><fmt:message key="user.id" /></label>
                    <input type="text" name="userID" class="form-control"
                           value="${requestScope.customerCare.userID}" readonly/>
                </div>

                <div class="mb-3">
                    <label for="subject" class="form-label"><fmt:message key="subject" /></label>
                    <input type="text" id="subject" name="subject" class="form-control" 
                           value="${requestScope.customerCare.subject}" required />
                </div>

                <div class="mb-3">
                    <label for="content" class="form-label"><fmt:message key="content" /></label>
                    <textarea id="content" name="content" class="form-control" rows="4" required>
                        ${requestScope.customerCare.content}
                    </textarea>
                </div>

                <div class="mb-3">
                    <input type="text" id="status" name="status" class="form-control" 
                           value="${requestScope.customerCare.status}" required />
                    
                    <label for="status" class="form-label"><fmt:message key="status" /></label>
                    <select id="status" name="status" required class="form-select">
                        <option value="active" ${customerCare.status == 'waiting' ? 'selected' : ''}>
                            <fmt:message key="waiting" />
                        </option>
                        <option value="inactive" ${customerCare.status == 'answered' ? 'selected' : ''}>
                            <fmt:message key="answered" />
                        </option>
                        <option value="outOfStock" ${customerCare.status == 'closed' ? 'selected' : ''}>
                            <fmt:message key="closed" />
                        </option>
                    </select>
                </div>

                <div class="mb-3">
                    <label for="reply" class="form-label"><fmt:message key="reply" /></label>
                    <textarea id="reply" name="reply" class="form-control" rows="3">
                        ${requestScope.customerCare.reply}
                    </textarea>
                </div>

                <button type="submit" class="btn btn-primary"><fmt:message key="update" /></button>
            </form>
        </div>
        <jsp:include page="/footer.jsp" flush="true" />
    </body>
</html>
