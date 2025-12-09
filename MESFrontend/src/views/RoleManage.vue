<template>
  <div class="page">
    <!-- 搜索区域 -->
    <el-form :inline="true" :model="searchForm" class="search-box">
      <div class="search-left">
        <el-form-item label="角色名称">
          <el-input v-model="searchForm.name" placeholder="输入角色名称" />
        </el-form-item>
      </div>

      <div class="search-right">
        <el-button type="primary" @click="getListWithResetPage">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
        <el-button type="success" @click="openAddDialog">新增角色</el-button>
      </div>
    </el-form>

    <!-- 角色列表 -->
    <el-table :data="tableData" border style="width: 100%">
      <el-table-column prop="roleId" label="ID" width="80" />
      <el-table-column prop="roleName" label="角色名" />
      <el-table-column prop="roleDesc" label="备注" />
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column label="操作" width="220">
        <template #default="scope">
          <el-button type="primary" size="small" @click="openEditDialog(scope.row)">编辑</el-button>
          <el-button type="danger" size="small" @click="deleteRole(scope.row.roleId)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      v-model:current-page="page"
      :page-size="pageSize"
      :total="total"
      layout="prev, pager, next"
      @current-change="handlePageChange"
    />

    <!-- 新增/编辑 弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="form" label-width="90px">
        <el-form-item label="角色名称">
          <el-input v-model="form.roleName" />
        </el-form-item>

        <el-form-item label="备注">
          <el-input v-model="form.roleDesc" type="textarea" />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import {
  apiGetRoleList,
  apiAddRole,
  apiUpdateRole,
  apiDeleteRole
} from "@/api/role";

import { ElMessage, ElMessageBox } from "element-plus";

const searchForm = ref({ name: "" });

const page = ref(1);
const pageSize = 10;
const total = ref(0);
const tableData = ref([]);

const dialogVisible = ref(false);
const dialogTitle = ref("新增角色");

// 表单数据（适配你的后端字段）
const form = ref({
  roleId: null,
  roleName: "",
  roleDesc: ""
});

// ---------------------
//  加载角色列表（无分页，本地分页）
// ---------------------
const getList = async () => {
  try {
    const res = await apiGetRoleList();

    // 后端返回数组： [{ roleId, roleName, roleDesc }, ...]
    let list = res || [];

    // 搜索功能（前端匹配）
    if (searchForm.value.name) {
      list = list.filter(item =>
        item.roleName.includes(searchForm.value.name)
      );
    }

    // 分页
    total.value = list.length;
    const start = (page.value - 1) * pageSize;
    tableData.value = list.slice(start, start + pageSize);

  } catch (err) {
    console.error("获取角色失败", err);
    ElMessage.error("获取角色失败");
  }
};

const getListWithResetPage = () => {
  page.value = 1;
  getList();
};

const resetSearch = () => {
  searchForm.value.name = "";
  page.value = 1;
  getList();
};

const handlePageChange = (p) => {
  page.value = p;
  getList();
};

// 新增角色
const openAddDialog = () => {
  dialogTitle.value = "新增角色";
  form.value = { roleId: null, roleName: "", roleDesc: "" };
  dialogVisible.value = true;
};

// 编辑角色
const openEditDialog = (row) => {
  dialogTitle.value = "编辑角色";
  form.value = JSON.parse(JSON.stringify(row));
  dialogVisible.value = true;
};

// 保存角色
const submitForm = async () => {
  if (form.value.roleId) {
    await apiUpdateRole(form.value);
    ElMessage.success("更新成功");
  } else {
    await apiAddRole(form.value);
    ElMessage.success("新增成功");
  }
  dialogVisible.value = false;
  getList();
};

// 删除角色
const deleteRole = (id) => {
  ElMessageBox.confirm("确定删除该角色吗？", "提示", { type: "warning" })
    .then(async () => {
      await apiDeleteRole(id);
      ElMessage.success("删除成功");
      getList();
    })
    .catch(() => {});
};

onMounted(getList);
</script>

<style scoped>
.page {
  background: #f4f6f9;
  min-height: 100vh;
  padding: 20px;
  color: #2d2d2d;
}

.search-box {
  background: #ffffff;
  padding: 12px 20px;
  border: 1px solid #dcdcdc;
  margin-bottom: 20px;
  border-radius: 0;
}

.search-box :deep(.el-input__wrapper) {
  border-radius: 0 !important;
  box-shadow: none !important;
  border: 1px solid #c0c4cc !important;
  background: #fff !important;
}

.el-table {
  background: #fff !important;
  border: 1px solid #dcdcdc;
  border-radius: 0;
}

:deep(.el-table th) {
  background: #f0f0f0 !important;
  color: #333 !important;
  font-weight: 600;
  border-bottom: 1px solid #dcdcdc !important;
}

:deep(.el-table td) {
  background: #fff !important;
  border-color: #e5e5e5 !important;
  color: #333;
}

.el-pagination {
  margin-top: 15px;
  background: #fff;
  border: 1px solid #dcdcdc;
  padding: 10px;
  border-radius: 0;
}

:deep(.el-button) {
  border-radius: 0 !important;
}

:deep(.el-dialog) {
  border-radius: 0 !important;
}
</style>
