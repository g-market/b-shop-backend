package com.gabia.bshop.dto.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FileValidator.class)
public @interface ValidFile {

	String message() default "업로드하는 파일 형식이 올바르지 않습니다";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
