package com.gabia.bshop.dto.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FileLengthValidator.class)
public @interface ValidFileLength {

	String message() default "업로드 하려는 파일이 없습니다";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
