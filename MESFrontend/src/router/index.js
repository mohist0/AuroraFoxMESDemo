import { createRouter, createWebHistory } from 'vue-router'
import Order from '../views/ProductionPlan/Order.vue'   // 路径别写错

const routes = [
  {
    path: '/order',
    name: 'Order',
    component: Order
  }

]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router