package com.github.lxchinesszz.easysentinel.limiter;

import com.github.lxchinesszz.easysentinel.annotation.EasySentinel;
import com.github.lxchinesszz.easysentinel.namespace.NameSpaceHandler;

import java.util.List;

/**
 * @author liuxin
 * @version Id: EasySentinelLimiter.java, v 0.1 2019-04-20 14:14
 */
public class EasySentinelRedisLimiter extends AbstractResourceLimiter {

    /**
     * 简单
     *
     * @param easySentinel 限流信息
     * @param resourceName 资源名
     * @return redis脚本操作信息
     */
    @Override
    public RedisScriptOption scriptOption(EasySentinel easySentinel, String resourceName) {
        NameSpaceHandler.ResourceNameRedisKeyMeta resourceNameRedisKeyMeta = nameSpaceHandler.build(resourceName);
        int limitQps = easySentinel.Qps();
        List<String> keys = keys(resourceNameRedisKeyMeta.getResourceNameKey(),
                resourceNameRedisKeyMeta.getResourceStrategyKey(),
                resourceNameRedisKeyMeta.getResourceTotalCountKey(), resourceNameRedisKeyMeta.getResourceLimitKey()
                , resourceNameRedisKeyMeta.getResourceCountKey());
        List<String> argv = argv(limitQps);
        return new RedisScriptOption(keys, argv);
    }


    @Override
    public String resolveScriptPath(EasySentinel easySentinel) {
        return "limiter.lua";
    }
}
