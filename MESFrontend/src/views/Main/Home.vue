<template>
  <div class="common-layout">
    <el-container>
      <!-- 顶部栏 -->
      <el-header class="top-header">
        <div class="header-content">
          <!-- 系统Logo -->
          <div class="logo">
            <span>Smart MES</span>
          </div>

          <!-- 用户区域 -->
          <div class="user-area">
            <el-avatar :size="30" :src="avatarUrl" class="user-avatar">
              {{ userInitial }}
            </el-avatar>

            <!-- 用户下拉菜单 -->
            <el-dropdown trigger="click" @command="handleUserCommand">
              <span class="user-name">
                {{ userName }}
                <el-icon><ArrowDown /></el-icon>
              </span>

              <template #dropdown>
                <el-dropdown-menu>
                  <!-- 个人中心标题 -->
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
                      <ArrowDown />
                    </el-icon>
                  </div>

                  <!-- 子菜单 -->
                  <template v-if="showProfileSubmenu">
                    <el-dropdown-item command="user-manage">
                      <el-icon><User /></el-icon>
                      用户管理
                    </el-dropdown-item>
                    <el-dropdown-item command="role-manage">
                      <el-icon><Key /></el-icon>
                      角色管理
                    </el-dropdown-item>
                    <el-dropdown-item command="permission-manage">
                      <el-icon><Lock /></el-icon>
                      权限管理
                    </el-dropdown-item>
                  </template>

                  <!-- 分割线 -->
                  <el-dropdown-item disabled divided style="padding: 0; height: 1px; margin: 6px 0;">
                    <div style="border-top: 1px solid #2d3a4b;"></div>
                  </el-dropdown-item>

                  <!-- 设置 -->
                  <el-dropdown-item command="settings">
                    <el-icon><Setting /></el-icon>
                    设置
                  </el-dropdown-item>

                  <!-- 注销 -->
                  <el-dropdown-item command="logout" divided>
                    <el-icon><SwitchButton /></el-icon>
                    注销
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </el-header>

      <!-- 主体内容 -->
      <el-container class="main-wrapper">
        <!-- 左侧折叠菜单 -->
        <el-aside class="sidebar" :width="isCollapse ? '64px' : '200px'">
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
            <!-- 循环遍历菜单数据 -->
            <template v-for="(group, index) in menuData" :key="group.title">
              <!-- 如果有子菜单 -->
              <el-sub-menu
                v-if="group.children && group.children.length > 0"
                :index="`group-${index}`"
                :class="{ 'active-group': isGroupActive(index) }"
              >
                <template #title>
                  <!-- 菜单图标 -->
                  <el-icon v-if="group.icon" class="menu-icon">
                    <component :is="group.icon" />
                  </el-icon>
                  <!-- 菜单标题 -->
                  <span :class="{ 'active-group-text': isGroupActive(index) }">{{ group.title }}</span>
                  <!-- 菜单折叠箭头 -->
                  <el-icon class="arrow-icon" :class="{ 
                    'active-arrow': isGroupActive(index),
                    'rotated': openedGroups.includes(`group-${index}`)
                  }">
                    <ArrowDown />
                  </el-icon>
                </template>
                <!-- 子菜单项 -->
                <el-menu-item
                  v-for="item in group.children"
                  :key="item.route"
                  :index="item.route"
                  @click="goToPage(item.route, index)"
                >
                  <!-- 子菜单图标 -->
                  <el-icon v-if="item.icon" class="submenu-icon">
                    <component :is="item.icon" />
                  </el-icon>
                  <!-- 子菜单名称 -->
                  <span>{{ item.name }}</span>
                </el-menu-item>
              </el-sub-menu>

              <!-- 如果没有子菜单 -->
              <el-menu-item
                v-else
                :index="group.route || `group-${index}`"
                @click="goToPage(group.route)"
              >
                <!-- 菜单图标 -->
                <el-icon v-if="group.icon" class="menu-icon">
                  <component :is="group.icon" />
                </el-icon>
                <!-- 菜单名称 -->
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
                :to="item.path ? { path: '/' + item.path } : undefined"
              >
                {{ item.title }}
              </el-breadcrumb-item>
            </el-breadcrumb>

            <!-- 页面内容 - 必须有 router-view -->
            <div class="page-content">
              <router-view />
            </div>
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import { ref, computed, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useUserStore } from "../../stores";
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
  name: "Home",
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
    const userStore = useUserStore();

    // 控制个人中心子菜单的显示
    const showProfileSubmenu = ref(false);

    // 用户名称
    const userName = computed(() => {
      return userStore.username || localStorage.getItem("username") || "未登录";
    });

    // 用户名称的首字母
    const userInitial = computed(() => {
      return userName.value.charAt(0).toUpperCase();
    });

    // 用户头像URL
    const avatarUrl = ref("");

    // 控制侧边栏的折叠状态
    const isCollapse = ref(false);

    // 当前激活的菜单项
    const activeMenu = ref("");

    // 当前激活的菜单组索引
    const activeGroupIndex = ref(null);

    // 默认打开的菜单组
    const openedGroups = ref([]);

    // 面包屑导航数据
    const breadcrumb = computed(() => {
      const matched = route.matched.filter(item => item.meta && item.meta.title);
      return matched.map(item => ({
        title: item.meta.title,
        path: item.path
      }));
    });

    // 菜单数据
    const menuData = ref([
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
          { name: "部门对比", route: "DeptCompare", icon: PieChart }
        ]
      },
      {
        title: "用户管理",
        icon: Setting,
        children: [
          { name: "用户管理", route: "UserManage", icon: User },
          { name: "角色管理", route: "RoleManage", icon: Key },
          { name: "权限管理", route: "PermissionManage", icon: Lock }
        ]
      }
    ]);

    // 切换个人中心子菜单的显示状态
    const toggleProfileSubmenu = () => {
      showProfileSubmenu.value = !showProfileSubmenu.value;
    };

    // 判断菜单组是否激活
    const isGroupActive = (groupIndex) => {
      if (activeGroupIndex.value === groupIndex) return true;
      const group = menuData.value[groupIndex];
      if (!group || !group.children) return false;
      return group.children.some(child => child.route === activeMenu.value);
    };

    // 监听路由变化，更新激活的菜单项和菜单组
    watch(
      () => route.name,
      (newRouteName) => {
        activeMenu.value = newRouteName || "";
        let found = false;
        for (let i = 0; i < menuData.value.length; i++) {
          const group = menuData.value[i];
          if (group.children) {
            const child = group.children.find(item => item.route === newRouteName);
            if (child) {
              activeGroupIndex.value = i;
              if (!openedGroups.value.includes(`group-${i}`)) {
                openedGroups.value.push(`group-${i}`);
              }
              found = true;
              break;
            }
          }
        }
        if (!found) {
          activeGroupIndex.value = null;
        }
      },
      { immediate: true }
    );

    // 切换侧边栏的折叠状态
    const toggleCollapse = () => {
      isCollapse.value = !isCollapse.value;
    };

    // 跳转到指定页面
    const goToPage = (routeName, groupIndex) => {
      if (!routeName) return;
      router.push({ name: routeName });
      if (groupIndex !== undefined) {
        activeGroupIndex.value = groupIndex;
        if (!openedGroups.value.includes(`group-${groupIndex}`)) {
          openedGroups.value.push(`group-${groupIndex}`);
        }
      }
    };

    // 处理菜单项选择
    const handleMenuSelect = (index) => {
      goToPage(index);
      for (let i = 0; i < menuData.value.length; i++) {
        const group = menuData.value[i];
        if (group.children) {
          const child = group.children.find(item => item.route === index);
          if (child) {
            activeGroupIndex.value = i;
            break;
          }
        }
      }
    };

    // 处理用户命令（下拉菜单项点击）
    const handleUserCommand = async (command) => {
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
          router.push({ name: "Dashboard" });
          break;
        case "logout":
          await userStore.logoutUser();
          router.push({ name: "Login" });
          break;
      }
    };

    return {
      userName,
      userInitial,
      avatarUrl,
      menuData,
      isCollapse,
      activeMenu,
      activeGroupIndex,
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
.common-layout {
  height: 100vh;
  width: 100vw;
  overflow: hidden;
}

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
  font-weight: bold;
}

