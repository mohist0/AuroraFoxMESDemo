<template>
  <el-container class="layout-container">
    <!-- 顶部栏 -->
    <el-header class="top-header">
      <div class="header-content">
        <div class="logo">
          <el-icon class="logo-icon"><Setting /></el-icon>
          <span>Smart MES</span>
        </div>

        <div class="user-area" @click="goToUser">
          <el-avatar :size="30" :src="avatarUrl" class="user-avatar">
            {{ userName.charAt(0) }}
          </el-avatar>
          <el-dropdown trigger="click" @command="handleUserCommand">
            <span class="user-name">
              {{ userName }}
              <el-icon><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">
                  <el-icon><User /></el-icon>个人中心
                  
                </el-dropdown-item>
                
                <el-dropdown-item command="settings">
                  <el-icon><Setting /></el-icon>设置
                </el-dropdown-item>
                <el-dropdown-item divided command="logout">
                  <el-icon><SwitchButton /></el-icon>注销
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </el-header>

    <el-container class="main-wrapper">
      <!-- 左侧折叠菜单 -->
      <el-aside class="sidebar">
        <el-menu
          :default-active="activeMenu"
          :default-openeds="openedGroups"
          class="el-menu-vertical"
          background-color="#1f2d3d"
          text-color="#bfcbd9"
          active-text-color="#409EFF"
          :collapse="isCollapse"
          @select="handleMenuSelect"
        >
          <template v-for="(group, index) in menu" :key="group.title">
            <el-sub-menu 
              v-if="group.children && group.children.length > 0"
              :index="`group-${index}`" 
            >
              <template #title>
                <el-icon v-if="group.icon" class="menu-icon">
                  <component :is="group.icon" />
                </el-icon>
                <span>{{ group.title }}</span>
              </template>

              <el-menu-item 
                v-for="item in group.children" 
                :key="item.route" 
                :index="item.route"
              >
                <el-icon v-if="item.icon" class="submenu-icon">
                  <component :is="item.icon" />
                </el-icon>
                <span>{{ item.name }}</span>
              </el-menu-item>
            </el-sub-menu>

            <el-menu-item v-else :index="group.route || `group-${index}`">
              <el-icon v-if="group.icon" class="menu-icon">
                <component :is="group.icon" />
              </el-icon>
              <span>{{ group.title }}</span>
            </el-menu-item>
          </template>
        </el-menu>

        <!-- 折叠按钮 -->
        <div class="collapse-btn" @click="toggleCollapse">
          <el-icon :class="{ 'rotate-icon': isCollapse }">
            <Expand v-if="isCollapse" />
            <Fold v-else />
          </el-icon>
        </div>
      </el-aside>

      <!-- 右侧内容区 -->
      <el-main class="content-area">
        <div class="content-container">
          <!-- 面包屑导航 -->
          <el-breadcrumb separator="/" class="breadcrumb" v-if="breadcrumb.length > 0">
            <el-breadcrumb-item 
              v-for="(item, index) in breadcrumb" 
              :key="index"
              :to="index < breadcrumb.length - 1 ? { path: item.path } : undefined"
            >
              {{ item.title }}
            </el-breadcrumb-item>
          </el-breadcrumb>
          
          <!-- 页面内容 -->
          <div class="page-content">
            <router-view v-slot="{ Component }">
              <transition name="fade-transform" mode="out-in">
                <component :is="Component" />
              </transition>
            </router-view>
          </div>
        </div>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import { ref, computed, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import * as Icons from '@element-plus/icons-vue'

export default {
  name: "MainLayout",
  components: {
    ...Icons
  },
  setup() {
    const route = useRoute()
    const router = useRouter()

    const userName = ref(localStorage.getItem("userName") || "未登录")
    const avatarUrl = ref('')
    const isCollapse = ref(false)
    const activeMenu = ref('')
    const activeGroupIndex = ref(null)
    const openedGroups = ref([])

    const breadcrumb = computed(() => {
      const matched = route.matched.filter(item => item.meta && item.meta.title)
      return matched.map(item => ({ title: item.meta.title, path: item.path }))
    })

    const menu = ref([
      {
        title: "生产计划",
        icon: Icons.DataBoard,
        children: [
          { name: "订单管理", route: "Order", icon: Icons.Document },
          { name: "工单管理", route: "Dispatch", icon: Icons.List },
          { name: "计划甘特图", route: "PlanGantt", icon: Icons.Calendar }
        ]
      },
      {
        title: "生产执行",
        icon: Icons.Operation,
        children: [
          { name: "生产报工", route: "Report", icon: Icons.Edit },
          { name: "注意事项", route: "Notice", icon: Icons.Warning },
          { name: "异常上报", route: "Abnormal", icon: Icons.Warning }
        ]
      },
      {
        title: "质量管理",
        icon: Icons.TrendCharts,
        children: [
          { name: "质检录入", route: "QualityInput", icon: Icons.Edit },
          { name: "品质追溯", route: "Trace", icon: Icons.PieChart },
          { name: "不良分析", route: "Defect", icon: Icons.Histogram }
        ]
      },
      {
        title: "设备管理",
        icon: Icons.Box,
        children: [
          { name: "设备状态", route: "EQPState", icon: Icons.Monitor },
          { name: "维护计划", route: "Maintain", icon: Icons.Tools },
          { name: "设备台账", route: "EQPBook", icon: Icons.Document }
        ]
      },
      {
        title: "数据看板",
        icon: Icons.DataBoard,
        children: [
          { name: "总览看板", route: "Dashboard", icon: Icons.DataBoard },
          { name: "趋势分析", route: "Trend", icon: Icons.TrendCharts },
          { name: "部门/生产班组对比", route: "DeptCompare", icon: Icons.PieChart }
        ]
      }
    ])

    watch(() => route.name, (newRouteName) => {
      activeMenu.value = newRouteName || ''
      menu.value.forEach((group, i) => {
        if (group.children && group.children.some(c => c.route === newRouteName)) {
          activeGroupIndex.value = i
          const groupKey = `group-${i}`
          if (!openedGroups.value.includes(groupKey)) openedGroups.value.push(groupKey)
        }
      })
    }, { immediate: true })

    const toggleCollapse = () => { isCollapse.value = !isCollapse.value }
    const goToPage = (routeName) => { router.push({ name: routeName }) }
    const handleMenuSelect = (index) => { goToPage(index) }
    const goToUser = () => { router.push({ name: "UserManage" }) }

    const handleUserCommand = (command) => {
      switch(command) {
        case 'profile': router.push({ name: 'UserProfile' }); break
        case 'settings': router.push({ name: 'Settings' }); break
        case 'logout': handleLogout(); break
      }
    }
    const handleLogout = () => {
      localStorage.removeItem('userName')
      localStorage.removeItem('token')
      router.push({ name: 'Login' })
    }

    return {
      userName, avatarUrl, menu, isCollapse, activeMenu, openedGroups,
      breadcrumb, toggleCollapse, goToPage, handleMenuSelect,
      goToUser, handleUserCommand
    }
  }
}
</script>

<style scoped>
/* 布局容器 */
.layout-container {
  height: 100vh;
  width: 100vw;
  overflow: hidden;
}

/* 顶部栏 */
.top-header {
  height: 60px;
  background: linear-gradient(135deg, #1f2d3d 0%, #324057 100%);
  border-bottom: 1px solid #2d3a4b;
  padding: 0;
  display: flex;
  align-items: center;
}

.header-content {
  width: 100%;
  padding: 0 25px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.logo {
  display: flex;
  align-items: center;
  gap: 12px;
  color: white;
  font-size: 20px;
  font-weight: bold;
  cursor: pointer;
}

.logo-icon {
  font-size: 24px;
  color: #409EFF;
}

/* 用户区域 */
.user-area {
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;
  color: white;
}

.user-avatar {
  background-color: #409EFF;
  color: white;
}

.user-name {
  color: white;
  display: flex;
  align-items: center;
  gap: 5px;
}

.user-name:hover {
  color: #409EFF;
}

/* 主体部分 */
.main-wrapper {
  flex: 1;
  overflow: hidden;
}

/* 侧边栏 */
.sidebar {
  width: auto;
  background: #1f2d3d;
  border-right: 1px solid #2d3a4b;
  position: relative;
  display: flex;
  flex-direction: column;
}

.el-menu-vertical {
  border-right: none;
  flex: 1;
  overflow-y: auto;
}

.el-menu-vertical:not(.el-menu--collapse) {
  width: 220px;
}

.menu-icon {
  font-size: 18px;
  margin-right: 8px;
}

.submenu-icon {
  font-size: 16px;
  margin-right: 8px;
}

/* 父菜单箭头图标 */
.arrow-icon {
  margin-left: auto;
  font-size: 12px;
  transition: transform 0.3s;
  color: #bfcbd9;
}

.active-arrow {
  color: #409EFF !important;
}

/* 活跃的父菜单组 */
.active-group :deep(.el-sub-menu__title) {
  background-color: #2d3a4b !important;
}

.active-group-text {
  color: #409EFF !important;
}

/* 折叠按钮 */
.collapse-btn {
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #1a2332;
  border-top: 1px solid #2d3a4b;
  cursor: pointer;
  color: #bfcbd9;
  transition: all 0.3s;
}

.collapse-btn:hover {
  background: #2d3a4b;
  color: white;
}

.collapse-btn .el-icon {
  font-size: 20px;
  transition: transform 0.3s;
}

.rotate-icon {
  transform: rotate(180deg);
}

/* 内容区域 */
.content-area {
  padding: 0;
  background: #f0f2f5;
  overflow: hidden;
}

.content-container {
  height: 100%;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.breadcrumb {
  padding: 16px 20px;
  background: white;
  border-bottom: 1px solid #e6ebf5;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
}

.page-content {
  flex: 1;
  padding: 20px;
  overflow: auto;
  background: white;
  margin: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

/* 路由切换动画 */
.fade-transform-enter-active,
.fade-transform-leave-active {
  transition: all 0.3s;
}

.fade-transform-enter-from {
  opacity: 0;
  transform: translateX(-30px);
}

.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(30px);
}

/* 自定义滚动条 */
.el-menu-vertical::-webkit-scrollbar,
.page-content::-webkit-scrollbar {
  width: 6px;
}

.el-menu-vertical::-webkit-scrollbar-track,
.page-content::-webkit-scrollbar-track {
  background: #1a2332;
}

.el-menu-vertical::-webkit-scrollbar-thumb,
.page-content::-webkit-scrollbar-thumb {
  background: #4a5568;
  border-radius: 3px;
}

.el-menu-vertical::-webkit-scrollbar-thumb:hover,
.page-content::-webkit-scrollbar-thumb:hover {
  background: #5a6578;
}
</style>

<!-- 全局样式覆盖 -->
<style>
/* 修复 ElementPlus 可能造成的全局样式问题 */
html, body {
  margin: 0 !important;
  padding: 0 !important;
  width: 100vw !important;
  height: 100vh !important;
  overflow: hidden !important;
}

/* 自定义 ElMenu 样式 */
.el-menu-item,
.el-sub-menu__title {
  height: 50px !important;
  line-height: 50px !important;
}

.el-menu-item.is-active {
  background-color: #2d3a4b !important;
}

.el-menu-item:hover,
.el-sub-menu__title:hover {
  background-color: #2d3a4b !important;
}

/* 箭头旋转动画 */
.el-sub-menu.is-opened .el-sub-menu__title .arrow-icon {
  transform: rotate(180deg);
}

/* 下拉菜单样式 */
.el-dropdown-menu {
  background-color: #1f2d3d !important;
  border: 1px solid #2d3a4b !important;
}

.el-dropdown-menu__item {
  color: #bfcbd9 !important;
}

.el-dropdown-menu__item:hover {
  background-color: #2d3a4b !important;
  color: white !important;
}

.el-dropdown-menu__item .el-icon {
  margin-right: 8px;
}

/* 面包屑样式 */
.el-breadcrumb__inner,
.el-breadcrumb__separator {
  color: #909399 !important;
}

.el-breadcrumb__item:last-child .el-breadcrumb__inner {
  color: #303133 !important;
  font-weight: bold;
}
</style>