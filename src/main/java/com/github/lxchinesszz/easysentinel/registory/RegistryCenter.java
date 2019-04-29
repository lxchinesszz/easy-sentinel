package com.github.lxchinesszz.easysentinel.registory;

/**
 * 分布式环境下如何满足应用进行通信？
 * 1. 应用服务器内存,减少网络请求,但是服务交互免不了通信的问题。
 * 2. 直接利用分布式的数据库做中间件,减少通信问题。
 *
 * 在分布式数据库的选型中，毫无疑问的选择了Redis,好处有以下几点:
 * 1. 数据结构多样
 * 2. 通过lua脚本定制命令,并且保证了操作的原子性
 * @author liuxin
 * @version Id: RegistryCenter.java, v 0.1 2019-04-22 16:45
 */
public interface RegistryCenter {

    /**
     * 注册当前服务
     */
    void registry();

    /**
     * 拉去并替换资源信息
     *
     * @return 远程资源信息
     */
    ResourceMetaInfo pullResource();

    /**
     * 获取命名空间
     *
     * @return 命名空间
     */
    String getNameSpace();

    /**
     * 获取Service名字
     *
     * @return Service名字
     */
    String getServiceName();
}
