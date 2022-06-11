package com.cos.photogramstart.domain.comment;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;

import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.likes.Likes;
import com.cos.photogramstart.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity //디비에 테이블을 생성
public class Comment { //N(한명의 유저는 여러개의 댓글을 달 수 있다.)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //번호 증가 전략이 데이터베이스를 따라간다.
	private int id;
	
	@Column(length = 100, nullable = false)
	private String content;
	
	//Eager / Lazy 선택기준 : 댓글을 하나 가져올 때, 유저정보와 이미지정보는 하나씩 밖에 존재하지 않는다.
	//그래서 DB에서 join을 해서 댓글 데이터에 담아서 가져와도 크게 무리는 없다.
	//그렇기 때문에 이런식으로 다대일인 경우 Eager을 통해 조인으로 가져와서 데이터를 매핑한다.
	@JsonIgnoreProperties({"images"})
	@JoinColumn(name="userId")
	@ManyToOne(fetch = FetchType.EAGER) 
	private User user; //1
	
	@JoinColumn(name="imageId")
	@ManyToOne(fetch = FetchType.EAGER)
	private Image image; //1 
	
	private LocalDateTime createDate;
	
	@PrePersist //디비에 Insert 되기 직전에 실행된다. createDate값이 자동으로 알아서 들어간다는 뜻
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}
}
