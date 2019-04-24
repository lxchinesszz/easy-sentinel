package com.github.lxchinesszz.easysentinel.configuration;

import com.github.lxchinesszz.easysentinel.limiter.EasySentinelRedisLimiter;
import com.github.lxchinesszz.easysentinel.aop.EasySentinelResourceAspect;
import com.github.lxchinesszz.easysentinel.namespace.NameSpaceHandler;
import com.github.lxchinesszz.easysentinel.registory.RedisRegistryCenter;
import com.github.lxchinesszz.easysentinel.registory.RegistryCenter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;


/**
 * @author liuxin
 * @version Id: EasySentinelConfiguration.java, v 0.1 2019-04-20 14:30
 */
@Slf4j
@Configuration
@ConditionalOnClass(RedisAutoConfiguration.class)
public class EasySentinelAutoConfiguration {


    @Bean("easySentinelNameSpace")
    public NameSpaceHandler nameSpaceHandler() {
        return new NameSpaceHandler();
    }


    @Bean(value = "redisRegistryCenter", initMethod = "registry")
    public RegistryCenter registryCenter() {
        return new RedisRegistryCenter();
    }


    @Bean("easySentinelAspect")
    public EasySentinelResourceAspect easySentinelResourceAspect() {
        return new EasySentinelResourceAspect();
    }

    @Bean("easySentinelLimiter")
    public EasySentinelRedisLimiter easySentinelRedisLimiter() {
        return new EasySentinelRedisLimiter();
    }
}
