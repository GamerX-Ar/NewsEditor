<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ page import="com.hubble.data.domain.Dictionary" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="f" uri="http://www.hubble.com/functions" %>

<c:remove var="news" scope="session"/>
<c:set var="news" value="${ f:getNews(param.n)}"/>

<main class="container ffr">
    <div class="newseditor__page-header">
        <span class="fs22">Детальная информация</span>
    </div>
    <ol class="property-list fs18">
        <li>
            <span id="season-label" class="property-label">Публикация в сезоне</span>
            <span class="property-value" aria-labelledby="season-label">${news.seasonCode}</span>
        </li>
        <li>
            <span id="fd-label" class="property-label">Дата и время публикации новости</span>
            <span class="property-value" aria-labelledby="fd-label"><fmt:formatDate value="${news.fd}" pattern="dd-MM-yyyy HH:mm:ss"/></span>
        </li>
        <li>
            <span id="td-label" class="property-label">Дата и время закрытия новости</span>
            <span class="property-value" aria-labelledby="fd-label"><fmt:formatDate value="${news.td}" pattern="dd-MM-yyyy HH:mm:ss"/></span>
        </li>
        <li>
            <span id="title-label" class="property-label">Название</span>
            <span class="property-value" aria-labelledby="title-label">${fn:escapeXml(news.title)}</span>
        </li>
        <li>
            <span id="smallBody-label" class="property-label">Анонс</span>
            <span class="property-value" aria-labelledby="smallBody-label">${fn:escapeXml(news.smallBody)}</span>
        </li>
        <li>
            <span id="body-label" class="property-label">Тело новости</span>
            <span class="property-value" aria-labelledby="body-label">${fn:escapeXml(news.body)}</span>
        </li>
        <li>
            <span id="icon-label" class="property-label">Изображение</span>
            <span class="property-value" aria-labelledby="icon-label">${news.icon}</span>
        </li>
        <li>
            <span id="type-label" class="property-label">Тип новости</span>
            <span class="property-value">${f:getDictionary(Dictionary.Type.OBJECTS).get(news.type).term}</span>
        </li>
        <li>
            <span id="secNo-label" class="property-label">Порядковый номер новости</span>
            <span class="property-value" aria-labelledby="secNo-label">${news.secNo}</span>
        </li>
        <li>
            <span id="status-label" class="property-label">Статус новости</span>
            <span class="property-value" aria-labelledby="status-label">${f:getDictionary(Dictionary.Type.NEWS_STATUSES).get(news.status).term}</span>
        </li>
    </ol>
    <div class="ribbon bottom fs16">
        <a href="?view=list" onclick="aRefProcess(this); return false;" class="button main-button">Список</a>
        <a href="?view=create" onclick="aRefProcess(this); return false;" class="button main-button">Создать</a>
        <a href="?view=edit&n=${param.n}" onclick="aRefProcess(this); return false;" class="button main-button">Редактировать</a>
        <a href="?view=delete&n=${param.n}" onclick="deleteBtnClicked(this); return false;" class="button main-button">Удалить</a>
    </div>
</main>