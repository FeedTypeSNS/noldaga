package com.noldaga.configuration;


import com.noldaga.configuration.filter.JwtTokenFilter;
import com.noldaga.exception.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor

public class AuthenticationConfig {

//    private final UserService userService; //토큰 필터에 넣어줄려고
    private final UserDetailsService userDetailsService;
    @Value("${jwt.secret-key}")
    private String key;//토큰 필터에 넣어줄려고



    //ignore 안된 애들이 토큰필터를 거쳐서 여기로 오게됨.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http.csrf().disable()//csrf disable 안해주면 get만 가능함...
                .authorizeRequests(auth -> auth
                        .mvcMatchers(
                                HttpMethod.GET
                        ).permitAll()
                        .mvcMatchers(
                                HttpMethod.POST,
                                "/test",
                                "/api/users/join",
                                "/api/users/login",
                                "/api/users/check"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(new JwtTokenFilter(key, userDetailsService), UsernamePasswordAuthenticationFilter.class) //로그인 인증처리를 하는 Username~ 필터전에 JwtTokenFilter를 실행
                .exceptionHandling() //인증중 예외 터졌을때 여기로 이동
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()

                .formLogin().and()
                .logout()
                .logoutSuccessUrl("/")
                .and()
                .build();
    }


    @Bean//단순 페이지 불러오는 요청에서는 토큰이 있든없든 토큰필터를 거치지 않아도 되는데 무조건 토큰 필터를 거치게 되어있음.
    public WebSecurityCustomizer webSecurityCustomizer(){ //스프링 시큐리티에서 제외하겠다 ( api 이외의것들인 static resource 등: 페이지 불러오기, favicon 요청등)

        return (web) -> web.ignoring().regexMatchers("^(?!/api).*"); //  /api로 시작 하지않으면 시큐리티 무시 (토큰필터 안거침)

    }



}
