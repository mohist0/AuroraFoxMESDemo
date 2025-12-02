import { createRouter, createWebHistory } from "vue-router";
import { useUserStore } from "@/stores/user";

const routes = [
  {
    path: "/",
    component: () => import("@/views/Layout.vue"),
    meta: { requiresAuth: true },
    children: [
      // 这里可以添加其他需要认证的子路由
      {
        path: "",
        component: () => import("@/components/HelloWorld.vue")
      }
    ]
  },
  {
    path: "/login",
    component: () => import("@/views/Login.vue"),
    meta: { requiresAuth: false }
  },
  // 捕获所有未匹配的路由，重定向到登录页
  {
    path: "/:pathMatch(.*)*",
    redirect: "/"
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

// 路由守卫
router.beforeEach((to, from, next) => {
  const userStore = useUserStore();
  
  // 检查路由是否需要认证
  if (to.meta.requiresAuth && !userStore.token) {
    // 需要认证但未登录，重定向到登录页
    next({
      path: "/login",
      query: { redirect: to.fullPath }
    });
  } else if (to.path === "/login" && userStore.token) {
    // 已登录但尝试访问登录页，重定向到首页
    next("/");
  } else {
    // 其他情况，正常导航
    next();
  }
});

export default router;
