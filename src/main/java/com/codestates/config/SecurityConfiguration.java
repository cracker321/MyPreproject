package com.codestates.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

public class SecurityConfiguration {



   //[유어클래스 코드 4-8] : 스프링 시큐리티의 보안 구성 중에서, 우리가 만들어 둔 '커스텀 로그인 페이지'를 사용하기 위한
   //                      최소한의 설정만을 추가한 코드
   //1.'HttpSecurity'를 통해 'HTTP 요청에 대한 보안 설정'을 구성함.
   //2.'커스텀 로그인 페이지 지정'하기
    public SecurityFilterChain filterChainV1(HttpSecurity http) throws Exception{
        http
                .csrf().disable() //CSRF(Cross-Site Request Forgery) 공격에 대한 시큐리티 설정 비활성화
                                  //스프링시큐리티는 기본적으로 아무 설정을 하지 않으면, csrf() 공격을 받ㅇ지하기 위해
                                  //클라이언트로부터 CSRF Token을 수신 후 검증함
                                  //지금은 로컬 환경에서 학습을 진행하기 때문에, CSRF 공격에 대한 설정 필요X
                                  //만약, 'csrf().disable()'설정을 하지 않는다면, '403 에러'로 인해 정상접속 불가능함.
                .formLogin() //'기본적인 인증 방법'을 '폼 로그인 방식'으로 지정함
                .loginPage("/auths/login-form")  //'컨트롤러 AuthController의 메소드 loginForm()'에
                                                 //요청을 전송하는 URL임
                .loginPage("/process_login") //'로그인 인증 요청을 수행할 요청 URL'을 지정함
                .loginProcessingUrl("/process_login") //'로그인 인증 요청을 수행할 요청 URL'을 지정함
                                                      //'인자값 '/process_login''은 우리가 만들어 둔 'login.html'에서
                                                      //'form 태그의 action 속성에 지정한 URL'과 '동일'함.
                .failureUrl("/auths/login-form?error") //'로그인 인증에 실패할 경우'에,
                                                                         //'어떤 화면으로 리다이렉트 해줄지'를 지정해줌
                //'로그인에 실패할 경우'에, '로그인 화면을 표시해주고, 로그인 인증에 실패했다는 메시지를 표시해주는 것'이 
                //가장 자연스럽기 때문에, '메소드 failureUrl'의 '인자값'으로 '로그인 페이지의 URL인 '/auths/login-form?error''
                //를 지정해줌
                .and()
                .authroizeHttpRequests() //'클라이언트의 요청'이 들어오면, '접근 권한을 확인할게!' 라고 정의함
                .anyRequest() //'클라이언트의 모든 요청'에 대해 '접근을 허용'함
                .permitAll(); //'클라이언트의 모든 요청'에 대해 '접근을 허용'함

            return http.build();

    }




    //[유어클래스 코드 4-11] : 요청 URI에 대한 접근 권한 부여
    //저 위에 코드들을 작성함으로써, '클래스 SecurityConfiguration'에서 설정해 둔 '사용자 계정 kevin@gmail.com'을 통해
    //로그인 인증에 성공할 수 있음
    //이제 아래 코드들을 통해, '사용자에게 부여된 Role'을 이용하여, '요청 URI에 대한 접근 권한을 부여'할 수 있음
    @Bean
    public SecurityFilterChain filterChainV2(HttpSecurity http) throws Exception{

        http
                .csrf().disable()
                .formLogin()
                .loginPage("/auths/login-form")
                .loginProcessingUrl("/process_login")
                .failureUrl("/auths/login-form?error")
                .and()
                .exceptionHandling().accessDeniedPage("/auths/access-denied")
                //'권한이 없는 사용자'가 '특정 request URI에 접근할 경우에 발생하는 403 Forbidden 에러를 처리'하기 위한
                //페이지를 설정함.
                //즉, '권한이 없는 사용자'가 '특정 request URI'로 'request를 전송할 경우',
                //'www.localhost:8080/orders 화면'에 '접근 권한이 없는 계정으로 로그인 했습니다'를 보여줌
                .and()
                .authorizeHttpRequests(authorize -> authorize //'람다식'을 통해 'request URI'에 대한 '접근 권한'을
                                                              //부여해줌
                 //'메소드 antMatchers'는 'ant 라는 빌드 툴에서 사용되는 Path Pattern'을 이용해서
                 //매치되는 URL를 표현해줌
                .antMatchers("/orders/**").hasRole("ADMIN") //'ADMIN이라는 Role을 부여받은 사용자만'
                                                                       //'/orders로 시작하는 모든 URL에 접근 가능'함.
                 // '/orders/*'라면, '/orders/1'과 같이 한 자릿수, '/orders/**'라면, '/orders/11'과 같이 두 자릿수 가능.
                .antMatchers("/members/my-page").hasRole("USERS")
                .antMatchers("/**").permitAll() //위에서 지정한 URL '이외'의 '나머지 모든 URL'은 
                                                           //'Role에 상관 없이' '접근이 가능함'을 의미
                // '메소드 permitAll'은 '항상 마지막'에 와야 한다. 왜냐하면, 이게 먼저 올 경우, '스프링 시큐리티'는
                // 'Role에 상관 없이 request URL에 대한 접근을 허용한다는 의미'가 되기 때문.
                // 즉, 그 뒤에 오는 'antMatchers(~~).hasRole(~~)' 들이 제 기능을 하지 못하게 된다는 뜻.
                // 결과적으로, '사용자의 Role과는 무관하게 모든 request URL에 접근이 가능'하게 되어짐.
                // 따라서, '더 구체적인 URL 경로부터 접근 권한을 부여한 후', '그 다음 덜 구체적인 URL 경로에 대한 접근권한을
                // 부여'해야 한다!
                );

        return http.build();
    }




