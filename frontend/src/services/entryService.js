import axios from "axios";

const API_URL = "/api/entries";

const authHeader = () => {
  const user = JSON.parse(localStorage.getItem("user"));
  if (user && user.token) {
    return { Authorization: "Bearer " + user.token };
  } else {
    return {};
  }
};

const createEntry = (title, content) => {
  const entryData = {
    title,
    pages: [
      {
        pageNumber: 1,
        content: content,
      },
    ],
  };
  return axios.post(API_URL, entryData, { headers: authHeader() });
};

const getEntry = async (id) => {
  const user = JSON.parse(localStorage.getItem("user"));
  const response = await axios.get(`/api/entries/${id}`, {
    headers: { Authorization: `Bearer ${user.token}` },
  });
  return response.data;
};

const updateEntry = async (id, title, content) => {
  const entryData = {
    title,
    pages: [
      {
        pageNumber: 1,
        content: content,
      },
    ],
  };
  return axios.put(`${API_URL}/${id}`, entryData, { headers: authHeader() });
};

const getAllEntries = async (page = 0, size = 20) => {
  const response = await axios.get(
    `${API_URL}/list?page=${page}&size=${size}`,
    {
      headers: authHeader(),
    }
  );
  return response.data;
};

const deleteEntry = async (id) => {
  return axios.delete(`${API_URL}/${id}`, { headers: authHeader() });
};

export default {
  createEntry,
  getEntry,
  updateEntry,
  getAllEntries,
  deleteEntry,
};
