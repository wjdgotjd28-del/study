package com.catboardback.service;

import com.catboardback.constant.Category;
import com.catboardback.dto.BoardDto;
import com.catboardback.dto.BoardFormDto;
import com.catboardback.dto.BoardImgDto;
import com.catboardback.entity.Board;
import com.catboardback.entity.BoardImg;
import com.catboardback.entity.Member;
import com.catboardback.repository.BoardImgRepository;
import com.catboardback.repository.BoardRepository;
import com.catboardback.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private static ModelMapper modelMapper = new ModelMapper();
    private final BoardRepository boardRepository;
    private final BoardImgService boardImgService;
    private final MemberRepository memberRepository;
    private final BoardImgRepository boardImgRepository;


    public BoardDto addBoard(BoardDto boardDto) {
        Board board = Board.builder()
                .category(boardDto.getCategory())
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .build();
        Board savedBoard = boardRepository.save(board);

        boardDto.setId(savedBoard.getId());
        return boardDto;
    }
/// 멤버 리포지토리 만들고 이메ㅇㄹ로 멤버 찾아서 toEntity에 멤버 넘겨서 board 엔티티에 member를 채우기.
    public Long saveBoard(@Valid BoardFormDto boardFormDto, List<MultipartFile> boardImgFileList
            , String email) throws Exception {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));

        Board board = boardFormDto.toEntity(member); // Dto -> Entity

        boardRepository.save(board);
        if (boardImgFileList != null) {
            for (int i = 0; i < boardImgFileList.size(); i++) {
                BoardImg boardImg = new BoardImg();
                boardImg.setBoard(board);
                if (i == 0) { //첫번째 이미지일 경우 대표 이미지로.
                    boardImg.setRepImgYn("Y");
                } else {
                    boardImg.setRepImgYn("N");
                }
                boardImgService.saveBoardImg(boardImg, boardImgFileList.get(i));
            }
            //저장된 item ID를 반환
        }
        return board.getId();
        // 2. 이미지 여러 개 저장 (반복문 돌면서 처리)

    }

    public List<BoardDto> getBoardList(Category category) {
        List<Board> boardList = boardRepository.findByCategory(category);

        List<BoardDto> boardDtoList = new ArrayList<>();
        for (Board board : boardList) {
            BoardDto boardDto = BoardDto.builder()
                    .id(board.getId())
                    .category(board.getCategory())
                    .title(board.getTitle())
                    .regTime(board.getRegTime())
                    .nickname(board.getMember().getNickName())
                    .build();
            boardDtoList.add(boardDto);
        }
        return boardDtoList;
    }

    ///  img랑 같이 보내야함.
    @Transactional(readOnly = true)
    public BoardFormDto getBoardDtl(Long boardId) {
        /// board_id인 사진 목록 ID 오름차순으로 조회하기
        List<BoardImg> boardImgList = boardImgRepository.findByBoardIdOrderByIdAsc(boardId);
        List<BoardImgDto > boardImgDtoList = new ArrayList<>();

        // Entity -> DTO
        for(BoardImg boardImg : boardImgList ){
            boardImgDtoList.add(BoardImgDto.of(boardImg));
        }

        // board갖고오기
        Board board = boardRepository.findById(boardId).orElseThrow(EntityNotFoundException::new);

        //formdto로 변환
        BoardFormDto boardFormDto = BoardFormDto.of(board);
        boardFormDto.setBoardImgDtoList(boardImgDtoList);
        return boardFormDto;
    }

//    public Long updateBoard(BoardFormDto boardFormDto, List<MultipartFile> boardImgFileList) throws Exception {
//        // 기존데이터 갖고오기
//        Board board = boardRepository.findById(boardFormDto.getId())
//                .orElseThrow(EntityNotFoundException::new); // 아이디로 db에 저장되어있는거 item에 담기.
//
//        // DTO에 있는 내용으로 다 교체
//        board.updateBoard(boardFormDto);
//        for(MultipartFile boardImgFile : boardImgFileList){
//            System.out.println(boardImgFile);
//        }
//        // 아이템 엔티티 정보 반영 -> 이미지 수정 내역 반영
//        List<Long> boardImgIds = boardFormDto.getBoardImgIds(); // 어떤 엔티티는 1~5, 6~10 일 것.
//
//        // 상품 이미지 업데이트
//        for (int i = 0; i < boardImgFileList.size(); i++) {
//            boardImgService.updateBoardImg(boardImgIds.get(i), boardImgFileList.get(i));
//        }
//
//        return board.getId(); // 수정한 item id 반환.
//    }

    @Value("${boardImgLocation}")
    private String boardImgLocation;
    private final FileService fileService;
    @Transactional
    public Long updateBoard(BoardFormDto boardFormDto, List<MultipartFile> boardImgFileList) throws Exception {
        // 1. 게시글 엔티티 조회
        Board board = boardRepository.findById(boardFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);

        // 2. 게시글 내용 업데이트
        board.updateBoard(boardFormDto);

        // 3. 기존 이미지 삭제
        List<BoardImg> oldImgs = boardImgRepository.findByBoardIdOrderByIdAsc(board.getId());
        for (BoardImg oldImg : oldImgs) {
            // 파일 삭제
            if (oldImg.getImgName() != null) {
                fileService.deleteFile(boardImgLocation + "/" + oldImg.getImgName());
            }
            boardImgRepository.delete(oldImg);
        }

        // 4. 새 이미지 저장
        if (boardImgFileList != null && !boardImgFileList.isEmpty()) {
            for (MultipartFile newFile : boardImgFileList) {
                if (newFile != null && !newFile.isEmpty()) {
                    BoardImg boardImg = new BoardImg();
                    boardImg.setBoard(board);
                    boardImg.setRepImgYn("N"); // 필요시 대표 이미지 로직
                    boardImgService.saveBoardImg(boardImg, newFile);
                }
            }
        }

        return board.getId();
    }


    public Long deleteBoard(Long boardId) throws Exception {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(EntityNotFoundException::new);

        // BoardImg 삭제 (ID 기준)
        List<BoardImg> imgs = boardImgRepository.findByBoardIdOrderByIdAsc(boardId);
        for (BoardImg img : imgs) {
            if (img.getImgName() != null) {
                fileService.deleteFile(boardImgLocation + "/" + img.getImgName());
            }
            boardImgRepository.delete(img);
        }

        // Board 삭제
        boardRepository.deleteById(boardId);

        return boardId;
    }

}