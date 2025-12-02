import { createApp } from 'vue'
import { createPinia } from 'pinia'
import { createI18n } from 'vue-i18n'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import './style.css' 
import App from './App.vue'
import router from './router'

// 创建 Vue 应用实例
const app = createApp(App)

// 初始化 Pinia 状态管理
const pinia = createPinia()

// 初始化 Vue I18n 多语言支持
const i18n = createI18n({
  legacy: false, // 使用 Composition API 模式
  locale: 'zh-CN', // 默认语言
  fallbackLocale: 'en', // 回退语言
  messages: {
    // 语言包配置
  }
})

// 使用各种插件
app.use(pinia)
app.use(router)
app.use(ElementPlus)
app.use(i18n)

// 注册所有 Element Plus 图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// 全局配置 Axios（可选）
// 通常建议在具体的 store 或组件中导入使用，而不是全局挂载
// 但如果需要全局挂载，可以这样：
// import axios from 'axios'
// app.config.globalProperties.$axios = axios

// 全局配置 ECharts（可选）
// import * as echarts from 'echarts'
// app.config.globalProperties.$echarts = echarts

// 挂载应用
app.mount('#app')
