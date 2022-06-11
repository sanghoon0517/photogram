package com.cos.photogramstart.handler.aop;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.handler.ex.CustomValidationException;

@Component //무슨 기능인지는 모르겠지만 메모리에 띄울 때는 Component를 붙이면 된다.(RestController,Service,Configuration 등 모든 것들이 Component를 상속받아서 만들어졌다.)
@Aspect
public class ValidationAdvice {//Advice는 "공통기능"이라고 통용된다.
	
	
	
	/**
	 * @throws Throwable 
	 * @전상훈
	 * 
	 * AOP를 사용할 때는 Before, After, Around 어노테이션들이 있다.
	 * 
	 * @Before : 특정 함수가 실행되기전에 실행시키고 싶을 때 붙이는 어노테이션
	 * @After : 특정 함수가 실행된 이후에 실행시키고 싶을 때 붙이는 어노테이션
	 * @Around : 특정 함수가 실행되기전에서 관여하고, 실행 이후에도 관여하고 싶을 때 붙이는 어노테이션
	 */
	
	//제일처음의 *은 어떤함수를 선택할래?(public, protected, 접근지정자 등등)
	//마지막의 *은 메소드를 가리킨다.
	//"(..)" 끝에 이 표시는 메소드의 파라미터가 무엇이든 상관없다는 의미
	//ProceedingJoinPoint을 사용하면 해당 사용될 수 있는 메서드에 접근하여 모든 자원들에 접근이 가능하다.
	@Around("execution(* com.cos.photogramstart.web.api.*Controller.*(..))") 
	public Object apiAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		
		System.out.println("web api 컨트롤러==========================");
		Object[] args = proceedingJoinPoint.getArgs();
		for (Object arg : args) {
			if(arg instanceof BindingResult) {
				System.out.println("유효성 검사를 하는 함수입니다.");
				BindingResult bindingResult = (BindingResult)arg;
				
				//BindingResult 인터페이스는 에러내역들을 모두 모아준다.
				if(bindingResult.hasErrors()) { //에러가 있다면
					Map<String, String> errorMap = new HashMap<>();
					
					for(FieldError error : bindingResult.getFieldErrors()) {
						errorMap.put(error.getField(), error.getDefaultMessage());
					}
					throw new CustomValidationApiException("유효성 검사 실패함",errorMap);
				}

			}
		}
		//profile이라는 메서드가 있다고 가정.
		//proceedingJoinPoint -> profile함수의 모든 자원에 접근이 가능하다.
		//profile함수보다 먼저 실행된다.
		
		return proceedingJoinPoint.proceed(); //해당함수로 다시 돌아가라 ex) 프로파일 함수로 다시 돌아가라
	}
	
	@Around("execution(* com.cos.photogramstart.web.*Controller.*(..))")
	public Object advice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		System.out.println("web 컨트롤러=============================");
		Object[] args = proceedingJoinPoint.getArgs();
		for (Object arg : args) {
			if(arg instanceof BindingResult) {
				System.out.println("유효성 검사를 하는 함수입니다.");
				
				BindingResult bindingResult = (BindingResult)arg;
				
				//BindingResult 인터페이스는 에러내역들을 모두 모아준다.
				if(bindingResult.hasErrors()) { //에러가 있다면
					Map<String, String> errorMap = new HashMap<>();
					
					for(FieldError error : bindingResult.getFieldErrors()) {
						errorMap.put(error.getField(), error.getDefaultMessage());
						System.out.println("===================================");
						System.out.println(error.getDefaultMessage());
						System.out.println("===================================");
					}
					throw new CustomValidationException("유효성 검사 실패함",errorMap);
				}
			}
		}
		return proceedingJoinPoint.proceed(); //해당함수로 다시 돌아가라
	}
}
