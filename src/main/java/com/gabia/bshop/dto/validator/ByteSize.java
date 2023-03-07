package com.gabia.bshop.dto.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 참고 : https://github.com/terasolunaorg/terasoluna-gfw/commit/545070ab2988f9c061e0af739f39d6fa8efbca83
 **/
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ByteSizeValidator.class})
public @interface ByteSize {
	String message() default "description 의 크기가 허용된 크기 이상입니다.(64 Byte).";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	long max() default 64L;

	String charset() default "UTF-8";
}
