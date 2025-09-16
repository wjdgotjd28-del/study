package com.catboardback.service;

import com.catboardback.constant.Role;
import com.catboardback.dto.MemberDto;
import com.catboardback.entity.Member;
import com.catboardback.repository.MemberRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public String saveMember(@Valid MemberDto memberDto) {
        Member member = Member.builder()
                .email(memberDto.getEmail())
                .password(memberDto.getPassword())
                .nickName(memberDto.getNickName())
                .role(Role.USER)
                .build();
        validateDuplicateMember(member);
        Member savedMember = memberRepository.save(member);
        return member.getEmail();
    }

    private void validateDuplicateMember(Member member){
        Optional<Member> foundMember = memberRepository.findByEmail(member.getEmail());
        if(foundMember.isPresent()){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }
}
