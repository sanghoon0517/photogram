# 포토그램 - 인스타그램 클론 코딩

위 프로젝트는 포트폴리오용을 위한 인스타그램 클론 코딩입니다.

언어 : 자바,자바스크립트

프레임워크 : 스프링부트

DB 모듈 : JPA - HIbernate 오라클

스프링부트와 ORM기반의 DB연동 모듈인 JPA를 사용하였습니다.

### STS 툴 버그가 발견되었습니다.
- 아래 주소로 가서 4.0.6 버전으로 설치해주세요. 아니면 의존성 다운로드 79프로에서 무한루프가 발생합니다.
- https://github.com/spring-projects/sts4/wiki/Previous-Versions

### STS 툴에 세팅하기 - 플러그인 설정
- https://blog.naver.com/getinthere/222322821611

### 의존성

- Sring Boot DevTools
- Lombok
- Spring Data JPA
- MariaDB Driver
- Spring Security
- Spring Web
- oauth2-client

```xml
<!-- 시큐리티 태그 라이브러리 -->
<dependency>
	<groupId>org.springframework.security</groupId>
	<artifactId>spring-security-taglibs</artifactId>
</dependency>

<!-- JSP 템플릿 엔진 -->
<dependency>
	<groupId>org.apache.tomcat</groupId>
	<artifactId>tomcat-jasper</artifactId>
	<version>9.0.43</version>
</dependency>

<!-- JSTL -->
<dependency>
	<groupId>javax.servlet</groupId>
	<artifactId>jstl</artifactId>
</dependency>
```

### yml 설정

```yml
server:
  port: 8080
  servlet:
    context-path: /
    encoding:
      charset: utf-8
      enabled: true
    
spring:
  mvc:
    view:
      prefix: /WEB-INF/views/
      suffix: .jsp
      
  datasource:
    driver-class-name: org.mariadb.jdbc.Driver
    url: jdbc:mariadb://localhost:3306/cos?serverTimezone=Asia/Seoul
    username: cos
    password: cos1234
    
  jpa:
    open-in-view: true
    hibernate:
      ddl-auto: update
      naming:
        physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
    show-sql: true
      
  servlet:
    multipart:
      enabled: true
      max-file-size: 2MB

  security:
    user:
      name: test
      password: 1234   

file:
  path: C:/src/springbootwork-sts/upload/
```

### 태그라이브러리

```jsp
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
```

![image](https://user-images.githubusercontent.com/66873195/173172699-626a4df1-3dac-4c9c-adcd-d0d36eaea568.png)

최초 로그인 페이지



![image](https://user-images.githubusercontent.com/66873195/173172705-96838fae-8bae-4ea2-b41f-04596aed46f1.png)

페이스북 로그인 연동



![image](https://user-images.githubusercontent.com/66873195/173172721-9d1385a5-b0d9-46a7-8668-055de3883db8.png)

로그인 성공 시 보여지는 화면
좋아요와 댓글을 다는 기능이 있다.



![image](https://user-images.githubusercontent.com/66873195/173172771-3cf9db8a-6609-481b-b90a-45adee028e14.png)

회원정보 화면



![image](https://user-images.githubusercontent.com/66873195/173172786-b4b2be2e-de31-4eca-9e2e-9c0bb31e055d.png)

SNS가입이 아닌 회원가입 화면



![image](https://user-images.githubusercontent.com/66873195/173172726-932c4319-4dd9-4340-8db4-4e5dba3a6d92.png)

마우스를 올리면 좋아요 개수를 확인할 수 있다.



![image](https://user-images.githubusercontent.com/66873195/173172811-109ceec6-50f8-4366-94e0-ca0f0ced79ac.png)

사진 업로드 화면



![image](https://user-images.githubusercontent.com/66873195/173172741-45803223-98dd-4d16-a499-f412daa322e9.png)

구독정보를 눌러서 내가 구독하고 있는 구독 리스트를 확인할 수 있다. 구독취소 버튼도 있어 즉시 구독을 취소할 수도 있다.
