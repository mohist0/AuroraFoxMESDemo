<template>
  <form @submit.prevent="handleSubmit" class="login-form">
    <div class="form-item">
      <label>账号：</label>
      <input v-model="form.username" placeholder="请输入账号" />
    </div>
    <div class="form-item">
      <label>密码：</label>
      <input type="password" v-model="form.password" placeholder="请输入密码" />
    </div>
    <div class="form-item">
      <label>角色ID：</label>
      <input v-model="form.roleId" placeholder="请输入角色ID" />
    </div>
    <button class="login-btn" type="submit" :disabled="loading">
      {{ loading ? "登录中…" : "登录" }}
    </button>
  </form>
</template>

<script setup>
import { reactive, ref } from "vue";
import { useUserStore } from "@/stores/user";
import { useRouter } from "vue-router";

const userStore = useUserStore();
const router = useRouter();
const loading = ref(false);

const form = reactive({
  username: "",
  password: "",
  roleId: ""
});

function validateForm() {
  if (!form.username.trim()) { alert("请输入账号"); return false; }
  if (!form.password) { alert("请输入密码"); return false; }
  if (!form.roleId) { alert("请输入角色ID"); return false; }
  return true;
}

async function handleSubmit() {
  if (!validateForm()) return;
  loading.value = true;
  try {
    await userStore.loginUser(form);
    alert("登录成功！");
    // 这里跳转到根路径或一个欢迎页
    router.push("/");
  } catch (e) {
    alert("登录失败：" + (e.response?.data?.message || e.message));
  } finally {
    loading.value = false;
  }
}
</script>

<style scoped>
.login-form { display: flex; flex-direction: column; max-width: 350px; margin: 100px auto; }
.form-item { margin-bottom: 15px; }
input { width: 100%; height: 36px; padding: 0 10px; border: 1px solid #ccc; border-radius: 6px; }
.login-btn { height: 36px; background: #409eff; color: white; border: none; border-radius: 6px; cursor: pointer; }
.login-btn:disabled { background: #a0cfff; }
</style>
