package com.github.lxchinesszz.easysentinel.exception;

/**
 * lua脚本加载异常
 *
 * @author liuxin
 * @version Id: LuaScriptLoadException.java, v 0.1 2019-04-21 20:11
 */
public class LuaScriptLoadException extends RuntimeException {

    public LuaScriptLoadException() {
    }

    public LuaScriptLoadException(String message) {
        super(message);
    }

    public LuaScriptLoadException(Throwable cause) {
        super(cause);
    }

    public LuaScriptLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
