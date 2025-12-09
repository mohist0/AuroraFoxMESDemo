import { createRouter, createWebHistory } from 'vue-router'
import LoginForm from '../components/LoginForm.vue'
import MainLayout from '../components/MainLayout.vue'
import Order from '../views/ProductionPlan/Order.vue'

const routes = [
  // 1. 登录页
  { path: '/login', name: 'Login', component: LoginForm, meta: { guest: true } },

  // 2. 带布局的父路由，重定向到默认子页
  {
    path: '/',
    component: MainLayout,
    redirect: '/order',
    meta: { auth: true },
    children: [
      { path: 'order', name: 'Order', component: Order }
      // 以后其他业务页继续往 children 里加
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

/* 登录态拦截 */
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.meta.auth && !token) return next('/login')
  if (to.meta.guest && token) return next('/')
  next()
})

export default router