<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="i18n.label" />

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8" />
        <title><fmt:message key="create.product.title" /></title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    </head>
    <body>
        <jsp:include page="/header.jsp" flush="true" />
        <div class="post-container" style="min-height: 80vh">
            <h3><fmt:message key="create.new.product" /></h3>

            <p class="message">${requestScope.MSG}</p>

            <form action="${pageContext.request.contextPath}/main/product/create" method="POST">
                <label for="name"><fmt:message key="product.name" /></label>
                <input type="text" id="name" name="name" placeholder="<fmt:message key="enter.product.name" />" required class="form-control mb-3" />

                <label for="categoryID"><fmt:message key="category.id" /></label>
                <input type="number" id="categoryID" name="categoryID" placeholder="<fmt:message key="enter.category.id" />" required class="form-control mb-3" min="1" />

                <label for="price"><fmt:message key="price" /></label>
                <input type="number" id="price" name="price" placeholder="<fmt:message key="enter.price" />" required class="form-control mb-3" min="0" step="0.01"/>

                <label for="quantity"><fmt:message key="quantity" /></label>
                <input type="number" id="quantity" name="quantity" placeholder="<fmt:message key="enter.quantity" />" required class="form-control mb-3" min="1" />

                <label for="status"><fmt:message key="status" /></label>
                <select id="status" name="status" required class="form-select mb-3">
                    <option value="active"><fmt:message key="active" /></option>
                    <option value="inactive"><fmt:message key="inactive" /></option>
                </select>

                <label for="promoID"><fmt:message key="promotion.id" /></label>
                <input type="number" id="promoID" name="promoID" placeholder="<fmt:message key="enter.promotion.id" />" class="form-control mb-3"/>

                <button type="submit" class="btn btn-primary w-100"><fmt:message key="create" /></button>
            </form>
        </div>
        <jsp:include page="/footer.jsp" flush="true" />
    </body>
</html>
