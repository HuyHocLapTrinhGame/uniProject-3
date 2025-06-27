<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="i18n.label" />

<!-- Footer -->
<footer class="bg-dark text-white py-5">
    <div class="container">
        <div class="row">
            <!-- Introduce -->
            <div class="d-flex justify-content-around mb-3">
                <div>
                    <h2 class="h4 fw-bold mb-3"><fmt:message key="shop.name" /></h2>
                    <p class="small mb-3">
                        <fmt:message key="introduce" />
                    </p>
                    <p class="small mb-1">
                        <fmt:message key="email.value" var="emailAddress" />
                        <fmt:message key="email" />: <a href="mailto:${emailAddress}" class="text-info text-decoration-none">
                            ${emailAddress}
                        </a>
                    </p>
                    <fmt:message key="phone.value" var="phoneNumber" />
                    <p class="small"><fmt:message key="phone" />: ${phoneNumber}</p>
                </div>

                <!-- social -->
                <div class="col-md-3 d-flex flex-column align-items-start">
                    <h3 class="h5 fw-semibold mb-3"><fmt:message key="connect.with.us" /></h3>
                    <div class="d-flex gap-3 mb-3 d-flex flex-column">
                        <a href="#" class="text-white text-decoration-none"><fmt:message key="facebook" /></a>
                        <a href="#" class="text-white text-decoration-none"><fmt:message key="instagram" /></a>
                        <a href="#" class="text-white text-decoration-none"><fmt:message key="zalo" /></a>
                    </div>

                </div>
            </div>
            <div class="d-flex  justify-content-center">
                <p class="small mb-0" "><fmt:message key="copyright" /></p>
            </div>
        </div>
    </div>
</footer>
