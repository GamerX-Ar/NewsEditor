<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="auth" value="${ not empty sessionScope.user}"/>

<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/pages/includes/SITE_VERSION.jsp" %>
    <%@include file="/WEB-INF/pages/includes/include_BS_JQ.jsp" %>
    <script src="/js/lib/jquery/jquery-ui.min.js"></script>
    <link href="${pageContext.request.contextPath}/css/lib/jquery/ui/jquery-ui.min.css" rel="stylesheet">
    <%@include file="/WEB-INF/pages/includes/includes_main_css.jsp" %>
    <%@include file="/WEB-INF/pages/includes/include_dlgwnd_js.jsp" %>
    <link href="${pageContext.request.contextPath}/css/emoji.css?v=${site_version}" rel="stylesheet">
</head>

<body class="m-home">
    <div id="wrapper" class="wrapper">
        <%@include file="/WEB-INF/pages/common/headerLogo2.jsp" %>
        <div class="b-content-holder">

            <%--НАЧАЛО КОНТЕНТА --%>

            <h3 style="text-align: center;"><c:out value="${sessionScope.news.title}" escapeXml="false" /></h3>
            <br>
            <c:out value="${sessionScope.news.body}" escapeXml="false" />
            <p><fmt:formatDate pattern="dd.MM.yyyy" value="${sessionScope.news.fd}" /> #${sessionScope.news.secNo}</p>
            <br>
            <%-- like & poll --%>
            <br>
            <div id="news_like" style="background-color: #d0d0d0;">Тут будет форма оценки новости!</div>

            <%-- КОНЕЦ  КОНТЕНТА --%>
        </div>

    </div>
</body>
</html>