package com.cos.photogramstart.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.service.UserService;
import com.cos.photogramstart.web.dto.user.UserProfileDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class UserController {
	
	private final UserService userService;
	
	@GetMapping("user/{pageUserId}")
	public String profile(@PathVariable int pageUserId, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		UserProfileDto dto = userService.회원프로필(pageUserId, principalDetails.getUser().getId());
		model.addAttribute("dto", dto);
		return "user/profile";
	}
	
	@GetMapping("user/{id}/update")
	public String updateForm(@PathVariable int id, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		//Principal의 의미 : 접근주체, 인증주체. 즉, 인증된 사용자의 오브젝트 명으로 주로 사용
		
		/*
		 * 추천방식!!
		 * 시큐리티에서는 세션에 접근할 때, 세션정보는 Authentication객체에 담겨져 있기 때문에, 
		 * @AuthenticationPrincipal 어노테이션을 사용하여 Authentication 객체에 접근할 수 있다. 
		 * Authentication안에는 유저정보를 담고있는 PrincipalDetails 객체를 담고 있기 때문에, 
		 * 위 어노테이션을 사용하여 PrincipalDetails 객체에 직접 접근할 수 있고, 그 안에 있는 유저정보에 접근할 수 있다. 
		 * 
		*/
//		System.out.println("세션 정보 : "+principalDetails.getUser());
//		
//		//직접 찾아서 세션에 접근하는 원리(극혐... 쓰지말자..) 
//		//왜? -> 세션 공간에 있는 SecurityContextHolder에 접근해서 Authentication 객체에 접근하고,
//		//또 PrincipalDetails객체에 접근을 한 다음에 User객체에 접근을 해야 유저정보를 받아올 수 있기 때문에
//		//너무 복잡하고 오래들어가는 방식이 극혐이다.
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		PrincipalDetails mPrincipalDetails = (PrincipalDetails) auth.getPrincipal(); 
//		System.out.println("직접찾은 세션 정보 : "+mPrincipalDetails.getUser());
		
		//모델에 담을 필요없이 시큐리티 태그라이브러리를 이용하여 jsp에 직접 세션정보를 불러올 수 있는 태그가 있다.
//		model.addAttribute("principal", principalDetails.getUser());
		
		return "user/update";
	}
}