.user-name {
  color: white;
  display: flex;
  align-items: center;
  gap: 5px;
  padding: 5px 10px;
  border-radius: 4px;
  transition: background-color 0.3s;
  font-weight: 500;
}

.user-name:hover {
  background-color: rgba(255, 255, 255, 0.1);
  color: #409EFF;
}

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

.title-content {
  display: flex;
  align-items: center;
  gap: 8px;
  transition: color 0.3s;
}

.dropdown-arrow {
  font-size: 12px;
  transition: transform 0.3s;
  color: #bfcbd9;
}

.dropdown-arrow.rotated {
  transform: rotate(180deg);
}

.main-wrapper {
  flex: 1;
  overflow: hidden;
  height: 100%;
  
}

.sidebar {
  width: auto;
  background: #1f2d3d;
  border-right: 1px solid #2d3a4b;
  position: relative;
  display: flex;
  flex-direction: column;
  transition: width 0.3s;
}

.el-menu-vertical {
  border-right: none;
  flex: 1;
  overflow-y: auto;
}

.el-menu-vertical:not(.el-menu--collapse) {
  width: 200px;
}

.menu-icon {
  font-size: 18px;
  margin-right: 8px;
}

.submenu-icon {
  font-size: 16px;
  margin-right: 8px;
}

:deep(.el-sub-menu__title .el-sub-menu__icon-arrow) {
  display: none !important;
}

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

.active-group :deep(.el-sub-menu__title) {
  background-color: #2d3a4b !important;
}

.active-group-text {
  color: #409EFF !important;
}

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

.content-area {
  padding: 0;
  background: #f0f2f5;
  overflow: hidden;
  height: 100%;
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

/* 修复 ElementPlus 可能造成的全局样式问题 */
html,
body {
  margin: 0 !important;
  padding: 0 !important;
  width: 100vw !important;
  height: 100vh !important;
  overflow: hidden !important;
}

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

.el-dropdown-menu {
  background-color: #1f2d3d !important;
  border: 1px solid #2d3a4b !important;
  min-width: 180px !important;
}

.el-dropdown-menu__item {
  color: #bfcbd9 !important;
  display: flex !important;
  align-items: center !important;
  gap: 6px !important;
  padding: 8px 16px !important;
  font-size: 14px !important;
  transition: all 0.3s !important;
}

.el-dropdown-menu__item:hover {
  background-color: #2d3a4b !important;
}

.el-dropdown-menu__item .el-icon {
  font-size: 16px !important;
  width: 16px !important;
  height: 16px !important;
  transition: color 0.3s;
}

.el-dropdown-divider {
  margin: 6px 0 !important;
  background-color: #2d3a4b !important;
}

.el-breadcrumb__inner,
.el-breadcrumb__separator {
  color: #909399 !important;
}

.el-breadcrumb__item:last-child .el-breadcrumb__inner {
  color: #303133 !important;
  font-weight: bold !important;
}
</style>