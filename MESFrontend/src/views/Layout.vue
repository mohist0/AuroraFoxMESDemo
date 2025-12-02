<template>
  <div class="layout">
    <header class="header">
      <div class="header-left">
        <h1>MES 系统</h1>
      </div>
      <div class="header-right">
        <span class="username" v-if="userStore.username">欢迎，{{ userStore.username }}</span>
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
import { useRouter } from "vue-router";

const userStore = useUserStore();
const router = useRouter();

async function handleLogout() {
  if (confirm("确定要退出登录吗？")) {
    await userStore.logoutUser();
    router.push("/login");
  }
}
</script>

<style scoped>
.layout { height: 100vh; display: flex; flex-direction: column; }
.header { height: 60px; background: #304156; color: white; display: flex; justify-content: space-between; align-items: center; padding: 0 20px; }
.main { flex: 1; padding: 20px; overflow: auto; }
.logout-btn { padding: 6px 16px; background: #f56c6c; color: white; border: none; border-radius: 4px; cursor: pointer; }
.logout-btn:hover { background: #e64c65; }
</style>
