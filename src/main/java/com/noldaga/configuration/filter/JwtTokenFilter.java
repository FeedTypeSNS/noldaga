package com.noldaga.configuration.filter;


import com.noldaga.domain.UserDto;
import com.noldaga.service.UserService;
import com.noldaga.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {//각요청에서 1번만 이 필터가 수행됨
    //요청의 헤더에는 token 이 있고 token 에는 claim 이 있음. 여기에서 유저정보를 꺼내서 사용함 ( 토큰 인증이 완료되면 : 조작된 토큰인지 아닌지를 검사함: 이토큰이 서버가 발행한 토큰이 맞는지 아닌지를 검사)
    //서버가 발행한 토큰이 맞다면, 토큰 만료기간 등등을 따지고 유효하면 유저정보를 꺼내서 사용.


    //토큰필터는 전처리이고, 토큰필터에서 헤더에서 토큰을 꺼내서 처리하고 뒤에서 이어받아서 시큐리티가 팅기든 말든 결정하는듯

    private final String key;
    private final UserService userService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

//        final String tokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);//헤더 필드중에서 토큰이 들어있는 부분의 헤더를 꺼냄
//
//        if(tokenHeader == null || ! tokenHeader.startsWith("Bearer ")){//토큰이 없는경우
//            log.error("Error occurs while getting header. header is null or invalid {}",request.getRequestURL());
//            filterChain.doFilter(request,response);
//            return;
//        }

        Cookie[] cookies = request.getCookies();
        Optional<Cookie> tokenCookie =null;
        if(cookies !=null){
            tokenCookie = Arrays.stream(request.getCookies()).filter(c -> c.getName().equals("tokenCookie")).findFirst();
            if(tokenCookie.isEmpty()){
                log.error("Error occurs while getting token. cookie is null or invalid{}",request.getRequestURL());
                filterChain.doFilter(request, response);
                return;
            }
        }

        try {
//            final String token = tokenHeader.split(" ")[1].trim(); //토큰 꺼냄
            final String token = tokenCookie.get().getValue();

            //토큰이 유효한지 검사 (유통기한 확인)
            if (JwtTokenUtils.isExpired(token, key)) {
                log.error("Token is expired");
                filterChain.doFilter(request, response);
                return;
            }

            //토큰에서 유저정보 꺼냄
            String username = JwtTokenUtils.extractUsername(token, key);

            //유저정보가 유효한지 확인(유저가 실제로 존재하는지)
            UserDto userDto = userService.loadUserByUsername(username);


            //모든것이 유효하면 정보를 다음으로 넘김
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDto, null, userDto.getAuthorities()
                    //Principal 에 넘겨준것이 authentication.getName() 했을때 반환됨
                    //-> UserDto implements UserDetails 처리를 안해주면 authentication.getName() 했을때 userDto.toString()이 반환되어서 뒤에서 에러남 : 임시로 여기에서 principal 에 userDto.getName() 넣어줄수있음
                    //뒤에서 authentication.getName() 반환값이 userRepository.findByUsername() 의 파라미터로 들어가게됨.
            );

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication); //여기서 넣어준 정보들은 뒤에서 이어지는 로직들에서 사용 할 수 있음


        } catch (RuntimeException e) {
            log.error("Error occurs while validating. {}", e.toString());
            filterChain.doFilter(request, response);
            return ;
        }


        filterChain.doFilter(request, response);
    }

}
