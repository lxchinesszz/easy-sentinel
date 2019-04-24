package com.github.lxchinesszz.easysentinel.aop;

import com.github.lxchinesszz.easysentinel.annotation.EasySentinel;
import com.github.lxchinesszz.easysentinel.exception.BlockException;
import com.github.lxchinesszz.easysentinel.limiter.Limiter;
import lombok.NoArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;

/**
 * @author liuxin
 * @version Id: EasySentinelResourceAspect.java, v 0.1 2019-04-19 17:28
 */

@Aspect
@NoArgsConstructor
public class EasySentinelResourceAspect extends AbstractEasySentinelAspectSupport {

    @Autowired
    private Limiter limiter;


    @Pointcut("@annotation(com.github.lxchinesszz.easysentinel.annotation.EasySentinel)")
    public void easySentinelResourceAnnotationPointcut() {
    }

    @Around("easySentinelResourceAnnotationPointcut()")
    public Object invokeResourceWithSentinel(ProceedingJoinPoint pjp) throws Throwable {
        Method method = resolveMethod(pjp);
        EasySentinel easySentinel = AnnotationUtils.getAnnotation(method, EasySentinel.class);
        String resourceName = getResourceName(method);
        if (this.limiter.limiter(easySentinel, resourceName)) {
            BlockException blockException = new BlockException("The current resource access QPS exceeds the maximum limit");
            return handleBlockException(pjp, easySentinel, blockException);
        }
        return method.invoke(pjp.getTarget(), pjp.getArgs());
    }


}
