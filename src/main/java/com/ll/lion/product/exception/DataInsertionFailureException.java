package com.ll.lion.product.exception;

import org.springframework.dao.DataAccessException;

public class DataInsertionFailureException extends RuntimeException {
    public DataInsertionFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
