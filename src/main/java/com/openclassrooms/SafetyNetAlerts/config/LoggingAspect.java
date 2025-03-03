package com.openclassrooms.SafetyNetAlerts.config;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* com.openclassrooms.*.controller.*.*(..))") // Adjust package to your setup
    public void logRequest(JoinPoint joinPoint) {
//        Object[] args = joinPoint.getArgs();
//        logger.info("Request received in method: {} with arguments: {}", joinPoint.getSignature(), args);

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String url = request.getRequestURI();
            String fullUrl = request.getQueryString() !=null ? url + "?" + request.getQueryString() : url;
            String method = request.getMethod();

            logger.info("HTTP {} request to: {} | Handled by: {} | Arguments: {}",
                    method, fullUrl, joinPoint.getSignature(), joinPoint.getArgs());
        } else {
            logger.info("Request received in method: {} with arguments: {}", joinPoint.getSignature(), joinPoint.getArgs());
        }
    }

    @AfterReturning(value = "execution(* com.openclassrooms.*.controller.*.*(..))", returning = "result")
    public void logResponse(JoinPoint joinPoint, Object result) {
        logger.info("Method {} returned: {}", joinPoint.getSignature(), result);
    }

    @AfterThrowing(value = "execution(* com.openclassrooms.*.controller.*.*(..))", throwing = "ex")
    public void logException(JoinPoint joinPoint, Throwable ex) {
        logger.error("Method {} threw exception: {}", joinPoint.getSignature(), ex.getMessage());
    }
}

