package com.catboardback.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtFilter jwtFilter;
    private final AuthEntryPoint authEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        // CSRF 보호 기능 끄기 (JWT 기반 인증에선 세션을 쓰지 않으므로 필요 없음)
        http.csrf((csrf) -> csrf.disable())
                // 세션을 아예 만들지 않도록 설정 (STATELESS → 모든 요청은 JWT 토큰 기반으로 인증)
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 요청 URL 별 접근 권한 설정
                .authorizeHttpRequests((request) -> request
                        // 로그인 요청(/login)은 누구나 접근 허용
                        .requestMatchers(HttpMethod.POST, "/login","/signup").permitAll()
                        // 이미지
                        .requestMatchers("/images/**").permitAll()
                        // 그 외 모든 요청은 인증 필요
                        .anyRequest().authenticated())
                // 로그인 필터보다 먼저 모든 요청을 가로채서 JWT 토큰을 확인하고 인증 정보를 SecurityContext에 넣겠다
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling((ex)->ex
                        .authenticationEntryPoint(authEntryPoint));
        // 최종적으로 보안 필터 체인 객체 반환
        return http.build();
    }
    /*
       PasswordEncoder : Spring Security에서 비밀번호를 암호화하고 검증하는 인터페이스
       NoOpPasswordEncoder : 암호화를 전혀 하지 않는 방식
       즉, DB에 저장된 비밀번호와 사용자가 입력한 비밀번호를 그대로 비교
       {noop}1234처럼 비밀번호 앞에 {noop}을 붙여서 사용
       */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
    //스프링 시큐리티에서 기본으로 설정된 인증 매니저(AuthenticationManager)를 Bean으로 등록해두는 메서드
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        // JWT 로그인 시, 아이디/비밀번호 검증을 직접 수행할 때 필요.
        // => 로그인 성공 시 Authentication 반환 → 이 정보로 JWT 발급
        return authConfig.getAuthenticationManager();

    }
}
