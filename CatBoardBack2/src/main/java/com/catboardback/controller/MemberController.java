package com.catboardback.controller;

import com.catboardback.dto.AccountCredentials;
import com.catboardback.dto.MemberDto;
import com.catboardback.service.JwtService;
import com.catboardback.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController
{
    private final MemberService memberService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(
            @Valid @RequestBody MemberDto memberDto,
            BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("필수 입력값이 누락되었습니다.");
        }
        try {
            memberService.saveMember(memberDto);
            return ResponseEntity.ok(memberDto.getEmail());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("회원가입 중 에러가 발생했습니다.");
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AccountCredentials credentials){
        // 1. user의 id pw 정보를 기반으로 UsernamePasswordAuthenticationToken을 생성한다.
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(
                        credentials.getEmail(), credentials.getPassword());
        // 2. 생성된 UsernamePasswordAuthenticationToken을 authenticationManager에게 전달.
        // 3. authenticationManager은 UserDetailsService의 loadUserByUsername을 호출 (DB에 있는 유저정보 UserDetails객체를 불러옴)
        // 4. 조회된 유저정보(UserDetails와 UsernamePasswordAuthenticationToken을 비교해 인증처리.
        Authentication authentication = authenticationManager.authenticate(token);
        // 5. 최종 반환된 Authentication(인증된 유저 정보)를 기반으로 JWT TOKEN 발급
        String jwtToken = jwtService.generateToken(authentication.getName());
        // 6. 컨트롤러는 응답 헤더(Authorication)에 Bearer <JWT TOKEN VALUE> 형태로 응답
        // 이후 클라이언트는 이 토큰을 가지고 다른 API 요청시 Authorization 헤더에 넣어 인증을 받게됨.
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .build();
    }
}
