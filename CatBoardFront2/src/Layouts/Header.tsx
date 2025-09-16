import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";
import { useNavigate } from "react-router-dom";
import { useAuthStore } from "../ts/store";

export default function Header() {
  const navigate = useNavigate();
  const handleLoginClick = () => {
    navigate("/login"); // /login 페이지로 이동
  };
  const { logout } = useAuthStore();
  const handleLogoutClick = () => {
    sessionStorage.setItem("jwt", "");
    logout();
  };
  const handleCategory = (categoryId: string) => {
    navigate(`/board/category/${categoryId}`);
  };
  const { isAuthenticated } = useAuthStore();
  return (
    <Box sx={{ flexGrow: 1 }}>
      <AppBar position="static" color="primary">
        <Toolbar>
          <Box
            component="img"
            src="/냥냥.jpg" // 이미지 경로
            sx={{ width: 50, height: 50, mr: 1, cursor: "pointer" }}
            onClick={() => navigate("/")}
          />
          <Typography
            variant="h6"
            component="div"
            sx={{ cursor: "pointer", display: "inline-block" }} // inline-block으로 텍스트 크기만
            onClick={() => navigate("/")}
          >
            냥냥
          </Typography>
          <Box sx={{ marginLeft: "auto", display: "flex", gap: 1 }}>
            <Button
              color="inherit"
              onClick={() => handleCategory("CAT_SHOWCASE")}
            >
              고양이 자랑
            </Button>
            <Button color="inherit" onClick={() => handleCategory("CAT_HUMOR")}>
              고양이 유머
            </Button>
            <Button
              color="inherit"
              onClick={() => handleCategory("INFORMATION")}
            >
              정보 게시판
            </Button>
            <Button
              color="inherit"
              onClick={() => handleCategory("FREE_BOARD")}
            >
              자유 게시판
            </Button>
            {!isAuthenticated ? (
              <Button color="inherit" onClick={handleLoginClick}>
                Login
              </Button>
            ) : (
              <Button color="inherit" onClick={handleLogoutClick}>
                LogOut
              </Button>
            )}
          </Box>
        </Toolbar>
      </AppBar>
    </Box>
  );
}
