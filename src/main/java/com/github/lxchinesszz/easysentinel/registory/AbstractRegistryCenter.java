package com.github.lxchinesszz.easysentinel.registory;

import com.github.lxchinesszz.easysentinel.annotation.EasySentinel;
import com.github.lxchinesszz.easysentinel.namespace.NameSpaceHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * @author liuxin
 * @version Id: AbstractRegistryCenter.java, v 0.1 2019-04-23 10:17
 */
@Slf4j
public abstract class AbstractRegistryCenter implements RegistryCenter, BeanFactoryAware {

    @Autowired
    protected NameSpaceHandler nameSpaceHandler;

    private BeanFactory beanFactory;

    private MethodMatcher methodMatcher;


    @PostConstruct
    public void initMethodMater() {
        methodMatcher = AnnotationMatchingPointcut.forMethodAnnotation(EasySentinel.class).getMethodMatcher();
    }

    @Override
    public String getNameSpace() {
        return nameSpaceHandler.getNameSpace();
    }

    @Override
    public String getServiceName() {
        return nameSpaceHandler.getAppName();
    }

    @Override
    public void registry() {
        String nameSpace = nameSpaceHandler.getNameSpace();
        String serverHost = nameSpaceHandler.getServerHost();
        EasySentinelInstance easySentinelInstance = buildEasySentinelInstance();
        log.info("local easySentinelInstance:{}", prettyPrint(easySentinelInstance));
        doRegistry(pullResource(), easySentinelInstance);
        log.info("{},in {} registry success,easySentinelInstance", nameSpace, serverHost);
    }


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }


    public EasySentinelInstance buildEasySentinelInstance() {
        EasySentinelInstance esi = new EasySentinelInstance(nameSpaceHandler.getAppName(), nameSpaceHandler.getServerHost());
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) this.beanFactory;
        buildEasySentinelInstance(esi, beanFactory.getBeanNamesForAnnotation(Controller.class));
        return esi;
    }


    private void buildEasySentinelInstance(EasySentinelInstance esi, String[] controllerBeanNames) {
        for (int i = 0; i < controllerBeanNames.length; i++) {
            String controllerBeanName = controllerBeanNames[i];
            Object bean = beanFactory.getBean(controllerBeanName);
            Method[] methods = bean.getClass().getSuperclass().getMethods();
            Stream<Method> methodStream = Arrays.stream(methods).filter(m -> methodMatcher.matches(m, bean.getClass()));
            methodStream.forEach(m -> {
                EasySentinel easySentinel = AnnotationUtils.getAnnotation(m, EasySentinel.class);
                if (null != easySentinel) {
                    String resourceName = easySentinel.resourceName();

                    if (StringUtils.isEmpty(resourceName)) {
                        resourceName = NameSpaceHandler.getResourceName(m);
                    }
                    esi.setCover(easySentinel.cover());
                    esi.addResourceName(new ResourceNameMeta(easySentinel.Qps(), resourceName,NameSpaceHandler.getResourceName(m)));
                }
            });
        }
    }

    protected String toGson(ResourceMetaInfo resourceMetaInfo) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.disableHtmlEscaping().create();
        return gson.toJson(resourceMetaInfo);

    }

    protected String prettyPrint(Object object) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.disableHtmlEscaping().setPrettyPrinting().create();
        return gson.toJson(object);
    }

    @Override
    public abstract ResourceMetaInfo pullResource();

    public abstract void doRegistry(ResourceMetaInfo resourceMetaInfo, EasySentinelInstance easySentinelInstance);

}
