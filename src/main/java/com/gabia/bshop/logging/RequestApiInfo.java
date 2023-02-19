package com.gabia.bshop.logging;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RequestApiInfo {

	private String method = null;
	private String url = null;
	private String name = null;
	private Map<String, String> header = new HashMap<>();
	private Map<String, String> parameters = new HashMap<>();
	private Map<String, String> body = new HashMap<>();
	private String ipAddress = null;
	private final String dateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"))
		.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

	// Request에서 Header 추출
	private void setHeader(final HttpServletRequest request) {
		final Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			final String headerName = headerNames.nextElement();
			this.header.put(headerName, request.getHeader(headerName));
		}
	}

	// Request에서 ipAddress 추출
	private void setIpAddress(final HttpServletRequest request) {
		this.ipAddress = Optional.of(request)
			.map(httpServletRequest -> Optional.ofNullable(request.getHeader("X-Forwarded-For"))
				.orElse(Optional.ofNullable(request.getHeader("Proxy-Client-IP"))
					.orElse(Optional.ofNullable(request.getHeader("WL-Proxy-Client-IP"))
						.orElse(Optional.ofNullable(request.getHeader("HTTP_CLIENT_IP"))
							.orElse(Optional.ofNullable(request.getHeader("HTTP_X_FORWARDED_FOR"))
								.orElse(request.getRemoteAddr())))))).orElse(null);

	}

	// API 정보 추출
	private void setApiInfo(final JoinPoint joinPoint, final Class clazz) {
		final MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
		final Method method = methodSignature.getMethod();
		final String baseUrl = "/" + method.getName();
		Stream.of(GetMapping.class, PutMapping.class, PostMapping.class, DeleteMapping.class, RequestMapping.class,
				PatchMapping.class)
			.filter(method::isAnnotationPresent)
			.findFirst()
			.ifPresent(mappingClass -> {
				final Annotation annotation = method.getAnnotation(mappingClass);
				try {
					final String[] methodUrl = (String[])mappingClass.getMethod("value").invoke(annotation);
					this.method = (mappingClass.getSimpleName().replace("Mapping", "")).toUpperCase();
					this.url = String.format("%s%s", baseUrl, methodUrl.length > 0 ? "/" + methodUrl[0] : "");
					this.name = (String)mappingClass.getMethod("name").invoke(annotation);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
	}

	// Body와 Parameters 추출
	private void setInputStream(final JoinPoint joinPoint, final ObjectMapper objectMapper) {
		try {
			final CodeSignature codeSignature = (CodeSignature)joinPoint.getSignature();
			final String[] parameterNames = codeSignature.getParameterNames();
			final Object[] args = joinPoint.getArgs();
			for (int i = 0; i < parameterNames.length; i++) {
				if (parameterNames[i].equals("request")) {
					this.body = objectMapper.convertValue(args[i], new TypeReference<Map<String, String>>() {
					});
				} else {
					this.parameters.put(parameterNames[i], objectMapper.writeValueAsString(args[i]));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public RequestApiInfo(final JoinPoint joinPoint, final Class clazz, final ObjectMapper objectMapper) {
		try {
			final HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
			setHeader(request);
			setIpAddress(request);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			setApiInfo(joinPoint, clazz);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			setInputStream(joinPoint, objectMapper);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
