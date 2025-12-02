import { createRouter, createWebHistory } from 'vue-router'
import MainLayout from '../components/MainLayout.vue'
import Order from '../views/ProductionPlan/Order.vue'
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
import UserManage from '../views/Main/UserManage.vue'
const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      component: MainLayout,
      children: [
        {
          path: '',
          name: 'Home',
          // 主页不需要组件，MainLayout 会显示默认内容
          component: null
        },
        {
          path: 'Order',
          name: 'Order',
          component: Order
        },
        {
          path: 'Dispatch',
          name: 'Dispatch',
          component: Dispatch
        },
        {
          path: 'PlanGantt',
          name: 'PlanGantt',
          component: PlanGantt
        },
        {
          path: 'Report',
          name: 'Report',
          component: Report
        },
        {
          path: 'Notice',
          name: 'Notice',
          component: Notice
        },
        {
          path: 'Abnormal',
          name: 'Abnormal',
          component: Abnormal
        },
        {
          path: 'QualityInput',
          name: 'QualityInput',
          component: QualityInput
        },
        {
          path: 'Trace',
          name: 'Trace',
          component: Trace
        },
        {
          path: 'Defect',
          name: 'Defect',
          component: Defect
        },
        {
          path: 'EQPState',
          name: 'EQPState',
          component: EQPState
        },
        {
          path: 'Maintain',
          name: 'Maintain',
          component: Maintain
        },       
        {
          path: 'EQPBook',
          name: 'EQPBook',
          component: EQPBook
        },       
        {
          path: 'Dashboard',
          name: 'Dashboard',
          component: Dashboard
        },        
        {
          path: 'Trend',
          name: 'Trend',
          component: Trend
        },        
        {
          path: 'DeptCompare',
          name: 'DeptCompare',
          component: DeptCompare
        },        
        {
          path: 'UserManage',
          name: 'UserManage',
          component: UserManage
        },
      ]
    }
  ]
})

export default router