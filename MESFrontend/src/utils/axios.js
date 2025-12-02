import axios from "axios";

const service = axios.create({
    baseURL: "/api",    // 关键点：这里写 /api
    timeout: 5000
});

// 拦截器可以自由加
service.interceptors.request.use(
    (config) => {
        // 如果你有 token，自动挂上
        const token = localStorage.getItem("token");
        if (token) config.headers["Authorization"] = token;
        return config;
    },
    (error) => Promise.reject(error)
);

export default service;
