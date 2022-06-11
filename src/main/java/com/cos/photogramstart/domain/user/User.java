package com.cos.photogramstart.domain.user;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;

import com.cos.photogramstart.domain.image.Image;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 전상훈
 * 
 * JPA - Java Persistence API(자바로 데이터를 영구적으로 저장할 수 있는 API를 제공)
 *
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity //디비에 테이블을 생성
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //번호 증가 전략이 데이터베이스를 따라간다.
	private int id;
	
	@Column(length = 100, unique = true) //OAuth2.0 로그인을 위해 칼럼 늘리기
	private String username;
	@Column(nullable = false)
	private String password;
	@Column(nullable = false)
	private String name;
	private String website; //웹 사이트
	private String bio; //자기소개
	@Column(nullable = false)
	private String email;
	private String phone;
	private String gender;
	
	private String profileImageUrl; //사진
	private String role; //권한
	
	//나는 연관관계의 주인이 아니다. 그러니깐 테이블에 컬럼을 만들지 마.
	//User를 select 할 때, 해당 User id로 등록된 image들을 다 가져와
	//Lazy일 때는, User를 Select할 때, 해당 User id로 등록된 image들을 가져오지 마. - 대신 getImages()의 image들이 호출될 때 가져와.
	//Eager일 때는, User를 Select할 때, 해당 User id로 등록된 image들을 전부 join해서 가져와.
	@OneToMany(mappedBy="user",fetch = FetchType.LAZY)
	@JsonIgnoreProperties({"user"}) //Image 클래스 내부에 있는 것들 중 user를 뺴고 파싱해준다.
	private List<Image> images; //양방향 매핑
	
	private LocalDateTime createDate;
	
	@PrePersist //디비에 Insert 되기 직전에 실행된다. createDate값이 자동으로 알아서 들어간다는 뜻
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", name=" + name + ", website="
				+ website + ", bio=" + bio + ", email=" + email + ", phone=" + phone + ", gender=" + gender
				+ ", profileImageUrl=" + profileImageUrl + ", role=" + role + ", createDate="
				+ createDate + "]";
	}
	
	
}
