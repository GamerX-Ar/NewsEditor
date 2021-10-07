package com.hubble.service;

import com.hubble.data.domain.AbstractObject;
import com.hubble.data.domain.UTM;
import com.hubble.data.domain.User;
import com.hubble.dao.exceptions.ApiException;
import com.hubble.jdbc.UtmDAO;
import com.hubble.service.exceptions.ServiceException;
import java.sql.Connection;
import java.sql.SQLException;

public class UtmService {

    static public void saveUTM(Connection con, UTM utm, long userN) throws ApiException {
        new UtmDAO().saveUTM(con, utm, userN);
    }

    static public void saveUTM(UTM utm, User user) {
        try {
            new UtmDAO().saveUTM(utm, user.getId());
        } catch (ApiException e) {
            System.out.printf("An error occurred while saving an UTM for user %d.%n", user.getId());
        }
    }

    static public User registerUser(UTM utm, long userN, String email, int seasonCode, AbstractObject.Type objT, long objN)
            throws ServiceException {
        try {
            return new UserService().getUser(new UtmDAO().registerUser(utm, userN, email, seasonCode, objT.getCode(), objN));
        } catch (ApiException e) {
            System.out.println("An error occurred while saving an UTM for new user.");
            throw new ServiceException(e.getMessage(), e.getErrorCode(), e);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new ServiceException(e);
        }
    }

}
