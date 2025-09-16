// import { Box } from "@mui/material";

// export default function MainPage() {
//   return (
//     <Box display="flex" justifyContent="center" height={300}>
//       <img
//         src="../public/냥냥냥.png"
//         alt="배너"
//         style={{ maxWidth: "100%", height: 400, width: 1000 }}
//       />
//     </Box>
//   );
// }

import { Box, Grid } from "@mui/material";
import { useNavigate } from "react-router-dom";
import { categoryOptions } from "../ts/category"; // 경로 맞게 수정

export default function MainPage() {
  const navigate = useNavigate();

  // 이미지 매핑 (public 폴더에 넣기)
  const imageMap: Record<string, string> = {
    CAT_SHOWCASE: "자랑.webp",
    CAT_HUMOR: "/유머.jpg",
    INFORMATION: "/정보.avif",
    FREE_BOARD: "/자유.jpg",
  };

  return (
    <>
      <Box display="flex" justifyContent="center" height={300}>
        <img
          src="/냥냥냥.png"
          alt="배너"
          style={{ maxWidth: "100%", height: 400, width: 1000 }}
        />
      </Box>

      <Box
        display="flex"
        justifyContent="center"
        alignItems="center"
        minHeight="50vh"
      >
        <Grid container spacing={4} justifyContent="center">
          {categoryOptions.map((cat) => (
            <Grid key={cat.value}>
              <Box
                onClick={() => navigate(`/board/category/${cat.value}`)}
                sx={{
                  cursor: "pointer",
                  textAlign: "center",
                  "&:hover": { opacity: 0.8 },
                }}
              >
                <img
                  src={imageMap[cat.value]}
                  alt={cat.label}
                  style={{ width: 300, height: 300, objectFit: "cover" }}
                />
                <div style={{ marginTop: "8px", fontWeight: "bold" }}>
                  {cat.label}
                </div>
              </Box>
            </Grid>
          ))}
        </Grid>
      </Box>
    </>
  );
}
