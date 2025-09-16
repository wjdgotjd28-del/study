package com.catboardback.controller;

import com.catboardback.constant.Category;
import com.catboardback.dto.BoardDto;
import com.catboardback.dto.BoardFormDto;
import com.catboardback.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    // 게시글 작성
    @PostMapping("/board/new")
    public ResponseEntity<?> createBoard(
            @Valid @ModelAttribute BoardFormDto boardFormDto,
            BindingResult bindingResult,
            @RequestParam(value = "boardImgFile", required = false) List<MultipartFile> boardImgFileList,
            Authentication authentication) {

        // 유효성 검증
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("필수 입력값이 누락되었습니다.");
        }

        try {
            Long boardId = boardService.saveBoard(boardFormDto, boardImgFileList,authentication.getName());
            return ResponseEntity. ok(boardId); // 게시글아이디 반환
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("게시글 등록 중 에러가 발생했습니다.");
        }
    }

    // 카테고리별 조회
    @GetMapping("/board/category/{category}")
    public List<BoardDto> getBoardList(@PathVariable Category category){
        return boardService.getBoardList(category);
    }

    // 게시글 상세 조회
    @GetMapping("/board/{boardId}")
    public BoardFormDto getBoardDtl(@PathVariable Long boardId) {
        return boardService.getBoardDtl(boardId);
    }

    @PutMapping("/board/{boardId}")
    public ResponseEntity<?> updateBoard(
            @Valid @ModelAttribute BoardFormDto boardFormDto,
            BindingResult bindingResult,
            @RequestParam(value = "boardImgFile", required = false) List<MultipartFile> boardImgFileList,
            Authentication authentication) {

        // 유효성 검증
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("필수 입력값이 누락되었습니다.");
        }

        try {
            Long boardId = boardService.updateBoard(boardFormDto, boardImgFileList);
            return ResponseEntity.ok(boardId); // 게시글아이디 반환
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("게시글 등록 중 에러가 발생했습니다.");
        }
    }
    @DeleteMapping("/board/{boardId}")
    public Long deleteBoard(@PathVariable("boardId") Long boardId) throws Exception {
        return boardService.deleteBoard(boardId);
    }
}