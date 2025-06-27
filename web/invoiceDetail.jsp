<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="i18n.label" />

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><fmt:message key="invoice.detail" /></title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    </head>
    <body>
        <jsp:include page="/header.jsp" flush="true" />
        <div class="container bg-white p-4 shadow-sm" style="min-height: 80vh">
            <jsp:include page="navbarInvoice.jsp" />
            
            <h2 class="mt-3"><fmt:message key="invoice.detail" /></h2>
            <!-- Thông tin hóa đơn -->
            <p><strong><fmt:message key="fullname" />:</strong> ${invoiceViewModel.userName}</p>

            <c:choose>
                <c:when test="${!empty requestScope.returnStatus}">
                    <p><strong><fmt:message key="status" />:</strong> ${requestScope.returnStatus}</p>
                    <p><strong><fmt:message key="reason" />:</strong> ${requestScope.reason}</p>
                </c:when>
                <c:otherwise>
                    <p><strong><fmt:message key="status" />:</strong> ${invoiceViewModel.status}</p>
                </c:otherwise>
            </c:choose>

            <p><strong><fmt:message key="created.date" />:</strong> ${invoiceViewModel.createdDate}</p>

            <!-- Hidden fields -->
            <input type="hidden" name="invoiceID" value="${invoiceViewModel.invoiceID}" />

            <table class="table table-bordered table-hover">
                <thead class="table-light">
                    <tr>
                        <th><fmt:message key="product.name" /></th>
                        <th><fmt:message key="quantity" /></th>
                        <th><fmt:message key="price" /></th>
                        <th><fmt:message key="subtotal" /></th>
                        <th><fmt:message key="action" /></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="detail" items="${invoiceViewModel.invoiceDetailList}">
                        <tr>
                            <td>${detail.productName}</td>

                            <!-- Form Update: mỗi sản phẩm có 1 form riêng -->
                            <td style="width: 200px">
                                <c:choose>
                                    <c:when test="${requestScope.status == 'pending'}">
                                        <form action="${pageContext.request.contextPath}/main/invoice/updateInvoiceDetailQuantity" method="POST">
                                            <input type="hidden" name="invoiceID" value="${invoiceViewModel.invoiceID}" />
                                            <input type="hidden" name="productID" value="${detail.productID}" />
                                            <input type="number" name="quantity" value="${detail.quantity}" 
                                                    min="1" required class="form-control"/>
                                        </c:when>
                                        <c:otherwise>
                                            <input type="number" name="quantity" value="${detail.quantity}" readonly 
                                                    class="form-control"/>
                                        </c:otherwise>
                                    </c:choose>
                            </td>

                            <td><fmt:formatNumber type="number" maxFractionDigits="2" value="${detail.price}" /></td>
                            <td><fmt:formatNumber type="number" maxFractionDigits="2" value="${detail.subTotalAmount}" /></td>

                            <!-- Action: mỗi form update và delete riêng biệt -->
                            <td>
                                <c:if test="${requestScope.status == 'pending'}">
                                    <!-- Nút Update nằm trong form -->
                                    <button type="submit" class="btn btn-sm btn-warning"><fmt:message key="update" /></button>
                                    </form> <!-- Đóng form update tại đây -->

                                    <form action="${pageContext.request.contextPath}/main/invoice/deleteInvoiceDetail" method="POST" style="display: inline;">
                                        <input type="hidden" name="invoiceID" value="${invoiceViewModel.invoiceID}" />
                                        <input type="hidden" name="status" value="pending" />
                                        <input type="hidden" name="productID" value="${detail.productID}" />
                                        <button type="submit" class="btn btn-sm btn-danger"><fmt:message key="delete" /></button>
                                    </form>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>


            <h4><fmt:message key="total.amount" />: <fmt:formatNumber type="number" maxFractionDigits="2" value="${invoiceViewModel.totalAmount}" /></h4>
            <c:if test="${!empty requestScope.MSG}">
                <h3 style="color: green">${MSG}</h3>
            </c:if>
            <c:if test="${requestScope.status == 'pending'}">
                <div class="d-flex gap-2 w-100">
                    <form action="${pageContext.request.contextPath}/main/delivery/create" method="POST"
                           class="d-flex align-items-center gap-2">
                        <input type="hidden" name="status" value="paid" />
                        <input type="hidden" name="invoiceID" value="${invoiceViewModel.invoiceID}" />
                        <input type="hidden" name="createdDate" value="${invoiceViewModel.createdDate}" />
                        <input type="text" name="address" placeholder="<fmt:message key="enter.address" />" class="form-control" required>
                        <button type="submit" class="btn btn-primary" style="width: 200px" id="paid"><fmt:message key="paid" /></button>
                    </form>
                    <form action="${pageContext.request.contextPath}/main/invoice/updateInvoiceStatus" method="POST">
                        <input type="hidden" name="status" value="cancel" />
                        <input type="hidden" name="invoiceID" value="${invoiceViewModel.invoiceID}" />
                        <button type="submit" class="btn btn-danger" id="cancel"><fmt:message key="cancel" /></button>
                    </form>
                </div>
            </c:if>
            <c:if test="${requestScope.status == 'paid' or requestScope.status == 'delivered'}">
                <form action="${pageContext.request.contextPath}/main/return/create" method="POST">
                    <input type="hidden" name="status" value="return" />
                    <input type="hidden" name="invoiceID" value="${invoiceViewModel.invoiceID}" />
                    <textarea type="text" name="reason" placeholder="<fmt:message key="enter.reason" />" 
                              class="form-control mb-3" required=""></textarea>
                    <button type="submit" class="btn btn-danger" id="cancel"><fmt:message key="return" /></button>
                </form>
            </c:if>
        </div>
        <jsp:include page="/footer.jsp" flush="true" />
    </body>
</html>
