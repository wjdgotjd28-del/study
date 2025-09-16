package com.catboardback.repository;

import com.catboardback.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 이메일로 회원(Member) 조회
    // → SQL: SELECT * FROM member WHERE email = ?;
    // 반환 타입이 Optional<Member>인 이유:
    //   - 조회 결과가 없을 수도 있기 때문에 null 대신 Optional로 감싸서 반환
    Optional<Member> findByEmail(String email);
}


//만약 DB에 해당 이메일이 없으면, null을 반환해야 함.
//그런데 null을 직접 다루면 NullPointerException 위험이 큼.
//Optional을 사용하면, isPresent(), orElse(), orElseThrow() 같은 메서드로 안전하게 처리 가능.