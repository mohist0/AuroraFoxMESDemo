<template>
  <div class="order-container">
    <!-- 页面标题（与图中一致） -->
    <h2 class="page-title">订单管理</h2>

    <!-- 订单列表模块（合并筛选+表格+分页，去掉创建订单） -->
    <el-card class="order-list-card" shadow="hover">
      <!-- 筛选区域（与图中布局一致：订单号、产品名、状态、日期、搜索/重置/导出） -->
      <div class="filter-bar" style="margin-bottom: 15px; display: flex; align-items: center; gap: 10px;">
        <div class="filter-item">
          <span>订单号</span>
          <el-input v-model="filterOrderId" placeholder="输入订单号" style="width: 180px; margin-left: 5px;" />
        </div>
        <div class="filter-item">
          <span>产品名</span>
          <el-input v-model="filterProductName" placeholder="输入产品名" style="width: 180px; margin-left: 5px;" />
        </div>
        <div class="filter-item">
          <span>状态</span>
          <el-select v-model="filterStatus" placeholder="全部" style="width: 120px; margin-left: 5px;">
            <el-option label="全部" value="" />
            <el-option label="待排程" value="PENDING_SCHEDULE" />
            <el-option label="已排程" value="SCHEDULED" />
            <el-option label="生产中" value="IN_PRODUCTION" />
            <el-option label="已完成" value="COMPLETED" />
          </el-select>
        </div>
        <div class="filter-item" style="margin-left: 10px;">
          <span>日期</span>
          <el-date-picker v-model="filterDateRange" type="daterange" range-separator="~" start-placeholder="开始日期" end-placeholder="结束日期" style="width: 220px; margin-left: 5px;" />
        </div>
        <!-- 按钮区域（与图中一致：搜索、重置、导出Excel） -->
        <div class="filter-buttons" style="margin-left: auto;">
          <el-button type="primary" @click="fetchOrderList">搜索</el-button>
          <el-button type="default" @click="resetFilter" style="margin-left: 5px;">重置</el-button>
          <el-button type="success" @click="exportExcel" style="margin-left: 5px;">导出Excel</el-button>
        </div>
      </div>

      <!-- 订单表格（与图中一致：灰色占位区域，加载时显示） -->
      <el-table
        :data="filteredOrderList"
        border
        style="width: 100%; min-height: 400px;"
        :loading="isLoading"
        stripe
        empty-text="暂无数据"
      >
        <el-table-column prop="orderId" label="订单编号" min-width="120" />
        <el-table-column prop="productId" label="产品编号" min-width="100" />
        <el-table-column prop="productName" label="产品名称" min-width="120" />
        <el-table-column prop="orderQuantity" label="订单数量" min-width="80" align="right" />
        <el-table-column
          prop="orderStatus"
          label="订单状态"
          min-width="100"
          align="center"
        >
          <template #default="scope">
            <el-tag
              :type="statusTagType(scope.row.orderStatus)"
              size="small"
            >
              {{ statusTagName(scope.row.orderStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="deliveryDate" label="交期" min-width="100" />
        <el-table-column prop="createTime" label="创建时间" min-width="180" />
        <el-table-column label="操作" min-width="80" align="center">
          <template #default="scope">
            <el-button
              type="text"
              icon="el-icon-delete"
              @click="handleDeleteOrder(scope.row.orderId)"
              size="small"
              confirm-type="danger"
              v-confirm="{ title: '确定删除该订单吗？' }"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页（与图中一致：Total、每页条数、页码跳转） -->
      <div class="pagination-bar" style="margin-top: 15px; display: flex; align-items: center; justify-content: space-between;">
        <span>Total {{ total }}</span>
        <div style="display: flex; align-items: center; gap: 10px;">
          <el-select v-model="pageSize" placeholder="100/page" style="width: 100px;">
            <el-option label="10/page" value="10" />
            <el-option label="20/page" value="20" />
            <el-option label="50/page" value="50" />
            <el-option label="100/page" value="100" />
          </el-select>
          <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="currentPage"
            :page-size="pageSize"
            layout="prev, pager, next, jumper"
            :total="total"
            style="margin: 0;"
          />
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue';
import axios from 'axios';
import { ElMessage, ElMessageBox } from 'element-plus';
// 若需要导出Excel，需引入对应库（如xlsx）
import * as XLSX from 'xlsx';

// 1. 订单列表数据
const orderList = ref([]);
const isLoading = ref(false);
const currentPage = ref(4); // 与图中“当前页4”一致
const pageSize = ref(100);  // 与图中“100/page”一致
const total = ref(400);     // 与图中“Total 400”一致

// 2. 筛选条件（与图中筛选项对应）
const filterOrderId = ref('');       // 订单号
const filterProductName = ref('');   // 产品名
const filterStatus = ref('');        // 状态
const filterDateRange = ref([]);     // 日期范围

// 3. 状态映射（与后端枚举对应）
const statusMap = {
  PENDING_SCHEDULE: '待排程',
  SCHEDULED: '已排程',
  IN_PRODUCTION: '生产中',
  COMPLETED: '已完成'
};
const statusTagType = (status) => {
  switch (status) {
    case 'PENDING_SCHEDULE': return 'info';
    case 'SCHEDULED': return 'primary';
    case 'IN_PRODUCTION': return 'warning';
    case 'COMPLETED': return 'success';
    default: return '';
  }
};
const statusTagName = (status) => statusMap[status] || status;

// 4. 筛选后的订单列表（分页+筛选，与图中逻辑一致）
const filteredOrderList = computed(() => {
  let list = [...orderList.value];
  // 订单号筛选
  if (filterOrderId.value) {
    list = list.filter(item => item.orderId.includes(filterOrderId.value.trim()));
  }
  // 产品名筛选
  if (filterProductName.value) {
    list = list.filter(item => item.productName.includes(filterProductName.value.trim()));
  }
  // 状态筛选
  if (filterStatus.value) {
    list = list.filter(item => item.orderStatus === filterStatus.value);
  }
  // 日期范围筛选
  if (filterDateRange.value.length > 0) {
    const startDate = new Date(filterDateRange.value[0]);
    const endDate = new Date(filterDateRange.value[1]);
    list = list.filter(item => {
      const createDate = new Date(item.createTime);
      return createDate >= startDate && createDate <= endDate;
    });
  }
  // 分页（与图中逻辑一致）
  total.value = list.length;
  return list.slice((currentPage.value - 1) * pageSize.value, currentPage.value * pageSize.value);
});

// 5. 接口请求：获取所有订单
const fetchOrderList = async () => {
  isLoading.value = true;
  try {
    // 实际请求时可携带筛选参数
    const res = await axios.get('/api/productionorder/getAllProductionOrders', {
      params: {
        orderId: filterOrderId.value,
        productName: filterProductName.value,
        status: filterStatus.value,
        startDate: filterDateRange.value[0] || '',
        endDate: filterDateRange.value[1] || ''
      }
    });
    orderList.value = res.data;
    // 若后端返回总数，可直接赋值total
    // total.value = res.data.total;
  } catch (err) {
    ElMessage.error('获取订单列表失败');
    console.error(err);
  } finally {
    isLoading.value = false;
  }
};

// 6. 接口请求：删除订单
const handleDeleteOrder = async (orderId) => {
  try {
    await axios.delete(`/api/productionorder/deleteProductionOrder/${orderId}`);
    ElMessage.success('订单删除成功！');
    fetchOrderList(); // 删除后刷新列表
  } catch (err) {
    ElMessage.error('删除订单失败');
    console.error(err);
  }
};

// 7. 导出Excel功能（与图中“导出Excel”按钮对应）
const exportExcel = () => {
  if (filteredOrderList.value.length === 0) {
    ElMessage.warning('暂无数据可导出');
    return;
  }
  // 将表格数据转为Excel格式
  const ws = XLSX.utils.json_to_sheet(filteredOrderList.value);
  const wb = XLSX.utils.book_new();
  XLSX.utils.book_append_sheet(wb, ws, '订单列表');
  // 下载Excel文件
  XLSX.writeFile(wb, `订单列表_${new Date().getTime()}.xlsx`);
};

// 8. 重置筛选条件
const resetFilter = () => {
  filterOrderId.value = '';
  filterProductName.value = '';
  filterStatus.value = '';
  filterDateRange.value = [];
  currentPage.value = 1;
  fetchOrderList();
};

// 9. 分页事件
const handleSizeChange = (val) => {
  pageSize.value = val;
  currentPage.value = 1;
};
const handleCurrentChange = (val) => {
  currentPage.value = val;
};

// 10. 页面初始化：加载订单列表（与图中初始状态一致）
onMounted(() => {
  fetchOrderList();
});
</script>

<style scoped>
.order-container {
  padding: 20px;
  background-color: #fff; /* 与图中背景一致 */
}
.page-title {
  margin: 0 0 20px 0;
  font-weight: 600;
  font-size: 18px;
}
.order-list-card {
  border-radius: 8px;
  border: 1px solid #e6e6e6;
}
.filter-bar {
  flex-wrap: wrap;
  padding: 10px 0;
}
.filter-item {
  display: flex;
  align-items: center;
}
/* 适配图中分页样式 */
.pagination-bar {
  font-size: 14px;
  color: #666;
}
</style>