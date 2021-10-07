<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ page import="com.cbossgroup.data.enums.DictionaryType" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri = "http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="f" uri="http://www.cboss.ru/functions" %>

<%-- Пагинация --%>
<c:set var="news_count" value="${f:getNewsCount()}"/> <%-- всего новостей --%>
<c:set var="limit" value="10"/> <%-- кол-во на странице --%>
<c:set var="page_count" value="${(news_count - news_count % limit) / limit + (news_count % limit > 0 ? 1 : 0)}"/>
<fmt:formatNumber var="page_count" value="${page_count}" pattern="#"/> <%-- вычисленное кол-во страниц --%>
<c:set var="page_step" value="3"/> <%-- pages before and after current --%>
<c:set var="curr_page" value="${param.page eq null ? 1 : param.page}"/>

<main class="container">
    <div class="newseditor__page-header ffr">
        <span class="fs22">Список новостей</span>
        <a href="?view=create" class="button main-button fs16" onclick="aRefProcess(this); return false;">Создать</a>
    </div>
    <div class="table-wrapper news-list">
        <table class="table ffr fs18">
            <thead>
            <tr>
                <th>n</th>
                <th>Title</th>
                <th>Type</th>
                <th>Status</th>
                <th>SecNo</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${f:getNewsList((curr_page - 1) * limit, limit)}" var="item">
                <tr>
                    <td>${item.n}</td>
                    <td><a href="?view=show&n=${item.n}" onclick="aRefProcess(this); return false;">${fn:escapeXml(item.title)}</a></td>
                    <td>${f:getDictionary(DictionaryType.OBJECTS).get(item.type).term}</td>
                    <td>${f:getDictionary(DictionaryType.NEWS_STATUSES).get(item.status).term}</td>
                    <td>${item.secNo}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <div class="pagination ribbon bottom ffr fs18">
        <a href="?view=list&page=${curr_page eq 1 ? 1 : curr_page - 1}" onclick="aRefProcess(this); return false;">&#9668;</a>
        <span>
            <c:choose>
                <c:when test="${page_count < page_step * 2}">
                    <c:forEach begin="1" end="${page_count}" var="i">
                        <a class="${curr_page eq i ? 'current' : ''}"
                           href="?view=list&page=${i}" onclick="aRefProcess(this); return false;">${i}</a>
                    </c:forEach>
                </c:when>
                <c:when test="${curr_page < page_step * 2}">
                    <c:forEach begin="1" end="${page_step * 2}" var="i">
                        <a class="${curr_page eq i ? 'current' : ''}"
                           href="?view=list&page=${i}" onclick="aRefProcess(this); return false;">${i}</a>
                    </c:forEach>
                    <i>...</i><a href="?view=list&page=${page_count}" onclick="aRefProcess(this); return false;">${page_count}</a>
                </c:when>
                <c:when test="${curr_page > page_count - page_step * 2}">
                    <a href="?view=list&page=1" onclick="aRefProcess(this); return false;">1</a><i>...</i>
                    <c:forEach begin="${page_count - page_step * 2}" end="${page_count}" var="i">
                        <a class="${curr_page eq i ? 'current' : ''}"
                           href="?view=list&page=${i}" onclick="aRefProcess(this); return false;">${i}</a>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <a href="?view=list&page=1" onclick="aRefProcess(this); return false;">1</a><i>...</i>
                    <c:forEach begin="${curr_page - page_step}" end="${curr_page + page_step}" var="i">
                        <a class="${curr_page eq i ? 'current' : ''}"
                           href="?view=list&page=${i}" onclick="aRefProcess(this); return false;">${i}</a>
                    </c:forEach>
                    <i>...</i><a href="?view=list&page=${page_count}" onclick="aRefProcess(this); return false;">${page_count}</a>
                </c:otherwise>
            </c:choose>
        </span>
        <a href="?view=list&page=${curr_page eq page_count ? page_count : curr_page + 1}" onclick="aRefProcess(this); return false;">&#9658;</a>
    </div>
</main>