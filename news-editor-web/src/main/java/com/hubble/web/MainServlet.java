package com.hubble.web;

import com.hubble.data.domain.News;
import com.hubble.data.domain.User;
import com.hubble.service.NewsService;
import org.json.JSONException;
import org.json.JSONObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@WebServlet(name = "MainServlet", urlPatterns = "/newseditor")
public class MainServlet extends HttpServlet {

    private static final String MAIN_PAGE = "/WEB-INF/pages/admin/newseditor/news/main.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Проверка доступа
        User user = (User)req.getSession().getAttribute("user");
        if ( user != null && !user.hasRole(User.Role.EDITOR) ) {
            System.out.println("An error occurred while creating/editing news. Access denied.");
            resp.sendRedirect("/main");
            return;
        }
        BaseHttpServlet.includePage(MAIN_PAGE, req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        // Проверка доступа
        User user = (User)req.getSession().getAttribute("user");
        if ( user == null ) {
            System.out.println("An error occurred while creating/editing news. Not authorized.");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("Время сессии истекло, пожалуйста, авторизуйтесь снова.");
            return;
        }
        if ( !user.hasRole(User.Role.EDITOR) ) {
            System.out.println("An error occurred while creating/editing news. Access denied.");
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.getWriter().write("У Вас недостаточно прав для выполнения данной операции.");
            return;
        }

        // Проверка наличия обязательных полей
        List<String> properties = Arrays.asList("title", "smallBody", "body", "icon", "type", "secNo", "status", "seasonCode");
        for ( String prop : properties ) {
            if ( req.getParameter(prop) == null || req.getParameter(prop).trim().isEmpty() ) {
                System.out.println("An error occurred while creating/editing news. Required parameter " + prop + " is not found.");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Отсутствует параметр: " + prop);
                return;
            }
        }

        // Создание новости из принятых данных
        News news = new News();
        try {
            if ( req.getParameter("fd") != null && !req.getParameter("fd").trim().isEmpty() ) {
                news.setFd(req.getParameter("fd"));
            } else {
                news.setFd(Timestamp.valueOf(LocalDateTime.now()));
            }
            news.setType(Integer.valueOf(req.getParameter("type")));
            news.setSecNo(Integer.valueOf(req.getParameter("secNo")));
            news.setStatus(Integer.valueOf(req.getParameter("status")));
            news.setSeasonCode(Integer.valueOf(req.getParameter("seasonCode")));
        } catch (IllegalArgumentException e) {
            System.out.println("An error occurred while creating/editing news. A parsing error has occurred.");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Произошла ошибка разбора свойств новости на сервере.");
            return;
        } catch (DateTimeParseException e) {
            System.out.println("An error occurred while creating/editing news. A date parsing error has occurred.");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Произошла ошибка разбора даты новости на сервере.");
            return;
        }
        news.setTitle(req.getParameter("title"));
        news.setSmallBody(req.getParameter("smallBody"));
        news.setBody(req.getParameter("body").replaceAll("(?i)<p(.*)><iframe", "<p class=\"video-container\"><iframe"));
        news.setIcon(req.getParameter("icon"));
        news.setAuthorId(user.getId());

        // Сохранение новости в БД
        try {
            if ( req.getParameter("n") != null && !req.getParameter("n").isEmpty() ) {
                news.setId(Long.valueOf(req.getParameter("n")));
                NewsService.updateNews(news);
            } else { // только редактирование новости имеет поле N
                news.setId(NewsService.createNews(news));
            }
        } catch (NumberFormatException e) {
            System.out.println("An error occurred while creating/editing news. A parsing error has occurred.");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Произошла ошибка разбора свойств новости на сервере.");
            return;
        } catch (Exception e) {
            System.out.println("An error occurred while creating/editing news.");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(e.getMessage());
            return;
        }

        resp.getWriter().write(String.format("/newseditor?view=show&n=%d", news.getId()));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        // Проверка доступа
        User user = (User)req.getSession().getAttribute("user");
        if ( user == null ) {
            System.out.println("An error occurred while creating/editing news. Not authorized.");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("Время сессии истекло, пожалуйста, авторизуйтесь снова.");
            return;
        }
        if ( !user.hasRole(User.Role.EDITOR) ) {
            System.out.println("An error occurred while creating/editing news. Access denied.");
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.getWriter().write("У Вас недостаточно прав для выполнения данной операции.");
            return;
        }

        // Извлечение тела запроса
        Scanner scanner = new Scanner(req.getInputStream(), "UTF-8");
        String requestBody = scanner.hasNext() ? scanner.useDelimiter("\\A").next() : "";

        // Разбор тела запроса
        JSONObject jsonNews;
        try {
            jsonNews = new JSONObject(requestBody);
        } catch (JSONException e) {
            System.out.println("An error occurred while previewing news.");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Не удалось разобрать параметры запроса.");
            return;
        }

        // Удалить SN новости
        jsonNews.remove("n");

        // Создание новости из принятых данных
        News news;
        try {
            news = new News(jsonNews);
        } catch (InvocationTargetException e) {
            System.out.println("An error occurred while previewing news.");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(e.getTargetException().getMessage());
            return;
        } catch (IllegalAccessException | IllegalArgumentException e) {
            System.out.println("An error occurred while previewing news.");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(e.getMessage());
            return;
        }

        if ( news.getBody() != null && !news.getBody().trim().isEmpty() ) {
            news.setBody(news.getBody().replaceAll("(?i)<p(.*)><iframe", "<p class=\"video-container\"><iframe"));
        }

        req.getSession().setAttribute("news", news);

        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.setHeader("Location", new URL(new URL(req.getRequestURL().toString()),
                "/newseditor?view=preview").toString());
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        User user = (User)req.getSession().getAttribute("user");

        // Проверка доступа
        if ( user == null ) {
            System.out.println("An error occurred while deleting news. Not authorized.");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("Время сессии истекло, пожалуйста, авторизуйтесь снова.");
            return;
        }
        if ( !user.hasRole(User.Role.EDITOR) ) {
            System.out.println("An error occurred while creating/editing news. Access denied.");
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.getWriter().write("У Вас недостаточно прав для выполнения данной операции.");
            return;
        }

        String newsN = req.getParameter("n");
        if ( newsN == null || newsN.isEmpty() ) {
            System.out.println("An error occurred while deleting news. Failed to retrieve news N.");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Не удалось получить СН новости.");
            return;
        }

        News news = new News();
        try {
            news.setId(Long.valueOf(newsN));
        } catch (NumberFormatException e) {
            System.out.println("An error occurred while deleting news. A parsing error has occurred.");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Не удалось разобрать СН новости.");
            return;
        }
        news.setAuthorId(user.getId());

        try {
            NewsService.deleteNews(news);
        } catch (Exception e) {
            System.out.println("An error occurred while deleting news.");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(e.getMessage());
            return;
        }

        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        resp.setHeader("Location", new URL(new URL(req.getRequestURL().toString()), "/newseditor?view=list").toString());
    }

}
