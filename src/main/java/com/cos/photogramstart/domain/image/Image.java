package com.cos.photogramstart.domain.image;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import com.cos.photogramstart.domain.comment.Comment;
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
public class Image { //1개 올릴 수도 있고, N개 올릴 수도 있다. (photogram이니깐 하나는 무조건 올려야됌)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //번호 증가 전략이 데이터베이스를 따라간다.
	private int id;
	
	private String caption; //오늘 나 좀 피곤해~ 등등 잡담
	private String postImageUrl; //사진을 전송받아서 그 사진을 서버에 특정 폴더에 저장 - db에 그 저장된 경로를 insert할 것이다.

	@JsonIgnoreProperties({"images"})
	@JoinColumn(name="userId")
	@ManyToOne(fetch = FetchType.EAGER) //이미지를 select하면 조인해서 User정보를 같이 들고온다.
	private User user; //1명이서 이미지를 올린다. (여러명이 이미지를 올리는건 말이 안된다.) //이미지 N개 : 유저 1명
	
	//이미지 좋아요
	@JsonIgnoreProperties({"image"}) //likes를 리턴할 때, Likes 클래스 내부의 이미지를 리턴하지 않으려면 Image클래스 내부의 image를 리턴받지 않기 위해 적용
	@OneToMany(mappedBy = "image") //Likes클래스의 image변수명에 양방향 매핑걸기
	private List<Likes> likes;
	
	@Transient //DB에 컬럼이 만들어지지 않는다.
	private boolean likeState;
	
	@Transient //DB에 컬럼이 만들어지지 않는다.
	private int likeCount;
	
	//이미지에 댓글 달기
	@OrderBy("id DESC") //id 역순으로 정렬
	@JsonIgnoreProperties({"image"}) //Comment 객체가 가지고 있는 image를 들고오지 마라. (무한참조 방지)
	@OneToMany(mappedBy = "image", fetch = FetchType.LAZY) //Comment객체의 image변수명에 양방향 매핑걸기
	private List<Comment> comments;
	
	private LocalDateTime createDate;
	
	@PrePersist //디비에 Insert 되기 직전에 실행된다. createDate값이 자동으로 알아서 들어간다는 뜻
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}

	//오브젝트를 콘솔에 출력할 때, 문제가 될 수 있어서 User부분을 출력되지 않게 함
//	@Override
//	public String toString() {
//		return "Image [id=" + id + ", caption=" + caption + ", postImageUrl=" + postImageUrl
//				+ ", createDate=" + createDate + "]";
//	}
	
	
}
