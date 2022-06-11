package com.cos.photogramstart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cos.photogramstart.config.oauth.OAuth2DetailsService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableWebSecurity //해당 파일로 시큐리티를 활성화시킴
@Configuration //IoC(제어의 역전)를 하여 스프링 빈 컨테이너가 해당 클래스를 인지하여 관리할 수 있도록 등록 시킨다.
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	private final OAuth2DetailsService oAuth2DetailsService;
	
	@Bean
	public BCryptPasswordEncoder encode() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//super를 삭제 -> 기존 시큐리티가 가지고 있는 기능이 다 비활성화된다.
		http.csrf().disable(); //csrf토큰 비활성화
		http.authorizeRequests()
			.antMatchers("/", "/user/**", "/image/**", "/subscribe/**", "/comment/**", "/api/**").authenticated()
			.anyRequest().permitAll()
			.and()
			.formLogin()
			.loginPage("/auth/signin") //GET
			.loginProcessingUrl("/auth/signin") //POST -> 스프링 시큐리티가 로그인 프로세스 진행 
			.defaultSuccessUrl("/")
			.and()
			.oauth2Login() //form 로그인도 하는데, oauth2로그인도 할거야.
			.userInfoEndpoint() //oauth2로그인을 하게 되면, 최종응답으로 회원정보를 바로 받을 수 있다.
			.userService(oAuth2DetailsService);
	}
}
