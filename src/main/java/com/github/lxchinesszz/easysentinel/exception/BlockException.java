package com.github.lxchinesszz.easysentinel.exception;

/**
 * @author liuxin
 * @version Id: BlockException.java, v 0.1 2019-04-21 23:02
 */
public class BlockException extends RuntimeException {
    public BlockException() {
    }

    public BlockException(String message) {
        super(message);
    }

    public BlockException(String message, Throwable cause) {
        super(message, cause);
    }

    public BlockException(Throwable cause) {
        super(cause);
    }

    public BlockException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
