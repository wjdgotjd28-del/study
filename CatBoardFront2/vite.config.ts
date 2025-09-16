import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  // server: {
  //   host: "172.20.20.33",
  //   proxy: {
  //     "/api": {
  //       // 프론트에서 "/api"로 시작하는 요청이 들어오면
  //       target: "http://172.20.20.33:8080", // Spring Boot 서버로 전달
  //       // 경로 재작성 : 프론트에서 /api/...라고 요청하지만, 백엔드에서는 /api가 필요 없을 경우.
  //       rewrite: (path) => path.replace(/^\/api/, ""),
  //       changeOrigin: true,
  //     },
  //   },
  // },
  server: {
    host: "172.20.20.33",
    port: 80,
    strictPort: true,
    proxy: {
      "/api": {
        target: "http://172.20.20.33:8080",
        rewrite: (path) => path.replace(/^\/api/, ""),
        changeOrigin: true,
      },
    },
  },
});
