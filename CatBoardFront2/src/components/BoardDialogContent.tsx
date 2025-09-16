import {
  FormControl,
  InputLabel,
  MenuItem,
  Select,
  Stack,
  TextField,
} from "@mui/material";
import type { Board } from "../ts/type";
import { categoryOptions } from "../ts/category";

type BoardDialogContentProps = {
  board: Board;
  handleChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  setBoard: React.Dispatch<React.SetStateAction<Board>>;
};

export default function BoardDialogContent({
  board,
  handleChange,
  setBoard,
}: BoardDialogContentProps) {
  return (
    <Stack spacing={2} mt={1}>
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
      {/* 새 이미지 업로드 */}
      <input
        type="file"
        accept="image/*"
        multiple
        onChange={(e) => {
          if (e.target.files) {
            const files = Array.from(e.target.files).slice(0, 5);
            setBoard({ ...board, imgFiles: files, boardImgDtoList: [] });
          }
        }}
      />

      {/* 기존 이미지 미리보기 */}
      {board.boardImgDtoList &&
        board.boardImgDtoList?.map((img) => (
          <img
            key={img.id}
            src={`/api${img.imgUrl}`}
            alt={img.imgName}
            style={{ maxWidth: "50%", marginTop: 10 }}
          />
        ))}

      {/* 새 업로드한 이미지 미리보기 */}
      {board.imgFiles &&
        board.imgFiles.map((file, idx) => (
          <img
            key={idx}
            src={URL.createObjectURL(file)}
            alt={`새 이미지 ${idx + 1}`}
            style={{ maxWidth: "50%", marginTop: 10 }}
          />
        ))}
    </Stack>
  );
}
