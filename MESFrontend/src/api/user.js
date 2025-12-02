import request from "@/utils/request";

// 登录
export function loginApi(data) {
    return request.post("/auth/login", data); // axios 会自动序列化 JSON
}

// 注册
export function registerApi(data) {
    return request.post("/auth/register", data);
}

// 注销
export function logoutApi() {
    return request.post("/auth/logout");
}

// 获取用户信息
export function getUserInfoApi() {
    return request.get("/auth/info");
}

export default {
    loginApi,
    registerApi,
    logoutApi,
    getUserInfoApi,
};
