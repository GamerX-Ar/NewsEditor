package com.hubble.dao;

import java.sql.SQLException;

/**
 * Represents an operation that accepts a single input argument and returns no
 * result.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #accept(Object)}.
 *
 * @param <T> the type of the input to the operation
 */
@FunctionalInterface
public interface SQLConsumer<T> {

    /**
     * Performs this operation on the given argument.
     *
     * @param statement the input argument
     * @throws SQLException if something went wrong
     */
    void accept(T statement) throws SQLException;
}
