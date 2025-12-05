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
    port: 8080, // 可选：设置启动端口
    open: true // 可选：自动打开浏览器
  }
});