import { createRouter, createWebHashHistory } from 'vue-router';
// 改用相对路径导入Order组件（彻底解决路径报错）
import Order from '../views/ProductionPlan/Order.vue';

// 路由规则（极简版，仅保留订单管理）
const routes = [
  {
    path: '/',
    name: 'OrderManagement',
    component: Order,
    meta: { title: '订单管理' }
  }
];

// 创建路由实例
const router = createRouter({
  history: createWebHashHistory(),
  routes
});

// 路由前置守卫：设置页面标题
router.beforeEach((to) => {
  document.title = to.meta.title || '订单管理系统';
});

export default router;