    //[유어클래스 코드 4-14] : 로그아웃 설정이 추가된 '클래스 SecurityConfiguration'
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .formLogin()
                .loginPage("/auths/login-form")
                .loginProcessingUrl("/process_login")
                .failureUrl("/auths/login-form?error")
                .and()
                .logout() //로그아웃에 대한 추가 설정을 위해 '메소드 logout'을 먼저 호출해야 함
                          //'메소드 logout'은 로그아웃 설정을 위한 'LogoutConfiurer'를 리턴해줌
                .logoutUrl("/logout") //사용자가 로그아웃을 수행하기 위한 '요청 URL'을 지정함
                                //여기서 설정한 URL은 'header.html의 로그아웃 메뉴에서 지정한 href="/logout"과
                                //동일해야 함
                .logoutSuccessUrl("/") //로그아웃을 성공적으로 수행한 이후 리다이렉트할 URL을 지정함.
                .and()
                .exceptionHandling().accessDeniedPage("/auths/access-denied")
                .and()
                .authorizeHttpRequests(authorize -> authorize
                        .antMatchers("/orders/**")).hasRole("ADMIN")
                .antMatchers("/members/my-page").hasRole("USER")
                .antMatchers("/**").permitAll()
                );
        return http.build();
    }


/*
아래의 '메소드 userDetailsService'는, 'db 연동 없는 상태'에서의 'InMemory User 등록'을 하는 역할이지만,
우리는 'db에 연동'하여 'User를 등록'하고, 'db에 저장된 User의 인증 정보를 사용'할 것이기 때문에,
'메소드 userDetailsService'를 삭제해버림.

    //[유어클래스 코드 4-6, 4-12, 4-15] :
    //어플리케이션이 실행된 상태에서, '사용자 인증을 위한 계정 정보'를 '메모리 상에 고정된 값으로 설정'하는 코드임
    //'사용자의 계정 정보를 메모리 상에 지정했기 때문'에, '애플리케이션이 실행될 때마다 사용자 계정 정보가 바뀔 일은 없음'.
    //기존에 SpringSecurity의 기본 설정을 사용할 경우에는, 매 번 애플리케이션이 실행될 때마다 랜덤하게 생성되는 패스워드를
    //이용해야 했었음.
    //즉, 어플리케이션이 실행되면, '메소드 userDetailsService'에서 설정한 '두 개의 User'가 'InMemory User'로 등록됨
    //

    //(1)USER Role을 가진 사용자 추가. [코드 4-6]
    //(2)ADMIN Role을 가진 사용자 추가. [코드 4-12]

    //'인터페이스 UserDetails': '인증된 사용자의 핵심정보를 포함'하고 있음.
    //'UserDetails 객체': '인증된 사용자의 인증 정보를 생성'함.
    @Bean
    public UserDetailsManager userDetailsService(){

        //(1)USER Role을 가진 사용자 추가. [코드 4-6]
        //'kevin@gmail.com'이라는 'InMemory User'를 추가.
        //즉, 이제 'kevin@gmail.com'에게는 'USER 역할'이 부여되었음.
        //'kevin@gmail.com'은 이제 '전체 주문 목록 보기 메뉴'를 클릭하면, '정상적으로 접근 가능'함
        UserDetails user =
                User.withDefaultPasswordEncoder() //'메소드 withDefaultPasswordEncoder'는 '디폴트 패스워드 인코더'를
                                                  //이용하여, '사용자의 패스워드를 암호화'해줌
                        .username("kevin@gmail.com") //'사용자의 username(=사용자의 id)'
                                                     //'고유한 사용자를 식별할 수 잇는 사용자 아이디 값'과 같은 것
                                                     //즉, 여기서는 '사용자의 이메일'을 'username'으로 지정함
                        .password("1111") //'사용자의 password'를 설정함. '인자값 1111'은
                                          //'메소드 withDefaultPasswordEncoder'를 통해 이제 '암호화'된다.
                                          //즉, '메소드 password의 매개변수로 들어은 1111'을 '암호화'해줌
                        .roles("USER") //'일반 사용자인 USER의 역할을 지정'해주는 메소드.
                        //대부분의 애플리케이션은 크게 '일반 사용자' 또는 '관리자 역할'로 구분되어 있고,
                        //'관리자만이 접속할 수 있는 기능'이 별도로 존재함.
                        //즉, 서비스의 '관리자'인지 or '일반 사용자'인지를 지정해줌.
                        //'관리자'에게는 '추가적으로 접속할 수 있는 기능'이 '별도로 존재'.
                        //'메소드 roles'가 이렇게 'Users의 역할'을 지정해주는 기능을 함
                        .build();


        //(2)ADMIN Role을 가진 사용자 추가. [코드 4-12]
        //'admin@gmail.com'이라는 'InMemory User'를 추가.
        //즉, 이제 'admin@gmail.com'에게는 'Admin 역할'이 부여되었음.
        //'admin@gmail.com'은 이제 '전체 주문 목록 보기 메뉴'를 클릭하면, '정상적으로 접근 가능'함
        UserDetails admin =
                User.withDefaultPasswordEncoder()
                        .username("admin@gmail.com")
                        .password("2222")
                        .roles("ADMIN")
                        .build();
        return new InMemoryUserDetailsManager(user, admin);
        //스프링시큐리티는 '인터페이스 UserDetailsManager'를 통해서, '사용자의 핵심 정보를 포함하고 있는 UserDetails'를
        //관리함. 그런데, 우리는 메모리 상에서 UserDetails를 관리하므로, '그 구현체인 클래스 InMemoryUserDetailsManager'를
        //사용함
        //위와 같이, 'new InMemoryUserDetailsManger'를 통해 'UserDetailsManager 객체'를 'Bean'에 등록하면,
        //스프링에서는 '해당 Bean이 가지고 있는 사용자의 인증 정보'가 클라이언트로부터 넘어올 경우, 정상적인 인증 프로세스를
        //수행함.

    }
*/


    //[유어클래스 코드 4-16] : '회원가입 폼으로부터 전달받은 최초의 패스워드'를 '암호화시켜주는' 역할
    //'PassEncoder'는 스프링시큐리티에서 제공하는 '패스원드 암호화 기능'을 실행시켜주는 '컴포넌트'임
    //'회원가입 폼'을 통해 최초로 애플리케이션에 전달되는 패스워드는, 암호화되어있지 않은 '플레인 텍스트(Plain Text)'임
    //따라서, '회원가입 폼으로부터 최초로 전달받은 패스워드'를 'InMemory User 등록하기 전에 암호화시켜줘야' 함.
   @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        //'PasswordEncoderFactories.createDelegatingPasswordEncoder()'를 통해 'DelegatingPasswordEncoder'를
        //먼저 생성하는데, 이 'DelegatingPasswordEncoder'가 실질적으로 'PasswordEncdoer 구현 객체'를 생성해줌.
        //'메소드 userDetailsService'에서 '미리 생성하는 InMemoryUser의 패스워드'는
        //내부적으로 디폴트 PasswordEncoder를 통해 암호화된다는 사실 잊지 말자!
    }



}
