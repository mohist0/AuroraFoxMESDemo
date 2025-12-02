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
            const res = await login({ username, password, roleId });
            this.token = res.token;
            this.username = username;
            this.roleId = res.roleId;
            this.roleName = res.roleName;
            this.permissions = res.permissions || [];
            localStorage.setItem("token", res.token);
        },
        async logoutUser() {
            if (this.token) {
                await logout(this.token);
            }
            this.token = "";
            this.username = "";
            this.roleId = "";
            this.roleName = "";
            this.permissions = [];
            localStorage.removeItem("token");
        },
    },
});
