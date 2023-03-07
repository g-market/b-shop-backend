package com.gabia.bshop.dto.validator;

import java.nio.charset.Charset;

import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class ByteSizeValidator implements ConstraintValidator<ByteSize, String> {
	private Charset charset;
	private long max;

	@Override
	public void initialize(ByteSize constraintAnnotation) {
		charset = Charset.forName(constraintAnnotation.charset());
		max = constraintAnnotation.max();
	}

	@Override
	public boolean isValid(final String value, final ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}

		final long byteLength = value.getBytes(charset).length;
		return byteLength <= max;
	}
}
