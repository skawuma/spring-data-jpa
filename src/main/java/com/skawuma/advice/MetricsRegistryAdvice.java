package com.skawuma.advice;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author samuelkawuma
 * @package com.skawuma.advice
 * @project spring-data-AOP
 * @date 7/19/24
 */
@Aspect
@Component
@Slf4j
public class MetricsRegistryAdvice {

    @Autowired
    private ObservationRegistry registry;

    //@After(value = "execution (* com.skawuma.controller.ProductController.*(..))")
    public void sendMetrics(JoinPoint joinPoint){
        //logic
        log.info("application collecting metrics");
        Observation.createNotStarted(joinPoint.getSignature().getName(), registry)
                .observe(()->joinPoint.getArgs());
        log.info("application publish the metrics");
    }
}
