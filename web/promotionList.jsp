<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="i18n.label" />

<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title><fmt:message key="promotion.list" /></title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    </head>
    <body>
        <jsp:include page="/header.jsp" flush="true" />
        <div class="container mt-4" style="min-height: 80vh">
            <h2 class="mb-3"><fmt:message key="promotion.list" /></h2>

            <c:if test="${sessionScope.currentUser.role == 'MARKETING'}">
                <form action="${pageContext.request.contextPath}/main/promotion/create" method="GET" class="mb-3">
                    <button type="submit" class="btn btn-success"><fmt:message key="create.new.promotion" /></button>
                </form>
            </c:if>

            <form action="${pageContext.request.contextPath}/main/promotion" method="GET" class="row g-2 mb-3">
                <div class="col-md-6">
                    <input type="text" name="keySearch" class="form-control" placeholder="<fmt:message key="enter.promotion.name" />..." value="${param.keySearch}" />
                </div>
                <div class="col-md-auto">
                    <button type="submit" name="action" value="searchByName" class="btn btn-primary"><fmt:message key="search" /></button>
                    <a href="${pageContext.request.contextPath}/main/promotion" class="btn btn-secondary"><fmt:message key="clear" /></a>
                </div>
            </form>

            <c:if test="${not empty requestScope.MSG}">
                <div class="alert alert-info">${requestScope.MSG}</div>
            </c:if>

            <c:choose>
                <c:when test="${not empty requestScope.promotions}">
                    <c:forEach var="promotion" items="${requestScope.promotions}" varStatus="status">
                        <div class="mb-4 p-3 border border-primary rounded">
                            <input type="hidden" value="${promotion.promoID}" />

                            <h4 class="mb-2">
                                <form action="${pageContext.request.contextPath}/main/promotion/update" method="GET" style="display:inline;">
                                    <input type="hidden" name="keySearch" value="${promotion.promoID}"/>
                                    <button type="submit" class="btn btn-link p-0 m-0 fw-bold" style="text-decoration: none;">
                                        #${promotion.promoID} - ${promotion.name}
                                    </button>
                                </form>
                            </h4>

                            <ul class="mb-3 list-unstyled ps-3">
                                <li><strong><fmt:message key="discount.percent" />:</strong> ${(promotion.discountPercent * 10000).intValue() / 100.0}%</li>
                                <li><strong><fmt:message key="start.date" />:</strong> ${promotion.startDate}</li>
                                <li><strong><fmt:message key="end.date" />:</strong> ${promotion.endDate}</li>
                                <li><strong><fmt:message key="status" />:</strong> ${promotion.status}</li>
                                <li><strong><fmt:message key="number.of.products" />:</strong> 
                                    <c:choose>
                                        <c:when test="${not empty promotion.products}">
                                            ${fn:length(promotion.products)}
                                        </c:when>
                                        <c:otherwise>0</c:otherwise>
                                    </c:choose>
                                </li>
                            </ul>

                            <c:if test="${sessionScope.currentUser.role == 'MARKETING'}">
                                <div class="mb-2">
                                    <form action="${pageContext.request.contextPath}/main/promotion/update" method="GET" style="display:inline;">
                                        <input type="hidden" name="keySearch" value="${promotion.promoID}"/>
                                        <button class="btn btn-sm btn-outline-primary"><fmt:message key="update" /></button>
                                    </form>
                                    <form action="${pageContext.request.contextPath}/main/promotion/delete" method="POST" style="display:inline;" 
                                          onsubmit="return confirm('<fmt:message key="confirm.delete.promotion" />');">
                                        <input type="hidden" name="promoID" value="${promotion.promoID}"/>
                                        <button class="btn btn-sm btn-outline-danger"><fmt:message key="delete" /></button>
                                    </form>
                                </div>
                            </c:if>
                            <c:if test="${not empty promotion.products}">
                                <table class="table table-bordered table-hover">
                                    <thead class="table-light">
                                        <tr>
                                            <th><fmt:message key="no" /></th>
                                            <th><fmt:message key="product.name" /></th>
                                            <th><fmt:message key="category.name" /></th>
                                            <th><fmt:message key="sale.price" /></th>
                                            <th><fmt:message key="base.price" /></th>
                                            <th><fmt:message key="quantity.stock" /></th>
                                            <th><fmt:message key="seller.name" /></th>
                                            <th><fmt:message key="status" /></th>
                                            <th><fmt:message key="action" /></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="product" items="${promotion.products}" varStatus="st">
                                            <tr>
                                                <td>${st.count}</td>
                                                <td>${product.name}</td>
                                                <td>${product.categoryName}</td>
                                                <td class="text-success">${product.salePrice}</td>
                                                <td class="text-muted" style="text-decoration: line-through;">${product.basePrice}</td>
                                                <td>${product.quantity}</td>
                                                <td>${product.sellerFullName}</td>
                                                <td>${product.status}</td>
                                                <td class="table-actions gap-2">
                                                    <c:choose>
                                                        <c:when test="${sessionScope.currentUser.role == 'SELLER' and sessionScope.currentUser.userID eq product.sellerID}">
                                                            <form 
                                                                action="${pageContext.request.contextPath}/main/product/update" 
                                                                method="GET"
                                                                >
                                                                <button type="submit" name="productID" value="${product.productID}" class="btn btn-sm btn-warning">
                                                                    <fmt:message key="update" />
                                                                </button>
                                                            </form>
                                                            <form 
                                                                action="${pageContext.request.contextPath}/main/product/delete" 
                                                                method="POST" 
                                                                onsubmit="return confirm('<fmt:message key="confirm.delete.product" />');"
                                                                >
                                                                <button type="submit" name="productID" value="${product.productID}" class="btn btn-sm btn-danger">
                                                                    <fmt:message key="delete" />
                                                                </button>
                                                            </form>
                                                        </c:when>
                                                        <c:when test="${sessionScope.currentUser.role == 'BUYER' and product.status eq 'active'}">
                                                            <form 
                                                                action="${pageContext.request.contextPath}/main/cart/addToCart" 
                                                                method="POST"
                                                                class="table-actions gap-2"
                                                                >
                                                                <input type="hidden" name="returnUrl" value="/main/product" readonly />
                                                                <input type="hidden" name="returnMethod" value="GET" readonly />
                                                                <input type="number" name="quantity" class="form-control" 
                                                                       placeholder="<fmt:message key="enter.quantity" />" style="width:150px"
                                                                       min="1" max="${product.quantity}" required />
                                                                <button type="submit" name="productID" value="${product.productID}" class="btn btn-sm btn-warning">
                                                                    <fmt:message key="add.to.cart" />
                                                                </button>
                                                            </form>
                                                        </c:when>
                                                    </c:choose>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </c:if>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="alert alert-warning"><fmt:message key="promotion.not.found" /></div>
                </c:otherwise>
            </c:choose>
        </div>
        <jsp:include page="/footer.jsp" flush="true" />
    </body>
</html>
