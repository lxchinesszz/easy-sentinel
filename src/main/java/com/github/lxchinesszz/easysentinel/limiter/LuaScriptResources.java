package com.github.lxchinesszz.easysentinel.limiter;

import com.github.lxchinesszz.easysentinel.util.ScriptLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liuxin
 * @version Id: LuaScriptResources.java, v 0.1 2019-04-21 21:51
 */
@Slf4j
public class LuaScriptResources {

    private ScriptLoader scriptLoader = new ScriptLoader(log);

    private Map<String, String> luaScriptMap;

    public LuaScriptResources() {
        this.luaScriptMap = new ConcurrentHashMap<>();
    }

    public String script(String scriptName) {
        String script = luaScriptMap.get(scriptName);
        if (StringUtils.isEmpty(script)) {
            script = scriptLoader.loader(scriptName);
            luaScriptMap.put(scriptName, script);
        }
        return script;
    }
}
