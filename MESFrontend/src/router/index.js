import { createRouter, createWebHistory } from 'vue-router'; // 引入Vue Router创建函数
import { useUserStore } from '@/stores/index.js'; // 使用命名导入

// 导入各个组件
import Home from '@/views/Main/Home.vue'; // 主页组件
import UserManage from '@/views/User-Manage/UserManage.vue'; // 用户管理组件
import RoleManage from '@/views/User-Manage/RoleManage.vue'; // 角色管理组件
import PermissionManage from '@/views/User-Manage/PermissionManage.vue';
import Order from '@/views/Production-Plan/Order.vue'; // 订单管理组件
import Login from '@/views/Main/Login.vue'; // 登录组件

const routes = [
  {
    path: '/', // 路径为根路径
    name: 'Home', // 路由名称为Home
    component: Home, // 使用Home组件
    meta: { requiresAuth: true }, // 需要认证的路由
    children: [
      // 用户管理-UserManage-路由
      {
        path: 'user-manage', // 子路径为user-manage
        name: 'UserManage', // 子路由名称为UserManage
        component: UserManage, // 使用UserManage组件
        meta: { requiresAuth: true }, // 子路由也需要认证
      },
      // 角色管理-RoleManage-路由
      {
        path: 'role-manage', // 子路径为role-manage
        name: 'RoleManage', // 子路由名称为RoleManage
        component: RoleManage, // 使用RoleManage组件
        meta: { requiresAuth: true }, // 子路由也需要认证
      },
      // 权限管理-PermissionManage-路由
      {
        path: 'permission-manage', // 子路径为permission-manage
        name: 'PermissionManage', // 子路由名称为PermissionManage
        component: PermissionManage, // 使用PermissionManage组件
        meta: { requiresAuth: true }, // 子路由也需要认证
      },
      // 订单管理-Order-路由
      {
        path: 'order', // 子路径为order
        name: 'Order', // 子路由名称为Order
        component: Order, // 使用Order组件
        meta: { requiresAuth: true }, // 子路由也需要认证
      },
    ]
  },
  {
    path: '/login', // 路径为/login
    name: 'Login', // 路由名称为Login
    component: Login, // 使用Login组件
    meta: { requiresAuth: false } // 登录页面不需要认证
  },
];

// 创建路由实例
const router = createRouter({
  history: createWebHistory(), // 使用HTML5 History模式
  routes // 使用定义的路由配置
});

// 路由守卫
router.beforeEach((to, from, next) => {
  const store = useUserStore(); // 创建useUserStore实例
  const isAuthenticated = store.token !== ""; // 检测用户是否已登录

  // 判断当前路由是否需要认证
  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (!isAuthenticated) {
      // 如果未认证，重定向到登录页面，并保存当前路径以便登录后重定向回来
      next({
        path: '/login',
        query: { redirect: to.fullPath }
      });
    } else {
      // 如果已认证，继续导航
      next();
    }
  } else {
    // 如果不需要认证，继续导航
    next();
  }
});

export default router; // 导出路由实例
