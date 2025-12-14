<template>
  <div class="user-page">
    <h2>用户管理</h2>

    <!-- 添加用户表单 -->
    <div class="add-user">
      <input
        v-model="newUser.userId"
        placeholder="请输入用户ID（如U001）"
        class="input-item"
      />
      <input
        v-model="newUser.userName"
        placeholder="请输入用户名"
        class="input-item"
      />
      <input
        v-model="newUser.password"
        placeholder="请输入密码"
        type="password"
        class="input-item"
      />
      <!-- 角色选择器：选项值改为 R001、R002 -->
      <select v-model="newUser.selectedRole" class="input-item">
        <option value="R001">角色R001（例如：管理员）</option>
        <option value="R002">角色R002（例如：普通用户）</option>
      </select>
      <button @click="handleAddUser" class="btn add-btn">添加用户</button>
    </div>

    <!-- 加载状态提示 -->
    <div v-if="loading" class="loading-tip">加载中...</div>

    <!-- 用户列表表格 -->
    <table class="user-table" v-else>
      <thead>
        <tr>
          <th>ID</th>
          <th>用户名</th>
          <th>角色</th>
          <th>修改密码</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="user in userStore.users" :key="user.userId">
          <td>{{ user.userId }}</td>
          <td>
            <input v-model="user.userName" class="edit-input" />
          </td>
          <td>
            <!-- 编辑时角色选项也使用 R001、R002 -->
            <select v-model="user.selectedRole" class="edit-select">
              <option value="R001">角色R001（例如：管理员）</option>
              <option value="R002">角色R002（例如：普通用户）</option>
            </select>
          </td>
          <td>
            <input
              v-model="user.newPassword"
              placeholder="新密码"
              type="password"
              class="edit-input"
            />
          </td>
          <td class="op-btns">
            <button @click="handleUpdateUser(user)" class="btn update-btn">保存</button>
            <button @click="handleDeleteUser(user.userId)" class="btn del-btn">删除</button>
          </td>
        </tr>

        <tr v-if="userStore.users.length === 0">
          <td colspan="5" class="empty-tip">暂无用户数据</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useUserStore } from "@/stores/userStore";

const userStore = useUserStore();
// 新增用户参数：角色使用 R001/R002
const newUser = ref({
  userId: "",
  userName: "",
  password: "",
  selectedRole: "R002" // 默认角色为 R002（对应原普通用户）
});
const loading = ref(false);

// 页面加载时获取用户列表
onMounted(async () => {
  loading.value = true;
  await userStore.loadUsers();
  loading.value = false;
});

// 添加用户（roleIds 为 [R001] 或 [R002]）
const handleAddUser = async () => {
  // 校验必填字段
  if (!newUser.value.userId.trim()) return alert("用户ID不能为空");
  if (!newUser.value.userName.trim()) return alert("用户名不能为空");
  if (!newUser.value.password.trim()) return alert("密码不能为空");

  // 构造请求参数：roleIds 数组元素为 R001/R002
  const userData = {
    userId: newUser.value.userId.trim(),
    userName: newUser.value.userName.trim(),
    password: newUser.value.password.trim(),
    roleIds: [newUser.value.selectedRole] // 例如 ["R001"] 或 ["R002"]
  };

  await userStore.addUserFunc(userData);
  // 重置表单
  newUser.value = { userId: "", userName: "", password: "", selectedRole: "R002" };
};

// 更新用户（角色同样使用 R001/R002）
const handleUpdateUser = async (user) => {
  const updateData = {
    userId: user.userId,
    userName: user.userName.trim(),
    roleIds: [user.selectedRole], // 例如 ["R001"]
    ...(user.newPassword && { password: user.newPassword.trim() })
  };
  await userStore.updateUserFunc(updateData);
};

// 删除用户
const handleDeleteUser = async (userId) => {
  if (confirm("确定删除？")) {
    await userStore.deleteUserFunc(userId);
  }
};
</script>

<style scoped>
/* 样式不变 */
.user-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  background: #f5f5f5;
  border-radius: 8px;
}
.add-user {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
  align-items: center;
}
.input-item {
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  flex: 1;
  min-width: 120px;
}
.btn {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  color: #fff;
}
.add-btn { background: #409eff; }
.update-btn { background: #67c23a; margin-right: 8px; }
.del-btn { background: #f56c6c; }
.user-table {
  width: 100%;
  border-collapse: collapse;
  background: #fff;
  border-radius: 4px;
  overflow: hidden;
}
.user-table th, .user-table td {
  border: 1px solid #eee;
  padding: 12px 15px;
  text-align: left;
}
.user-table th { background: #f8f9fa; font-weight: 600; }
.edit-input, .edit-select { width: 100%; padding: 4px 8px; border: 1px solid #ddd; border-radius: 4px; }
.empty-tip { text-align: center; color: #999; padding: 20px; }
.loading-tip { text-align: center; padding: 20px; color: #666; font-size: 14px; }
</style>
