import axios from "axios";
import type { Member } from "../ts/type";
import { BASE_URL } from "./boardApi";

export const signUp = async (member: Member) => {
  const response = await axios.post(`${BASE_URL}/signup`, member);
  return response.data;
};

export const getAuthToken = async (member: Member) => {
  const response = await axios.post(`${BASE_URL}/login`, member);
  return response.headers["authorization"];
};
