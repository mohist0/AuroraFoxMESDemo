import { createRouter, createWebHistory } from "vue-router";
import Login from "@/views/Login.vue";

const routes = [
  { path: "/login", component: Login },
  // 根路径直接重定向到登录页
  { path: "/", redirect: "/login" },
  // 通配符路径，也重定向到登录页，防止报错
  { path: "/:pathMatch(.*)*", redirect: "/login" }
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
