package com.gabia.bshop.support;

import java.util.Map;

import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.snippet.TemplatedSnippet;

import com.gabia.bshop.exception.ErrorCode;

public class ErrorCodeSnippet extends TemplatedSnippet {

	public ErrorCodeSnippet(ErrorCode... errorCodes) {
		super("error-code-table", Map.of("error-codes", errorCodes));
	}

	@Override
	protected Map<String, Object> createModel(final Operation operation) {
		return operation.getAttributes();
	}
}
