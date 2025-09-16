import { useState } from "react";
import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
} from "@mui/material";

import type { Board } from "../ts/type";
import { updateBoard } from "../api/boardApi";
import BoardDialogContent from "./BoardDialogContent";

type EditBoardProps = {
  boardData: Board;
  loadBoardData: () => void;
};

export default function EditBoard({
  boardData,
  loadBoardData,
}: EditBoardProps) {
  const [open, setOpen] = useState(false);
  const [board, setBoard] = useState<Board>({
    id: 0,
    title: "",
    content: "",
    regTime: "",
    category: "",
    imgUrl: [],
    imgFiles: [],
    boardImgDtoList: [],
  });

  const handleOpen = () => {
    setBoard({
      id: boardData.id ?? 0,
      title: boardData.title ?? "",
      content: boardData.content ?? "",
      regTime: boardData.regTime ?? "",
      category: boardData.category ?? "",
      imgUrl: boardData.imgUrl ?? [],
      imgFiles: [],
      boardImgDtoList: boardData.boardImgDtoList ?? [], // 기존 이미지 객체 그대로 복사
    });
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  const handleSave = async () => {
    try {
      // 새 파일이 있든 없든 FormData 기반 updateBoard 호출
      await updateBoard(board, board.imgFiles ?? []);

      loadBoardData(); // 저장 후 상세 데이터 새로 로드

      // 상태 초기화
      setBoard({
        id: 0,
        title: "",
        content: "",
        regTime: "",
        category: "",
        imgUrl: [],
        imgFiles: [],
        boardImgDtoList: [],
      });

      handleClose();
    } catch (err) {
      console.error(err);
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    const name = e.target.name;
    setBoard({ ...board, [name]: value });
  };

  return (
    <>
      <Button onClick={handleOpen}>수정</Button>
      <Dialog open={open} onClose={handleClose} maxWidth="sm" fullWidth>
        <DialogTitle>글 수정</DialogTitle>
        <DialogContent>
          <BoardDialogContent
            board={board}
            handleChange={handleChange}
            setBoard={setBoard}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleSave}>저장</Button>
          <Button onClick={handleClose}>닫기</Button>
        </DialogActions>
      </Dialog>
    </>
  );
}
