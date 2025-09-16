import { Button, Snackbar, Stack, TextField, Alert } from "@mui/material";
import { useNavigate } from "react-router-dom";
import type { Member } from "../ts/type";
import { useState } from "react";
import { useAuthStore } from "../ts/store";
import { getAuthToken } from "../api/memberApi";

export default function LoginPage() {
  const navigate = useNavigate();
  const [member, setMember] = useState<Member>({ email: "", password: "" });
  const [toast, setToast] = useState<{
    open: boolean;
    msg: string;
    severity: "success" | "error";
  }>({
    open: false,
    msg: "",
    severity: "success",
  });

  const { login } = useAuthStore();

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setMember({ ...member, [e.target.name]: e.target.value });
  };

  const handleLogin = () => {
    if (!member.email.includes("@")) {
      setToast({
        open: true,
        msg: "올바른 이메일 주소를 입력해주세요.",
        severity: "error",
      });
      return;
    }

    getAuthToken(member)
      .then((token) => {
        if (token != null) {
          sessionStorage.setItem("jwt", token);
          login();
          navigate("/");
        }
      })
      .catch(() => {
        setToast({
          open: true,
          msg: "이메일 또는 비밀번호가 잘못 되었습니다. 아이디와 비밀번호를 정확히 입력해 주세요.",

          severity: "error",
        });
      });
  };

  return (
    <>
      <Stack spacing={2} mt={2} alignItems="center">
        <TextField
          label="이메일"
          name="email"
          onChange={handleChange}
          type="email"
          required
        />
        <TextField
          label="비밀번호"
          name="password"
          type="password"
          onChange={handleChange}
          required
        />
        <Button color="primary" onClick={handleLogin}>
          로그인
        </Button>
        <Button color="primary" onClick={() => navigate("/signUp")}>
          회원가입
        </Button>
      </Stack>

      <Snackbar
        open={toast.open}
        autoHideDuration={2000}
        onClose={() => setToast({ ...toast, open: false })}
        anchorOrigin={{ vertical: "top", horizontal: "center" }} // 화면 상단 중앙
      >
        <Alert
          severity={toast.severity}
          variant="filled"
          sx={{ width: "100%" }}
        >
          {toast.msg}
        </Alert>
      </Snackbar>
    </>
  );
}
