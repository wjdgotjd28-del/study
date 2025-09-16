package com.catboardback.dto;

import com.catboardback.constant.Category;
import com.catboardback.entity.Board;
import com.catboardback.entity.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class BoardFormDto {

    private Long id;
    // 게시글 ID, DB에서 조회하거나 수정 시 참조용

    @NotNull(message = "카테고리는 필수 입력 값입니다.")
    private Category category;
    // 게시글 카테고리(enum). null이면 검증 오류 발생

    @NotBlank(message = "제목은 필수 입력 값입니다.")
    private String title;
    // 게시글 제목. 공백/빈 문자열이면 검증 오류 발생

    @NotBlank(message = "내용은 필수 입력 값입니다.")
    private String content;
    // 게시글 내용. 공백/빈 문자열이면 검증 오류 발생

    private LocalDateTime regTime;
    // 게시글 등록/수정 시간. 화면 표시용, 엔티티와 변환 시 사용

    private String nickname;
    // 작성자 닉네임. Member 엔티티에서 꺼내 DTO에 담음

    private String email;
    // 작성자 이메일(Optional, 필요시 사용)

    private List<BoardImgDto> boardImgDtoList = new ArrayList<>();
    // 게시글 이미지 리스트(파일 업로드 시 DTO 사용)

    private List<Long> boardImgIds = new ArrayList<>();
    // 기존 이미지 ID 리스트(수정 시 삭제/관리 용)

    // -------------------------------
    // Entity -> DTO 변환
    // Board 엔티티 → BoardFormDto DTO 변환
    // 화면(프론트엔드)에서 게시글 수정/조회용 데이터를 전달할 때 사용
    // 기존 게시물 조회, 수정 화면 초기값용
    public static BoardFormDto fromEntity(Board board) {
        // 엔티티에서 필요한 데이터만 추출해 DTO 생성
        return BoardFormDto.builder()
                .id(board.getId())
                .category(board.getCategory())
                .title(board.getTitle())
                .content(board.getContent())
                .regTime(board.getRegTime())
                .build();
    }

    // -------------------------------
    // DTO -> Entity 변환
    // DTO는 단순히 데이터 전달용 → DB 저장 불가
    // toEntity()는 DTO를 **DB 저장 가능 객체(Board)**로 변환
    public Board toEntity(Member member) {
        // DTO 데이터를 기반으로 새 Board 엔티티 생성
        // member 객체를 넣어 작성자 정보 설정
        return Board.builder()
                .category(this.category)
                .title(this.title)
                .content(this.content)
                .regTime(LocalDateTime.now()) // 등록/수정 시각 현재 시각으로 설정
                .member(member)
                .build();
    }

    // -------------------------------
    private static ModelMapper modelMapper = new ModelMapper();
    // ModelMapper 객체 생성: 엔티티 ↔ DTO 자동 매핑 도구

    public static BoardFormDto of(Board board){
        // ModelMapper로 엔티티 -> DTO 변환 후, 닉네임 수동 설정
        BoardFormDto dto = modelMapper.map(board, BoardFormDto.class);
        dto.setNickname(board.getMember() != null ? board.getMember().getNickName() : null);
        return dto;
    }
}