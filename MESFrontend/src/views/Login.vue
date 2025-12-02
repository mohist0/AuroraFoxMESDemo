<template>
  <div class="login-page">
    <h2>MES 系统登录</h2>
    <LoginForm />
  </div>
</template>

<script setup>
import LoginForm from "@/components/LoginForm.vue";
</script>

<style scoped>
.login-page { display: flex; flex-direction: column; align-items: center; justify-content: center; height: 100vh; }
</style>
