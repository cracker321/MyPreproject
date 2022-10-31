package member.controller;


import lombok.RequiredArgsConstructor;
import member.dto.MemberDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor //생성자 주입
@RequestMapping("/member") //이제 'URL 요청이 /member로 들어오는 것'은 다 아래 컨트롤러가 처리한다.
public class MemberController {

    private final member.service.MemberService memberService;

    @GetMapping("/save-form") //'회원가입 폼' 띄워주는 것.
    public String saveForm(){

        return "memberPage/save";
    }


    @GetMapping("/login-form")
    public String saveForm(){

        return "memberPages/login";
    }


    @PostMapping("/save")
    public String save(@ModelAttribute MemberDTO memberDTO){

        memberService.save(memberDTO);

        return "memberPages/login"; //회원가입이 완료되면, 사용자에게 '로그인 페이지'를 보여줌
    }


    @PostMapping("/login")
    public String login(@ModelAttribute MemberDTO memberDTO){
        boolean loginResult = memberService.login(memberDTO);
        if(loginResult){ //로그인 결과가
            return "memberPages/main"; //성공하면 -> 'main 페이지'를 보여주고
        } else{
            return "memberPages/login"; //실패하면 -> 'login 페이지'를 다시 보여주고
        }
}
