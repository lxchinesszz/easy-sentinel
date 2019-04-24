package com.github.lxchinesszz.easysentinel.registory;

/**
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
