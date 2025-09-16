package com.catboardback.service;

import com.catboardback.entity.Member;
import com.catboardback.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 실제 해당 username(ID)을 가지는 유저가 DB에 존재하는지 확인
        // + 해당 유저정보를 UserDetails 타입으로 반환하는 메서드
        Optional<Member> member = memberRepository.findByEmail(email);
        UserDetails userDetails = null;
        if(member.isPresent()){
            Member newMember = member.get();
            userDetails = User.withUsername(email)
                    .password(newMember.getPassword())
                    .roles(String.valueOf(newMember.getRole()))
                    .build();
        }else{
            throw new UsernameNotFoundException("User not found");
        }
        return userDetails;
    }
}
