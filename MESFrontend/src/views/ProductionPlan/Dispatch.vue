<template>
  <div class="work-order-management">
    <!-- 顶部查询与操作区 -->
    <div class="search-bar">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="工单编号">
          <el-input
            v-model="searchForm.work_order_id"
            placeholder="请输入工单编号"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="生产订单编号">
          <el-input
            v-model="searchForm.order_id"
            placeholder="请输入生产订单编号"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="产品编号">
          <el-input
            v-model="searchForm.product_id"
            placeholder="请输入产品编号"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="生产线编号">
          <el-input
            v-model="searchForm.line_id"
            placeholder="请输入生产线编号"
            clearable
            style="width: 180px"
          />
        </el-form-item>
        <el-form-item label="工单状态">
          <el-select
            v-model="searchForm.work_order_status"
            placeholder="请选择状态"
            clearable
            style="width: 180px"
          >
            <el-option label="未排程" value="未排程" />
            <el-option label="处理中" value="处理中" />
            <el-option label="已完成" value="已完成" />
            <el-option label="已取消" value="已取消" />
          </el-select>
        </el-form-item>
        <el-form-item label="计划开工时间">
          <el-date-picker
            v-model="searchForm.planned_start_time_range"
            type="daterange"
            range-separator="至"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DDTHH:mm:ss"
            clearable
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetSearchForm">重置</el-button>
        </el-form-item>
      </el-form>
      <el-button type="primary" icon="el-icon-plus" @click="openAddDialog">新增工单</el-button>
    </div>

    <!-- 工单列表区 -->
    <div class="order-table">
      <el-table
        :data="orderList"
        border
        stripe
        v-loading="loading"
        @selection-change="handleSelectionChange"
        style="width: 100%"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="work_order_id" label="工单编号" width="120" sortable />
        <el-table-column prop="order_id" label="生产订单编号" width="120" />
        <el-table-column prop="product_id" label="产品编号" width="100" />
        <el-table-column prop="quantity" label="生产数量" width="100" />
        <el-table-column prop="scheduled_quantity" label="已排程数量" width="100" />
        <el-table-column prop="completed_quantity" label="已完成数量" width="100" />
        <el-table-column prop="line_id" label="生产线编号" width="100" />
        <el-table-column
          prop="work_order_status"
          label="工单状态"
          width="100"
          :formatter="statusFormatter"
        />
        <el-table-column prop="planned_start_time" label="计划开工时间" width="200" />
        <el-table-column prop="planned_end_time" label="计划完工时间" width="200" />
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button
              type="text"
              icon="el-icon-edit"
              @click="openEditDialog(scope.row)"
            >编辑</el-button>
            <el-button
              type="text"
              icon="el-icon-delete"
              text-color="danger"
              @click="handleDelete(scope.row)"
            >删除</el-button>
            <el-button
              type="text"
              icon="el-icon-view"
              @click="handleView(scope.row)"
            >查看</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页控件 -->
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pagination.pageNum"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pagination.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.total"
        style="margin-top: 20px; text-align: right"
      >
      </el-pagination>
    </div>

    <!-- 新增/编辑工单弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="700px"
      @close="resetForm"
    >
      <el-form
        :model="form"
        :rules="formRules"
        ref="formRef"
        label-width="120px"
        style="max-width: 600px"
      >
        <el-form-item label="工单编号" prop="work_order_id" v-if="form.work_order_id">
          <el-input v-model="form.work_order_id" disabled placeholder="系统自动生成" />
        </el-form-item>
        <el-form-item label="生产订单编号" prop="order_id">
          <el-input v-model="form.order_id" placeholder="请输入生产订单编号" />
        </el-form-item>
        <el-form-item label="产品编号" prop="product_id">
          <el-input v-model="form.product_id" placeholder="请输入产品编号" />
        </el-form-item>
        <el-form-item label="生产数量" prop="quantity">
          <el-input
            v-model.number="form.quantity"
            type="number"
            min="1"
            placeholder="请输入生产数量"
          />
        </el-form-item>
        <el-form-item label="已排程数量" prop="scheduled_quantity">
          <el-input
            v-model.number="form.scheduled_quantity"
            type="number"
            min="0"
            placeholder="请输入已排程数量"
          />
        </el-form-item>
        <el-form-item label="已完成数量" prop="completed_quantity">
          <el-input
            v-model.number="form.completed_quantity"
            type="number"
            min="0"
            placeholder="请输入已完成数量"
          />
        </el-form-item>
        <el-form-item label="生产线编号" prop="line_id">
          <el-input v-model="form.line_id" placeholder="请输入生产线编号" />
        </el-form-item>
        <el-form-item label="工单状态" prop="work_order_status">
          <el-select v-model="form.work_order_status" placeholder="请选择工单状态">
            <el-option label="未排程" value="未排程" />
            <el-option label="处理中" value="处理中" />
            <el-option label="已完成" value="已完成" />
            <el-option label="已取消" value="已取消" />
          </el-select>
        </el-form-item>
        <el-form-item label="计划开工时间" prop="planned_start_time">
          <el-date-picker
            v-model="form.planned_start_time"
            type="datetime"
            placeholder="请选择计划开工时间"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DDTHH:mm:ss"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="计划完工时间" prop="planned_end_time">
          <el-date-picker
            v-model="form.planned_end_time"
            type="datetime"
            placeholder="请选择计划完工时间"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DDTHH:mm:ss"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="form.remark"
            type="textarea"
            placeholder="请输入备注信息"
            rows="3"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确认</el-button>
      </template>
    </el-dialog>

    <!-- 查看工单详情弹窗 -->
    <el-dialog
      v-model="viewDialogVisible"
      title="工单详情"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-descriptions :column="2" border title="工单基本信息">
        <el-descriptions-item label="工单编号">{{ viewForm.work_order_id }}</el-descriptions-item>
        <el-descriptions-item label="生产订单编号">{{ viewForm.order_id }}</el-descriptions-item>
        <el-descriptions-item label="产品编号">{{ viewForm.product_id }}</el-descriptions-item>
        <el-descriptions-item label="生产数量">{{ viewForm.quantity }}</el-descriptions-item>
        <el-descriptions-item label="已排程数量">{{ viewForm.scheduled_quantity }}</el-descriptions-item>
        <el-descriptions-item label="已完成数量">{{ viewForm.completed_quantity }}</el-descriptions-item>
        <el-descriptions-item label="生产线编号">{{ viewForm.line_id }}</el-descriptions-item>
        <el-descriptions-item label="工单状态">
          <el-tag :type="getStatusTagType(viewForm.work_order_status)">{{ viewForm.work_order_status }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="计划开工时间">{{ viewForm.planned_start_time }}</el-descriptions-item>
        <el-descriptions-item label="计划完工时间">{{ viewForm.planned_end_time }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ viewForm.remark || '无' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'

// 后端接口基础路径
const baseUrl = 'http://localhost:8080/api/work-orders'

// Axios全局配置
axios.defaults.withCredentials = true
axios.defaults.headers.post['Content-Type'] = 'application/json;charset=utf-8'
axios.defaults.headers.put['Content-Type'] = 'application/json;charset=utf-8'

// 状态管理
const loading = ref(false)
const selectedOrders = ref([])
const dialogVisible = ref(false)
const viewDialogVisible = ref(false)
const formRef = ref(null)

// 搜索表单
const searchForm = reactive({
  work_order_id: '',
  order_id: '',
  product_id: '',
  line_id: '',
  work_order_status: '',
  planned_start_time_range: []
})

// 分页参数
const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

// 工单列表
const orderList = ref([])

// 新增/编辑表单
const form = reactive({
  work_order_id: '',
  order_id: '',
  product_id: '',
  quantity: 0,
  scheduled_quantity: 0,
  completed_quantity: 0,
  work_order_status: '未排程',
  planned_start_time: '',
  planned_end_time: '',
  line_id: '',
  remark: ''
})

// 查看详情表单
const viewForm = reactive({})
const dialogTitle = ref('新增工单')

// 表单校验规则
const formRules = ref({
  order_id: [{ required: true, message: '请输入生产订单编号', trigger: 'blur' }],
  product_id: [{ required: true, message: '请输入产品编号', trigger: 'blur' }],
  quantity: [
    { required: true, message: '请输入生产数量', trigger: 'blur' },
    { type: 'number', min: 1, message: '生产数量不能小于1', trigger: 'blur' }
  ],
  line_id: [{ required: true, message: '请输入生产线编号', trigger: 'blur' }],
  work_order_status: [{ required: true, message: '请选择工单状态', trigger: 'change' }]
})

// ========== 核心工具方法 ==========
// 时间格式化
const formatDateTime = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')} ${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}:${date.getSeconds().toString().padStart(2, '0')}`
}

// 下划线转驼峰
const toCamelCase = (obj) => {
  const result = {}
  for (const key in obj) {
    if (obj.hasOwnProperty(key)) {
      const camelKey = key.replace(/_([a-z])/g, (_, letter) => letter.toUpperCase())
      result[camelKey] = obj[key]
    }
  }
  return result
}

// 获取下一个顺序工单号（核心：按W001、W002、W003...生成）
const getNextWorkOrderId = async () => {
  try {
    // 调用后端查询最大工单号接口
    const maxId = await request('/max-id', 'GET')
    if (!maxId) {
      return 'W001' // 无数据时默认从W001开始
    }
    // 提取序号并自增（W002 → 002 → 2 → 3 → W003）
    const num = parseInt(maxId.replace('W', ''), 10) + 1
    return `W${num.toString().padStart(3, '0')}`
  } catch (error) {
    ElMessage.warning('获取最大工单号失败，默认使用W001')
    return 'W001'
  }
}

// ========== 接口请求封装 ==========
const request = async (url, method = 'GET', data = null, params = null) => {
  try {
    loading.value = true
    const config = {
      url: `${baseUrl}${url}`,
      method,
      headers: { 'Content-Type': 'application/json' }
    }
    if (params) config.params = params
    if (data) config.data = data
    
    const res = await axios(config)
    return res.data
  } catch (error) {
    if (error.message.includes('CORS')) {
      ElMessage.error('跨域请求被拦截，请检查后端CORS配置！')
    } else if (error.response?.status === 404) {
      ElMessage.error('请求的资源不存在！')
    } else if (error.response?.status === 500) {
      ElMessage.error('服务器内部错误，请联系管理员！')
    } else {
      ElMessage.error(`接口请求失败：${error.response?.statusText || error.message}`)
    }
    return null
  } finally {
    loading.value = false
  }
}

// ========== 业务逻辑实现 ==========
// 获取工单列表
const getOrderList = async () => {
  loading.value = true
  // 构造查询DTO
  const queryDTO = {
    workOrderId: searchForm.work_order_id,
    orderId: searchForm.order_id,
    productId: searchForm.product_id,
    lineId: searchForm.line_id,
    workOrderStatus: searchForm.work_order_status,
    plannedStartTimeStart: searchForm.planned_start_time_range[0] || null,
    plannedStartTimeEnd: searchForm.planned_start_time_range[1] || null
  }
  // 分页参数
  const pageParams = {
    page: pagination.pageNum - 1,
    size: pagination.pageSize
  }
  // 调用查询接口
  const data = await request('/query', 'POST', queryDTO, pageParams)
  
  if (data) {
    // 转换数据格式
    orderList.value = data.content.map(item => ({
      work_order_id: item.workOrderId,
      order_id: item.orderId,
      product_id: item.productId,
      quantity: item.quantity,
      scheduled_quantity: item.scheduledQuantity,
      completed_quantity: item.completedQuantity,
      line_id: item.lineId,
      work_order_status: item.workOrderStatus,
      planned_start_time: formatDateTime(item.plannedStartTime),
      planned_end_time: formatDateTime(item.plannedEndTime),
      remark: item.remark
    }))
    // 更新总数
    pagination.total = data.totalElements || 0
  }
  loading.value = false
}

// 状态格式化
const statusFormatter = (row) => {
  return row.work_order_status
}

// 状态标签类型映射
const getStatusTagType = (status) => {
  const typeMap = {
    '未排程': 'warning',
    '处理中': 'primary',
    '已完成': 'success',
    '已取消': 'danger'
  }
  return typeMap[status] || 'info'
}

// 查询工单
const handleSearch = () => {
  pagination.pageNum = 1
  getOrderList()
}

// 重置搜索表单
const resetSearchForm = () => {
  Object.keys(searchForm).forEach(key => {
    searchForm[key] = key === 'planned_start_time_range' ? [] : ''
  })
  pagination.pageNum = 1
  getOrderList()
  ElMessage.info('搜索条件已重置')
}

// 分页事件处理
const handleSizeChange = (val) => {
  pagination.pageSize = val
  getOrderList()
}
const handleCurrentChange = (val) => {
  pagination.pageNum = val
  getOrderList()
}

// 选中工单
const handleSelectionChange = (val) => {
  selectedOrders.value = val
}

// 打开新增弹窗
const openAddDialog = () => {
  dialogTitle.value = '新增工单'
  resetForm()
  dialogVisible.value = true
}

// 打开编辑弹窗
const openEditDialog = (row) => {
  dialogTitle.value = '编辑工单'
  Object.assign(form, {
    work_order_id: row.work_order_id,
    order_id: row.order_id,
    product_id: row.product_id,
    quantity: row.quantity || 0,
    scheduled_quantity: row.scheduled_quantity || 0,
    completed_quantity: row.completed_quantity || 0,
    line_id: row.line_id,
    work_order_status: row.work_order_status || '未排程',
    planned_start_time: row.planned_start_time,
    planned_end_time: row.planned_end_time,
    remark: row.remark || ''
  })
  dialogVisible.value = true
}

// 查看工单详情
const handleView = async (row) => {
  const detail = await request(`/${row.work_order_id}`, 'GET')
  if (detail) {
    Object.assign(viewForm, {
      work_order_id: detail.workOrderId,
      order_id: detail.orderId,
      product_id: detail.productId,
      quantity: detail.quantity,
      scheduled_quantity: detail.scheduledQuantity,
      completed_quantity: detail.completedQuantity,
      line_id: detail.lineId,
      work_order_status: detail.workOrderStatus,
      planned_start_time: formatDateTime(detail.plannedStartTime),
      planned_end_time: formatDateTime(detail.plannedEndTime),
      remark: detail.remark
    })
  } else {
    Object.assign(viewForm, { ...row })
  }
  viewDialogVisible.value = true
}

// 重置表单
const resetForm = () => {
  Object.keys(form).forEach(key => {
    if (key === 'work_order_status') {
      form[key] = '未排程'
    } else if (['quantity', 'scheduled_quantity', 'completed_quantity'].includes(key)) {
      form[key] = 0
    } else {
      form[key] = ''
    }
  })
  if (formRef.value) {
    formRef.value.clearValidate()
  }
}

// 提交新增/编辑表单
const handleSubmit = async () => {
  // 表单校验
  const valid = await formRef.value.validate()
  if (!valid) return

  // 转换为驼峰DTO
  const submitData = toCamelCase(form)

  let res = null
  if (form.work_order_id) {
    // 编辑工单
    res = await request(`/${form.work_order_id}`, 'PUT', submitData)
  } else {
    // 新增工单：生成顺序工单号
    const nextId = await getNextWorkOrderId()
    submitData.workOrderId = nextId
    res = await request('', 'POST', submitData)
  }

  if (res) {
    ElMessage.success(form.work_order_id ? '工单编辑成功' : '工单新增成功')
    dialogVisible.value = false
    getOrderList() // 刷新列表
  }
}

// 删除工单
const handleDelete = (row) => {
  ElMessageBox.confirm(
    '确定要删除该工单吗？删除后不可恢复！',
    '警告',
    { type: 'warning' }
  ).then(async () => {
    const res = await request(`/${row.work_order_id}`, 'DELETE')
    if (res !== undefined) {
      ElMessage.success('工单删除成功')
      getOrderList()
    }
  }).catch(() => {
    ElMessage.info('已取消删除')
  })
}

// 页面初始化加载列表
onMounted(() => {
  getOrderList()
})
</script>

<style scoped>
.work-order-management {
  padding: 20px;
  background: #f5f7fa;
  min-height: 100vh;
  box-sizing: border-box;
}

.search-bar {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  justify-content: space-between;
  align-items: center;
  background: #fff;
  padding: 15px 20px;
  border-radius: 4px;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
}

.search-form {
  flex: 1;
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.order-table {
  background: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
}

@media (max-width: 1400px) {
  .search-bar {
    flex-direction: column;
    align-items: flex-start;
  }
  .el-table {
    font-size: 12px;
  }
}
</style>