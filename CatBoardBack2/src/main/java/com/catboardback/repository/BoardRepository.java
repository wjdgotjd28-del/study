package com.catboardback.repository;

import com.catboardback.constant.Category;
import com.catboardback.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    // 특정 카테고리에 속한 게시글 목록 조회
    // → SQL: SELECT * FROM board WHERE category = ?;
    List<Board> findByCategory(Category category);
}

//상속받은 JpaRepository<Board, Long> 덕분에 기본 CRUD 메서드 제공:

//save(Board entity) → 게시글 저장

//findById(Long id) → 게시글 단건 조회

//findAll() → 게시글 전체 조회

//deleteById(Long id) → 게시글 삭제