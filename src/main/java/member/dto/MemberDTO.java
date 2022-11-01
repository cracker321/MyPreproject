package member.dto;

import lombok.Data;
import member.entity.MemberEntity;

@Data
public class MemberDTO {

    private Long id;
    private String memberEmail;
    private String memberPassword;
    private String memberName;
    private int memberAge;
    private String memberMobile;

    public static MemberDTO toMemberDTO(MemberEntity memberEntity){ //1강 42:20~
        MemberDTO memberDTO = new MemberDTO();

        memberDTO.setId(memberEntity.getId());
        memberDTO.setMemberEmail(memberEntity.getMemberEmail());
        memberDTO.setMemberPassword(memberEntity.getMemberPassword());
        memberDTO.setMemberName(memberEntity.getMemberName());
        memberDTO.setMemberAge(memberEntity.getMemberAge());
        memberDTO.setMemberMobile(memberEntity.getMemberMobile());

        return memberDTO;
    }
}
