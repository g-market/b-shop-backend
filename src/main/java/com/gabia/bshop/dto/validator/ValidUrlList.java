package com.gabia.bshop.dto.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UrlListValidator.class)
public @interface ValidUrlList {

	String message() default "urlList 가 비어있습니다.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
