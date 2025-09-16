import { createTheme } from "@mui/material/styles";

const theme = createTheme({
  palette: {
    primary: {
      main: "#8d6e63", // 카푸치노 브라운
      contrastText: "#ffffff", // 버튼/글자색 흰색
    },
    secondary: {
      main: "#4e342e", // 진한 브라운 (보조 색상)
    },
    background: {
      default: "#f5f5f5", // 페이지 기본 배경 (밝은 회색)
    },
  },
  typography: {
    fontFamily: `"Noto Sans KR", "Roboto", "Helvetica", "Arial", sans-serif`,
  },
});

export default theme;
