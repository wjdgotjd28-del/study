package com.catboardback.entity;

import com.catboardback.constant.Category;
import com.catboardback.dto.BoardFormDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity // JPA 엔티티임을 선언 (DB 테이블과 매핑)
@Table(name = "board") // 매핑할 DB 테이블 이름을 지정
@NoArgsConstructor // 파라미터 없는 기본 생성자 생성 (JPA 필수)
@AllArgsConstructor // 모든 필드를 파라미터로 받는 생성자 생성
@Getter // 모든 필드에 대한 getter 메서드 자동 생성
@Builder // Builder 패턴을 적용할 수 있게 해줌
public class Board
{
    @Id // PK(primary key) 필드 지정
    @Column(name="board_id") // 매핑될 컬럼명 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 기본 키 생성을 DB에 위임 (AUTO_INCREMENT)
    private Long id;

    @Enumerated(EnumType.STRING)
    // Enum값을 DB로 저장하려면 이렇게 어노테이션을 붙여 줘야한다
    // 이렇게 쓰면 Enum 값을 문자열(String)로 DB에 저장
    private Category category;

    private String title;   // 게시글 제목

    private String content; // 게시글 내용

    private LocalDateTime regTime; // 게시글 등록/수정 시간

    @ManyToOne(fetch = FetchType.LAZY)
    // 회원 1명 : 게시글 N개 → 다대일(N:1) 관계
    @JoinColumn(name = "member_id")
    // FK 컬럼명: member_id
    private Member member;

    // 게시글 수정 시 사용되는 메서드
    public void updateBoard(BoardFormDto boardFormDto) {
    this.category = boardFormDto.getCategory(); // 카테고리 수정
    this.title = boardFormDto.getTitle();       // 제목 수정
    this.content = boardFormDto.getContent();   // 내용 수정
    this.regTime = LocalDateTime.now();         // 수정 시간 갱신
}
}