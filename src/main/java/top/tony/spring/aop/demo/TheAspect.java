package top.tony.spring.aop.demo;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class TheAspect {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * The pointcut for all methods in the Controller class.
     */
    @Pointcut("execution(* top.tony.spring.aop.demo.Controller.*(..))")
    private void pointcut() {
    }

    @Before("pointcut()")
    public void before(JoinPoint point) {
        log.info("Method '{}' is called.", point.getSignature().getName());
    }

    @Before("execution(* top.tony.spring.aop.demo.Controller.validation(..))")
    public void validation(JoinPoint point) {
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        for (int i = 0; i < method.getParameterCount(); i++) {
            if (method.getParameters()[i].getName().equals("number")) {
                int number = (int) point.getArgs()[i];
                if (number > 100) {
                    throw new IllegalArgumentException("Number is too large");
                }
            }
        }
    }

    @AfterReturning(pointcut = "pointcut()", returning = "result")
    public void afterReturning(JoinPoint point, Object result) {
        log.info("Method '{}' returns '{}'.", point.getSignature().getName(), result);
    }

    @AfterThrowing(pointcut = "pointcut()", throwing = "e")
    public void exception(JoinPoint point, Exception e) {
        log.error("Method '{}' throws exception: {}", point.getSignature().getName(), e.getMessage());
    }

    @Around("@annotation(top.tony.spring.aop.demo.ModifyParameter)")
    public Object modification(ProceedingJoinPoint point) throws Throwable {
        Object[] args = point.getArgs();
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        for (int i = 0; i < method.getParameterCount(); i++) {
            if (method.getParameters()[i].getName().equals("number")) {
                args[i] = 66;
            }
        }
        return point.proceed(args);
    }
}
