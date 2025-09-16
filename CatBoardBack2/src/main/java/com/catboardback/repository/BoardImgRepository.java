package com.catboardback.repository;

import com.catboardback.entity.BoardImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardImgRepository extends JpaRepository<BoardImg, Long> {
//JpaRepository<BoardImg, Long> 상속 → 기본적인 CRUD (save, findById, delete, findAll 등) 자동 제공
    // 특정 게시글(boardId)에 속한 이미지들을 id 오름차순으로 조회
    // → SQL: SELECT * FROM board_img WHERE board_id = ? ORDER BY id ASC;
    List<BoardImg> findByBoardIdOrderByIdAsc(Long boardId);
}


//BoardImgRepository는 "게시글(Board)에 연결된 이미지(BoardImg) 데이터"를 DB와 연결해주는 통로예요.
//즉, BoardImg 테이블에 대한 조회/저장/삭제/수정을 전담하는 역할을 합니다.