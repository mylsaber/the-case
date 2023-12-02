package com.mylsaber.exception.handle.exception;

import java.io.Serial;

/**
 * @author mylsaber
 */
public class BusinessException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public BusinessException(String message) {
        super(message);
    }
}
