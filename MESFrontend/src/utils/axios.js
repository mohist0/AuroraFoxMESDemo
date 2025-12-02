import axios from "axios";

const service = axios.create({
    baseURL: "http://localhost:8080/api", // 指向后端
    timeout: 5000,
    withCredentials: true, // 如果后端需要 cookie
});

service.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem("token");
        // 登录请求不加 token
        if (token && !config.url.includes("/auth/login")) {
            config.headers["Authorization"] = `Bearer ${token}`;
        }
        return config;
    },
    (error) => Promise.reject(error)
);

export default service;
