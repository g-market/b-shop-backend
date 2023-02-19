package com.gabia.bshop.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.sentry.ITransaction;
import io.sentry.Sentry;
import io.sentry.SentryEvent;
import io.sentry.SentryLevel;
import io.sentry.protocol.Message;
import io.sentry.protocol.Request;
import io.sentry.protocol.SentryException;
import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {

	private final ObjectMapper objectMapper;

	@Pointcut("within(com.gabia.bshop.controller..*)")  // 패키지 범위 설정
	public void onRequest() {}

	@Around("onRequest()")
	public Object requestLogging(final ProceedingJoinPoint joinPoint) throws Throwable {
		// API의 정보 담는 클래스
		final RequestApiInfo apiInfo = new RequestApiInfo(joinPoint, joinPoint.getTarget().getClass(), objectMapper);
		final String requestMessage = String.format("%s %s", apiInfo.getMethod(), apiInfo.getUrl());
		final String body = objectMapper.writeValueAsString(apiInfo.getBody());
		final String parameters = objectMapper.writeValueAsString(apiInfo.getParameters());

		// Request 설정
		final Request request = new Request();
		request.setUrl(apiInfo.getUrl());
		request.setMethod(apiInfo.getMethod());
		request.setData(apiInfo.getBody());
		request.setQueryString(apiInfo.getParameters().keySet().stream()
			.map(key -> key + "=" + apiInfo.getParameters().get(key))
			.reduce("", (a, b) -> a + "&" + b)
		);

		// 트랜잭션 설정
		final ITransaction transaction = Sentry.startTransaction(requestMessage, "request-api");
		Sentry.configureScope(scope -> {
			scope.setRequest(request);
		});
		try {
			final Object result = joinPoint.proceed(joinPoint.getArgs());

			return result;
		} catch (Exception e) {
			final StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			final String exceptionAsString = sw.toString();

			// Sentry Event 생성 및 설정
			final SentryEvent event = new SentryEvent();
			event.setRequest(request);
			event.setLevel(SentryLevel.ERROR);
			event.setTransaction(transaction.getName());

			// Event Message 설정
			final Message message = new Message();
			message.setMessage(requestMessage);
			event.setMessage(message);

			// Exception 설정
			final SentryException exception = new SentryException();
			exception.setType(e.getClass().getSimpleName());
			exception.setValue(exceptionAsString);
			event.setExceptions(List.of(exception));
			Sentry.captureEvent(event);

			throw e;
		} finally {
			// 트랜잭션 close
			transaction.finish();
		}
	}
}
