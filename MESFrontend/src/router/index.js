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
import Dispatch from '../components/ProductionPlan/Dispatch.vue'
import PlanGantt from '../components/ProductionPlan/PlanGantt.vue'

//未完成的组件
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


const routes = [
  {
    path: '/login',
    name: 'Login',
    component: LoginForm,
    meta: { isPublic: true } // 标记为公开路由
  },
  {
    path: '/layout',
    name: 'Layout',
    component: Layout,
  },
  {
    path: '/',
    component: MainLayout,
    meta: { requiresAuth: true }, // 需要登录
    redirect: '/order',
     children: [
      { path: '', name: 'Home', component: null },
      // 生产计划中的组件
      { path: 'Order', name: 'Order', component: Order },
      { path: 'Dispatch', name: 'Dispatch', component: Dispatch },
      { path: 'PlanGantt', name: 'PlanGantt', component: PlanGantt },
      //未完成的组件
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
      //系统管理的组件
      { path: 'UserManage', name: 'UserManage', component: UserManage },
      { path: 'RoleManage', name: 'RoleManage', component: RoleManage },
      { path: 'PermissionManage', name: 'PermissionManage', component: PermissionManage },
    ]
  },
  {
    path: '/:pathMatch(.*)*', // 404 处理
    redirect: '/login'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  
  // 已登录但访问登录页，跳转到首页
  if (to.name === 'Login' && token) {
    return next('/')
  }
  
  // 需要登录但未登录，跳转到登录页
  if (to.meta.requiresAuth && !token) {
    return next({ name: 'Login', query: { redirect: to.fullPath } })
  }
  
  next()
})

export default router

