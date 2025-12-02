<template>
  <el-container class="layout-container">
    <!-- 顶部栏 -->
    <el-header class="top-header">
      <div class="header-content">
        <div class="logo">
          <el-icon class="logo-icon">
            <Setting />
          </el-icon>
          <span>Smart MES</span>
        </div>

        <!-- 用户区域 -->
        <div class="user-area">
          <el-avatar :size="30" :src="avatarUrl" class="user-avatar">
            {{ userName.charAt(0) }}
          </el-avatar>

          <el-dropdown trigger="click" @command="handleUserCommand">
            <span class="user-name">
              {{ userName }}
              <el-icon><arrow-down /></el-icon>
            </span>

            <template #dropdown>
              <el-dropdown-menu>
                <!-- 个人中心标题 - 可点击展开/收起 -->
                <div 
                  class="dropdown-title" 
                  :class="{ active: showProfileSubmenu }"
                  @click.stop="toggleProfileSubmenu"
                >
                  <div class="title-content">
                    <el-icon><User /></el-icon>
                    <span>个人中心</span>
                  </div>
                  <el-icon class="dropdown-arrow" :class="{ rotated: showProfileSubmenu }">
                    <arrow-down />
                  </el-icon>
                </div>

                <!-- 子菜单 -->
                <template v-if="showProfileSubmenu">
                  <el-dropdown-item 
                    command="user-manage" 
                    class="submenu-item"
                    :class="{ active: activeUserCommand === 'user-manage' }"
                  >
                    <span class="submenu-content">
                      <el-icon><User /></el-icon>
                      <span class="submenu-text">用户管理</span>
                    </span>
                  </el-dropdown-item>

                  <el-dropdown-item 
                    command="role-manage" 
                    class="submenu-item"
                    :class="{ active: activeUserCommand === 'role-manage' }"
                  >
                    <span class="submenu-content">
                      <el-icon><Key /></el-icon>
                      <span class="submenu-text">角色管理</span>
                    </span>
                  </el-dropdown-item>

                  <el-dropdown-item 
                    command="permission-manage" 
                    class="submenu-item"
                    :class="{ active: activeUserCommand === 'permission-manage' }"
                  >
                    <span class="submenu-content">
                      <el-icon><Lock /></el-icon>
                      <span class="submenu-text">权限管理</span>
                    </span>
                  </el-dropdown-item>
                </template>

                <el-dropdown-divider />

                <!-- 设置 -->
                <el-dropdown-item 
                  command="settings"
                  :class="{ active: activeUserCommand === 'settings' }"
                >
                  <el-icon><Setting /></el-icon>
                  <span>设置</span>
                </el-dropdown-item>

                <!-- 注销 -->
                <el-dropdown-item 
                  command="logout" 
                  divided
                  :class="{ active: activeUserCommand === 'logout' }"
                >
                  <el-icon><SwitchButton /></el-icon>
                  <span>注销</span>
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
              :class="{ 'active-group': isGroupActive(index) }"
              :show-arrow="false"
            >
              <template #title>
                <el-icon v-if="group.icon" class="menu-icon">
                  <component :is="group.icon" />
                </el-icon>
                <span :class="{ 'active-group-text': isGroupActive(index) }">{{ group.title }}</span>
                <el-icon class="arrow-icon" :class="{ 
                  'active-arrow': isGroupActive(index),
                  'rotated': openedGroups.includes(`group-${index}`)
                }">
                  <arrow-down />
                </el-icon>
              </template>
              <el-menu-item
                v-for="item in group.children"
                :key="item.route"
                :index="item.route"
                @click="goToPage(item.route, index)"
              >
                <el-icon v-if="item.icon" class="submenu-icon">
                  <component :is="item.icon" />
                </el-icon>
                <span>{{ item.name }}</span>
              </el-menu-item>
            </el-sub-menu>

            <el-menu-item
              v-else
              :index="group.route || `group-${index}`"
              @click="goToPage(group.route)"
            >
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
            <expand v-if="isCollapse" />
            <fold v-else />
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
              :to="item.path ? { path: item.path } : undefined"
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
import { ref, computed, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import {
  Setting,
  User,
  ArrowDown,
  SwitchButton,
  Expand,
  Fold,
  DataBoard,
  TrendCharts,
  PieChart,
  Box,
  Tools,
  Histogram,
  Document,
  List,
  Calendar,
  Warning,
  Monitor,
  Operation,
  Edit,
  Key,
  Lock
} from "@element-plus/icons-vue";

export default {
  name: "MainLayout",
  components: {
    Setting,
    User,
    ArrowDown,
    SwitchButton,
    Expand,
    Fold,
    Key,
    Lock
  },
  setup() {
    const route = useRoute();
    const router = useRouter();
    const showProfileSubmenu = ref(false);
    const activeUserCommand = ref("");

    const userName = ref(localStorage.getItem("userName") || "未登录");
    const avatarUrl = ref("");
    const isCollapse = ref(false);
    const activeMenu = ref("");
    const activeGroupIndex = ref(null);
    const openedGroups = ref([]);

    const breadcrumb = computed(() => {
      const matched = route.matched.filter(item => item.meta && item.meta.title);
      return matched.map(item => ({
        title: item.meta.title,
        path: item.path
      }));
    });

    const menu = ref([
      {
        title: "生产计划",
        icon: DataBoard,
        children: [
          { name: "订单管理", route: "Order", icon: Document },
          { name: "工单管理", route: "Dispatch", icon: List },
          { name: "计划甘特图", route: "PlanGantt", icon: Calendar }
        ]
      },
      {
        title: "生产执行",
        icon: Operation,
        children: [
          { name: "生产报工", route: "Report", icon: Edit },
          { name: "注意事项", route: "Notice", icon: Warning },
          { name: "异常上报", route: "Abnormal", icon: Warning }
        ]
      },
      {
        title: "质量管理",
        icon: TrendCharts,
        children: [
          { name: "质检录入", route: "QualityInput", icon: Edit },
          { name: "品质追溯", route: "Trace", icon: PieChart },
          { name: "不良分析", route: "Defect", icon: Histogram }
        ]
      },
      {
        title: "设备管理",
        icon: Box,
        children: [
          { name: "设备状态", route: "EQPState", icon: Monitor },
          { name: "维护计划", route: "Maintain", icon: Tools },
          { name: "设备台账", route: "EQPBook", icon: Document }
        ]
      },
      {
        title: "数据看板",
        icon: DataBoard,
        children: [
          { name: "总览看板", route: "Dashboard", icon: DataBoard },
          { name: "趋势分析", route: "Trend", icon: TrendCharts },
          { name: "部门/生产班组对比", route: "DeptCompare", icon: PieChart }
        ]
      }
    ]);

    // 切换个人中心子菜单
    const toggleProfileSubmenu = () => {
      showProfileSubmenu.value = !showProfileSubmenu.value;
    };

    const isGroupActive = groupIndex => {
      if (activeGroupIndex.value === groupIndex) return true;
      const group = menu.value[groupIndex];
      if (!group || !group.children) return false;
      return group.children.some(child => child.route === activeMenu.value);
    };

    watch(
      () => route.name,
      newRouteName => {
        activeMenu.value = newRouteName || "";
        for (let i = 0; i < menu.value.length; i++) {
          const group = menu.value[i];
          if (group.children) {
            const child = group.children.find(item => item.route === newRouteName);
            if (child) {
              activeGroupIndex.value = i;
              if (!openedGroups.value.includes(`group-${i}`)) {
                openedGroups.value.push(`group-${i}`);
              }
              break;
            }
          }
        }
      },
      { immediate: true }
    );

    const toggleCollapse = () => {
      isCollapse.value = !isCollapse.value;
    };

    const goToPage = (routeName, groupIndex) => {
      router.push({ name: routeName });
      if (groupIndex !== undefined) {
        activeGroupIndex.value = groupIndex;
        if (!openedGroups.value.includes(`group-${groupIndex}`)) {
          openedGroups.value.push(`group-${groupIndex}`);
        }
      }
    };

    const handleMenuSelect = index => {
      goToPage(index);
      for (let i = 0; i < menu.value.length; i++) {
        const group = menu.value[i];
        if (group.children) {
          const child = group.children.find(item => item.route === index);
          if (child) {
            activeGroupIndex.value = i;
            break;
          }
        }
      }
    };

    const handleUserCommand = command => {
      activeUserCommand.value = command;
      
      switch (command) {
        case "user-manage":
          router.push({ name: "UserManage" });
          break;
        case "role-manage":
          router.push({ name: "RoleManage" });
          break;
        case "permission-manage":
          router.push({ name: "PermissionManage" });
          break;
        case "settings":
          router.push({ name: "Settings" });
          break;
        case "logout":
          handleLogout();
          break;
      }
    };

    const handleLogout = () => {
      localStorage.removeItem("userName");
      localStorage.removeItem("token");
      router.push({ name: "Login" });
    };

    return {
      userName,
      avatarUrl,
      menu,
      isCollapse,
      activeMenu,
      activeGroupIndex,
      activeUserCommand,
      openedGroups,
      breadcrumb,
      showProfileSubmenu,
      isGroupActive,
      toggleCollapse,
      toggleProfileSubmenu,
      goToPage,
      handleMenuSelect,
      handleUserCommand
    };
  }
};
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
  padding: 5px 10px;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.user-name:hover {
  background-color: rgba(255, 255, 255, 0.1);
  color: #409EFF;
}

/* 下拉菜单标题 */
.dropdown-title {
  padding: 12px 16px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  color: #bfcbd9;
  font-size: 14px;
  font-weight: 600;
  border-bottom: 1px solid #2d3a4b;
  background-color: #1a2332;
  cursor: pointer;
  user-select: none;
  transition: all 0.3s;
}

.dropdown-title:hover,
.dropdown-title.active {
  background-color: #2d3a4b !important;
  color: #409EFF !important;
}

.dropdown-title.active .title-content {
  color: #409EFF !important;
}

.title-content {
  display: flex;
  align-items: center;
  gap: 8px;
  transition: color 0.3s;
}

.dropdown-title .el-icon {
  font-size: 16px;
}

/* 下拉箭头 */
.dropdown-arrow {
  font-size: 12px;
  transition: transform 0.3s;
  color: #bfcbd9;
}

.dropdown-title.active .dropdown-arrow {
  color: #409EFF;
}

.dropdown-arrow.rotated {
  transform: rotate(180deg);
}

/* 子菜单项 */
.submenu-item {
  padding-left: 40px !important;
}

.submenu-content {
  display: flex;
  align-items: center;
  gap: 8px;
  width: 100%;
}

.submenu-text {
  font-size: 13px !important;
  color: #bfcbd9;
  transition: color 0.3s;
}

/* 激活状态样式 */
.submenu-item.active .submenu-text,
.el-dropdown-menu__item.active span {
  color: #409EFF !important;
}

.submenu-item.active .el-icon,
.el-dropdown-menu__item.active .el-icon {
  color: #409EFF !important;
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

/* 隐藏 ElementPlus 的默认箭头 */
:deep(.el-sub-menu__title .el-sub-menu__icon-arrow) {
  display: none !important;
}

/* 父菜单箭头图标 */
.arrow-icon {
  margin-left: auto;
  font-size: 12px;
  transition: transform 0.3s;
  color: #bfcbd9;
}

.arrow-icon.rotated {
  transform: rotate(180deg);
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
html,
body {
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

/* 下拉菜单样式 */
.el-dropdown-menu {
  background-color: #1f2d3d !important;
  border: 1px solid #2d3a4b !important;
  min-width: 180px !important;
}

.el-dropdown-menu__item {
  color: #bfcbd9 !important;
  display: flex !important;
  align-items: center !important;
  gap: 8px !important;
  padding: 8px 16px !important;
  font-size: 14px !important;
  transition: all 0.3s !important;
}

.el-dropdown-menu__item:hover {
  background-color: #2d3a4b !important;
}

/* 激活的下拉菜单项 */
.el-dropdown-menu__item.active {
  background-color: #2d3a4b !important;
  color: #409EFF !important;
}

.el-dropdown-menu__item.active:hover {
  background-color: #2d3a4b !important;
}

.el-dropdown-menu__item .el-icon {
  font-size: 16px !important;
  width: 16px !important;
  height: 16px !important;
  transition: color 0.3s;
}

/* 分隔线样式 */
.el-dropdown-divider {
  margin: 6px 0 !important;
  background-color: #2d3a4b !important;
}

/* 主菜单项图标 */
.el-dropdown-menu__item .el-icon {
  font-size: 16px;
}

/* 子菜单项图标 */
.submenu-item .el-icon {
  font-size: 14px !important;
}

/* 面包屑样式 */
.el-breadcrumb__inner,
.el-breadcrumb__separator {
  color: #909399 !important;
}

.el-breadcrumb__item:last-child .el-breadcrumb__inner {
  color: #303133 !important;
  font-weight: bold !important;
}
</style>