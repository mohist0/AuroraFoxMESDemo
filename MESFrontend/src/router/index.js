import { createRouter, createWebHistory } from 'vue-router'
//登录页面
import LoginForm from '../components/LoginForm.vue'

//退出登录
import Layout from '../components/Layout.vue'

// 主界面
import MainLayout from '../components/MainLayout.vue'

// 个人中心里的组件
import UserManage from '../components/UserManage/UserManage.vue'
import PermissionManage from '../components/UserManage/PermissionManage.vue'
import RoleManage from '../components/UserManage/RoleManage.vue'

//生产计划里面的组件
import Order from '../components/ProductionPlan/Order.vue'


import Dispatch from '../views/ProductionPlan/Dispatch.vue'
import PlanGantt from '../views/ProductionPlan/PlanGantt.vue'
import Report from '../views/ProductionExecution/Report.vue'
import Notice from '../views/ProductionExecution/Notice.vue'
import Abnormal from '../views/ProductionExecution/Abnormal.vue'
import QualityInput from '../views/QualityManagement/QualityInput.vue'
import Trace from '../views/QualityManagement/Trace.vue'
import Defect from '../views/QualityManagement/Defect.vue'
import EQPState from '../views/DeviceManagement/EQPState.vue'
import Maintain from '../views/DeviceManagement/Maintain.vue'
import EQPBook from '../views/DeviceManagement/EQPBook.vue'
import Dashboard from '../views/DataDashboard/Dashboard.vue'
import Trend from '../views/DataDashboard/Trend.vue'
import DeptCompare from '../views/DataDashboard/DeptCompare.vue'

import Settings from '../views/Main/Settings.vue'


const routes = [
  {
    path: '/login',
    name: 'Login',
    component: LoginForm
  },
  {
    path: '/',
    component: MainLayout,
    meta: { requiresAuth: true }, // 主页面需要登录
    children: [
      { path: '', name: 'Home', component: null },
      // 生产计划中的组件lu
      { path: 'Order', name: 'Order', component: Order },
      { path: 'Dispatch', name: 'Dispatch', component: Dispatch },
      { path: 'PlanGantt', name: 'PlanGantt', component: PlanGantt },
      { path: 'Report', name: 'Report', component: Report },
      { path: 'Notice', name: 'Notice', component: Notice },
      { path: 'Abnormal', name: 'Abnormal', component: Abnormal },
      { path: 'QualityInput', name: 'QualityInput', component: QualityInput },
      { path: 'Trace', name: 'Trace', component: Trace },
      { path: 'Defect', name: 'Defect', component: Defect },
      { path: 'EQPState', name: 'EQPState', component: EQPState },
      { path: 'Maintain', name: 'Maintain', component: Maintain },
      { path: 'EQPBook', name: 'EQPBook', component: EQPBook },
      { path: 'Dashboard', name: 'Dashboard', component: Dashboard },
      { path: 'Trend', name: 'Trend', component: Trend },
      { path: 'DeptCompare', name: 'DeptCompare', component: DeptCompare },
      { path: 'UserManage', name: 'UserManage', component: UserManage },
      { path: 'RoleManage', name: 'RoleManage', component: RoleManage },
      { path: 'PermissionManage', name: 'PermissionManage', component: PermissionManage },
      { path: 'Settings', name: 'Settings', component: Settings }
    ]
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token'); // 判断是否已登录
  if (to.meta.requiresAuth && !token) {
    // 如果页面需要登录，但没有 token，则跳转登录
    next({ name: 'Login' });
  } else if (to.name === 'Login' && token) {
    // 如果已登录，访问登录页，直接跳主页面
    next({ path: '/' });
  } else {
    next();
  }
});

export default router;
