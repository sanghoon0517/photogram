package com.cos.photogramstart.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.image.ImageRepository;
import com.cos.photogramstart.web.dto.image.ImageUploadDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ImageService {
	
	private final ImageRepository imageRepository;
	
	@Transactional(readOnly = true)
	public List<Image> 인기사진() {
		return imageRepository.mPopular();
	}
	
	@Transactional(readOnly = true) //영속성 컨텍스트에서 변경감지를 통해 더티체킹을 하여 DB에 flush(반영)를 한다. 근데 readOnly가 true인 경우에는 안한다.
	public Page<Image> 이미지스토리(int principalId, Pageable pageable) {
		Page<Image> images = imageRepository.mStory(principalId, pageable);
		
		
		//images에 좋아요 상태 담기
		images.forEach((image) -> {
			
			image.setLikeCount(image.getLikes().size());
			
			image.getLikes().forEach((like) -> {
				if(like.getUser().getId() == principalId) { // 해당이미지에 좋아요를 누른 사람 찾아서 현재 로그인한 사람이 한것인지 비교
					image.setLikeState(true);
				}
			});
		});
		return images;
	}
	
	@Value("${file.path}") //application.yml에 적어놓은 경로 명의 값을 가져온다.
	private String uploadFolder;
	
	@Transactional //Service에서 db에 변화를 주는 작업을 하게 될 때는 무조건 @Transactional 어노테이션을 걸어주어야 한다.
	public void 사진업로드(ImageUploadDto imageUploadDto, PrincipalDetails principalDetails) {
		UUID uuid = UUID.randomUUID(); //uuid(Universally Unique Identifier)란, 네트워크 상에서 고유성이 보장되는 id를 만들기 위한 표준규약을 말한다. 즉 중복값없는 랜덤값이다. 그러나 아주 극단적인 확률로 중복이 날 가능성이 희박하게 있다.
		String imageFileName = uuid+"_"+imageUploadDto.getFile().getOriginalFilename(); //1.jpg
		System.out.println("이미지 파일 이름 : "+imageFileName);
		
		Path imageFilePath = Paths.get(uploadFolder+imageFileName);
		
		//통신 또는 I/O(하드디스크에 기록을 하거나 읽을 때)가 일어날 때, 예외가 발생할 수 있다.(Runtime(실행중) 에러 발생 가능.)
		try {
			Files.write(imageFilePath, imageUploadDto.getFile().getBytes()); //파일을 쓴다.
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		//image 테이블에 저장
		Image image = imageUploadDto.toEntity(imageFileName, principalDetails.getUser()); 
		Image imageEntity = imageRepository.save(image);
		
//		//imageEntity안에 있는 Image클래스와 User클래스가 서로 참조하는 관계라 무한 참조가 일어나서 에러가 났음.
//		//그래서 toString을 재정의하여 User에 대한 내용을 아예 삭제함
//		//Object를 sysout할때 주의할 것.. return할 때도 마찬가지.
//		System.out.println(imageEntity.toString()); 
		
	}
}
