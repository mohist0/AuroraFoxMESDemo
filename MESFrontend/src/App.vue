<template>
  <!-- 唯一核心：渲染路由匹配的组件（订单页面） -->
  <router-view />
</template>

<script setup>
// 无需额外逻辑，仅承载路由渲染
</script>

<style>
/* 全局样式：消除默认边距，适配页面背景 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", Arial, sans-serif;
  background-color: #f5f7fa;
}

/* 适配Element Plus组件样式 */
:root {
  --el-color-primary: #409EFF;
  --el-color-success: #67C23A;
  --el-color-warning: #E6A23C;
  --el-color-danger: #F56C6C;
  --el-color-info: #909399;
}
</style>