package member.repository;

import member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    Optional<MemberEntity> findByMemberEmail(String memberEmail); //'이메일 주소'로 찾을거야!. 1강 30:55~
    //이 'JPArepository'를 통해서,
    //1.'어떤 entity를 다룰 것인지('MemberEntity'),
    //2.그리고 그 'entity 객체의 id값(=pk값)의 타입이 무엇인지('Long')를 적어줌

    //DB에 전달해주고 싶은 쿼리 : select * from member_table where memberEmail =? //30:18~
    //    //리턴타입: MemberEntity :
    //    //매개변수: MemberEmail(String 타입)
    //    Optional<MemberEntity> findByMemberEmail(String memberEmail);
    //    //'MemberEmail'로 'db'에서 'email 데이터'를 찾을 것이기 때문에, 위에처럼 선언해줌



    //'인터페이스' 내부의 메소드는 '구현부'를 가질 수 없는 '추상 메소드'이어야 한다!
    //'추상 메소드'는 '메소드의 형태(리턴타입, 메소드 이름, 매개변수)'만 정의해주면 된다!
    //그리고, '해당 추상 메소드'의 '구현부'는 '해당 인터페이스'를 구현하는 '구현 클래스의 내부'에서 '구현 메소드'로 정의해서 해주는 것이다.


}
