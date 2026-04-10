import axios from "axios";

const axiosClient = axios.create({
  baseURL: "https://asset-management-backend-h978.onrender.com",
  headers: {
    "Content-Type": "application/json",
  },
});

export default axiosClient;