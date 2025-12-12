// src/stores/userStore.js
import { defineStore } from 'pinia';
import { ref } from 'vue';
import { getAllUsers, addUser, updateUser, deleteUser } from '@/api/user';

export const useUserStore = defineStore('userStore', () => {
  const users = ref([]);

  // 加载用户列表
  const loadUsers = async () => {
    try {
      const res = await getAllUsers();
      // 后端返回的 roleIds 应为 ["R001"] 或 ["R002"]，前端取第一个角色展示
      users.value = (res.data || res).map(user => ({
        userId: user.userId,
        userName: user.userName,
        selectedRole: user.roleIds?.[0] || 'R002', // 默认显示 R002
        newPassword: ''
      }));
    } catch (err) {
      console.error('加载用户失败:', err);
      alert('加载用户失败：' + err.message);
    }
  };

  // 添加用户（参数中的 roleIds 为 R001/R002 数组）
  const addUserFunc = async (userData) => {
    try {
      console.log('发送添加用户请求:', userData); 
      // 示例请求参数：
      // { userId: "U003", userName: "test", password: "123", roleIds: ["R001"] }
      await addUser(userData);
      alert('添加用户成功');
      await loadUsers();
    } catch (err) {
      console.error('添加用户失败:', err);
      alert('添加失败：' + (err.response?.data?.message || err.message));
    }
  };

  // 更新用户（角色同样使用 R001/R002）
  const updateUserFunc = async (updateData) => {
    try {
      await updateUser(updateData.userId, updateData);
      alert('更新用户成功');
      await loadUsers();
    } catch (err) {
      console.error('更新用户失败:', err);
      alert('更新失败：' + (err.response?.data?.message || err.message));
    }
  };

  // 删除用户
  const deleteUserFunc = async (userId) => {
    try {
      await deleteUser(userId);
      alert('删除成功');
      await loadUsers();
    } catch (err) {
      console.error('删除失败:', err);
      alert('删除失败：' + err.message);
    }
  };

  return {
    users,
    loadUsers,
    addUserFunc,
    updateUserFunc,
    deleteUserFunc
  };
});