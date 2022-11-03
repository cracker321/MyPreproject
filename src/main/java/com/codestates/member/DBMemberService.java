package com.codestates.member;

import com.codestates.auth.utils.HelloAuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


//[코드 4-19(db 연동 전), 4-24(db 연동 후), 4-30(연동 후)]
//: 회원가입 과정에서 데이터베이스에 User를 등록하기 위한 '클래스 DBMemberService'
@Transactional
public class DBMemberService implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    private final HelloAuthorityUtils authorityUtils;

    //'생성자'를 통해 'MemberRepository 객체'와 'PasswordEncoder 객체'를 '의존성 주입 DI 받음'
    public DBMemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }


    //[코드 4-30] : 회원 등록 시, 권한 정보를 DB에 저장
    public Member createMember(Member member) {
        verifyExistsEmail(member.getEmail());
        String encryptedPassword = passwordEncoder.encode(member.getPassword()); //'passwordEncoder'를 이용해
        //'패스워드를 암호화'함
        member.setPassword(encryptedPassword); //'암호화된 패스워드'를 '필드 password'에 다시 할당해줌



        //(1)Role을 DB에 저장
        //authorityUtils.createRoles(member.getEmail());를 통해 회원의 권한 정보(List<String> roles)를 생성한 뒤
        List<String> roles = authorityUtils.createRoles(member.getEmail());
        //이를 member 객체에 넘겨줌
        member.setRoles(roles);

        Member savedMember = memberRepository.save(member);

        return savedMember;
    }
}




