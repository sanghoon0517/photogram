package com.cos.photogramstart.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

//어노테이션이 없어도 JpaRepository를 상속하면 IoC등록이 자동으로 이루어진다.
public interface UserRepository extends JpaRepository<User, Integer>{
	//JPA Named Query(명명된 쿼리) -Query creation(Query method)
	//JPA가 제공하는 명명된 쿼리는 특정 규칙으로 이루어진 메소드 명을 적으면 자동으로 구현해준다.
	User findByUsername(String username); //https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#reference 를 참고하면 된다.
}
