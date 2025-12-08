import axios from "axios";

// 让 axios 和 fetch 的请求完全一致
const service = axios.create({
  baseURL: "/api",
  timeout: 5000,
  withCredentials: false, // ← 必须禁用，否则会带 Cookie 触发后端权限
});

// 禁用所有拦截器里注入的 Authorization 等 header
service.interceptors.request.use(
  (config) => {
    // 强制删除 axios 自动加的各种头部，保持最干净
    delete config.headers['Authorization'];
    delete config.headers['authorization'];
    delete config.headers['cookie'];
    delete config.headers['Cookie'];

    // 有些浏览器 axios 会自动加 X-Requested-With，这也可能触发后端拦截
    delete config.headers['X-Requested-With'];

    return config;
  },
  (error) => Promise.reject(error)
);

service.interceptors.response.use(
  (response) => response.data,
  (error) => {
    console.error("API ERROR:", error);
    return Promise.reject(error);
  }
);

export default service;
