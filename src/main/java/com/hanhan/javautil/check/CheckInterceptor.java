package com.hanhan.javautil.check;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import java.util.Collection;

@Component
@Aspect
@Slf4j
public class CheckInterceptor {
    @Pointcut("execution(* com.hanhan.javautil.check..*.*(..)) " +
            "&& args(com.hanhan.javautil.check.ParamChecked,..)")
    public void pointCut() {
    }

    @Pointcut("execution(* com.hanhan.javautil.check..*.*(..)) " +
            "&& args(java.util.Collection<? extends com.hanhan.javautil.check.ParamChecked>)")
    public void listPointCut() {
    }

    @Before("pointCut()")
    public void check(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof ParamChecked) {
                ((ParamChecked) arg).check();
            }
        }
    }

    @Before("listPointCut()")
    public void listCheck(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if(arg instanceof Collection){
                for (Object c : (Collection<?>) arg) {
                    if (c instanceof ParamChecked) {
                        ((ParamChecked) c).check();
                    }
                }
            }
        }
    }

}
