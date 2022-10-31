package member.repository;

import member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    //이 'JPArepository'를 통해서,
    //1.'어떤 entity를 다룰 것인지('MemberEntity'),
    //2.그리고 그 'entity 객체의 id값(=pk값)의 타입이 무엇인지('Long')를 적어줌

    //DB에 전달해주고 싶은 쿼리 : select * from member_table where memberEmail =? //30:18~
    //리턴타입: MemberEntity
    //매개변수: MemberEmail(String 타입)

    MemberEntity

}
