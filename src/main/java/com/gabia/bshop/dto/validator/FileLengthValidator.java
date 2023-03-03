package com.gabia.bshop.dto.validator;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class FileLengthValidator implements ConstraintValidator<ValidFileLength, MultipartFile[]> {
	@Override
	public boolean isValid(MultipartFile[] fileList, ConstraintValidatorContext context) {

		if (fileList == null || fileList.length == 0) {
			return false;
		}

		for (MultipartFile file : fileList) {
			if (file.isEmpty()) {
				return false;
			}
		}
		return true;
	}
}
