import { defineStore } from "pinia";
import { login, logout } from "@/api/auth";

export const useUserStore = defineStore("user", {
    state: () => ({
        token: localStorage.getItem("token") || "",
        username: "",
        roleId: "",
        roleName: "",
        permissions: [],
    }),
    actions: {
        async loginUser({ username, password, roleId }) {
            try {
                const res = await login({ username, password, roleId });
                console.log("登录接口返回：", res);

                // 后端返回 token 在 res.data.token
                const data = res.data || res;
                this.token = data.token;
                this.username = username;
                this.roleId = data.roleId;
                this.roleName = data.roleName;
                this.permissions = data.permissions || [];
                localStorage.setItem("token", data.token);
            } catch (err) {
                console.error("登录失败", err);
                throw err;
            }
        },
        async logoutUser() {
            if (this.token) await logout(this.token);
            this.token = "";
            this.username = "";
            this.roleId = "";
            this.roleName = "";
            this.permissions = [];
            localStorage.removeItem("token");
        },
    },
});
