import { Button, Stack, TextField } from "@mui/material";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

import type { Member } from "../ts/type";

import { signUp } from "../api/memberApi";

export default function SignupPage() {
  const navigate = useNavigate();
  const [member, setMember] = useState<Member>({
    email: "",
    password: "",
    nickname: "",
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setMember({ ...member, [e.target.name]: e.target.value });
  };

  const handleSignUp = () => {
    if (!member.email.includes("@")) {
      alert("올바른 이메일 주소를 입력해주세요.");
      return;
    }
    signUp(member)
      .then(() => {
        navigate("/login");
      })
      .catch((err) => {
        alert(err.response?.data);
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
        <TextField
          label="닉네임"
          name="nickName"
          onChange={handleChange}
          required
        />
        <Button color="primary" onClick={handleSignUp}>
          회원가입
        </Button>
      </Stack>
    </>
  );
}
