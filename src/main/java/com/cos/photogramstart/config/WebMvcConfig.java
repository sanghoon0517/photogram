package com.cos.photogramstart.config;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer{ //web설정파일이 된다. (web.xml역할)
	
	@Value("${file.path}")
	private String uploadFolder;
	
	@Override
		public void addResourceHandlers(ResourceHandlerRegistry registry) {
			WebMvcConfigurer.super.addResourceHandlers(registry);
			
			// "/upload/**"라고 적으면 C:/dev/spring-4.11.0_workspace/upload/ 로 바꿔주겠다.
			registry
				.addResourceHandler("/upload/**") //jsp페이지에서  "/upload/**" 이런 주소로 패턴이 나오면 발동
				.addResourceLocations("file:///"+uploadFolder)
				.setCachePeriod(60*10*6) //1시간 캐시 기간
				.resourceChain(true)
				.addResolver(new PathResourceResolver());
		}
	
}
