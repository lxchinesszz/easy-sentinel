package com.github.lxchinesszz.easysentinel.limiter;

import com.github.lxchinesszz.easysentinel.annotation.EasySentinel;
import com.github.lxchinesszz.easysentinel.namespace.NameSpaceHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 实现对redis操作的
 *
 * @author liuxin
 * @version Id: AbstractResourceLimiter.java, v 0.1 2019-04-21 21:35
 */
@Slf4j
public abstract class AbstractResourceLimiter implements Limiter, RequestCounter {
    @Autowired
    protected NameSpaceHandler nameSpaceHandler;

    @Autowired
    private StringRedisTemplate redisTemplate;


    private LuaScriptResources luaScriptResources = new LuaScriptResources();

    @Override
    public boolean limiter(EasySentinel easySentinel, String resourceName) {
        //加载原子性脚本
        String script = luaScriptResources.script(resolveScriptPath(easySentinel));
        RedisScriptOption redisScriptOption = scriptOption(easySentinel, resourceName);
        boolean limiter = callbackCommand(script, redisScriptOption);
        count(resourceName);
        return limiter;
    }

    /**
     * @param script            lua脚本
     * @param redisScriptOption redis操作lua参数
     * @return true: 限流 ， false: 不限流
     */
    private boolean callbackCommand(String script, RedisScriptOption redisScriptOption) {
        Long currentCount = (Long) redisTemplate.execute(RedisScript.of(script, Long.class), redisScriptOption.getKeys(), redisScriptOption.argvs());
        if (currentCount == 0) {
            log.error("Current resource access exceeds maximum limit");
        } else {
            log.debug("Current resource access QPS =" + currentCount);
        }
        return currentCount == 0;
    }

    /**
     * lua操作参数
     *
     * @param easySentinel 限流信息
     * @param resourceName 资源名
     * @return lua操作信息
     */
    public abstract RedisScriptOption scriptOption(EasySentinel easySentinel, String resourceName);

    /**
     * 选择脚本
     *
     * @param easySentinel 限流信息
     * @return 脚本路径名
     */
    public abstract String resolveScriptPath(EasySentinel easySentinel);


    /**
     * 资源的请求次数
     *
     * @param resourceName 资源完成名
     * @return 请求次数
     */
    @Override
    public long count(String resourceName) {
        String resourceNameCountKey = nameSpaceHandler.resourceTotalCountKey(resourceName);
        String requestCount = redisTemplate.opsForValue().get(resourceNameCountKey);
        log.debug(resourceNameCountKey + " Request Count:" + requestCount);
        return Long.parseLong(requestCount);
    }

    public List<String> keys(Object... key) {
        List<String> keys = new ArrayList<>();
        Arrays.stream(key).forEach(x -> add(keys, x, "keys"));
        return keys;
    }

    public List<String> argv(Object... arg) {
        List<String> argv = new ArrayList<>();
        Arrays.stream(arg).forEach(x -> add(argv, x, "argv"));
        return argv;
    }

    private void checkArgs(Object arg) {
        if (!(arg instanceof String) && !(arg instanceof Integer)) {
            throw new IllegalArgumentException("The parameter:[" + arg + "] type can only be int or String");
        }
    }

    public void add(List<String> list, Object arg, String logPrefix) {
        checkArgs(arg);
        log.debug(logPrefix + " [" + arg + "]");
        list.add(String.valueOf(arg));
    }
}
