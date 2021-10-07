package com.hubble.dao;

import com.hubble.dao.exceptions.ApiException;
import com.hubble.data.domain.UTM;
import java.sql.Connection;
import java.sql.SQLException;

public interface IUtmDAO {

    long registerUser(UTM utm, long userId, String email, int seasonCode, int objT, long objN) throws ApiException, SQLException;

    void saveUTM(UTM utm, long userN) throws ApiException;

    void saveUTM(UTM utm, long userN, int objT, long objId) throws ApiException;

    void saveUTM(Connection con, UTM utm, long userId, int objT, long objId) throws ApiException;

    void saveUTM(Connection con, UTM utm, long userId) throws ApiException;

}
