import {
  Box,
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  FormControl,
  InputLabel,
  MenuItem,
  Select,
  TextField,
} from "@mui/material";
import React, { useEffect, useState } from "react";
import type { Board } from "../ts/type";
import { addBoard } from "../api/boardApi";
import { categoryOptions } from "../ts/category";

type AddBoardProps = {
  loadBoardData: () => void;
  category?: string;
};

export default function AddBoard({ loadBoardData, category }: AddBoardProps) {
  const [open, setOpen] = useState(false);
  const [board, setBoard] = useState<Board>({
    id: 0,
    title: "",
    content: "",
    regTime: "",
    category: "",
    imgUrl: [],
    imgFiles: [],
  });

  // 부모에서 받은 category로 초기값 세팅
  useEffect(() => {
    if (category) {
      setBoard((prev) => ({ ...prev, category }));
    }
  }, [category]);

  // input 값 변경 처리
  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    const { name, value } = e.target;
    setBoard({ ...board, [name]: value });
  };

  // 저장 처리
  const handleSave = async () => {
    try {
      if (board.imgFiles) {
        await addBoard(board, board.imgFiles);
      } else {
        await addBoard(board, []);
      }

      loadBoardData(); // 저장 후 목록 새로고침
      setBoard({
        id: 0,
        title: "",
        content: "",
        regTime: "",
        category: "",
        imgUrl: [],
        imgFiles: [],
      });
      handleClose();
    } catch (err) {
      console.error(err);
    }
  };

  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);

  return (
    <>
      <Dialog open={open} onClose={handleClose} fullWidth maxWidth="sm">
        <DialogTitle>게시판 작성</DialogTitle>
        <DialogContent>
          {/* 카테고리 선택 */}
          <FormControl fullWidth margin="dense">
            <InputLabel id="category-label">카테고리</InputLabel>
            <Select
              labelId="category-label"
              name="category"
              value={board.category}
              onChange={(e) => setBoard({ ...board, category: e.target.value })}
            >
              {categoryOptions.map((c) => (
                <MenuItem key={c.value} value={c.value}>
                  {c.label}
                </MenuItem>
              ))}
            </Select>
          </FormControl>

          <TextField
            autoFocus
            margin="dense"
            label="제목"
            name="title"
            value={board.title}
            onChange={handleChange}
            fullWidth
          />
          <TextField
            margin="dense"
            label="내용"
            name="content"
            value={board.content}
            onChange={handleChange}
            multiline
            rows={15}
            fullWidth
          />
          <input
            type="file"
            accept="image/*"
            multiple
            onChange={(e) => {
              if (e.target.files) {
                const files = Array.from(e.target.files).slice(0, 5); // 최대 3개
                setBoard({ ...board, imgFiles: files });
              }
            }}
          />

          {/* 미리보기 */}
          {board.imgFiles &&
            board.imgFiles.map((file, idx) => (
              <img
                key={idx}
                src={URL.createObjectURL(file)}
                alt={`첨부 이미지 ${idx + 1}`}
                style={{ maxWidth: "50%", marginTop: 10 }}
              />
            ))}
        </DialogContent>

        <DialogActions>
          <Button onClick={handleSave} variant="contained">
            저장
          </Button>
          <Button onClick={handleClose}>닫기</Button>
        </DialogActions>
      </Dialog>
      <Box display="flex" justifyContent="center" mt={2}>
        <Button variant="contained" onClick={handleOpen}>
          글쓰기
        </Button>
      </Box>
    </>
  );
}
