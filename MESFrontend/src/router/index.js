import { createRouter, createWebHashHistory } from 'vue-router';
// 匹配图中实际文件路径（ProductionPlan下的组件）
import Order from '../views/ProductionPlan/Order.vue'; // 订单列表页
import ProductionOrderCreate from '../views/ProductionPlan/ProductionOrderCreate.vue'; // 创建订单页
import Dispatch from '../views/ProductionPlan/Dispatch.vue'; // 排产页（可选）
import PlanGantt from '../views/ProductionPlan/PlanGantt.vue'; // 甘特图页（可选）

// 路由规则（贴合图中文件结构）
const routes = [
  {
    path: '/',
    redirect: '/production-plan/order' // 默认跳转到订单列表
  },
  {
    path: '/production-plan/order',
    name: 'OrderManagement',
    component: Order,
    meta: { title: '订单管理' }
  },
  {
    path: '/production-plan/create',
    name: 'ProductionOrderCreate',
    component: ProductionOrderCreate,
    meta: { title: '创建生产订单' }
  },
  {
    path: '/production-plan/dispatch',
    name: 'Dispatch',
    component: Dispatch,
    meta: { title: '生产排产' }
  },
  {
    path: '/production-plan/gantt',
    name: 'PlanGantt',
    component: PlanGantt,
    meta: { title: '计划甘特图' }
  }
];

// 创建路由实例
const router = createRouter({
  history: createWebHashHistory(),
  routes
});

// 路由前置守卫：设置页面标题
router.beforeEach((to) => {
  document.title = to.meta.title || '生产管理系统';
});

export default router;