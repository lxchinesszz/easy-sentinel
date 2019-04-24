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

    int Qps() default -1;

    String resourceName() default "";

    String fallback() default "";

    String blockHandler() default "";

    Class<?> blockHandlerClass() default Void.class;

    /**
     * 覆盖远程的
     * 当true,项目每次启动都会优先覆盖注册中心的配置
     *
     * @return 是否被替换
     */
    boolean cover() default true;

}
