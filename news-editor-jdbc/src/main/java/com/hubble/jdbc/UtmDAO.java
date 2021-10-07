package com.hubble.jdbc;

import com.hubble.dao.IUtmDAO;
import com.hubble.dao.SQLConsumer;
import com.hubble.dao.exceptions.ApiException;
import com.hubble.data.domain.UTM;
import com.hubble.jdbc.db.SourceCloser;
import com.hubble.jdbc.db.DBManager;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

public class UtmDAO implements IUtmDAO {

    private static final String SAVE = "{? = call reg.register_utm(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
    private static final String SAVE_DEFAULT = "{? = call reg.register_utm(?, ?, ?, ?, ?, ?, ?, ?, ?)}";

    @Override
    public long registerUser(UTM utm, long userId, String email, int seasonCode, int objT, long objN)
            throws ApiException, SQLException {

        Connection con = null;

        try {
            con = DBManager.getConnection();

            con.setAutoCommit(false);  // обеспечиваем транзакцию

            UserDAO userDao = new UserDAO();
            userDao.register(con, userId, email, String.valueOf(userId), seasonCode);
            saveUTM(con, utm, userId, objT, objN);

            con.commit();
        } catch (ApiException e) {
            con.rollback();
            throw e;
        } catch (SQLException e) {
            if ( con != null ) con.rollback();
            throw new ApiException(e);
        } finally {
            if ( con != null ) con.setAutoCommit(true);
            SourceCloser.close(con);
        }

        return userId;
    }

    @Override
    public void saveUTM(UTM utm, long userN) throws ApiException {

        Connection con = null;
        try {
            con = DBManager.getConnection();
            saveUTM(con, utm, userN);
        } catch (SQLException e) {
            throw new ApiException(e);
        } finally {
            SourceCloser.close(con);
        }
    }

    @Override
    public void saveUTM(UTM utm, long userN, int objT, long objId) throws ApiException {

        Connection con = null;
        try {
            con = DBManager.getConnection();
            saveUTM(con, utm, userN, objT, objId);
        } catch (SQLException e) {
            throw new ApiException(e);
        } finally {
            SourceCloser.close(con);
        }
    }

    @Override
    public void saveUTM(Connection con, UTM utm, long userId, int objT, long objId) throws ApiException {
        try {
            saveUTM(cst -> {
                cst.setLong(3, userId);
                cst.setInt(11, objT);
                cst.setLong(12, objId);
            }, con.prepareCall(SAVE), utm);
        } catch (SQLException e) {
            throw new ApiException(e);
        }
    }

    @Override
    public void saveUTM(Connection con, UTM utm, long userId) throws ApiException {
        try {
            saveUTM(cst -> cst.setLong(3, userId), con.prepareCall(SAVE_DEFAULT), utm);
        } catch (SQLException e) {
            throw new ApiException(e);
        }
    }

    private void saveUTM(SQLConsumer<CallableStatement> consumer, CallableStatement cst, UTM utm)
            throws ApiException {

        try {
            consumer.accept(cst);

            cst.registerOutParameter (1, Types.NUMERIC);
            cst.registerOutParameter (2, Types.VARCHAR);

            cst.setString(4, utm.getUrl());
            cst.setString(5, utm.getUri());
            cst.setString(6,utm.getSource());
            cst.setString(7, utm.getMedium());
            cst.setString(8, utm.getCampaign());
            cst.setString(9, utm.getTerm());
            cst.setString(10, utm.getContent());

            cst.execute();

            if ( cst.getLong(1) <= 0 ) {
                throw new ApiException(cst.getString(2), cst.getInt(1));
            }
        } catch (SQLException e) {
            throw new ApiException(e);
        } finally {
            SourceCloser.close(cst);
        }
    }

}
