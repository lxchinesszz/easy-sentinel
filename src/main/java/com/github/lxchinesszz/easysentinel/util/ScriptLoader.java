package com.github.lxchinesszz.easysentinel.util;

import com.github.lxchinesszz.easysentinel.exception.LuaScriptLoadException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * @author liuxin
 * @version Id: ScriptLoader.java, v 0.1 2019-04-21 21:54
 */
public class ScriptLoader {

    private Logger logger;

    public ScriptLoader(Logger logger) {
        this.logger = logger;
    }

    public String loader(String scriptPath) {
        String luaScript;
        try (InputStream luaIn = ClassLoader.getSystemResourceAsStream(scriptPath)) {
            luaScript = StreamUtils.copyToString(luaIn, Charset.defaultCharset());
        } catch (IOException ioe) {
            throw new LuaScriptLoadException("Lua Script load exception", ioe);
        }
        logger.info("limiter.lua load success");
        return luaScript;
    }
}
