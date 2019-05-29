package com.tov2.framework.core.context.bean.exception;

public class NotBeanNameException extends RegisterException {
    public NotBeanNameException() {
    }

    public NotBeanNameException(String message) {
        super(message);
    }

    public NotBeanNameException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotBeanNameException(Throwable cause) {
        super(cause);
    }

    public NotBeanNameException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
