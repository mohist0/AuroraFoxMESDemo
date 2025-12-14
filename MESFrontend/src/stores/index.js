import { defineStore } from "pinia";
import { login, logout } from "../api/auth"; // 引入登录和登出的API函数
import router from '../router'; // 引入路由实例

export const useUserStore = defineStore("user", {
  // 定义状态，从localStorage中获取或设置默认值
  state: () => ({
    token: localStorage.getItem("token") || "", // 用户token
    username: localStorage.getItem("username") || "", // 用户名
    roleId: localStorage.getItem("roleId") || "", // 角色ID
    roleName: localStorage.getItem("roleName") || "", // 角色名
    permissions: JSON.parse(localStorage.getItem("permissions") || "[]"), // 权限列表
    menus: JSON.parse(localStorage.getItem("menus") || "[]") // 菜单列表
  }),

  actions: {
    // 异步登录用户
    async loginUser({ username, password, roleId }) {
      // 调用登录API并获取响应
      const res = await login({ username, password, roleId });
      const data = res.data ?? res; // 获取响应数据

      console.log("登录返回：", data); // 打印登录返回的数据

      // 检查登录是否成功
      if (!data.token) {
        throw new Error('登录失败：未返回 token'); // 如果没有token，抛出错误
      }

      // 将数据保存到状态中
      this.token = data.token;
      this.username = data.username || username;
      this.roleId = data.roleId || roleId;
      this.roleName = data.roleName || "";
      this.permissions = data.permissions || [];
      this.menus = data.menus || [];

      // 将数据保存到localStorage中
      localStorage.setItem("token", data.token);
      localStorage.setItem("username", data.username || username);
      localStorage.setItem("roleId", data.roleId || roleId);
      localStorage.setItem("roleName", data.roleName || "");
      localStorage.setItem("permissions", JSON.stringify(data.permissions || []));
      localStorage.setItem("menus", JSON.stringify(data.menus || []));
    },

    // 异步登出用户
    async logoutUser() {
      try {
        await logout(); // 调用登出API
      } catch (e) {
        console.error("注销失败:", e); // 如果登出失败，打印错误信息
      } finally {
        // 无论登出是否成功，都清除所有相关数据
        this.$reset(); // 重置Pinia store的状态
        localStorage.removeItem("token"); // 清除token
        localStorage.removeItem("username"); // 清除用户名
        localStorage.removeItem("roleId"); // 清除角色ID
        localStorage.removeItem("roleName"); // 清除角色名
        localStorage.removeItem("permissions"); // 清除权限列表
        localStorage.removeItem("menus"); // 清除菜单列表

        // 重定向到登录页面
        router.push('/login');
      }
    }
  }
});
