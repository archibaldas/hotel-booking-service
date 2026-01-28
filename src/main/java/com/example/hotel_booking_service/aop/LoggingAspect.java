package com.example.hotel_booking_service.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class LoggingAspect {
    @Value("${spring.application.name}")
    private String appName;

    @Around("@annotation(LogExecution)")
    public Object toLog(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        log.info("[{}] {}.{} started ,{}.",appName, className, methodName, Instant.now());

        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis() - start;

        if(log.isDebugEnabled()){
            log.debug("[{}] {}.{} completed by {} ms,{}.",appName, className, methodName, end, Instant.now());
        }

        return result;
    }
}
