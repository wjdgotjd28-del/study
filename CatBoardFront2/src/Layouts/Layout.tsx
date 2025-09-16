import { Container, CssBaseline } from "@mui/material";
import Header from "./Header";
import { Outlet } from "react-router-dom";

export default function Layout() {
  return (
    <Container maxWidth="xl">
      <CssBaseline />
      <Header />
      <Outlet /> {/* 여기에 각 페이지가 렌더링됨 */}
    </Container>
  );
}
