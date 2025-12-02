// src/stores/user.js
import { defineStore } from "pinia";
import request from "@/utils/request";
import router from "@/router";

// -----------------
// API 接口
// -----------------
export function loginApi(data) {
    return request.post("/auth/login", data); // data = {username, password}
}

export function registerApi(data) {
    return request.post("/auth/register", data);
}

export function logoutApi() {
    return request.post("/auth/logout");
}

export function getUserInfoApi() {
    return request.get("/auth/info");
}

// -----------------
// Pinia Store
// -----------------
export const useUserStore = defineStore("user", {
    state: () => ({
        token: "",
        username: "",
        userInfo: null,
    }),

    actions: {
        // 登录
        async login(form) {
            try {
                const res = await loginApi(form);
                const data = res.data; // 后端返回 {token, username,...}

                this.token = data.token;
                this.username = data.username;

                // 获取用户信息
                await this.fetchUserInfo();

                router.replace("/");
            } catch (error) {
                console.error("登录失败:", error);
                throw error;
            }
        },

        // 注册
        async register(form) {
            const res = await registerApi(form);
            return res.data;
        },

        // 注销
        async logout() {
            try {
                await logoutApi();
            } catch (e) {
                console.error("logout error:", e);
            } finally {
                this.$reset();
                router.replace("/login");
            }
        },

        // 获取用户信息
        async fetchUserInfo() {
            try {
                const res = await getUserInfoApi();
                const data = res.data;

                this.userInfo = data;
                if (data.username) {
                    this.username = data.username;
                }

                return this.userInfo;
            } catch (error) {
                console.error("获取用户信息失败:", error);
                this.logout();
            }
        },
    },

    persist: true, // 持久化状态
});
