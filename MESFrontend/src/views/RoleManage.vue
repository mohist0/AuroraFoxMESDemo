<template>
  <div class="page">

    <!-- 搜索区域 -->
    <el-form :inline="true" :model="searchForm" class="search-box">
      <el-form-item label="角色名称">
        <el-input v-model="searchForm.name" placeholder="输入角色名称" />
      </el-form-item>

      <el-button type="primary" @click="getList">查询</el-button>
      <el-button @click="resetSearch">重置</el-button>
      <el-button type="success" @click="openAddDialog">新增角色</el-button>
    </el-form>

    <!-- 表格 -->
    <el-table :data="tableData" border style="width: 100%">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="角色名" />
      <el-table-column prop="remarks" label="备注" />

      <!-- 操作 -->
      <el-table-column label="操作" width="220">
        <template #default="scope">
          <el-button type="primary" size="small" @click="openEditDialog(scope.row.id)">编辑</el-button>
          <el-button type="danger" size="small" @click="deleteRole(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      v-model:current-page="page"
      :page-size="pageSize"
      :total="total"
      layout="prev, pager, next"
      @current-change="getList"
    />

    <!-- 新增/编辑 弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">

      <el-form :model="form" label-width="90px">
        <el-form-item label="角色名称">
          <el-input v-model="form.name" />
        </el-form-item>

        <el-form-item label="备注">
          <el-input v-model="form.remarks" type="textarea" />
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
  apiGetRoleById,
  apiAddRole,
  apiUpdateRole,
  apiDeleteRole,
} from "@/api/role";
import { ElMessage, ElMessageBox } from "element-plus";

const searchForm = ref({
  name: ""
});

const page = ref(1);
const pageSize = 10;
const total = ref(0);

const tableData = ref([]);

// 获取角色列表
const getList = async () => {
  const res = await apiGetRoleList({
    page: page.value,
    pageSize,
    name: searchForm.value.name
  });

  const data = res?.data || {};

  tableData.value = data.records || [];
  total.value = data.total || 0;
};


// 重置
const resetSearch = () => {
  searchForm.value.name = "";
  getList();
};

// --------------------- 新增 / 编辑 ------------------------
const dialogVisible = ref(false);
const dialogTitle = ref("新增角色");

const form = ref({
  id: null,
  name: "",
  remarks: ""
});

// 打开新增窗口
const openAddDialog = () => {
  dialogTitle.value = "新增角色";
  form.value = { id: null, name: "", remarks: "" };
  dialogVisible.value = true;
};

// 打开编辑窗口（获取单个角色信息）
const openEditDialog = async (id) => {
  dialogTitle.value = "编辑角色";
  dialogVisible.value = true;

  const res = await apiGetRoleById(id);
  form.value = { ...res.data };
};

// 提交表单
const submitForm = async () => {
  if (form.value.id) {
    // 编辑
    await apiUpdateRole(form.value);
    ElMessage.success("更新成功");
  } else {
    // 新增
    await apiAddRole(form.value);
    ElMessage.success("新增成功");
  }

  dialogVisible.value = false;
  getList();
};

// --------------------- 删除 ------------------------
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
/* ==================== 后台管理经典工业风（无动画） ==================== */

/* 页面背景 */
.page {
  background: #f4f6f9;
  min-height: 100vh;
  padding: 20px;
  color: #2d2d2d;
}

/* 顶部搜索区域 */
.search-box {
  background: #ffffff;
  padding: 12px 20px;
  border: 1px solid #dcdcdc;
  margin-bottom: 20px;
  border-radius: 0; /* 无圆角 */
}

/* 输入框统一扁平风格 */
.search-box :deep(.el-input__wrapper) {
  border-radius: 0 !important;
  box-shadow: none !important;
  border: 1px solid #c0c4cc !important;
  background: #fff !important;
}

/* 表格：无动画、无阴影 */
.el-table {
  background: #fff !important;
  border: 1px solid #dcdcdc;
  border-radius: 0;
}

/* 表头颜色 */
:deep(.el-table th) {
  background: #f0f0f0 !important;
  color: #333 !important;
  font-weight: 600;
  border-bottom: 1px solid #dcdcdc !important;
}

/* 表格内容 */
:deep(.el-table td) {
  background: #fff !important;
  border-color: #e5e5e5 !important;
  color: #333;
}

/* 分页区域 */
.el-pagination {
  margin-top: 15px;
  background: #fff;
  border: 1px solid #dcdcdc;
  padding: 10px;
  border-radius: 0;
}

/* 按钮统一扁平 */
:deep(.el-button) {
  border-radius: 0 !important;
}

/* 弹窗扁平化 */
:deep(.el-dialog) {
  border-radius: 0 !important;
}
</style>
