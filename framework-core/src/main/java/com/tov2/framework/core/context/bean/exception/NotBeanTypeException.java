package com.tov2.framework.core.context.bean.exception;

public class NotBeanTypeException extends RegisterException {
    public NotBeanTypeException() {
    }

    public NotBeanTypeException(String message) {
        super(message);
    }

    public NotBeanTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotBeanTypeException(Throwable cause) {
        super(cause);
    }

    public NotBeanTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
