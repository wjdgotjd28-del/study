import {
  DataGrid,
  type GridCellParams,
  type GridColDef,
} from "@mui/x-data-grid";
import type { BoardList } from "../ts/type";
import { useEffect, useState } from "react";
import AddBoard from "../components/AddBoard";
import { getBoardList } from "../api/boardApi";
import { useNavigate, useParams } from "react-router-dom";
import { formatDateTime } from "../ts/dateFormat";
import { categoryOptions } from "../ts/category";

export default function BoardList() {
  const [data, setData] = useState<BoardList[]>([]);
  const { category } = useParams<{ category: string }>();
  const navigate = useNavigate();

  const columns: GridColDef[] = [
    { field: "id", headerName: "글번호", width: 150 },
    {
      field: "title",
      headerName: "제목",
      flex: 1,
      renderCell: (params: GridCellParams) => (
        <div
          style={{
            width: "100%",
            height: "100%",
            display: "flex",
            alignItems: "center",
            cursor: "pointer",
            paddingLeft: "8px",
          }}
          onClick={(e) => {
            e.stopPropagation();
            navigate(`/board/${params.row.id}`);
          }}
        >
          {params.value as string}
        </div>
      ),
    },
    { field: "nickname", headerName: "작성자", width: 150 },

    {
      field: "regTime",
      headerName: "작성일",
      width: 150,
      renderCell: (params: GridCellParams) => (
        <div>{formatDateTime(params.value as string)}</div>
      ),
    },
  ];

  const loadBoardData = async () => {
    if (!category) return;
    try {
      const boards = await getBoardList(category);
      setData(boards);
      console.log(boards);
    } catch (error) {
      console.error("게시글 불러오기 실패:", error);
    }
  };

  useEffect(() => {
    console.log("category:", category);
    loadBoardData();
  }, [category]);

  // 한글 라벨 찾기
  const categoryLabel =
    categoryOptions.find((opt) => opt.value === category)?.label || category;

  return (
    <>
      <h2>{categoryLabel}</h2>
      <DataGrid
        rows={data}
        columns={columns}
        getRowId={(row) => row.id}
        disableRowSelectionOnClick={true}
        showToolbar
        initialState={{
          sorting: {
            sortModel: [{ field: "id", sort: "desc" }],
          },
        }}
      />
      <AddBoard loadBoardData={loadBoardData} category={category || ""} />
    </>
  );
}
