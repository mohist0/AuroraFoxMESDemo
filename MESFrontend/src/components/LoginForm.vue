<template>
  <div class="login-page">
    <form @submit.prevent="handleSubmit" class="login-form">
      <h2 class="title">MES 系统登录</h2>

      <div class="form-item">
        <label>账号：</label>
        <input v-model="form.username" placeholder="请输入账号" />
      </div>

      <div class="form-item">
        <label>密码：</label>
        <input type="password" v-model="form.password" placeholder="请输入密码" />
      </div>

      <div class="form-item">
        <label>角色：</label>
        <select v-model="form.roleId">
          <option disabled value="">请选择角色</option>
          <option v-for="r in roles" :key="r.id" :value="r.id">
            {{ r.id }} - {{ r.name }}
          </option>
        </select>
      </div>

      <button class="login-btn" type="submit" :disabled="loading">
        {{ loading ? "登录中…" : "登录" }}
      </button>
    </form>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import { useUserStore } from "../stores/user";
import { useRouter } from "vue-router";

const router = useRouter();
const userStore = useUserStore();

const roles = [
  { id: "R001", name: "管理层" },
  { id: "R002", name: "系统管理员" },
  { id: "R003", name: "设备管理员" },
  { id: "R004", name: "生产计划员" },
  { id: "R005", name: "车间操作员" },
  { id: "R006", name: "质检人员" },
];

const form = reactive({
  username: "",
  password: "",
  roleId: "",
});

const loading = ref(false);

async function handleSubmit() {
  if (!form.username.trim()) return alert("请输入账号");
  if (!form.password) return alert("请输入密码");
  if (!form.roleId) return alert("请选择角色");

  loading.value = true;
  try {
    await userStore.loginUser(form);
    alert("登录成功！");
    router.push("/");// 自动跳转页面
  } catch (e) {
    alert("登录失败：" + (e.response?.data?.message || e.message));
  } finally {
    loading.value = false;
  }
}
</script>

<style scoped>
/* 整页居中，不再影响全局 body（重要） */
.login-page {
  width: 100vw;
  height: 100vh;
  background: linear-gradient(135deg, #eef2f6 0%, #f5f7fa 100%);
  display: flex;
  align-items: center;
  justify-content: center;
}

/* 登录框 */
.login-form {
  width: 360px;
  padding: 40px 35px;
  border-radius: 12px;
  background: #ffffff;
  box-shadow: 0 8px 26px rgba(0, 0, 0, 0.09);
  display: flex;
  flex-direction: column;
}

/* 标题 */
.title {
  text-align: center;
  margin-bottom: 25px;
  font-size: 20px;
  color: #333;
}

.form-item {
  margin-bottom: 22px;
}

label {
  display: block;
  font-size: 14px;
  color: #606266;
  margin-bottom: 8px;
}

/* 输入框 */
input,
select {
  width: 100%;
  height: 40px;
  padding: 0 12px;
  border: 1px solid #dcdfe6;
  border-radius: 8px;
  color: #303133;
  font-size: 14px;
  box-sizing: border-box;
  outline: none;
  transition: border-color 0.3s, box-shadow 0.3s;
  background: #ffffff;
}

input:focus,
select:focus {
  border-color: #409eff;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}

/* 登录按钮 */
.login-btn {
  height: 42px;
  background: #409eff;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 15px;
  cursor: pointer;
  margin-top: 8px;
  transition: background 0.25s, box-shadow 0.25s;
}

.login-btn:hover {
  background: #66b1ff;
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.32);
}

.login-btn:disabled {
  background: #a0cfff;
  cursor: not-allowed;
}
</style>
