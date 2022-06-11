package com.cos.photogramstart.web.dto.user;

import javax.validation.constraints.NotBlank;

import com.cos.photogramstart.domain.user.User;

import lombok.Data;

@Data
public class UserUpdateDto {
	@NotBlank
	private String name;
	@NotBlank
	private String password;
	private String website;
	private String bio;
	private String phone;
	private String gender;
	
	//조금 위험함. 수정이 필요할 예정
	public User toEntity() {
		return User.builder()
				.name(name)				
				.password(password)		//값이 없으면 안된다.
				.website(website)
				.bio(bio)
				.phone(phone)
				.gender(gender)
				.build();
	}
}
