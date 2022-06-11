package com.cos.photogramstart.config.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PrincipalDetailsService implements UserDetailsService{
	
	private final UserRepository userRepository;
	
	//패스워드는 알아서 체크한다.
	//리턴이 잘 되면 UserDetails타입을 세션으로 만들어준다.
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User userEntity = userRepository.findByUsername(username);
		if(userEntity == null) {
			return null;
		} else {
			return new PrincipalDetails(userEntity);
		}
	}
}
