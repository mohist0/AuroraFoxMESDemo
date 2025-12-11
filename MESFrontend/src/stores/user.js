import { defineStore } from "pinia";
import { login, logout } from "../api/auth";

export const useUserStore = defineStore("user", {
    state: () => ({
        token: localStorage.getItem("token") || "",
        username: "",
        roleId: "",
        roleName: "",
        permissions: [],
        menus: []        // ← 新增：左侧菜单
    }),

    actions: {
        async loginUser({ username, password, roleId }) {

            const res = await login({ username, password, roleId });
            const data = res.data ?? res;

            console.log("登录返回：", data);

            this.token = data.token;
            this.username = data.username;
            this.roleId = data.roleId;
            this.roleName = data.roleName;
            this.permissions = data.permissions || [];
            this.menus = data.menus || [];      // ← 保存菜单

            localStorage.setItem("token", data.token);
        },

        async logoutUser() {
            await logout();
            this.$reset();
            localStorage.removeItem("token");
        }
    },
});
