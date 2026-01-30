package toy.ojm.global.error;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
class SlackErrorAlertAspect {
  private final SlackErrorAlertService slackErrorAlertService;

  @Around("execution(* toy.ojm..*(..)) && !execution(* toy.ojm.global.async..*(..))")
  public Object logExecutionTime(ProceedingJoinPoint joinPoint) {
    try {
      return joinPoint.proceed();
    } catch (Throwable e) {
      slackErrorAlertService.alarm(e);
      throw new RuntimeException(e);
    }
  }
}
