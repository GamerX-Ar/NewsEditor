package com.hubble.jdbc.db;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

// Final, because it's not supposed to be subclassed
public final class DBManager {

    private static final String DATASOURCE_NAME = "jdbc/news_db";

    private static final DataSource ds;

    static {
        try {
            Context ctx = new InitialContext();
            ds = (DataSource) ctx.lookup("java:/comp/env/" + DATASOURCE_NAME);
        } catch (NamingException e) {
            System.out.println("Some errors occurred during DB initialization! Application will not work correctly!");
            throw new RuntimeException("Some errors occurred during DB initialization! Application will not work correctly!", e);
        }
    }

    // Private constructor to avoid unnecessary instantiation of the class
    private DBManager() {

    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

}
