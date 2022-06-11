package com.cos.photogramstart.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.handler.ex.CustomException;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.util.Script;
import com.cos.photogramstart.web.dto.CMRespDto;

@RestController	  //데이터를 리턴
@ControllerAdvice //모든 Exception을 다 낚아챔
public class ControllerExceptionHandler {
	
	@ExceptionHandler(CustomValidationException.class) //CustomValidationException이 발동하는 모든 예외들을 얘가 다 낚아챔
	public String validationException(CustomValidationException e) {
		//CMRespDto, Script 비교
		//1. 클라이언트에게 응답할 때는 Script 좋음
		//2. Ajax통신 - CMRespDto
		//3. Android통신 - CMRespDto
		
		if(e.getErrorMap() == null) {
			return Script.back(e.getMessage());
		} else {
			return Script.back(e.getErrorMap().toString());
		}
  		
	}
	
	@ExceptionHandler(CustomException.class) //CustomValidationException이 발동하는 모든 예외들을 얘가 다 낚아챔
	public String exception(CustomException e) {
		//CMRespDto, Script 비교
		//1. 클라이언트에게 응답할 때는 Script 좋음
		//2. Ajax통신 - CMRespDto
		//3. Android통신 - CMRespDto
		
		return Script.back(e.getMessage());
	}
	
	@ExceptionHandler(CustomValidationApiException.class) //CustomValidationApi Exception이 발동하는 모든 예외들을 얘가 다 낚아챔
	public ResponseEntity<?> validationApiException(CustomValidationApiException e) {
		return new ResponseEntity<>(new CMRespDto<>(-1, e.getMessage(), e.getErrorMap()),HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(CustomApiException.class) //CustomValidationApi Exception이 발동하는 모든 예외들을 얘가 다 낚아챔
	public ResponseEntity<?> apiException(CustomApiException e) {
		return new ResponseEntity<>(new CMRespDto<>(-1, e.getMessage(), null),HttpStatus.BAD_REQUEST);
	}
}
