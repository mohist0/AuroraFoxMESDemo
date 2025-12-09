import { defineStore } from 'pinia'
import service from '../utils/request'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    username: '',
    roleId: '',
    roleName: '',
    permissions: []
  }),

  actions: {
    async loginUser({ username, password, roleId }) {
      const data = await service.post('/auth/login', { username, password, roleId })
      this.token = data.token
      this.username = username
      this.roleId = data.roleId
      this.roleName = data.roleName
      this.permissions = data.permissions || []
      localStorage.setItem('token', data.token)
      return data
    },

    async logoutUser() {
      if (this.token) await service.post('/auth/logout')
      this.$reset()
      localStorage.removeItem('token')
    }
  }
})