package com.codestates.member;

import com.codestates.audit.Auditable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;


//[코드 4-29]
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Member extends Auditable implements Principal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(length = 100, nullable = false)
    private String fullName;

    @Column(nullable = false, updatable = false, unique = true)
    private String email;

    @Column(length = 100, nullable = false)
    private String password;

    @Enumerated(value = EnumType.STRING)
    @Column(length = 20, nullable = false)
    private MemberStatus memberStatus = MemberStatus.MEMBER_ACTIVE;


    //(1)User의 권한 정보 테이블과 매핑되는 정보
    //Member 엔티티 클래스와 User의 권한 정보를 매핑하는 것
    //(1)과 같이 List, Set 같은 컬렉션 타입의 필드는 @ElementCollection 애너테이션을 추가하면
    //User 권한 정보와 관련된 별도의 엔티티 클래스를 생성하지 않아도 간단하게 매핑 처리가 됨.
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();


    public Member(String email) {
        this.email = email;
    }

    public Member(String email, String fullName, String password) {
        this.email = email;
        this.fullName = fullName;
        this.password = password;
    }

    @Override
    public String getName() {
        return getEmail();
    }

    public enum MemberStatus {
        MEMBER_ACTIVE("활동중"),
        MEMBER_SLEEP("휴면 상태"),
        MEMBER_QUIT("탈퇴 상태");


    }

    @Getter
    private String status;

    MemberStatus(String status) {
        this.status = status;
    }
}

public enum MemberRole {
    ROLE_USER,
    ROLE_ADMIN
}

