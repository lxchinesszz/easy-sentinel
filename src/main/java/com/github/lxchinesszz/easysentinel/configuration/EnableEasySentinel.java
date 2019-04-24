package com.github.lxchinesszz.easysentinel.configuration;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author liuxin
 * @version Id: EnableEasySentinel.java, v 0.1 2019-04-20 14:29
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(EasySentinelAutoConfiguration.class)
public @interface EnableEasySentinel {

}
