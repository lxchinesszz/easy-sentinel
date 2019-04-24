package com.github.lxchinesszz.easysentinel.limiter;

/**
 * 根据资源获取请求次数
 *
 * @author liuxin
 * @version Id: Requestcounter.java, v 0.1 2019-04-21 21:40
 */
public interface RequestCounter {

    /**
     * 根据资源获取请求次数
     *
     * @param resourceName 资源完成名
     * @return 请求次数
     */
    long count(String resourceName);
}
