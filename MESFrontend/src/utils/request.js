// src/utils/request.js
import axios from "axios";
import { useUserStore } from "@/stores/user";

// 创建 axios 实例
const service = axios.create({
    baseURL: "/api", // 代理前缀
    timeout: 8000,
});

// 请求拦截器：自动带 token
service.interceptors.request.use((config) => {
    const store = useUserStore();
    if (store.token) {
        config.headers.Authorization = `Bearer ${store.token}`;
    }
    return config;
}, (error) => {
    return Promise.reject(error);
});

// 响应拦截器
service.interceptors.response.use(
    (res) => res, // 保留原始结构，不破坏 data
    (err) => {
        console.error("API Error:", err);
        return Promise.reject(err);
    }
);

export default service;
