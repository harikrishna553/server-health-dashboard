package com.sample.app.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sample.app.controller.SystemsToMonitorController;
import com.sample.app.dto.BaseException;

@ControllerAdvice(basePackageClasses = {SystemsToMonitorController.class})
@ResponseBody
public class UserExceptionHandler {
	
	@ExceptionHandler(Throwable.class)
    public ResponseEntity<com.sample.app.dto.BaseException> handleGlobalDataPortalException(Throwable ex) {
		BaseException baseException = new BaseException();
		baseException.setErrorMessage(ex.getMessage());
		
		return ResponseEntity.ok(baseException);
	}

}
