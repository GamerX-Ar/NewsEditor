package com.hubble.jdbc;

import com.hubble.dao.IDictionaryDAO;
import com.hubble.data.domain.Dictionary;
import com.hubble.jdbc.db.DBManager;
import com.hubble.jdbc.db.SourceCloser;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.Map;

public class DictionaryDAO implements IDictionaryDAO {

    private static final String TABLE_NAME = "DIC_DATA";
    private static final String GET_BY_ID = "select * from " + TABLE_NAME + " where id = ?";

    private static final String GET_DICTIONARY =
            "select * from %s where R_DIC_ID = ? and sysdate between FD and TD";
    private static final String ORDER_BY = " order by %s";
    private static final String AND_R_PARENT_TERM = " and R_PARENT_TERM_CODE = %d";
    private static final String AND_PAR1 = " and PAR1 = %d";

    private static final String GET_TERM =
            "select * from %s where R_DIC_ID = ? and CODE = ? and sysdate between FD and TD";

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public Map.Entry<Integer, Dictionary.Term> parseResultSet(ResultSet rs) throws SQLException {
        Dictionary.Term term = new Dictionary.Term();

        term.setId(rs.getLong("ID"));
        term.setFd(rs.getTimestamp("FD"));
        term.setTd(rs.getTimestamp("TD"));
        term.setTerm(rs.getString("TERM"));
        term.setTerm2(rs.getString("TERM2"));
        term.setParentCode(rs.getInt("R_PARENT_TERM_CODE"));
        term.setPar1(rs.getInt("PAR1"));

        return new AbstractMap.SimpleEntry<>(rs.getInt("CODE"), term);
    }

    @Override
    public Map.Entry<Integer, Dictionary.Term> getById(long id) throws SQLException {
        Map.Entry<Integer, Dictionary.Term> res = null;

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
    public Dictionary.Term getTerm(int dicId, int termCode) throws SQLException {

        Dictionary.Term term = null;

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = DBManager.getConnection();

            pst = con.prepareStatement(String.format(GET_TERM, TABLE_NAME));

            pst.setInt(1, dicId);
            pst.setInt(2, termCode);

            rs = pst.executeQuery();
            if (rs.next()) {
                term = parseResultSet(rs).getValue();
            }
        } finally {
            SourceCloser.close(rs);
            SourceCloser.close(pst);
            SourceCloser.close(con);
        }

        return term;
    }

    @Override
    public Dictionary getDictionary(int dicId, int parentCode, int par1, boolean termSorting) {

        Dictionary dictionary = new Dictionary();

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        String ORDER = termSorting ? "TERM" : "CODE";

        StringBuilder query = new StringBuilder();
        try {
            con = DBManager.getConnection();

            query.append(String.format(GET_DICTIONARY, TABLE_NAME));
            if ( parentCode >= 0 ) {
                query.append(String.format(AND_R_PARENT_TERM, parentCode));
            }
            if ( par1 >= 0 ) {
                query.append(String.format(AND_PAR1, par1));
            }
            query.append(String.format(ORDER_BY, ORDER));

            pst =  con.prepareStatement(query.toString());

            pst.setInt(1, dicId);

            rs = pst.executeQuery();
            while ( rs.next() ) {
                dictionary.put(parseResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("An error occurred during the dictionary " + dicId + " access.");
        } finally {
            SourceCloser.close(rs);
            SourceCloser.close(pst);
            SourceCloser.close(con);
        }

        return dictionary;
    }

}
