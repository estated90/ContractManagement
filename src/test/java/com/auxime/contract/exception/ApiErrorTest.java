package com.auxime.contract.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.auxime.contract.exception.exceptionhandler.ApiError;

class ApiErrorTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@DisplayName("Covering the use of ApiError")
	@Test
	void whenManipulatingApiError_thenModificationfunction() {
		ApiError apiError = new ApiError();
		apiError.setStatus(HttpStatus.BAD_REQUEST);
		apiError.setMessage("This is a test");
		List<String> errors = new ArrayList<>();
		errors.add("test1");
		errors.add("test2");
		apiError.setErrors(errors);

		assertEquals(HttpStatus.BAD_REQUEST, apiError.getStatus());
		assertEquals("This is a test", apiError.getMessage());
		assertEquals(errors, apiError.getErrors());
	}
}
