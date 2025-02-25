package toy.ojm.global;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import toy.ojm.tool.ErrorMessenger;

@Aspect
@Component
@RequiredArgsConstructor
class ErrorMessengerAspect {

	private final ErrorMessenger errorMessenger;

	@Around("execution(* toy.ojm..*(..)) && !execution(* toy.ojm.global.async..*(..))")
	public Object logExecutionTime(ProceedingJoinPoint joinPoint) {
		try {
			return joinPoint.proceed();
		} catch (Throwable e) {
			errorMessenger.alarm(e);
			throw new RuntimeException(e);
		}
	}
}