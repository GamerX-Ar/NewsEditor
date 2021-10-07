package com.hubble.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface IObjectDAO<T> {

    T getById(long id) throws SQLException;

    default List<T> loadResultSet(ResultSet rs) throws SQLException{
        List<T> res = new ArrayList<>();
        while (rs.next()) {
            res.add(parseResultSet(rs));
        }
        return res;
    }
    T parseResultSet(ResultSet rs) throws SQLException;

    String getTableName();
}
