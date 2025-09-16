export interface Board {
  id?: number;
  title: string;
  content: string;
  category: string; // Category enum 대응 (ex: "CAT_SHOWCASE")
  nickname?: string;
  regTime?: string;
  imgUrl?: string[]; // 서버 반환 이미지 URL
  imgFiles?: File[]; // 프론트에서만 쓰는 필드
  boardImgDtoList?: BoardImgDto[];
}

export type BoardList = {
  id?: number;
  title: string;
  regTime: string;
  nickname: string;
};

export type Member = {
  email: string;
  password: string;
  nickname?: string;
};

// ts/type.ts
export interface BoardImgDto {
  id: number;
  imgName: string;
  oriImgName: string;
  imgUrl: string;
  repimgYn: string;
}

export interface BoardFormDto {
  id: number;
  title: string;
  category: string;
  nickname: string;
  regTime: string;
  content: string;
  boardImgDtoList: BoardImgDto[];
  boardImgIds?: number[];
}
