import { createRoot } from "react-dom/client";
import App from "./App.tsx";
import { BrowserRouter } from "react-router-dom";
import { ThemeProvider } from "@mui/material/styles";
import CssBaseline from "@mui/material/CssBaseline";
import theme from "./ts/theme.ts";

createRoot(document.getElementById("root")!).render(
  <ThemeProvider theme={theme}>
    <CssBaseline /> {/* MUI 기본 스타일 초기화 */}
    <BrowserRouter>
      <App />
    </BrowserRouter>
  </ThemeProvider>
);
