package com.catboardback.dto;

import com.catboardback.entity.BoardImg;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardImgDto {

    private Long id;          // 이미지 고유 ID (DB PK)
    private String imgName;   // 저장된 이미지 파일명 (서버 저장용)
    private String oriImgName;// 원본 이미지 파일명 (사용자가 업로드한 실제 파일명)
    private String imgUrl;    // 이미지 접근용 URL
    private String repimgYn;  // 대표 이미지 여부 ("Y"/"N")

    // -------------------------------
    // Entity -> DTO 변환
    //게시글 이미지 업로드, 조회, 전송 등에서 사용되는 DTO
    public static BoardImgDto of(BoardImg boardImg) {
        // BoardImg 엔티티 객체를 DTO로 변환
        // Builder 패턴 사용으로 가독성 좋게 DTO 생성
        return BoardImgDto.builder()
                .id(boardImg.getId())                // DB PK 값 복사
                .imgName(boardImg.getImgName())      // 서버에 저장된 파일명 복사
                .oriImgName(boardImg.getOriImgName())// 업로드한 원본 파일명 복사
                .imgUrl(boardImg.getImgUrl())        // 접근 가능한 URL 복사
                .repimgYn(boardImg.getRepImgYn())    // 대표 이미지 여부 복사
                .build();                             // DTO 객체 생성
    }
}