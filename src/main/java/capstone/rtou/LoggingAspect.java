package capstone.rtou;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(* capstone.rtou.api..*(..))")
    public void cut() {}

    @Around("cut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long end = System.currentTimeMillis();

        long elapse = end - start;
        log.info(joinPoint.getSignature().getDeclaringTypeName() + "=> {} {}ms", joinPoint.getSignature().getName(), elapse);

        return result;
    }
}
