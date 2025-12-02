<template>
  <div class="user-page">
    <h2>用户管理</h2>

    <!-- 添加用户 -->
    <div class="add-user">
      <input v-model="newUser.username" placeholder="用户名">
      <input v-model="newUser.password" placeholder="密码" type="password">
      <select v-model="newUser.role">
        <option value="admin">管理员</option>
        <option value="user">普通用户</option>
      </select>
      <button @click="addUser">添加用户</button>
    </div>

    <!-- 用户表格 -->
    <table class="user-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>用户名</th>
          <th>角色</th>
          <th>修改用户名</th>
          <th>修改密码</th>
          <th>操作</th>
        </tr>
      </thead>

      <tbody>
        <tr v-for="u in users" :key="u.id">
          <td>{{ u.id }}</td>

          <!-- 修改用户名 -->
          <td>
            <input v-model="u.username">
          </td>

          <!-- 修改角色 -->
          <td>
            <select v-model="u.role">
              <option value="admin">管理员</option>
              <option value="user">普通用户</option>
            </select>
          </td>

          <!-- 修改密码 -->
          <td>
            <input v-model="u.newPassword" placeholder="新密码" type="password">
          </td>

          <td>
            <button @click="updateUser(u)">保存</button>
            <button @click="deleteUser(u.id)" class="del">删除</button>
          </td>
        </tr>
      </tbody>

    </table>
  </div>
</template>

<script>
export default {
  data() {
    return {
      users: [],

      newUser: {
        username: "",
        password: "",
        role: "user"
      }
    };
  },

  created() {
    this.loadUsers();
  },

  methods: {
    // 获取用户列表
    async loadUsers() {
      const res = await fetch("/api/user/getAllUsers");
      this.users = await res.json();
    },

    // 添加用户
    async addUser() {
      await fetch("/api/user/addUser", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(this.newUser)
      });

      this.newUser = { username: "", password: "", role: "user" };
      this.loadUsers();
    },

    // 修改用户信息
    async updateUser(user) {
      await fetch(`/api/user/updateUser/${user.id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          username: user.username,
          role: user.role,
          password: user.newPassword || null
        })
      });

      user.newPassword = "";
      this.loadUsers();
    },

    // 删除用户
    async deleteUser(id) {
      await fetch(`/api/user/deleteUser/${id}`, { method: "DELETE" });
      this.loadUsers();
    },

    // 可选：获取单个用户信息
    async getUserById(id) {
      const res = await fetch(`/api/user/getUserById/${id}`);
      return await res.json();
    },

    // 可选：获取用户绑定角色
    async getUserRoles(id) {
      const res = await fetch(`/api/user/${id}/roles`);
      return await res.json();
    }
  }
};
</script>

<style scoped>
.user-page {
  background: #fff;
  padding: 20px;
}

.user-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}
.user-table th,
.user-table td {
  border: 1px solid #ccc;
  padding: 8px 10px;
  text-align: left;
}

.add-user {
  display: flex;
  gap: 10px;
}
.del {
  background: #ff4d4d;
  color: #fff;
}
</style>
