package com.gabia.bshop.dto.validator;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class FileValidator implements ConstraintValidator<ValidFile, MultipartFile[]> {

	@Override
	public boolean isValid(final MultipartFile[] multipartFileList, final ConstraintValidatorContext context) {
		for (final MultipartFile multipartFile : multipartFileList) {
			final String contentType = multipartFile.getContentType();
			if (contentType.equals("image")) {
				return false;
			}
		}
		return true;
	}
}
