package com.hubble.dao;

import com.hubble.dao.exceptions.ApiException;
import com.hubble.data.domain.User;
import java.sql.Connection;
import java.util.Optional;

public interface IUserDAO extends IObjectDAO<User> {

    long register(Connection con, long userN, String email, String key, Integer seasonCode) throws ApiException;

    Optional<User> getByEmail(String email) throws ApiException;
}
