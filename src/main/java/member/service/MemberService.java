package member.service;


import lombok.RequiredArgsConstructor;
import member.dto.MemberDTO;
import member.entity.MemberEntity;
import member.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public MemberDTO login(MemberDTO memberDTO) {
        /*
        1.'login.html'로부터 입력받은 '이메일 주소, '비밀번호'를 받아오고
        ((1) 클라이언트 ----dto객체---->> 컨트롤러)
        2.이제 그 정보를 DB에 전달하여, DB로부터 '해당 이메일의 정보'를 가져와서
        ((2) 컨트롤러 ----entity객체(by 'repository 객체')---->> DB)
        ((3) 컨트롤러 <<--entity객체(by 'repository 객체')<<---- DB)
        3.'컨트롤러'가 '클라이언트로부터 입력받은 비밀번호'와 'DB에서 조회한 비밀번호'의 '일치 여부'를 판단하여
        4-(1) 일치하면 --> 로그인 성공 처리
        4-(2) 일치하지 않으면 --> 로그인 실패 처리
         */

        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberEmail(memberDTO.getMemberEmail());
        //'repository 객체(인터페이스 MemberRepository 참조)'가 db로부터 찾아서 가져온 데이터를 여기 '서비스 객체'로 전달해줌

        if(optionalMemberEntity.isPresent()) { //1강 38:00~
                                              //'db'에 'repository 받아온 기존이ㅡ DTO 데이터에 대응하는 데이터'가 있어서
                                              //그것을 'repository가 받아서 가져오게 된다면',
            MemberEntity loginEntity = optionalMemberEntity.get();
            if (loginEntity.getMemberPassword().equals(memberDTO.getMemberPassword())) {
                return MemberDTO.toMemberDTO(loginEntity); //'로그인 성공'하면 --> 'DTO 객체'를 반환해주고
            } else {
                return null; //'로그인 실패의 경우 1': 조회는 됐는데, 비밀번호가 다를 때
            }
        }else {
                return null; //로그인 실패의 경우 2': 조회가 안 될 때(=해당 계정이 아닐 때)
                             //근데, 지금 '경우 1'과 '경우 2'의 해석이 바뀐 것인지 내가 아직 긴가민가 함... 44:05~
            }
        }
    }


