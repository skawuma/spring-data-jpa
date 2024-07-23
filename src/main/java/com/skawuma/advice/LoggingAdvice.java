package com.skawuma.advice;

/**
 * @author samuelkawuma
 * @package com.skawuma.advice
 * @project spring-data-AOP
 * @date 7/18/24
 */
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAdvice {

    //  Logger logger= LoggerFactory.getLogger(LoggingAdvice.class);

   @Pointcut("execution(* com.skawuma.*.*.*(..))")
    //@Pointcut("within(com.skawuma..*)")
    //@Pointcut("target(com.skawuma.service.ProductService)")
    //@Pointcut("execution(* com.skawuma.service.ProductService.get*(int))")
//  @Pointcut("execution(* com.skawuma.controller.ProductController.*(..)) || " +
//            "execution(* com.skawuma.service.ProductService.*(..))")


      private void logPointcut()
          {

            }

    @Before("logPointcut()")

    public void logRequest(JoinPoint joinPoint) throws JsonProcessingException {
        log.info("class name {} ,method name {} ", joinPoint.getTarget(), joinPoint.getSignature().getName());
        log.info("Request Body {} ", new ObjectMapper().writeValueAsString(joinPoint.getArgs()));
    }

    @AfterReturning(value = "execution (* com.skawuma.controller.ProductController.*(..))",returning = "object")
    public void logResponse(JoinPoint joinPoint,Object object) throws JsonProcessingException {
        log.info("LoggingAdvice::logResponse class name {} ,method name {} ", joinPoint.getTarget(), joinPoint.getSignature().getName());
        log.info("LoggingAdvice::logResponse Response Body {} ", new ObjectMapper().writeValueAsString(object));
    }

//    @AfterThrowing(pointcut = "execution(* com.skawuma.controller.ProductController.*.*(..))", throwing = "ex")
//    public void logAfterThrowingExceptionCall(Exception ex) throws Throwable {
//        log.info("Exception Occured "  + ex.getMessage());
//        // handle exception with business logic like sending notification or default
//        // message
//    }

    // @After(value = "execution (* com.skawuma.controller.ProductController.*(..))")
//    public void logResponse(JoinPoint joinPoint) throws JsonProcessingException {
//        log.info("LoggingAdvice::logResponse class name {} ,method name {} ", joinPoint.getTarget(), joinPoint.getSignature().getName());
//        log.info("LoggingAdvice::logResponse Response Body {} ", new ObjectMapper().writeValueAsString(joinPoint.getArgs()));
//    }



//    @Around("logPointcut()")
//    public Object logTrack(ProceedingJoinPoint joinPoint) throws Throwable {
//        ObjectMapper objMapper = new ObjectMapper();
//        log.info("Paramter value of " + joinPoint.getSignature().getName() + " method" + " of class "
//                + joinPoint.getTarget().getClass().toString() + " value is "
//                + objMapper.writeValueAsString(joinPoint.getArgs()));
//
//        Object object = joinPoint.proceed();
//        log.info("Response value of " + joinPoint.getSignature().getName() + " method" + " of class "
//                + joinPoint.getTarget().getClass().toString() + " value is " + objMapper.writeValueAsString(object));
//        return object;
//
//    }

}
