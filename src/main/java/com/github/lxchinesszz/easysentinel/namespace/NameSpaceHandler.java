package com.github.lxchinesszz.easysentinel.namespace;

import com.github.lxchinesszz.easysentinel.util.IpUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;

/**
 * 1. 给当前服务命名
 *
 * @author liuxin
 * @version Id: NameSpaceHandler.java, v 0.1 2019-04-21 16:55
 */
@Slf4j
public class NameSpaceHandler implements EnvironmentAware {

    private static final String EASY_SENTINEL = "EASY_SENTINEL";

    private static final String REQUEST_TOTAL_COUNTER = "-request-total-count";

    private static final String STRATEGY_KEY = "-strategy";

    private static final String LIMITER_KEY = "-limiter-count";

    private static final String COUNT = "-count";

    private static final String[] SEPARATORS = new String[]{"-", "@", ":"};

    private static String PREFIX_NAME;

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }


    public String getNameSpace() {
        return EASY_SENTINEL;
    }


    public String getAppName() {
        return environment.getProperty("spring.application.name", "default");
    }

    /**
     * EASY_SENTINEL-{applicationName}@{ip}:{port}
     *
     * @return 策略key
     */
    @PostConstruct
    private void getName() {
        String applicationName = environment.getProperty("spring.application.name", "default");
        StringBuilder sb = new StringBuilder();
        String port = environment.getProperty("server.port", "0");
        if (!StringUtils.isEmpty(applicationName)) {
            sb.append(applicationName)
                    .append(SEPARATORS[1]).
                    append(IpUtils.getIp()).append(SEPARATORS[2])
                    .append(port).append(SEPARATORS[1]);
        }
        PREFIX_NAME = sb.toString();
    }

    public static String getResourceName(Method method) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(method.getDeclaringClass().getName()).
                append(".").append(method.getName());
        return stringBuilder.toString();
    }

    public String getServerHost() {
        String port = environment.getProperty("server.port", "0");
        return new StringBuilder().append(IpUtils.getIp()).append(SEPARATORS[2]).append(port).toString();
    }

    /**
     * 资源命名key
     *
     * @param resourceName 访问资源名
     * @return redis访问资源总请求次数key
     */
    public String resourceTotalCountKey(String resourceName) {
        return new StringBuilder().append(PREFIX_NAME).append(resourceName).append(REQUEST_TOTAL_COUNTER).toString();
    }

    /**
     * 资源命名key
     *
     * @param resourceName 访问资源名
     * @return redis访问资源命名key
     */
    public String resourceKey(String resourceName) {
        return new StringBuilder().append(PREFIX_NAME).append(resourceName).toString();
    }

    /**
     * 请求资源策略key
     *
     * @param resourceName 访问请求资源
     * @return 请求资源策略key
     */
    public String resourceStrategyKey(String resourceName) {
        return new StringBuilder().append(PREFIX_NAME).append(resourceName).append(STRATEGY_KEY).toString();
    }

    /**
     * 请求资源策略key
     *
     * @param resourceName 访问请求资源
     * @return 请求资源当前访问数
     */
    public String resourceLimitKey(String resourceName) {
        return new StringBuilder().append(PREFIX_NAME).append(resourceName).append(LIMITER_KEY).toString();
    }

    /**
     * @param resourceName 访问请求资源
     * @return 总次数
     */
    public String resourceCountKey(String resourceName) {
        return new StringBuilder().append(PREFIX_NAME).append(resourceName).append(COUNT).toString();
    }

    /**
     * 当前资源在redis中的key
     *
     * @param resourceName 资源
     * @return 资源原始数据
     */
    public ResourceNameRedisKeyMeta build(String resourceName) {
        return ResourceNameRedisKeyMeta.builder().resourceCountKey(resourceCountKey(resourceName))
                .resourceLimitKey(resourceLimitKey(resourceName)).resourceNameKey(resourceKey(resourceName))
                .resourceStrategyKey(resourceStrategyKey(resourceName)).resourceTotalCountKey(resourceTotalCountKey(resourceName)).build();
    }


    @Getter
    @Builder
    public static class ResourceNameRedisKeyMeta {
        String resourceNameKey;
        String resourceStrategyKey;
        String resourceTotalCountKey;
        String resourceLimitKey;
        String resourceCountKey;
    }
}
