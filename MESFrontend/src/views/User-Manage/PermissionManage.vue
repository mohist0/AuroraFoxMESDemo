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
      <el-table :data="filteredPermissionList" class="table">
        <el-table-column prop="permissionId" label="ID" width="120" />
        <el-table-column prop="permissionName" label="权限名称" />
        <el-table-column prop="permissionCode" label="权限编码" />
        <el-table-column prop="permissionRoute" label="对应路由" />
        <el-table-column prop="permissionDesc" label="描述" />
        <el-table-column label="操作" width="160">
          <template #default="scope">
            <el-button size="small" @click="openEditDialog(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="deletePermission(scope.row.permissionId)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 新增 / 编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="450px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="权限名称">
          <el-input v-model="form.permissionName" />
        </el-form-item>

        <el-form-item label="权限编码">
          <el-input v-model="form.permissionCode" />
        </el-form-item>

        <el-form-item label="页面路由">
          <el-input v-model="form.permissionRoute" placeholder="/role or /dashboard" />
        </el-form-item>

        <el-form-item label="描述">
          <el-input v-model="form.permissionDesc" type="textarea" />
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
import {
  getPermissionList,
  addPermission,
  updatePermission,
  deletePermission
} from "../../api/permission";

import { ElMessage, ElMessageBox } from "element-plus";

export default {
  data() {
    return {
      search: "",
      dialogVisible: false,
      dialogTitle: "新增权限",

      form: {
        permissionId: null,
        permissionName: "",
        permissionCode: "",
        permissionRoute: "",
        permissionDesc: ""
      },

      permissionList: [] // ← 后端真实数据
    };
  },

  computed: {
    filteredPermissionList() {
      if (!this.search) return this.permissionList;
      return this.permissionList.filter(item =>
        item.permissionName.includes(this.search)
      );
    }
  },

  methods: {
    /** 加载权限列表 */
    async loadData() {
      try {
        const res = await getPermissionList();
        this.permissionList = res.data || res; // 后端返回 List
      } catch (err) {
        console.error(err);
        ElMessage.error("权限加载失败");
      }
    },

    /** 打开新增弹窗 */
    openAddDialog() {
      this.dialogTitle = "新增权限";
      this.form = {
        permissionId: null,
        permissionName: "",
        permissionCode: "",
        permissionRoute: "",
        permissionDesc: ""
      };
      this.dialogVisible = true;
    },

    /** 打开编辑弹窗 */
    openEditDialog(row) {
      this.dialogTitle = "编辑权限";
      this.form = { ...row };
      this.dialogVisible = true;
    },

    /** 保存（新增 / 编辑） */
   async submit() {
  try {
    if (this.form.id == null) {
      await addPermission(this.form); // POST /permission
      this.$message.success("新增成功");
    } else {
      await updatePermission(this.form); // PUT /permission
      this.$message.success("更新成功");
    }

    this.dialogVisible = false;
    this.loadPermissionList();
  } catch (e) {
    console.error(e);
    this.$message.error("操作失败");
  }
},

    /** 删除权限 */
    deletePermission(id) {
      ElMessageBox.confirm("确认删除该权限吗？", "提示", { type: "warning" })
        .then(async () => {
          await deletePermission(id);
          ElMessage.success("删除成功");
          this.loadData();
        })
        .catch(() => {});
    }
  },

  mounted() {
    this.loadData();
  }
};
</script>

<style scoped>
/* 保留你全部原样式，不做任何修改 */

.permission-page {
  background: #f2f3f5;
  min-height: 100vh;
  padding: 20px;
  color: #333;
}

.title {
  color: #333;
  font-size: 22px;
  font-weight: bold;
  margin-bottom: 20px;
}

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

.table {
  background: #fff;
  padding: 15px;
  border-radius: 6px;
  border: 1px solid #e5e5e5;
}

.table :deep(th) {
  background-color: #fafafa !important;
  color: #333 !important;
  font-weight: bold;
}

.table :deep(td) {
  background-color: #fff !important;
  border-color: #ebeef5 !important;
}

:deep(.el-button:hover) {
  filter: brightness(1.05);
}
</style>
