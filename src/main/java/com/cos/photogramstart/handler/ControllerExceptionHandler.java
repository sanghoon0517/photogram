package com.cos.photogramstart.handler;

import java.util.Map;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.web.dto.CMRespDto;

@RestController	  //데이터를 리턴
@ControllerAdvice //모든 Exception을 다 낚아챔
public class ControllerExceptionHandler {
	
	@ExceptionHandler(CustomValidationException.class) //CustomValidationException이 발동하는 모든 예외들을 얘가 다 낚아챔
	public CMRespDto<?> validationException(CustomValidationException e) {
		return new CMRespDto(-1,e.getMessage(),e.getErrorMap());
	}
}
