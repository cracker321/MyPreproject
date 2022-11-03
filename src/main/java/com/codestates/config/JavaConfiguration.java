package com.codestates.config;

import com.codestates.member.DBMemberService;
import com.codestates.member.InMemoryMemberService;
import com.codestates.member.MemberRepository;
import com.codestates.member.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;


//[코드 4-20(db 연동 전), 4-23(db 연동 후)] : 회원가입 과정에서 MemberService Bean 등록을 위한 JavaConfiguration 구성
@Configuration
public class JavaConfiguration {

    //'인터페이스 MemeberService의 구현클래스인 InMemoryMemberService의 Bean 객체'를 생성함.
    //그러나, '메소드 inMemoryMemberService'는 'db 연동이 없을 때' 작업하는 것이므로,
    //우리는 db를 연동시켜 사용할 계획이기에, '메소드 dbMemoryMemberService'로 이름을 바꿔줌.

   @Bean
    public MemberService dbMemberService(MemberRepository memberRepository,
                                               PasswordEncoder passwordEncoder) {




       //[코드 4-23]
       //'DBMemberService 객체'는 내부에서 데이터를 db에 저장하고, 패스워드를 암호화해야 하므로,
       //그 '인자값'으로 'MemberRepository 객체'와 'PasswordEncoder 객체'를 '의존성 주입 DI' 시켜준다.
        return new DBMemberService(memberRepository, passwordEncoder);
    }
}
