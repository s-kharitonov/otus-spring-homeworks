package ru.otus.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

	private static final String BENCHMARK_PATTERN = "BENCHMARK: method name: {}, args: {}, execution time(ms): {}";
	private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

	@Around(value = "@annotation(ru.otus.annotations.Loggable)")
	public Object logTimeLoadingResources(final ProceedingJoinPoint pjp) throws Throwable {
		final String methodName = extractFullMethodName(pjp);
		final String args = Arrays.toString(pjp.getArgs());
		final long currentTime = System.currentTimeMillis();
		final Object result = pjp.proceed();
		final long methodExecutionTime = System.currentTimeMillis() - currentTime;

		logger.debug(BENCHMARK_PATTERN, methodName, args, methodExecutionTime);

		return result;
	}

	private String extractFullMethodName(final ProceedingJoinPoint pjp) {
		final String className = pjp.getSignature().getDeclaringType().getCanonicalName();
		final String methodName = pjp.getSignature().getName();
		return className + "." + methodName;
	}
}
