<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="f" uri="http://www.hubble.com/functions" %>

<c:set var="auth" value="${ not empty sessionScope.user}"/>
<c:if test="${ not auth }">
    <jsp:forward page="/login/"/>
</c:if>

<!DOCTYPE html>
<html lang="ru">
<head>
    <%@include file="/WEB-INF/pages/includes/SITE_VERSION.jsp" %>
    <meta name="robots" content="noindex">
    <%-- NOTE: Порядок подключения скриптов обуслолен следующим фактом:
             bootstrap.js переопределяет некоторые методы из jquery-ui.js, однако нуждается в jquery.js --%>
    <script src="/js/lib/jquery/jquery.min.js"></script>
    <script src="/js/lib/bootstrap/bootstrap.min.js" defer></script>
    <script src="/js/lib/jquery/jquery-ui.min.js" defer></script>
    <script src="/js/lib/datetime/tail.datetime.min.js"></script>
    <script src="/js/lib/datetime/tail.datetime-ru.js"></script>
    <script src="/js/lib/tinymce/tinymce.min.js"></script>
    <script src="/js/common/content.manager.js" defer></script>
    <script src="/js/common/general.js"></script>
    <script src="/js/modal/popper.js?v=${site_version}" defer></script>
    <script src="/js/pages/newseditor/newseditor.js"></script>

    <link rel="stylesheet" href="/css/lib/jquery/ui/jquery-ui.min.css">
    <link rel="stylesheet" href="/css/lib/bootstrap/bootstrap.min.css">
    <%--стили preheader, header, logo_ksi&parners, footer--%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles/styles_new_design.css?v=${site_version}">
    <%--font-face--%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/font-faces.css?v=${site_version}">
    <%--font style-font size--%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/fs-fs.css?v=${site_version}">
    <%--новые стили header'а--%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles/content.css?v=${site_version}">

    <link rel="stylesheet" href="/css/lib/datetime/tail.datetime-default-blue.min.css" />

    <link rel="stylesheet" href="/css/styles/common/inputs.css?v=${site_version}">
    <link rel="stylesheet" href="/css/styles/common/buttons.css?v=${site_version}">
    <link rel="stylesheet" href="/css/styles/common/tables.css?v=${site_version}">

    <link rel="stylesheet" href="/css/newseditor.css">

</head>
<body>
<div class="wrapper">
    <%--секция содержит прехедер - социальные сети и основные кнопки--%>
    <%@include file="/WEB-INF/pages/common/preheader_menu_new_design.jsp" %>
    <%--секция содержит меню страницы--%>
    <%@include file="/WEB-INF/pages/common/menu_new_design.jsp" %>

    <div class="content-holder">
        <c:choose>
            <c:when test="${param.view eq 'show'}">
                <jsp:include page="show.jsp"/>
            </c:when>
            <c:when test="${param.view eq 'create' or param.view eq 'edit'}">
                <jsp:include page="create.jsp"/>
            </c:when>
            <c:otherwise>
                <jsp:include page="list.jsp"/>
            </c:otherwise>
        </c:choose>
    </div>
</div>
<div id="popup" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="modal-title" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" style="user-select: none;" data-dismiss="modal" aria-label="Закрыть">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 id="popup-title" class="modal-title"></h4>
            </div>
            <div id="popup-body" class="modal-body">

            </div>
            <div id="popup-footer" class="modal-footer">

            </div>
        </div>
    </div>
</div>
</body>
</html>
