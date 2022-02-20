package com.cos.photogramstart.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.service.AuthService;
import com.cos.photogramstart.web.dto.auth.SignupDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor //DI 역할 : final이 걸려있는 아이들의 생성자를 자동으로 만들어줌 //authService의 생성자를 자동으로 만들어주는 역할을 함
@Controller //1. IoC에 등록 //2. 파일을 리턴하는 컨트롤러의 역할을 함
public class AuthController {
	
	private static final Logger log = LoggerFactory.getLogger(AuthController.class);
	
	private final AuthService authService;
	
	@GetMapping("/auth/signin")
	public String signinForm() {
		return "auth/signin";
	}
	
	@GetMapping("/auth/signup")
	public String signupForm() {
		return "auth/signup";
	}
	
	//회원가입버트 클릭 -> /auth/signup -> /auth/signin이 리턴
	//회원가입버튼 눌렀지만 아무코토 되질 않음
	@PostMapping("/auth/signup")
	public String signup(SignupDto signupDto) { //key=value형식(x-www-form-urlencoded)으로 들어올 것임
		log.info("signupDto : "+signupDto.toString());
		//User <- SignupDto
		User user = signupDto.toEntity();
		log.info(user.toString());
		User userEntity = authService.회원가입(user);
		System.out.println("userEntity : "+userEntity);
		return "auth/signin"; //회원가입이 성공하면 로그인페이지로 이동
	}
	
}
