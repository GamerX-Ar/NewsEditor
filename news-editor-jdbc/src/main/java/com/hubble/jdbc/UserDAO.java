package com.hubble.jdbc;

import com.hubble.dao.IUserDAO;
import com.hubble.dao.exceptions.ApiException;
import com.hubble.data.domain.User;
import com.hubble.jdbc.db.DBManager;
import com.hubble.jdbc.db.SourceCloser;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserDAO implements IUserDAO {

    private static final String TABLE_NAME = "USER";
    private static final String GET_BY_ID = "select * from " + TABLE_NAME + " where id = ?";

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public User parseResultSet(ResultSet rs) throws SQLException {
        return new User(
                rs.getLong("N"),
                rs.getString("EMAIL"),
                rs.getInt("D_ROLES")
        );
    }

    @Override
    public User getById(long id) throws SQLException {

        User res = null;

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
    public long register(Connection con, long userN, String email, String key, Integer seasonCode)
            throws ApiException {
        // TODO: ...

        return 0;
    }

    @Override
    public Optional<User> getByEmail(String email) throws ApiException {
        // TODO: ...

        return Optional.empty();
    }

}
