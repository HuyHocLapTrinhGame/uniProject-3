<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="i18n.label" />

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8" />
        <title><fmt:message key="update.user" /></title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    </head>
    <body>
        <jsp:include page="/header.jsp" flush="true" />
        <div class="post-container" style="min-height: 80vh">
            <h3><fmt:message key="update.user" /></h3>
            <p class="message">${requestScope.MSG}</p>
            <hr />

            <form id="updateForm" 
                  <c:if test="${sessionScope.currentUser.role == 'ADMIN'}">
                      action="${pageContext.request.contextPath}/main/user/update" 
                  </c:if>
                  <c:if test="${sessionScope.currentUser.role != 'ADMIN'}">
                      action="${pageContext.request.contextPath}/main/user/update-profile" 
                  </c:if>
                  method="POST"
            >
                <input type="hidden" name="userID" value="${user.userID}"/>

                <label for="fullName"><fmt:message key="fullname" /></label>
                <input type="text" id="fullName" name="fullName" value="${user.fullName}" required class="form-control mb-3" />

                <label for="phone"><fmt:message key="phone" /></label>
                <input type="text" id="phone" name="phone" value="${user.phone}" required class="form-control mb-3" />

                <c:if test="${sessionScope.currentUser.role == 'ADMIN'}">
                    <label for="roleID"><fmt:message key="role" /></label>
                    <select id="roleID" name="roleID" required class="form-select mb-3">
                        <c:forEach var="r" items="${roleList}">
                            <option value="${r.value}" ${r == user.role ? 'selected' : ''}>${r}</option>
                        </c:forEach>
                    </select>
                </c:if>

                <label for="oldPassword"><fmt:message key="old.password" /></label>
                <input type="password" id="oldPassword" name="oldPassword" placeholder="<fmt:message key="enter.old.password" />" class="form-control mb-3" />

                <label for="password"><fmt:message key="password" /></label>
                <input type="password" id="password" name="password" placeholder="<fmt:message key="enter.new.password" />" class="form-control mb-3" />

                <label for="confirmPassword"><fmt:message key="confirm.password" /></label>
                <input type="password" id="confirmPassword" name="confirmPassword" placeholder="<fmt:message key="confirm.new.password" />" class="form-control mb-3" />

                <button type="submit" class="btn btn-primary w-100"><fmt:message key="update" /></button>
            </form>
        </div>
        <jsp:include page="/footer.jsp" flush="true" />

        <script>
            const oldpwd = document.getElementById('oldPassword');
            const pwd = document.getElementById('password');
            const confirmpwd = document.getElementById('confirmPassword');
            const form = document.getElementById('updateForm');

            function checkRequired() {
                if (oldpwd.value.trim() !== '' || pwd.value.trim() !== '' || confirmpwd.value.trim() !== '') {
                    oldpwd.required = true;
                    pwd.required = true;
                    confirmpwd.required = true;
                } else {
                    oldpwd.required = false;
                    pwd.required = false;
                    confirmpwd.required = false;
                }
            }

            oldpwd.addEventListener('input', checkRequired);
            pwd.addEventListener('input', checkRequired);
            confirmpwd.addEventListener('input', checkRequired);

            form.addEventListener('submit', function (e) {
                checkRequired();
            });
        </script>
    </body>
</html>