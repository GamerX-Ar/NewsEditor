package com.hubble.jdbc;

import com.hubble.dao.INewsDAO;
import com.hubble.jdbc.db.DBManager;
import com.hubble.jdbc.db.SourceCloser;
import com.hubble.dao.exceptions.ApiException;
import com.hubble.data.domain.News;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NewsDAO implements INewsDAO {

    private static final String TABLE_NAME = "NEWS";
    private static final String GET_BY_ID = "select * from " + TABLE_NAME + " where id = ?";
    private static final String CREATE = "{? = call NEWS.CREATE_NEWS(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
    private static final String UPDATE = "{? = call NEWS.CHANGE_NEWS(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
    private static final String DELETE = "{? = call NEWS.TERMINATE_NEWS(?, ?, ?)}";
    private static final String GET_NEWS = "select * from (select ROWNUM RN, A.* from (select * from NEWS where SYSDATE < TD order by N desc) A) where RN > ? AND RN <= ?";
    private static final String GET_NEWS_T = "select * from (select ROWNUM RN, A.* from (select * from NEWS where SYSDATE between FD and TD and D_TYPE = ? order by N desc) A) where RN > ? AND RN <= ?";
    private static final String GET_NEWS_TS = "select * from (select ROWNUM RN, A.* from (select * from NEWS where SYSDATE between FD and TD and D_TYPE = ? and D_STATUS = ? order by N desc) A) where RN > ? AND RN <= ?";
    private static final String GET_NEWS_COUNT = "select count(N) as AMT from NEWS where SYSDATE < TD";
    private static final String GET_NEWS_COUNT_T = "select count(N) as AMT from NEWS where SYSDATE between FD and TD and D_TYPE = ?";
    private static final String GET_NEWS_COUNT_TS = "select count(N) as AMT from NEWS where SYSDATE between FD and TD and D_TYPE = ? and D_STATUS = ?";

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public News parseResultSet(ResultSet rs) throws SQLException {
        News news = new News(rs.getLong("N"));

        news.setFd(rs.getTimestamp("FD"));
        news.setTd(rs.getTimestamp("TD"));
        news.setAuthorId(rs.getLong("R_AUTHOR_ID"));
        news.setSecNo(rs.getInt("SEC_NO"));
        news.setType(rs.getInt("D_TYPE"));
        news.setStatus(rs.getInt("D_STATUS"));
        news.setSeasonCode(rs.getInt("SEASON_CODE"));
        news.setTitle(rs.getString("TITLE"));
        news.setSmallBody(rs.getString("SMALL_BODY"));
        news.setBody(rs.getString("BODY"));
        news.setIcon(rs.getString("ICON"));

        return news;
    }

    @Override
    public News getById(long id) throws SQLException {
        News res = null;

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = DBManager.getConnection();
            if (con != null) {
                pst = con.prepareStatement(GET_BY_ID);

                pst.setLong(1, id);

                rs = pst.executeQuery();
                if (rs.next()) {
                    res = parseResultSet(rs);
                }
            }
        } finally {
            SourceCloser.close(rs);
            SourceCloser.close(pst);
            SourceCloser.close(con);
        }
        return res;
    }

    @Override
    public long createNews(final News news) throws ApiException {

        long result = 0;

        Connection con = null;
        CallableStatement cst = null;

        try {
            con = DBManager.getConnection();

            cst = con.prepareCall(CREATE);

            cst.registerOutParameter(1, Types.INTEGER); // error code
            cst.registerOutParameter(2, Types.VARCHAR); // error desc
            cst.setLong(3, news.getAuthorId());
            cst.setInt(4, news.getType());
            cst.setInt(5, news.getSecNo());
            cst.setString(6, news.getTitle());
            cst.setString(7, news.getSmallBody());
            cst.setString(8, news.getBody());
            cst.setInt(9, news.getStatus());
            cst.setString(10, news.getIcon());
            cst.setInt(11, news.getSeasonCode());
            cst.setTimestamp(12, news.getFd());

            cst.execute();

            if ( (result = cst.getLong(1)) <= 0 ) {
                throw new ApiException(cst.getString(2), (int)result);
            }

        } catch (SQLException e) {
            System.out.println("An error occurred while creating news " + news.getId());
        } finally {
            SourceCloser.close(cst);
            SourceCloser.close(con);
        }

        return result;
    }

    @Override
    public void updateNews(final News news) throws ApiException {

        Connection con = null;
        CallableStatement cst = null;

        ByteArrayInputStream inputStream = new ByteArrayInputStream(news.getBody().getBytes(StandardCharsets.UTF_8));
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

        try {
            con = DBManager.getConnection();

            cst = con.prepareCall(UPDATE);

            cst.registerOutParameter(1, Types.INTEGER); // error code
            cst.registerOutParameter(2, Types.VARCHAR); // error desc
            cst.setLong(3, news.getAuthorId());
            cst.setLong(4, news.getId());
            cst.setInt(5, news.getType());
            cst.setInt(6, news.getSecNo());
            cst.setString(7, news.getTitle());
            cst.setString(8, news.getSmallBody());
            cst.setClob(9, inputStreamReader);
            cst.setInt(10, news.getStatus());
            cst.setString(11, news.getIcon());
            cst.setInt(12, news.getSeasonCode());
            cst.setTimestamp(13, news.getFd());

            cst.execute();

            if ( cst.getInt(1) <= 0 ) {
                throw new ApiException(cst.getString(2), cst.getInt(1));
            }

        } catch (SQLException e) {
            System.out.println("An error occurred while updating news " + news.getId());
        } finally {
            SourceCloser.close(cst);
            SourceCloser.close(con);
        }
    }

    @Override
    public void deleteNews(final long editorN, final long newsN) throws ApiException {

        Connection con = null;
        CallableStatement cst = null;

        try {
            con = DBManager.getConnection();

            cst = con.prepareCall(DELETE);

            cst.registerOutParameter(1, Types.INTEGER); // error code
            cst.registerOutParameter(2, Types.VARCHAR); // error desc
            cst.setLong(3, editorN);
            cst.setLong(4, newsN);

            cst.execute();

            if ( cst.getInt(1) <= 0 ) {
                throw new ApiException(cst.getString(2), cst.getInt(1));
            }

        } catch (SQLException e) {
            System.out.println("An error occurred while deleting news " + newsN);
        } finally {
            SourceCloser.close(cst);
            SourceCloser.close(con);
        }
    }

    @Override
    public List<News> getNewsList(int offset, int limit) {

        List<News> newsList = new ArrayList<>();

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = DBManager.getConnection();

            pst = con.prepareStatement(GET_NEWS);

            pst.setInt(1, offset);
            pst.setInt(2, offset+limit);

            rs = pst.executeQuery();
            newsList = loadResultSet(rs);
        } catch (SQLException e) {
            System.out.println("An error occurred while news retrieving.");
        } finally {
            SourceCloser.close(rs);
            SourceCloser.close(pst);
            SourceCloser.close(con);
        }

        return newsList;
    }

    @Override
    public List<News> getNewsList(int offset, int limit, int type) {

        List<News> newsList = new ArrayList<>();

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = DBManager.getConnection();

            pst = con.prepareStatement(GET_NEWS_T);

            pst.setInt(1, type);
            pst.setInt(2, offset);
            pst.setInt(3, offset+limit);

            rs = pst.executeQuery();
            newsList = loadResultSet(rs);
        } catch (SQLException e) {
            System.out.println("An error occurred while news retrieving.");
        } finally {
            SourceCloser.close(rs);
            SourceCloser.close(pst);
            SourceCloser.close(con);
        }

        return newsList;
    }

    @Override
    public List<News> getNewsList(int offset, int limit, int type, int status) {

        List<News> newsList = new ArrayList<>();

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = DBManager.getConnection();
            pst = con.prepareStatement(GET_NEWS_TS);

            pst.setInt(1, type);
            pst.setInt(2, status);
            pst.setInt(3, offset);
            pst.setInt(4, offset+limit);

            rs = pst.executeQuery();
            newsList = loadResultSet(rs);
        } catch (SQLException e) {
            System.out.println("An error occurred while news retrieving.");
        } finally {
            SourceCloser.close(rs);
            SourceCloser.close(pst);
            SourceCloser.close(con);
        }

        return newsList;
    }

    @Override
    public int getNewsCount() {
        int amount = 0;

        Connection con = null;
        Statement pst = null;
        ResultSet rs = null;

        try {
            con = DBManager.getConnection();

            pst = con.createStatement();
            rs = pst.executeQuery(GET_NEWS_COUNT);
            if ( rs.next() ) { amount = rs.getInt("AMT"); }

        } catch (SQLException e) {
            System.out.println("An error occurred while news retrieving.");
        } finally {
            SourceCloser.close(rs);
            SourceCloser.close(pst);
            SourceCloser.close(con);
        }

        return amount;
    }

    @Override
    public int getNewsCount(int type) {
        int amount = 0;

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = DBManager.getConnection();
            pst = con.prepareStatement(GET_NEWS_COUNT_T);

            pst.setInt(1, type);

            rs = pst.executeQuery();
            if ( rs.next() ) { amount = rs.getInt("AMT"); }
        } catch (SQLException e) {
            System.out.println("An error occurred while news retrieving.");
        } finally {
            SourceCloser.close(rs);
            SourceCloser.close(pst);
            SourceCloser.close(con);
        }

        return amount;
    }

    @Override
    public int getNewsCount(int type, int status) {
        int amount = 0;

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = DBManager.getConnection();

            pst = con.prepareStatement(GET_NEWS_COUNT_TS);

            pst.setInt(1, type);
            pst.setInt(2, status);

            rs = pst.executeQuery();
            if ( rs.next() ) { amount = rs.getInt("AMT"); }

        } catch (SQLException e) {
            System.out.println("An error occurred while news retrieving.");
        } finally {
            SourceCloser.close(rs);
            SourceCloser.close(pst);
            SourceCloser.close(con);
        }

        return amount;
    }

}
