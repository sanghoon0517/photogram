package com.cos.photogramstart.service;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.subscribe.SubscribeRepository;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.handler.ex.CustomException;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.web.dto.user.UserProfileDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
	
	private final UserRepository userRepository;
	private final SubscribeRepository subscribeRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Value("${file.path}") //application.yml에 적어놓은 경로 명의 값을 가져온다.
	private String uploadFolder;
	
	@Transactional
	public User 회원프로필사진변경(int principalId, MultipartFile profileImageFile) {
		UUID uuid = UUID.randomUUID(); //uuid(Universally Unique Identifier)란, 네트워크 상에서 고유성이 보장되는 id를 만들기 위한 표준규약을 말한다. 즉 중복값없는 랜덤값이다. 그러나 아주 극단적인 확률로 중복이 날 가능성이 희박하게 있다.
		String imageFileName = uuid+"_"+profileImageFile.getOriginalFilename(); //1.jpg
		System.out.println("이미지 파일 이름 : "+imageFileName);
		
		Path imageFilePath = Paths.get(uploadFolder+imageFileName);
		
		//통신 또는 I/O(하드디스크에 기록을 하거나 읽을 때)가 일어날 때, 예외가 발생할 수 있다.(Runtime(실행중) 에러 발생 가능.)
		try {
			Files.write(imageFilePath, profileImageFile.getBytes()); //파일을 쓴다. //파일 서버에 저장
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		User userEntity = userRepository.findById(principalId).orElseThrow(() -> {
			throw new CustomApiException("유저를 찾을 수 없습니다.");
		});
		userEntity.setProfileImageUrl(imageFileName);

		return userEntity;
	} //@Transactional에 의해 더티체킹()을 통해 업데이트된다. 
	
	@Transactional(readOnly = true)
	public UserProfileDto 회원프로필(int pageUserId, int principalId) {
		UserProfileDto dto = new UserProfileDto();
		
		// SELECT * FROM image WHERE userId = :userId;
		User userEntity = userRepository.findById(pageUserId).orElseThrow(() -> {
			throw new CustomException("존재하지 않는 프로필 페이지입니다.");
		});
		
		dto.setUser(userEntity);
		dto.setPageOwnerState(pageUserId == principalId); //1은 페이지 주인, -1은 페이지 주인 아님
		dto.setImageCount(userEntity.getImages().size());
		
		int subscribeState = subscribeRepository.mSubscribeState(principalId, pageUserId);
		int subscribeCount = subscribeRepository.mSubscribeCount(pageUserId);
		
		dto.setSubscribeState(subscribeState == 1);
		dto.setSubscribeCount(subscribeCount);
		
		userEntity.getImages().forEach((image)->{
			image.setLikeCount(image.getLikes().size());
		});
		
		return dto;
	}
	
	@Transactional
	public User 회원수정(int id, User user) {
		//1. 영속화
		//옵셔널을 쓰면 1. 무조건 찾았다. 걱정 ㄴㄴ : get() 2. 못찾았어 예외처리할게 : orElseThrow()
		User userEntity = userRepository.findById(id).orElseThrow(() -> {
				return new CustomValidationApiException("찾을 수 없는 ID입니다.");
		}); 
		
		//2. 영속화된 오브젝트를 수정 - 더티체킹 (업데이트 완료)
		userEntity.setName(user.getName());
		
		String rawPassword = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		
		userEntity.setPassword(encPassword);
		userEntity.setBio(user.getBio());
		userEntity.setWebsite(user.getWebsite());
		userEntity.setGender(user.getGender());
		userEntity.setPhone(user.getPhone());
		
		return userEntity;
	} //더티체킹이 일어나서 업데이트가 완료된다.
}
