import { defineStore } from "pinia"
import { login, logout } from "../api/auth"

export const useUserStore = defineStore("user", {
  state: () => ({
    token: localStorage.getItem("token") || "",
    username: localStorage.getItem("username") || "",
    roleId: localStorage.getItem("roleId") || "",
    roleName: localStorage.getItem("roleName") || "",
    permissions: JSON.parse(localStorage.getItem("permissions") || "[]"),
    menus: JSON.parse(localStorage.getItem("menus") || "[]")
  }),

  actions: {
    async loginUser({ username, password, roleId }) {
      const res = await login({ username, password, roleId })
      const data = res.data ?? res

      console.log("登录返回：", data)

      if (!data.token) {
        throw new Error('登录失败：未返回 token')
      }

      // 保存到 state
      this.token = data.token
      this.username = data.username || username
      this.roleId = data.roleId || roleId
      this.roleName = data.roleName || ""
      this.permissions = data.permissions || []
      this.menus = data.menus || []

      // 保存到 localStorage
      localStorage.setItem("token", data.token)
      localStorage.setItem("username", data.username || username)
      localStorage.setItem("roleId", data.roleId || roleId)
      localStorage.setItem("roleName", data.roleName || "")
      localStorage.setItem("permissions", JSON.stringify(data.permissions || []))
      localStorage.setItem("menus", JSON.stringify(data.menus || []))
    },

    async logoutUser() {
      try {
        await logout()
      } catch (e) {
        console.error("注销失败:", e)
      } finally {
        // 清除所有数据
        this.$reset()
        localStorage.removeItem("token")
        localStorage.removeItem("username")
        localStorage.removeItem("roleId")
        localStorage.removeItem("roleName")
        localStorage.removeItem("permissions")
        localStorage.removeItem("menus")
      }
    }
  }
})