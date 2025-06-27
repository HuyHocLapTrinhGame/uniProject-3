<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="i18n.label" />

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8" />
        <title><fmt:message key="create.user.title" /></title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    </head>
    <body>
        <jsp:include page="/header.jsp" flush="true" />
        <div class="post-container" style="min-height: 80vh">
            <h3><fmt:message key="create.new.user" /></h3>

            <p class="message">${requestScope.MSG}</p>

            <form action="${pageContext.request.contextPath}/main/user/create" method="POST">
                <label for="userID"><fmt:message key="user.id" /></label>
                <input type="text" id="userID" name="userID" placeholder="<fmt:message key="user.enter.id" />" required class="form-control mb-3" />

                <label for="fullName"><fmt:message key="fullname" /></label>
                <input type="text" id="fullName" name="fullName" placeholder="<fmt:message key="user.enter.fullName" />" required class="form-control mb-3" />

                <label for="phone"><fmt:message key="phone" /></label>
                <input type="text" id="phone" name="phone" placeholder="<fmt:message key="user.enter.phone" />" required class="form-control mb-3" />

                <label for="roleID"><fmt:message key="role" /></label>
                <select id="roleID" name="roleID" required class="form-select mb-3">
                    <c:forEach var="r" items="${roleList}">
                        <option value="${r.value}">${r}</option>
                    </c:forEach>
                </select>

                <label for="password"><fmt:message key="password" /></label>
                <input type="password" id="password" name="password" placeholder="<fmt:message key="user.enter.password" />" required class="form-control mb-3" />

                <label for="confirmPassword"><fmt:message key="confirm.password" /></label>
                <input type="password" id="confirmPassword" name="confirmPassword" placeholder="<fmt:message key="user.enter.confirmPassword" />" required class="form-control mb-3" />

                <button type="submit" class="btn btn-primary w-100"><fmt:message key="create" /></button>
            </form>
        </div>
        <jsp:include page="/footer.jsp" flush="true" />
    </body>
</html>
