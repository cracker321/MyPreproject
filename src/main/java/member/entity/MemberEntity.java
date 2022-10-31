package member.entity;


import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import member.dto.MemberDTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@NoArgsConstructor
@Getter
@Setter
@Table(name = "member_table")
@Entity
public class MemberEntity {

    @Id
    @Generated
    @Column(name = "member_id")
    private Long id;

    @Column(length = 50, unique = true)
    private String memberEmail;

    @Column(length = 20)
    private String memberPassword;

    @Column(length = 20)
    private String memberName;

    @Column
    private int memberAge;

    @Column(length = 30)
    private String memberMobile;

    
    public static MemberEntity toSaveEntity(MemberDTO memberDTO){ //'변수 toSaveEntity'는 '아직 사용(호출)되지 않아서' 그런 것임.
        //1.'클라이언트'로부터 'DTO 객체'를 받아서
        //2.'DTO 객체 속 값'을 '꺼내어' ' Entity 객체'로 '옮겨 담아주는' 과정임
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setMemberEmail(memberDTO.getMemberEmail);
        memberEntity.setMemberPassword(memberDTO.getMemberPassword);
        memberEntity.setMemberName(memberDTO.getMembername);
        memberEntity.setMemberAge(memberDTO.getMemberAge);
        memberEntity.setMemberMobile(memberDTO.getMemberMobile);

        return memberEntity;

    }

}
