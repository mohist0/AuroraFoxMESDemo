<template>
  <div class="app-container">
    <!-- 左侧导航栏 -->
    <aside class="app-aside">
      <div class="aside-header">
        <h1 class="logo">生产管理系统</h1>
      </div>
      <nav class="nav-menu">
        <!-- 生产计划模块分组 -->
        <div class="nav-group">
          <h3 class="group-title">生产计划</h3>
          <router-link to="/production-plan/order" class="nav-item">
            <i class="el-icon-menu"></i>
            <span>订单管理</span>
          </router-link>
          <router-link to="/production-plan/create" class="nav-item">
            <i class="el-icon-plus"></i>
            <span>创建订单</span>
          </router-link>
          <router-link to="/production-plan/dispatch" class="nav-item">
            <i class="el-icon-s-data"></i>
            <span>生产排产</span>
          </router-link>
          <router-link to="/production-plan/gantt" class="nav-item">
            <i class="el-icon-timeline"></i>
            <span>计划甘特图</span>
          </router-link>
        </div>
      </nav>
    </aside>

    <!-- 主内容区域（右侧） -->
    <div class="app-main-wrapper">
      <!-- 顶部面包屑（可选，提升体验） -->
      <header class="main-header">
        <el-breadcrumb separator="/" style="margin-left: 20px; line-height: 50px;">
          <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item v-if="$route.meta.title">{{ $route.meta.title }}</el-breadcrumb-item>
        </el-breadcrumb>
      </header>
      <!-- 路由视图 -->
      <main class="app-main">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup>
// 无需额外逻辑
</script>

<style>
/* 全局样式重置 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", Arial, sans-serif;
  background-color: #f5f7fa;
  color: #333;
  overflow: hidden; /* 防止整体滚动 */
}

/* 根容器：左侧导航 + 右侧内容 */
.app-container {
  display: flex;
  height: 100vh;
}

/* 左侧导航栏 */
.app-aside {
  width: 220px;
  background-color: #2f4050;
  color: #fff;
  height: 100vh;
  box-shadow: 2px 0 8px rgba(0,0,0,0.1);
  overflow-y: auto; /* 导航项过多时滚动 */
}

/* 导航栏头部logo */
.aside-header {
  padding: 0 15px;
  height: 60px;
  line-height: 60px;
  border-bottom: 1px solid #293846;
}

.logo {
  font-size: 16px;
  font-weight: 600;
  margin: 0;
  color: #fff;
  text-align: center;
}

/* 导航菜单 */
.nav-menu {
  padding: 10px 0;
}

/* 分组标题 */
.nav-group .group-title {
  font-size: 12px;
  color: #a7b1c2;
  padding: 10px 20px;
  margin: 0;
  font-weight: normal;
}

/* 导航项 */
.nav-item {
  display: flex;
  align-items: center;
  padding: 12px 20px;
  color: #a7b1c2;
  text-decoration: none;
  font-size: 14px;
  transition: all 0.2s;
}

/* 导航项图标 */
.nav-item i {
  margin-right: 10px;
  font-size: 14px;
}

/* 导航项hover/激活态 */
.nav-item:hover,
.nav-item.router-link-active {
  background-color: #1ab394;
  color: #fff;
}

/* 右侧内容容器 */
.app-main-wrapper {
  flex: 1;
  display: flex;
  flex-direction: column;
  height: 100vh;
  overflow-y: auto; /* 内容区滚动 */
}

/* 顶部面包屑栏 */
.main-header {
  height: 50px;
  line-height: 50px;
  background-color: #fff;
  border-bottom: 1px solid #e6e6e6;
}

/* 主内容区 */
.app-main {
  flex: 1;
  padding: 20px;
}

/* Element Plus 全局样式适配 */
:root {
  --el-color-primary: #1ab394;
  --el-color-success: #67C23A;
  --el-color-warning: #E6A23C;
  --el-color-danger: #F56C6C;
  --el-color-info: #909399;
}

/* 表格样式优化 */
.el-table {
  --el-table-empty-text-color: #999;
  border-radius: 4px;
  overflow: hidden;
}
</style>