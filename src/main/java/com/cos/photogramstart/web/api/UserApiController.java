package com.cos.photogramstart.web.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.service.SubScribeService;
import com.cos.photogramstart.service.UserService;
import com.cos.photogramstart.web.dto.CMRespDto;
import com.cos.photogramstart.web.dto.subscribe.SubscribeDto;
import com.cos.photogramstart.web.dto.user.UserUpdateDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor //final이 붙은 변수를 메모리에 올려준다.(DI)
@RestController
public class UserApiController {
	
	private final UserService userService;
	private final SubScribeService subscribeService;
	
	@PutMapping("/api/user/{principalId}/profileImageUrl")
	public ResponseEntity<?> profileImageUrlUpdate(@PathVariable int principalId, MultipartFile profileImageFile,
			@AuthenticationPrincipal PrincipalDetails principalDetails) { //jsp쪽에 있는 file타입의 name값과 명칭을 동일시 해야 잘 찾아온다.
		User user = userService.회원프로필사진변경(principalId, profileImageFile);
		principalDetails.setUser(user); //세션 값 변경
		return new ResponseEntity<>(new CMRespDto<>(1,"프로필사진변경 성공",null), HttpStatus.OK);
	}
	
	@GetMapping("api/user/{pageUserId}/subscribe")
	public ResponseEntity<?> subscribeList(@PathVariable int pageUserId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		List<SubscribeDto> subscribeDto = subscribeService.구독리스트(principalDetails.getUser().getId(), pageUserId);
		
		return new ResponseEntity<>(new CMRespDto<>(1, "구독자 정보 리스트 불러오기 성공",subscribeDto), HttpStatus.OK);
	}
	
	@PutMapping("api/user/{id}")
	public CMRespDto<?> update(
			@PathVariable int id, 
			@Valid UserUpdateDto userUpdateDto,
			BindingResult bindingResult, //반드시 @Valid가 적혀있는 다음 파라미터 다음에 적어줘야만 동작한다.
			@AuthenticationPrincipal PrincipalDetails principalDetails) {
					
		System.out.println("유저 업데이트 정보"+userUpdateDto);
		User userEntity = userService.회원수정(id, userUpdateDto.toEntity());
		principalDetails.setUser(userEntity);
		
		return new CMRespDto<>(1,"회원 수정 완료",userEntity); //응답시에 userEntity의 모든 getter 함수가 호출되고, JSON으로 파싱하여 응답한다.
		
	}
}
