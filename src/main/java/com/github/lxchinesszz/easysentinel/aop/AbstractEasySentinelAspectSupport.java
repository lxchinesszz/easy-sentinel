package com.github.lxchinesszz.easysentinel.aop;

import com.github.lxchinesszz.easysentinel.annotation.EasySentinel;
import com.github.lxchinesszz.easysentinel.exception.BlockException;
import com.github.lxchinesszz.easysentinel.namespace.NameSpaceHandler;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author liuxin
 * @version Id: AbstractEasySentinelAspectSupport.java, v 0.1 2019-04-19 17:47
 */
public class AbstractEasySentinelAspectSupport {


    protected String getResourceName(Method method) {
        return NameSpaceHandler.getResourceName(method);
    }

    protected Method resolveMethod(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Class<?> targetClass = joinPoint.getTarget().getClass();
        Method method = this.getDeclaredMethodFor(targetClass, signature.getName(), signature.getMethod().getParameterTypes());
        if (method == null) {
            throw new IllegalStateException("Cannot resolve target method: " + signature.getMethod().getName());
        } else {
            return method;
        }
    }

    protected Method getDeclaredMethodFor(Class<?> clazz, String name, Class... parameterTypes) {
        try {
            return clazz.getDeclaredMethod(name, parameterTypes);
        } catch (NoSuchMethodException var6) {
            Class<?> superClass = clazz.getSuperclass();
            return superClass != null ? this.getDeclaredMethodFor(superClass, name, parameterTypes) : null;
        }
    }

    protected Object handleBlockException(ProceedingJoinPoint pjp, EasySentinel easySentinel, BlockException ex) throws Exception {
        return this.handleBlockException(pjp, easySentinel.fallback(), easySentinel.blockHandler(), easySentinel.blockHandlerClass(), ex);
    }

    protected Object handleBlockException(ProceedingJoinPoint pjp, String fallback, String blockHandler, Class<?> blockHandlerClass, BlockException ex) throws Exception {
        Object[] originArgs = pjp.getArgs();
        Method blockHandlerMethod;
        blockHandlerMethod = this.extractFallbackMethod(pjp, fallback);
        if (blockHandlerMethod != null) {
            return blockHandlerMethod.invoke(pjp.getTarget(), originArgs);
        }

        blockHandlerMethod = this.extractBlockHandlerMethod(pjp, blockHandler, blockHandlerClass, ex);
        if (blockHandlerMethod != null) {
            Object[] args = new Object[originArgs.length + 1];
            args[0] = ex;
            System.arraycopy(originArgs, 0, args, 1, originArgs.length);
            return this.isStatic(blockHandlerMethod) ? blockHandlerMethod.invoke((Object) null, args) : blockHandlerMethod.invoke(blockHandlerClass.newInstance(), args);
        } else {
            throw ex;
        }
    }

    /**
     * 调用处理器来处理
     *
     * @param name          方法名
     * @param locationClass 依赖处理类
     * @return
     */
    private Method extractBlockHandlerMethod(ProceedingJoinPoint pjp, String name, Class<?> locationClass, BlockException ex) {
        Object[] originArgs = pjp.getArgs();
        Object[] args = new Object[originArgs.length + 1];
        args[0] = ex;
        System.arraycopy(originArgs, 0, args, 1, originArgs.length);
        return BeanUtils.findMethod(locationClass, name, getParameterTypes(args));
    }

    /**
     * 调用本实例中的方法
     *
     * @param pjp 切入点
     * @param fallbackName 备用方法名
     * @return
     */
    private Method extractFallbackMethod(ProceedingJoinPoint pjp, String fallbackName) {
        Object[] args = pjp.getArgs();
        if (StringUtils.isEmpty(fallbackName)) {
            return null;
        } else {
            Class<?> clazz = pjp.getTarget().getClass();
            return BeanUtils.findMethod(clazz, fallbackName, getParameterTypes(args));
        }
    }

    private static Class<?>[] getParameterTypes(Object[] args) {
        if (null == args) {
            return null;
        }
        Class[] parameterClass = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            parameterClass[i] = args[i].getClass();
        }
        return parameterClass;
    }

    private boolean isStatic(Method method) {
        return Modifier.isStatic(method.getModifiers());
    }
}
