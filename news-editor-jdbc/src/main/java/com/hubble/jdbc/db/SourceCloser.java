package com.hubble.jdbc.db;

import java.sql.*;

public class SourceCloser {

    public static void close(ResultSet o) {
        if (o != null) {
            try {
                o.close();
                o = null;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

    }

    public static void close(PreparedStatement o) {
        if (o != null) {
            try {
                o.close();
                o = null;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void close(Statement o) {
        if (o != null) {
            try {
                o.close();
                o = null;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void close(CallableStatement o) {
        if (o != null) {
            try {
                o.close();
                o = null;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void close(Connection o) {
        if (o != null) {
            try {
                o.close();
                o = null;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

}
