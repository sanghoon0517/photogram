package com.cos.photogramstart.domain.likes;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.cos.photogramstart.domain.image.Image;
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
@Table(
		uniqueConstraints= {
				@UniqueConstraint(
						name="likes_uk",
						columnNames= {"imageId", "userId"}
				)
		}
)
public class Likes {// N
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //번호 증가 전략이 데이터베이스를 따라간다.
	private int id;
	
	@JoinColumn(name = "imageId")
	@ManyToOne
	private Image image; // 1
	
	//오류가 터지고 나서 잡아보자
	@JsonIgnoreProperties({"images"}) //User 객체 안에 있는 images들의 정보를 가져오지 말아라
	@JoinColumn(name = "userId")
	@ManyToOne
	private User user; //1
	
	private LocalDateTime createDate;
	
	@PrePersist //디비에 Insert 되기 직전에 실행된다. createDate값이 자동으로 알아서 들어간다는 뜻
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}
}
