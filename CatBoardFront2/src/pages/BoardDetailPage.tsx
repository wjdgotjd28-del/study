import { useNavigate, useParams } from "react-router-dom";
import {
  Box,
  Typography,
  Paper,
  Stack,
  Divider,
  Avatar,
  Button,
} from "@mui/material";
import { useEffect, useState } from "react";
import { deleteBoard, getBoardDtl } from "../api/boardApi";
import type { Board } from "../ts/type";
import EditBoard from "../components/EditBoard";
import { formatDateTime } from "../ts/dateFormat";
// export const IMG_URL = "http://localhost:5173";

export default function BoardDetailPage() {
  const { id } = useParams<{ id: string }>();
  const boardId = Number(id);
  const navigate = useNavigate();

  const [data, setData] = useState<Board>({
    title: "",
    category: "",
    nickname: "",
    regTime: "",
    content: "",
    boardImgDtoList: [],
  });

  const loadBoardData = () => {
    // API로 게시글 상세 조회
    getBoardDtl(boardId).then((res) => {
      setData(res); // 응답 데이터 구조에 맞게 조정 필요
    });
  };
  useEffect(() => {
    loadBoardData();
  }, [boardId]);

  const deleteBoardData = (id: number) => {
    if (confirm(`${id}번 데이터를 삭제하시겠습니까?`)) {
      deleteBoard(id)
        .then(() => {
          navigate(`/board/category/${data.category}`);
        })
        .catch((err) => console.log(err));
    }
  };

  return (
    <Box maxWidth="800px" mx="auto" mt={4}>
      <Stack spacing={2}>
        {/* 카테고리 */}
        <Typography variant="body2" color="green">
          <a
            href={`/board/category/${data.category}`}
            style={{ textDecoration: "none" }}
          >
            {data.category || "Q&A 게시판"} &gt;
          </a>
        </Typography>

        {/* 제목 */}
        <Typography variant="h5" fontWeight="bold">
          {data.title}
        </Typography>

        <Stack
          direction="row"
          spacing={1}
          alignItems="center"
          justifyContent="space-between"
        >
          {/* 왼쪽: 프로필 + 작성자 */}
          <Stack direction="row" spacing={1} alignItems="center">
            <Avatar
              alt={data.nickname}
              src="/profile.png"
              sx={{ width: 33, height: 33 }}
            />
            <Typography variant="body2">{data.nickname}</Typography>
          </Stack>

          {/* 오른쪽 끝: 날짜 + 조회수 */}
          <Stack direction="row" spacing={1} alignItems="center">
            <Typography variant="body2">
              {formatDateTime(data.regTime)}
            </Typography>
          </Stack>
        </Stack>

        <Divider />

        <Paper variant="outlined" sx={{ p: 2, whiteSpace: "pre-line" }}>
          <Typography variant="body1" sx={{ mb: 2 }}>
            {data.content}
          </Typography>

          {/* 이미지 출력 */}
          <Stack spacing={1}>
            {data.boardImgDtoList?.map((img) => (
              <Box
                key={img.id}
                component="img"
                src={`/api${img.imgUrl}`}
                alt={img.imgName}
                sx={{
                  width: "100%",
                  maxHeight: 400,
                  objectFit: "contain",
                  borderRadius: 1,
                }}
              />
            ))}
          </Stack>
        </Paper>
      </Stack>

      {/* 수정 삭제 */}

      <Box display="flex" justifyContent="center" mt={2}>
        <EditBoard boardData={data} loadBoardData={loadBoardData}></EditBoard>
        <Button onClick={() => deleteBoardData(boardId)}>삭제</Button>
      </Box>
    </Box>
  );
}
