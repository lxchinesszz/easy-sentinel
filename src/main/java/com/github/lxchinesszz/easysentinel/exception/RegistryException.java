package com.github.lxchinesszz.easysentinel.exception;

/**
 * @author liuxin
 * @version Id: RegistryException.java, v 0.1 2019-04-23 15:40
 */
public class RegistryException extends RuntimeException {
    public RegistryException() {
    }

    public RegistryException(String message) {
        super(message);
    }

    public RegistryException(String message, Throwable cause) {
        super(message, cause);
    }

    public RegistryException(Throwable cause) {
        super(cause);
    }

    public RegistryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
