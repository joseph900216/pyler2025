package pyler.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    /***
     * target: 모든 컨트롤러
     */
    @Pointcut("execution(plyer..controller..*.*(..))")
    public void controllerPointcut() {
    }

    /***
     * target: 모든 서비스
     */
    @Pointcut("execution(plyer..service..*.*(..))")
    public void servicePointcut() {
    }

    /***
     * 컨트롤러 실행 전후의 모든 로깅
     * @param joinPoint
     * @throws Throwable
     */
    @Around("controllerPointcut()")
    public Object logAroundController(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        log.info("Request: {}.{}() with arguments = {}", className, methodName, Arrays.toString(joinPoint.getArgs()));

        try {
            Object result = joinPoint.proceed();
            log.info("Response: {}.{}() returned = {}", className, methodName, result);
            return result;
        } catch (Exception e) {
            log.error("Exception in {}.{}() with cause = {}", className, methodName, e.getMessage());
            throw e;
        }
    }

    /***
     * 서비스 메소드 실행 시간 로깅
     * @param joinPoint
     * @throws Throwable
     */
    @Around("servicePointcut()")
    public Object logAroundService(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        log.debug("Start: {}.{}() with arge = {}", className, methodName, Arrays.toString(joinPoint.getArgs()));

        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - start;

            if (executionTime > 1000) {
                log.warn("Long execution time: {}.{}() - {} ms", className, methodName, executionTime);
            } else {
                log.debug("End: {}.{}() - {} ms", className, methodName, executionTime);
            }

            return result;
        } catch (Exception e) {
            log.error("Exception in {}.{}() with cause = {}", className, methodName, e.getMessage());
            throw e;
        }
    }

    /***
     * 모든 예외 처리 로깅
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(pointcut = "controllerPointcut() || servicePointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        log.error("[Exception Error] ===> {}.{}(), [Error Message] ===> {}", className, methodName, e.getMessage());
    }
}
