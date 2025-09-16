package com.catboardback.config;

import com.catboardback.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 필터 => 요청, 응답을 중간에서 가로챈 다음 => 필요한 동작을 수행.
        // 1. 요청 헤더(Authorization)에서 JWT 토큰을 꺼냄
        String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (jwtToken != null) {
            // 2. 꺼낸 토큰에서 사용자 정보(예: email) 추출
            String email = jwtService.parseToken(request);
            // 3. 추출된 유저 정보로 Authentication을 만들어 SecurityContext에 set
            // SecurityContext에 인증 객체 저장UsernamePasswordAuthenticationToken(Principal, Credentials, 권한목록)
            if (email != null) {
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        email,
                        null, //JWT 인증 후에는 이미 인증이 끝난 상태라, 더 이상 비밀번호가 필요 없기 때문
                        java.util.Collections.emptyList());
                // SecurityContext에 인증 정보 세팅 → 이후 Security에서 사용자 인증됨으로 인식
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 마지막에 다음 필터를 호출
        filterChain.doFilter(request, response);
    }
}
