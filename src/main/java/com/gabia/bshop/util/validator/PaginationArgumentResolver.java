package com.gabia.bshop.util.validator;

import static com.gabia.bshop.exception.ErrorCode.*;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.gabia.bshop.exception.ConflictException;

@Component
public class PaginationArgumentResolver extends PageableHandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(final MethodParameter parameter) {
		boolean hasAnnotation = parameter.hasParameterAnnotation(LimitedSizePagination.class);
		boolean hasPageable = Pageable.class.isAssignableFrom(parameter.getParameterType());
		return hasAnnotation && hasPageable;
	}

	@Override
	public Pageable resolveArgument(MethodParameter methodParameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
		final Pageable pageable = super.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);
		if (methodParameter.hasParameterAnnotation(LimitedSizePagination.class)) {
			final int maxSize = methodParameter.getParameterAnnotation(LimitedSizePagination.class).maxSize();
			if (pageable.getPageSize() > maxSize) {
				throw new ConflictException(MAX_PAGE_ELEMENT_REQUEST_SIZE_EXCEPTION, maxSize);
			}
		}
		return pageable;
	}
}
