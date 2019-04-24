package com.github.lxchinesszz.easysentinel.limiter;

import com.github.lxchinesszz.easysentinel.annotation.EasySentinel;

/**
 * @author liuxin
 * @version Id: Limiter.java, v 0.1 2019-04-20 14:13
 */
public interface Limiter {
    /**
     * 是否限流
     *
     * @param easySentinel 限流信息
     * @param resourceName 限流的资源
     * @return true: 限流 ， false: 不限流
     */
    boolean limiter(EasySentinel easySentinel, String resourceName);

}
