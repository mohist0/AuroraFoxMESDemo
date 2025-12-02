<template>
  <div class="layout">
    <header class="header">
      <div class="header-left">
        <h1>MES 系统</h1>
      </div>
      <div class="header-right">
        <span class="username">欢迎，{{ userStore.username }}</span>
        <button class="logout-btn" @click="handleLogout">退出登录</button>
      </div>
    </header>
    <main class="main">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { useUserStore } from "@/stores/user";

const userStore = useUserStore();

async function handleLogout() {
  if (confirm("确定要退出登录吗？")) {
    await userStore.logout();
  }
}
</script>

<style scoped>
.layout {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.header {
  height: 60px;
  background: #304156;
  color: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
}

.header h1 {
  font-size: 20px;
  margin: 0;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.username {
  font-size: 14px;
}

.logout-btn {
  padding: 6px 16px;
  background: #f56c6c;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.logout-btn:hover {
  background: #e64c65;
}

.main {
  flex: 1;
  padding: 20px;
  overflow: auto;
}
</style>
