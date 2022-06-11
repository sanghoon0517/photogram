package com.cos.photogramstart.web;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.service.ImageService;
import com.cos.photogramstart.web.dto.image.ImageUploadDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ImageController {
	
	private final ImageService imageService;
	
	@GetMapping({"/","/image/story"})
	public String story() {
		return "image/story";
	}
	
	//API구현한다면? - 이유 : 브라우저에서 요청하는게 아니라, 안드로이드,IOS요청일 경우에는 html을 받을 수 없으니 데이터 리턴이 맞다.
	@GetMapping("/image/popular")
	public String popular(Model model) {
		//api는 데이터를 리턴하는 컨트롤러임. 근데 데이터를 리턴할 필요는 없어서 그냥 페이지를 리턴하기 위해 여기서 구현
		List<Image> images = imageService.인기사진();
		model.addAttribute("images", images);
		return "image/popular";
	}
	
	@GetMapping("/image/upload")
	public String upload() {
		return "image/upload";
	}
	
	@PostMapping("/image")
	public String imageUpload(ImageUploadDto imageUploadDto, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		if(imageUploadDto.getFile().isEmpty()) {
			throw new CustomValidationException("이미지가 첨부되지 않았습니다.", null);
		}

		imageService.사진업로드(imageUploadDto, principalDetails);
		return "redirect:/user/"+principalDetails.getUser().getId();			
	}
}
