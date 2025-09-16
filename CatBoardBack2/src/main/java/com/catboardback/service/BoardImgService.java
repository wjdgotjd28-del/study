package com.catboardback.service;

import com.catboardback.entity.BoardImg;
import com.catboardback.repository.BoardImgRepository;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardImgService {

    // 이미지가 저장될 경로 (application.properties 또는 yml에서 주입)
    @Value("${boardImgLocation}")
    private String boardImgLocation;

    private final BoardImgRepository boardImgRepository;
    private final FileService fileService;

    /**
     * 새 이미지를 저장하는 메서드
     * 1. 업로드된 파일(MultipartFile)에서 원본 파일명 추출
     * 2. FileService를 이eexe용해 실제 파일을 서버(파일시스템)에 저장
     * 3. 저장된 파일명과 접근 가능한 URL을 생성
     * 4. BoardImg 엔티티에 이 정보들을 업데이트
     * 5. BoardImg 엔티티를 DB에 저장
     */
    public void saveBoardImg(BoardImg boardImg, MultipartFile boardImgFile) throws Exception {
        String oriImgName = boardImgFile.getOriginalFilename(); // 사용자가 업로드한 원본 파일명
        String imgName = "";  // 서버에 저장될 새로운 파일명 (예: UUID.png)
        String imgUrl = "";   // 웹에서 접근 가능한 URL (예: /images/board/UUID.png)

        // 업로드된 파일이 존재할 경우에만 처리
        if (!StringUtils.isEmpty(oriImgName)) {
            // 실제 파일을 서버에 업로드하고 저장된 파일명을 반환받음
            imgName = fileService.uploadFile(boardImgLocation, oriImgName, boardImgFile.getBytes());
            // 저장된 파일을 웹에서 접근할 수 있는 URL 생성
            imgUrl = "/images/board/" + imgName;
        }

        // BoardImg 엔티티에 파일 관련 정보 세팅
        boardImg.updateItemImg(oriImgName, imgName, imgUrl);
        // DB에 엔티티 저장 (insert)
        boardImgRepository.save(boardImg);
    }

    /**
     * 기존 이미지를 수정하는 메서드
     * 1. DB에서 기존 이미지(BoardImg 엔티티)를 조회
     * 2. 기존 이미지 파일이 있으면 파일시스템에서 삭제
     * 3. 새 파일을 업로드하고, 저장된 파일명 및 URL을 생성
     * 4. 조회한 엔티티의 필드를 변경 (Dirty Checking으로 DB 업데이트)
     */
    public void updateBoardImg(Long boardImgId, MultipartFile boardImgFile) throws Exception {
        // 새 파일이 존재할 때만 실행
        if (!boardImgFile.isEmpty()) {
            // DB에서 기존 이미지 정보 조회 (없으면 예외 발생)
            BoardImg savedBoardImg = boardImgRepository.findById(boardImgId)
                    .orElseThrow(EntityExistsException::new);

            // 기존 파일명이 있다면, 서버에서 파일 삭제
            if (!StringUtils.isEmpty(savedBoardImg.getImgName())) {
                fileService.deleteFile(boardImgLocation + "/" + savedBoardImg.getImgName());
            }

            // 새로운 파일 업로드
            String oriImgName = boardImgFile.getOriginalFilename();
            String boardName = fileService.uploadFile(boardImgLocation, oriImgName, boardImgFile.getBytes());
            String boardUrl = "/images/board/" + boardName;

            // 엔티티에 새 파일 정보 업데이트 (Dirty Checking → DB 반영)
            savedBoardImg.updateItemImg(oriImgName, boardName, boardUrl);
        }
    }
}
