<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="i18n.label" />

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title><fmt:message key="update.promotion" /></title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />
    </head>
    <body>
        <jsp:include page="/header.jsp" flush="true" />
        <div class="container mt-5" style="min-height: 80vh">
            <h2><fmt:message key="update.promotion" /></h2>

            <c:if test="${not empty MSG}">
                <div class="alert alert-info">${MSG}</div>
            </c:if>

            <form action="${pageContext.request.contextPath}/main/promotion/update" method="post">
                <input type="hidden" name="promoID" value="${promotions.promoID}" />

                <div class="mb-3">
                    <label class="form-label"><fmt:message key="promotion.name" /></label>
                    <input type="text" name="name" class="form-control" value="${promotions.name}" required />
                </div>

                <div class="mb-3">
                    <label class="form-label"><fmt:message key="discount.percent" /> (%)</label>
                    <input type="number" name="discount" class="form-control" value="${promotions.discountPercent}" step="0.01" min="0" max="100" required />
                </div>

                <div class="mb-3">
                    <label class="form-label"><fmt:message key="start.date" /></label>
                    <input type="date" name="startDate" class="form-control" value="${promotions.startDate}" required />
                </div>

                <div class="mb-3">
                    <label class="form-label"><fmt:message key="end.date" /></label>
                    <input type="date" name="endDate" class="form-control" value="${promotions.endDate}" required />
                </div>

                <div class="mb-3">
                    <label class="form-label"><fmt:message key="status" /></label>
                    <select name="status" class="form-select" required>
                        <option value="active" ${promotions.status == 'active' ? 'selected' : ''}><fmt:message key="active" /></option>
                        <option value="inactive" ${promotions.status == 'inactive' ? 'selected' : ''}><fmt:message key="inactive" /></option>
                    </select>
                </div>

                <button type="submit" class="btn btn-primary"><fmt:message key="update" /></button>
            </form>
        </div>
        <jsp:include page="/footer.jsp" flush="true" />s
    </body>
</html>
