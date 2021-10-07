package com.hubble.service;

import com.hubble.dao.exceptions.ApiException;
import com.hubble.data.domain.News;
import com.hubble.jdbc.NewsDAO;
import com.hubble.service.exceptions.ServiceException;
import java.sql.SQLException;
import java.util.List;

public class NewsService {

    public static News getNews(long newsN) throws SQLException {
        return new NewsDAO().getById(newsN);
    }

    /**
     * Saves new news to database.
     *
     * @param news News to be saved.
     * @return Identifier of saved news.
     * @throws ServiceException API error code and description.
     */
    public static long createNews(final News news) throws ServiceException {
        try {
            return new NewsDAO().createNews(news);
        } catch (ApiException e) {
            throw new ServiceException(e.getMessage(), e.getErrorCode());
        }
    }

    public static void updateNews(final News news) throws ServiceException {
        try {
            new NewsDAO().updateNews(news);
        } catch (ApiException e) {
            throw new ServiceException(e.getMessage(), e.getErrorCode());
        }
    }

    public static void deleteNews(final News news) throws ServiceException {
        try {
            new NewsDAO().deleteNews(news.getAuthorId(), news.getId());
        } catch (ApiException e) {
            throw new ServiceException(e.getMessage(), e.getErrorCode());
        }
    }

    public static List<News> getNewsList() {
        return new NewsDAO().getNewsList(0, getNewsCount());
    }

    public static List<News> getNewsList(int offset, int limit) {
        return new NewsDAO().getNewsList(offset, limit);
    }

    public static List<News> getNewsList(int offset, int limit, News.Type type) {
        return new NewsDAO().getNewsList(offset, limit, type.getCode());
    }

    public static List<News> getNewsList(int offset, int limit, News.Type type, News.Status status) {
        return new NewsDAO().getNewsList(offset, limit, type.getCode(), status.getCode());
    }

    public static List<News> getPublishedNews(News.Type type) {
        return getNewsList(0, getNewsCount(type, News.Status.PUBLISHED), type, News.Status.PUBLISHED);
    }

    public static List<News> getNews4Approve(News.Type type) {
        return getNewsList(0, getNewsCount(type, News.Status.FOR_APPROVE), type, News.Status.FOR_APPROVE);
    }

    public static List<News> getDraftNews(News.Type type) {
        return getNewsList(0, getNewsCount(type, News.Status.CREATED), type, News.Status.CREATED);
    }

    public static int getNewsCount() {
        return new NewsDAO().getNewsCount();
    }

    public static int getNewsCount(News.Type type) {
        return new NewsDAO().getNewsCount(type.getCode());
    }

    public static int getNewsCount(News.Type type, News.Status status) {
        return new NewsDAO().getNewsCount(type.getCode(), status.getCode());
    }

}
