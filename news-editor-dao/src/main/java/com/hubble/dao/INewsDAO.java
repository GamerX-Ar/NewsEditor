package com.hubble.dao;

import com.hubble.dao.exceptions.ApiException;
import com.hubble.data.domain.News;
import java.util.List;

public interface INewsDAO extends IObjectDAO<News> {

    long createNews(final News news) throws ApiException;

    void updateNews(final News news) throws ApiException;

    void deleteNews(final long editorN, final long newsN) throws ApiException;

    List<News> getNewsList(int offset, int limit);

    List<News> getNewsList(int offset, int limit, int type);

    List<News> getNewsList(int offset, int limit, int type, int status);

    int getNewsCount();

    int getNewsCount(int type);

    int getNewsCount(int type, int status);

}
