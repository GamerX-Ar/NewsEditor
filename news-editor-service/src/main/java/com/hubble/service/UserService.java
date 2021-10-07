package com.hubble.service;

import com.hubble.dao.IUserDAO;
import com.hubble.dao.exceptions.ApiException;
import com.hubble.data.domain.User;
import com.hubble.jdbc.UserDAO;
import com.hubble.service.exceptions.ServiceException;
import java.sql.SQLException;
import java.util.Optional;

public class UserService {

    private final IUserDAO dao;

    public UserService() {
        dao = new UserDAO();
    }

    public User getUser(long userN) throws ServiceException {
        User user;
        try {
            user = dao.getById(userN);
        } catch (SQLException e) {
            System.out.println("An error occurred while retrieving a user by ID.");
            throw new ServiceException(e.getMessage());
        }
        return user;
    }

    public static Optional<User> getUser(String email) throws ServiceException {
        Optional<User> user = Optional.empty();
        try {
            user = new UserDAO().getByEmail(email);
        } catch (ApiException e) {
            if ( e.getErrorCode() != -112 ) { // ошибка не связана с тем, что участник не найден
                throw new ServiceException(e.getMessage(), e.getErrorCode());
            }
        }
        return user;
    }

}
