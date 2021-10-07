<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ page import="com.cbossgroup.data.enums.DictionaryType" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="f" uri="http://www.cboss.ru/functions" %>

<c:if test="${empty sessionScope.news}">
    <c:set var="news" value="${not empty param.n ? f:getNews(param.n) : null}" scope="page"/>
</c:if>

<main class="container ffr">
    <div class="newseditor__page-header">
        <span class="fs22">${empty param.n ? 'Создать' : 'Редактировать'} новость</span>
    </div>
    <div class="ribbon top fs16" style="text-align: right;">
        <div class="input-container__item">
            <span id="err-msg" style="color: red;" aria-live="polite"></span>
            <input id="file-select" class="button main-button" type="file" accept="image/*" multiple onchange="fileBtnChanged(this);">
            <label for="file-select">Выбрать файлы</label>
            <span id="file-select-status" style="font-weight: 500;">Файлы не выбраны</span>
            <button class="button main-button" type="button" onclick="uploadBtnClicked(this);">Выгрузить</button>
        </div>
    </div>
    <form id="news-form" class="fs18" action="/newseditor" autocomplete="off">
        <input name="n" type="number" value="${param.n}" hidden aria-label="СН" aria-hidden="true">
        <ol class="property-list">
            <li>
                <label for="seasonCode" class="property-label">Публикация в сезоне</label>
                <span class="property-value">
                    <select id="seasonCode" name="seasonCode" required onchange="this.setCustomValidity('');">
                        <option value="" selected disabled></option>
                        <c:forEach items="${f:getOpenSeasons()}" var="seasonCode">
                            <option value="${seasonCode}" <c:if test="${news.seasonCode eq seasonCode}">selected</c:if>>${seasonCode}</option>
                        </c:forEach>
                    </select>
                </span>
            </li>
            <li>
                <label for="fd" class="property-label">Дата и время публикации новости</label>
                <span class="property-value">
                    <input id="fd" name="fd" type="text" pattern="\d{2}\.\d{2}\.\d{4} \d{2}:\d{2}(:\d{2})?$"
                           value="<fmt:formatDate value="${empty news ? '' : news.fd}" pattern="dd.MM.yyyy HH:mm"/>"
                           placeholder="ДД.ММ.ГГГГ ЧЧ:ММ" class="marked-input">
                </span>
            </li>
            <li>
                <label for="title" class="property-label">Заголовок</label>
                <span class="property-value">
                    <textarea id="title" name="title" rows="3" style="width: 25em;"
                              required minlength="5">${empty news ? '' : news.title}</textarea>
                </span>
            </li>
            <li>
                <label for="smallBody" class="property-label">Анонс</label>
                <span class="property-value">
                    <textarea id="smallBody" name="smallBody" rows="3" style="width: 25em;"
                              required minlength="5">${empty news ? '' : news.smallBody}</textarea>
                </span>
            </li>
            <li>
                <label for="body" hidden>Тело новости</label>
                <textarea id="body" name="body" style="margin: 0 auto;">${empty news ? '' : news.body}</textarea>
            </li>
            <li>
                <label for="icon" class="property-label">Изображение</label>
                <span class="property-value">
                    <input id="icon" name="icon" type="text" class="marked-input" required placeholder="image.ext"
                           pattern="\w+\.\w{3,5}" value="${empty news ? '' : news.icon}">
                </span>
            </li>
            <li>
                <label for="type" class="property-label">Тип новости</label>
                <span class="property-value">
                    <select id="type" name="type">
                        <option value="8" <c:if test="${news.type eq 8}">selected</c:if>>Новости для КСИшников</option>
                        <option value="9" <c:if test="${news.type eq 9}">selected</c:if>>Новости для всех</option>
                    </select>
                </span>
            </li>
            <li>
                <label for="secNo" class="property-label">Номер новости</label>
                <span class="property-value">
                    <input id="secNo" name="secNo" type="number" class="marked-input" required
                           min="0" max="9999" value="${empty news ? '' : news.secNo}">
                </span>
            </li>
            <li>
                <label for="status" class="property-label">Статус новости</label>
                <span class="property-value">
                    <select id="status" name="status">
                        <c:forEach items="${f:getDictionary(DictionaryType.NEWS_STATUSES)}" var="entry">
                            <option value="${entry.key}" <c:if test="${news.status eq entry.key}">selected</c:if>>${entry.value.term}</option>
                        </c:forEach>
                    </select>
                </span>
            </li>
        </ol>
        <div class="ribbon bottom fs16">
            <a href="?view=list" onclick="aRefProcess(this); return false;" class="button main-button">Список</a>
            <a href="?view=preview" onclick="previewBtnClicked(this); return false;" class="button main-button">Предпросмотр</a>
            <button class="button main-button" type="submit" onclick="onBtnSubmitClick(this);">Сохранить</button>
        </div>
    </form>
</main>
<script>
    onCreateLoad();
</script>
<c:remove var="news" scope="session"/>