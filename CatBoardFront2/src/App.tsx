import { CssBaseline, Container } from "@mui/material";
import AppRoutes from "./AppRoutes";
import Header from "./Layouts/Header";

function App() {
  return (
    <>
      <CssBaseline />
      <Header />
      {/* 본문 */}
      <Container maxWidth="xl">
        <AppRoutes />
      </Container>

      {/* <Box display="flex" flexDirection="column" minHeight="100vh"></Box> */}
    </>
  );
}

export default App;
