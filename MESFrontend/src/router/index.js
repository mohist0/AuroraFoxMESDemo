import { createRouter, createWebHistory } from 'vue-router'
import LoginForm from '../components/LoginForm.vue'
import Order from '../views/ProductionPlan/Order.vue'

const routes = [
  { path: '/', name: 'Login', component: LoginForm },   
  { path: '/order', name: 'Order', component: Order, meta: { auth: true } }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

/* 登录态拦截 */
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.meta.auth && !token) return next('/login')   // 需要登录却没登录
  if (to.meta.guest && token) return next('/')        // 已登录就别再进登录页
  next()
})

export default router