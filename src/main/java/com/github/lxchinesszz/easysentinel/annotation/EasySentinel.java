package com.github.lxchinesszz.easysentinel.annotation;

import java.lang.annotation.*;

/**
 * @author liuxin
 * @version Id: EasySentinel.java, v 0.1 2019-04-19 17:14
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface EasySentinel {

    /**
     * 每秒最大请求次数
     *
     * @return qps
     */
    int Qps() default -1;

    /**
     * 资源名,当没有声明资源名,默认使用方法全路径名
     *
     * @return 资源名
     */
    String resourceName() default "";

    /**
     * fallback 从限流类中查询备用方法
     *
     * @return 备用方法
     */
    String fallback() default "";

    /**
     * 配合blockHandlerClass属性使用
     * 从blockHandlerClass中查询方法为blockHandler的命名
     *
     * @return 处理方法
     */
    String blockHandler() default "";

    /**
     * 用于处理被限流的请求
     *
     * @return 限流处理类
     */
    Class<?> blockHandlerClass() default Void.class;

    /**
     * 覆盖远程的
     * 当true,项目每次启动都会优先覆盖注册中心的配置
     *
     * @return 是否被替换
     */
    boolean cover() default true;

}
