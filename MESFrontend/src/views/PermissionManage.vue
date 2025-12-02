<template>
  <div class="permission-page">
    <div class="card">
      <h2 class="title">界面权限管理</h2>

      <!-- 工具栏 -->
      <div class="toolbar">
        <el-input
          v-model="search"
          placeholder="搜索权限名称"
          class="input"
        />
        <el-button type="primary" @click="openAddDialog">新增权限</el-button>
      </div>

      <!-- 权限列表 -->
      <el-table :data="permissionList" class="table">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="权限名称" />
        <el-table-column prop="code" label="权限编码" />
        <el-table-column prop="route" label="对应路由" />
        <el-table-column label="操作" width="160">
          <template #default="scope">
            <el-button size="small" @click="openEditDialog(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="deletePermission(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 新增 / 编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="450px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="权限名称">
          <el-input v-model="form.name" />
        </el-form-item>

        <el-form-item label="权限编码">
          <el-input v-model="form.code" />
        </el-form-item>

        <el-form-item label="页面路由">
          <el-input v-model="form.route" placeholder="/role or /dashboard" />
        </el-form-item>

        <el-form-item label="描述">
          <el-input v-model="form.desc" type="textarea" />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submit">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      search: "",
      dialogVisible: false,
      dialogTitle: "新增权限",
      form: {
        id: null,
        name: "",
        code: "",
        route: "",
        desc: ""
      },
      permissionList: [
        { id: 1, name: "查看角色页", code: "role:view", route: "/role", desc: "可访问角色管理界面" },
        { id: 2, name: "编辑权限", code: "permission:edit", route: "/permission", desc: "编辑权限信息" }
      ]
    };
  },
  methods: {
    // 打开新增弹窗
    openAddDialog() {
      this.dialogTitle = "新增权限";
      this.form = { id: null, name: "", code: "", route: "", desc: "" };
      this.dialogVisible = true;
    },

    // 打开编辑弹窗
    openEditDialog(row) {
      this.dialogTitle = "编辑权限";
      this.form = { ...row };
      this.dialogVisible = true;
    },

    // 保存（新增/更新）
    submit() {
      if (this.form.id == null) {
        console.log("POST /permission", this.form);
      } else {
        console.log("PUT /permission", this.form);
      }
      this.dialogVisible = false;
    },

    // 删除权限
    deletePermission(id) {
      console.log("DELETE /permission/" + id);
    }
  }
};
</script>


<style scoped>
/* ==================== PC 后台管理通用风格 ==================== */

/* 页面整体背景 */
.permission-page {
  background: #f2f3f5;
  min-height: 100vh;
  padding: 20px;
  color: #333;
}

/* 顶部标题 */
.title {
  color: #333;
  font-size: 22px;
  font-weight: bold;
  margin-bottom: 20px;
}

/* 工具栏布局 */
.toolbar {
  display: flex;
  gap: 15px;
  margin-bottom: 15px;
}

.input :deep(.el-input__wrapper) {
  background: #fff !important;
  border: 1px solid #dcdfe6 !important;
  color: #333 !important;
}

/* ==================== 表格默认风格（白底，自适应宽度） ==================== */

.table {
  background: #fff;
  padding: 15px;
  border-radius: 6px;
  border: 1px solid #e5e5e5;
}

/* 表头浅灰 */
.table :deep(th) {
  background-color: #fafafa !important;
  color: #333 !important;
  font-weight: bold;
}

/* 表格主体 */
.table :deep(td) {
  background-color: #fff !important;
  border-color: #ebeef5 !important;
}

/* ==================== 默认按钮风格即可 ==================== */
:deep(.el-button:hover) {
  filter: brightness(1.05);
}

/* ==================== 弹窗样式默认即可，更符合 PC 系统 ==================== */
</style>

