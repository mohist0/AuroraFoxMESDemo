<template>
  <div class="login-page">
    <div class="login-box">
      <h2 class="title">MES 系统登录</h2>
      <LoginForm />
    </div>
  </div>
</template>

<script setup>
import LoginForm from "@/components/LoginForm.vue";
</script>

<style scoped>
.login-page {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: #eef2f5;
}

.login-box {
  width: 360px;
  padding: 30px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 0 12px rgba(0, 0, 0, 0.08);
}

.title {
  text-align: center;
  margin-bottom: 25px;
}
</style>
