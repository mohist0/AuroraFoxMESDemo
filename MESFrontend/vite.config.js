// vite.config.js
import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';
import path from 'path';

export default defineConfig({
  plugins: [vue()],
  resolve: {
    // 配置路径别名，让 @ 指向 src 目录
    alias: {
      '@': path.resolve(__dirname, 'src')
    }
  },
  server: {
     proxy: {
      // 匹配/api开头的请求，转发到后端服务
      '/api': {
        target: 'http://localhost:5173', // 后端服务地址（端口与后端一致）
        changeOrigin: true, // 开启跨域
        rewrite: (path) => path.replace(/^\/api/, '') // 可选：去掉/api前缀（若后端接口无/api）
      }
  }
  }});