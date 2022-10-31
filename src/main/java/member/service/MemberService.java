package member.service;


import lombok.RequiredArgsConstructor;
import member.dto.MemberDTO;
import member.entity.MemberEntity;
import member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    public Long save(MemberDTO memberDTO){
        //MemberEntity memberEntity = memberRepository.save(); : 아래 1~5번을 이 하나의 문장으로 담아낼 수 있음
                                 // '메소드 save()의 매개변수로는 'entity 타입'만 들어올 수 있음
                                 //따라서, 'MemberDTO 객체'를 'entity 객체'로 '변환시켜주는 단계'가 필요함
                                 //따라서, '클래스 MemberEntity에

        MemberEntity memberEntity = MemberEntity.toSaveEntity(memberDTO);
        //< 순서 > 
        //1) 클라이언트 ----dto객체---->> 컨트롤러 
        //2) 컨트롤러 ----entity객체(by 'repository 객체')---->> DB
        //3) 컨트롤러 <<--entity객체(by 'repository 객체')<<---- DB

        //1.'클라이언트'로부터 받아온 'DTO 객체 속 데이터'를 'Entity 객체'로 옮겨 담고
        //(= MemberEntity.toSaveEntity(MemberDTO memberDTO) )
        //2.옮겨 담은 데이터를 MemberEntity memberEntity 로 받아서 여기에 저장해줌(넣어줌)
        //(= MemberEntity memberEntity)

        Long savedId = memberRepository.save(memberEntity).getId();
        //3.이제 'repository가' 데이터를 갖게 된 'Entity 객체'를, 'repository'에 저장해줌(넣어줌)
        //(= memberRepository.save(memberEntity)
        //4.이제 db로 가서 '해당하는 id값'을 가져옴
        //(= getId() )
        //5.그리고, db로부터 가져온 '해당 id값 데이터'를 '변수 savedId'에 저장해줌(넣어줌)

        return savedId;
    }

    public boolean login(MemberDTO memberDTO) {
        /*
        1.'login.html'로부터 입력받은 '이메일 주소, '비밀번호'를 받아오고
        ((1) 클라이언트 ----dto객체---->> 컨트롤러)
        2.이제 그 정보를 DB에 전달하여, DB로부터 '해당 이메일의 정보'를 가져와서
        ((2) 컨트롤러 ----entity객체(by 'repository 객체')---->> DB)
        ((3) 컨트롤러 <<--entity객체(by 'repository 객체')<<---- DB)
        3.'클라이언트로부터 입력받은 비밀번호'와 'DB에서 조회한 비밀번호'의 '일치 여부'를 판단하여
        4-(1) 일치하면 --> 로그인 성공 처리
        4-(2) 일치하지 않으면 --> 로그인 실패 처리
         */


    }
}
