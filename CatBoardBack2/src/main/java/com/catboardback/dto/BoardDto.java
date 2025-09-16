package com.catboardback.dto;

import com.catboardback.constant.Category;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDto {
    private Long id;               // 게시글 고유 ID
    private Category category;     // 게시글 카테고리(enum)
    private String title;          // 게시글 제목
    private String content;        // 게시글 내용
    private LocalDateTime regTime; // 게시글 등록 또는 수정 시간
    private String nickname;       // 글 작성자 닉네임
}