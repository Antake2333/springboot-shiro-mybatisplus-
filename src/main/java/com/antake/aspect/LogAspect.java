package com.antake.aspect;

import com.antake.pojo.Log;
import com.antake.service.LogService;
import com.antake.utils.JwtUtil;
import com.antake.utils.StringUtils;
import io.netty.util.internal.ThrowableUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Antake
 * @date 2020/5/19
 * @description this is description
 */
@Component
@Aspect
public class LogAspect {
    @Autowired
    private LogService logService;
    private static long startTime;

    /**
     * 配置切入点
     */
    @Pointcut("@annotation(com.antake.annotations.EnableLog)")
    public void logPointcut() {
        // 该方法无方法体,主要为了让同类中其他方法使用此切入点
    }

    /**
     * 配置环绕通知,使用在方法logPointcut()上注册的切入点
     *
     * @param joinPoint join point for advice
     */
    @Around("logPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        startTime = System.currentTimeMillis();
        Object result;
        result = joinPoint.proceed();
        Log log = new Log("INFO", System.currentTimeMillis() - startTime);
        HttpServletRequest request = JwtUtil.getRequest();
        logService.save(joinPoint, log, JwtUtil.getUserId(JwtUtil.getTokenFromRequest()), JwtUtil.getUsername(JwtUtil.getTokenFromRequest()), StringUtils.getBrowser(request), StringUtils.getOsName(request), StringUtils.getIp(request));
        return result;
    }

    /**
     * 配置异常通知
     *
     * @param joinPoint join point for advice
     * @param e         exception
     */
    @AfterThrowing(pointcut = "logPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        Log log = new Log("ERROR", System.currentTimeMillis() - startTime);
        log.setException(ThrowableUtil.stackTraceToString(e));
        HttpServletRequest request = JwtUtil.getRequest();
        logService.save((ProceedingJoinPoint) joinPoint, log, JwtUtil.getUserId(JwtUtil.getTokenFromRequest()), JwtUtil.getUsername(JwtUtil.getTokenFromRequest()), StringUtils.getBrowser(request), StringUtils.getOsName(request), StringUtils.getIp(request));
    }
}

