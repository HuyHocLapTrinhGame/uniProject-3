<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="i18n.label" />

<!DOCTYPE html>
<html>
    <head>
        <title><fmt:message key="cart.title" /></title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <jsp:include page="/header.jsp" flush="true" />
        <div class="container py-4" style="min-height: 80vh">
            <c:if test="${cart == null}">
                <form method="POST" action="${pageContext.request.contextPath}/main/cart/create">
                    <button type="submit" class="btn btn-success"><fmt:message key="create.cart" /></button>
                </form>
            </c:if>

            <c:if test="${!empty requestScope.MSG}">
                <div class="alert alert-success">${requestScope.MSG}</div>
            </c:if>

            <c:if test="${cart != null}">
                <h2>
                    <a 
                        href="${pageContext.request.contextPath}/main/cart" 
                        class="me-2 mb-3"
                        style="text-decoration: none; color: black">
                        <fmt:message key="your.cart" />
                    </a>
                </h2>

                <!-- User Info -->
                <div class="card mb-4">
                    <div class="card-body">
                        <p><strong><fmt:message key="fullname" />:</strong> ${cart.userFullName}</p>
                        <p><strong><fmt:message key="created.date" />:</strong> ${cart.createdDate}</p>
                    </div>
                </div>

                <!-- Form toàn bộ -->
                <form id="cartForm" method="POST">
                    <input type="hidden" name="cartID" value="${cart.cartID}" />

                    <!-- Cart table -->
                    <table class="table table-bordered table-hover align-middle">
                        <thead class="table-light">
                            <tr>
                                <th><input type="checkbox" onclick="toggleAll(this)" /></th>
                                <th><fmt:message key="no" /></th>
                                <th><fmt:message key="product" /></th>
                                <th><fmt:message key="quantity" /></th>
                                <th><fmt:message key="sale.price" /></th>
                                <th><fmt:message key="base.price" /></th>
                                <th><fmt:message key="subtotal" /></th>
                                <th><fmt:message key="action" /></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="item" items="${cart.carts}" varStatus="loop">
                                <tr>
                                    <td class="text-center">
                                        <input type="checkbox" class="form-check-input item-checkbox"
                                               name="productID" value="${item.productID}"
                                               data-subtotal="${item.subTotal}" onclick="updateTotal()" />
                                    </td>
                                    <td>${loop.index + 1}</td>
                                    <td>${item.productName}</td>
                                    <td>
                                        <input type="number" name="quantity" value="${item.quantity}"
                                               min="1" class="form-control form-control-sm" style="width: 90px;" required />
                                    </td>
                                    <td>
                                        <input type="hidden" name="price" value="${item.salePrice}" />
                                        $${item.salePrice}
                                    </td>
                                    <td><span style="text-decoration: line-through;">$${item.basePrice}</span></td>
                                    <td>$${item.subTotal}</td>
                                    <td>
                                        <button type="submit" class="btn btn-sm btn-success me-1"
                                                data-action="${pageContext.request.contextPath}/main/cart/updateItem"
                                                data-productid="${item.productID}"
                                                onclick="setActionAndProductID(this)"><fmt:message key="update" /></button>

                                        <button type="submit" class="btn btn-sm btn-danger"
                                                data-action="${pageContext.request.contextPath}/main/cart/deleteItem"
                                                data-productid="${item.productID}"
                                                onclick="setActionAndProductID(this)"><fmt:message key="delete" /></button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>

                    <!-- Tổng tiền và bulk actions -->
                    <div class="d-flex justify-content-between align-items-center mt-3">
                        <h5>Total (selected): <span id="totalAmount" class="text-success fw-bold">$0.00</span></h5>
                        <div>
                            <button type="submit" class="btn btn-danger me-2"
                                    data-action="${pageContext.request.contextPath}/main/cart/deleteItems"
                                    onclick="setAction(this)"><fmt:message key="delete.selected" /></button>

                            <button type="submit" class="btn btn-warning me-2"
                                    data-action="${pageContext.request.contextPath}/main/cart/clear"
                                    onclick="setAction(this)"><fmt:message key="clear.cart" /></button>

                            <button type="submit" class="btn btn-success"
                                    data-action="${pageContext.request.contextPath}/main/invoice/create"
                                    onclick="setAction(this)"><fmt:message key="create.invoice" /></button>
                        </div>
                    </div>
                </form>
            </c:if>
        </div>
        <jsp:include page="/footer.jsp" flush="true" />

        <!-- Scripts -->
        <script>
            function updateTotal() {
                let total = 0;
                document.querySelectorAll('.item-checkbox:checked').forEach(cb => {
                    total += parseFloat(cb.dataset.subtotal);
                });
                document.getElementById('totalAmount').innerText = "$" + total.toFixed(2);
            }

            function toggleAll(cb) {
                const checkboxes = document.querySelectorAll('.item-checkbox');
                checkboxes.forEach(chk => chk.checked = cb.checked);
                updateTotal();
            }

            function setAction(button) {
                const form = document.getElementById('cartForm');
                form.action = button.dataset.action;
            }

            function setActionAndProductID(button) {
                const form = document.getElementById('cartForm');
                form.action = button.dataset.action;

                // Clear existing hidden if có
                const old = document.querySelector('input[name="productID"]');
                if (old)
                    old.remove();

                // Add productID hidden
                const hidden = document.createElement('input');
                hidden.type = 'hidden';
                hidden.name = 'productID';
                hidden.value = button.dataset.productid;
                form.appendChild(hidden);
            }

            window.addEventListener("DOMContentLoaded", updateTotal);
        </script>
    </body>
</html>
