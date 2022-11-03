package com.codestates.member;


//[코드 4-17] : 회원가입 과정에서 'MemberService Bean 등록'을 위한 'JavaConfiguration' 구성

public interface MemberService {
    Member createMember(Member member);
}
