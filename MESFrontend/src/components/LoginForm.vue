<template>
  <form class="login-form" @submit.prevent="handleSubmit">
    <div class="form-item">
      <label>账号：</label>
      <input v-model="form.username" placeholder="请输入账号" />
    </div>

    <div class="form-item">
      <label>密码：</label>
      <input type="password" v-model="form.password" placeholder="请输入密码" />
    </div>

    <div v-if="isRegisterMode" class="form-item">
      <label>确认密码：</label>
      <input type="password" v-model="form.confirmPassword" placeholder="请再次输入密码" />
    </div>

    <button class="login-btn" type="submit" :disabled="loading">
      {{ loading ? (isRegisterMode ? "注册中…" : "登录中…") : (isRegisterMode ? "注册" : "登录") }}
    </button>

    <div class="switch-mode">
      <span @click="toggleMode">
        {{ isRegisterMode ? "已有账号？立即登录" : "没有账号？立即注册" }}
      </span>
    </div>
  </form>
</template>

<script setup>
import { reactive, ref } from "vue";
import { useUserStore } from "@/stores/user";

const userStore = useUserStore();
const loading = ref(false);
const isRegisterMode = ref(false);

const form = reactive({
  username: "",
  password: "",
  confirmPassword: ""
});

function toggleMode() {
  isRegisterMode.value = !isRegisterMode.value;
  // 切换模式时清空表单
  form.username = "";
  form.password = "";
  form.confirmPassword = "";
}

function validateForm() {
  if (!form.username.trim()) {
    alert("请输入账号");
    return false;
  }
  
  if (!form.password) {
    alert("请输入密码");
    return false;
  }
  
  if (isRegisterMode.value) {
    if (form.password !== form.confirmPassword) {
      alert("两次输入的密码不一致");
      return false;
    }
  }
  
  return true;
}

async function handleSubmit() {
  if (!validateForm()) return;

  loading.value = true;

  try {
    if (isRegisterMode.value) {
      await userStore.register({
        username: form.username,
        password: form.password,
      });
      alert("注册成功");
      toggleMode();
    } else {
      await userStore.login({
        username: form.username,
        password: form.password,
      });
    }
  } catch (e) {
    alert("操作失败，请检查账号或密码");
  } finally {
    loading.value = false;
  }
}

</script>

<style scoped>
.login-form {
  display: flex;
  flex-direction: column;
}

.form-item {
  margin-bottom: 15px;
}

.form-item label {
  margin-bottom: 5px;
  display: block;
}

input {
  width: 100%;
  height: 36px;
  padding: 0 10px;
  border: 1px solid #ccc;
  border-radius: 6px;
}

.login-btn {
  height: 36px;
  background: #409eff;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  margin-top: 10px;
}

.login-btn:disabled {
  background: #a0cfff;
}

.switch-mode {
  margin-top: 15px;
  text-align: center;
}

.switch-mode span {
  color: #409eff;
  cursor: pointer;
  font-size: 14px;
}

.switch-mode span:hover {
  text-decoration: underline;
}
</style>
