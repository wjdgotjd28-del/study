import { Navigate, Route, Routes } from "react-router-dom";
import MainPage from "./pages/MainPage";
import LoginPage from "./pages/LoginPage";
import SignupPage from "./pages/SignupPage";
import AdminPage from "./pages/AdminPage";
import BoardDetailPage from "./pages/BoardDetailPage";
import BoardList from "./pages/BoardList";
import type { JSX } from "@emotion/react/jsx-runtime";
import { useAuthStore } from "./ts/store";
type PrivateRouteProps = {
  children: JSX.Element;
};
function PrivateRoute({ children }: PrivateRouteProps) {
  const { isAuthenticated } = useAuthStore();
  return isAuthenticated ? children : <Navigate to="/login" replace />;
}
export default function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<MainPage />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/signup" element={<SignupPage />} />
      <Route path="/admin" element={<AdminPage />} />
      <Route
        path="/board/category/:category"
        element={
          <PrivateRoute>
            <BoardList />
          </PrivateRoute>
        }
      />
      <Route path="/board/:id" element={<BoardDetailPage />} />
    </Routes>
  );
}
