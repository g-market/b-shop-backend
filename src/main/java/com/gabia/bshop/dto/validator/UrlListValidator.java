package com.gabia.bshop.dto.validator;

import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class UrlListValidator implements ConstraintValidator<ValidUrlList, List<String>> {
	@Override
	public boolean isValid(List<String> urlList, ConstraintValidatorContext context) {
		return !urlList.isEmpty();
	}
}
