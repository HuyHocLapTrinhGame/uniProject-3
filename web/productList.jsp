<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="i18n.label" />

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title><fmt:message key="product.list" /></title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    </head>
    <body>
        <jsp:include page="/header.jsp" flush="true" />
        <div class="container bg-white p-4 shadow-sm" style="min-height: 80vh">
            <h2 class="mb-3"><fmt:message key="product.list" /></h2>

            <c:if test="${sessionScope.currentUser.role == 'SELLER'}">
                <form action="${pageContext.request.contextPath}/main/product" method="GET" class="mb-3">
                    <button type="submit" name="action" value="create" class="btn btn-success">
                        <fmt:message key="create.new.product" />
                    </button>
                </form>
            </c:if>

            <form action="${pageContext.request.contextPath}/main/product" method="GET" class="row g-2 mb-4">           
                <div class="col-md-4">
                    <select id="searchBy" name="action" class="form-select">
                        <option value="getProductsByName"><fmt:message key="search.name" /></option>
                        <option value="getProductsByCategoryName"><fmt:message key="search.category.name" /></option>
                        <option value="getProductsByMinQuantity"><fmt:message key="search.min.quantity" /></option>
                        <option value="getProductsByStatus"><fmt:message key="search.status" /></option>
                    </select>
                </div>

                <!-- Search by name, category -->
                <div class="col-md-4 search-input search-text">
                    <input type="text" class="form-control" name="keySearch" placeholder="<fmt:message key="search" />..." />
                </div>

                <!-- Search by min quantity -->
                <div class="col-md-4 search-input search-quantity" style="display:none;">
                    <input type="number" class="form-control" name="keySearch" placeholder="<fmt:message key="enter.quantity" />" min="0" />
                </div>

                <!-- Search by status -->
                <div class="col-md-4 search-input search-status" style="display:none;">
                    <select name="keySearch" class="form-select">
                        <option value="active"><fmt:message key="active" /></option>
                        <option value="inactive"><fmt:message key="inactive" /></option>
                        <option value="outOfStock"><fmt:message key="out.of.stock" /></option>
                    </select>
                </div>

                <div class="col-md-4">
                    <button type="submit" class="btn btn-primary w-100"><fmt:message key="search" /></button>
                </div>
            </form>

            <c:if test="${!empty param.msg}">
                <div class="alert alert-success">${param.msg}</div>
            </c:if>

            <c:if test="${!empty requestScope.MSG and empty param.msg}">
                <div class="alert alert-success">${requestScope.MSG}</div>
            </c:if>

            <c:if test="${empty products}">
                <div class="alert alert-warning"><fmt:message key="product.not.found" /></div>
            </c:if>

            <c:if test="${not empty products}">
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
                        <c:forEach var="product" items="${requestScope.products}" varStatus="st">
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
        <jsp:include page="/footer.jsp" flush="true" />

        <script>
            const searchBy = document.getElementById('searchBy');
            const textInputDiv = document.querySelector('.search-text');
            const quantityInputDiv = document.querySelector('.search-quantity');
            const statusInputDiv = document.querySelector('.search-status');

            function updateSearchInput() {
                // Ẩn toàn bộ, bỏ required và disabled
                const allInputs = document.querySelectorAll('.search-input');
                allInputs.forEach(div => {
                    div.style.display = 'none';
                    const input = div.querySelector('input, select');
                    input.removeAttribute('required');
                    input.setAttribute('disabled', 'disabled'); // Ngăn submit
                });

                // Hiện div tương ứng và thêm required + enable input
                let selectedDiv = null;
                if (searchBy.value === 'getProductsByName' || searchBy.value === 'getProductsByCategoryName') {
                    selectedDiv = textInputDiv;
                } else if (searchBy.value === 'getProductsByMinQuantity') {
                    selectedDiv = quantityInputDiv;
                } else if (searchBy.value === 'getProductsByStatus') {
                    selectedDiv = statusInputDiv;
                }

                if (selectedDiv) {
                    selectedDiv.style.display = 'block';
                    const input = selectedDiv.querySelector('input, select');
                    input.removeAttribute('disabled'); // Cho phép submit
                    input.setAttribute('required', 'required');
                }
            }

            // Khởi tạo khi load
            updateSearchInput();

            // Gắn sự kiện khi thay đổi dropdown
            searchBy.addEventListener('change', updateSearchInput);
        </script>
    </body>
</html>
