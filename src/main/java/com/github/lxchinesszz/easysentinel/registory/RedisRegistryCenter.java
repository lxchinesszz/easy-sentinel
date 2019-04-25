package com.github.lxchinesszz.easysentinel.registory;

import com.github.lxchinesszz.easysentinel.exception.RegistryException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;

import java.util.Iterator;
import java.util.List;

/**
 * redis直接操作放在改实现类,方便。替换
 * {@code hget EASY_SENTINEL ${serviceName}}
 * 采用hash结构,目的不影响其他服务,保证其隔离性
 * issue:
 * WRONGTYPE Operation against a key holding the wrong kind of value
 * 数据结构问题,登录redis服务器执行del EASY_SENTINEL
 * <p>
 * {@code {
 * "easySentinelInstances": [{
 * "id": "D306991F64412802D2F135E3FAD923FA",
 * "appName": "user-admin",
 * "host": "192.168.1.23:8888",
 * "cover": true,
 * "resourceNameMetas": [{
 * "Qps": 10,
 * "resourceName": "com.github.lxchinesszz.easysentinel.web.WebController.getUser"
 * }, {
 * "Qps": 1,
 * "resourceName": "com.github.lxchinesszz.easysentinel.web.WebController.getUser2"
 * }]
 * }]
 * }}
 *
 * @author liuxin
 * @version Id: RedisRegistryCenter.java, v 0.1 2019-04-22 16:46
 */
@Slf4j
public class RedisRegistryCenter extends AbstractRegistryCenter {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private HashOperations<String, String, String> hashOperations() {
        return stringRedisTemplate.opsForHash();
    }

    @Override
    public ResourceMetaInfo pullResource() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        String easySentinelMetaString;
        try {
            easySentinelMetaString = hashOperations().get(getNameSpace(), getServiceName());
        } catch (Exception rce) {
            throw new RegistryException(rce.getMessage());
        }
        if (StringUtils.isEmpty(easySentinelMetaString)) {
            return ResourceMetaInfo.builder().build();
        }
        ResourceMetaInfo resourceMetaInfo = gson.fromJson(easySentinelMetaString, ResourceMetaInfo.class);
        log.info("remote registry:{}", prettyPrint(resourceMetaInfo));
        return resourceMetaInfo;
    }

    @Override
    public void doRegistry(ResourceMetaInfo resourceMetaInfo, EasySentinelInstance easySentinelInstance) {
        mergeResource(resourceMetaInfo, easySentinelInstance);
        hashOperations().put(getNameSpace(), getServiceName(), toGson(resourceMetaInfo));
    }

    /**
     * 根据id和cover来判断是否移除远程配置
     * 从注册中心拉下来,如果cover=true就替换
     *
     * @param resourceMetaInfo     当前服务所有资源信息(多)
     * @param easySentinelInstance 当前限流资源信息(单)
     */
    private void mergeResource(ResourceMetaInfo resourceMetaInfo, EasySentinelInstance easySentinelInstance) {
        List<EasySentinelInstance> easySentinelInstances = resourceMetaInfo.getEasySentinelInstances();
        for (Iterator<EasySentinelInstance> iterator = easySentinelInstances.iterator(); iterator.hasNext(); ) {
            EasySentinelInstance next = iterator.next();
            if ((easySentinelInstance.getId().equalsIgnoreCase(next.getId()) && easySentinelInstance.isCover())) {
                iterator.remove();
                easySentinelInstances.add(easySentinelInstance);
            }
        }
        if (easySentinelInstances.isEmpty()) {
            easySentinelInstances.add(easySentinelInstance);
        }
    }

}
