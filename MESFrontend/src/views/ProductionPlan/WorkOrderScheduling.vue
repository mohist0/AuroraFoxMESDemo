<template>
  <div class="dispatch-page">
    <div class="dispatch-container">
      <!-- 左侧区域-->
      <div class="left-panel">
        <!-- 工单列表池 -->
        <div class="work-order-pool">
          <div class="pool-header">
            <span>工单列表池</span>
            <el-button type="primary" size="mini" class="refresh-btn" @click="refreshWorkOrderList">刷新</el-button>
          </div>
          <div 
            class="order-item" 
            v-for="order in workOrderList" 
            :key="order.work_order_id"
            draggable="true"
            @dragstart="handleOrderDragStart(order)"
            @dragend="handleDragEnd"
            @click="handleOrderClick(order)"
            :class="{ 'order-active': selectedWorkOrder?.work_order_id === order.work_order_id }"
          >
            <div class="order-info">
              <div class="order-name">{{ order.name }}</div>
              <div class="order-product">{{ order.product_name }}×{{ order.quantity }}</div>
            </div>
            <div class="order-btn-group">
              <el-button type="text" icon="el-icon-arrow-right" @click="quickAddToBoard(order)"></el-button>
            </div>
          </div>
        </div>

        <!-- 物料/设备提示 -->
        <div class="material-tip" v-if="selectedWorkOrder">
          <div class="tip-header">物料/设备提示</div>
          <div class="tip-item">选中工单：{{ selectedWorkOrder.name }}</div>
          <div class="tip-item">所需物料：物料{{ selectedWorkOrder.product_name }}×{{ selectedWorkOrder.material_qty }}</div>
          <div class="tip-item">推荐设备：设备组{{ selectedWorkOrder.recommend_device }}</div>
          <div class="tip-item">
            状态：<el-tag type="success" size="mini"><i class="el-icon-check"></i> 物料充足</el-tag>
          </div>
        </div>
      </div>

      <!-- 右侧区域 -->
      <div class="right-panel">
        <!-- 排程看板：动态行列 -->
        <div class="schedule-board">
          <!-- 1. 看板表头：给allDeviceGroups加兜底 + 可选链 -->
<div class="board-header">
  <div class="header-empty" :style="{ width: rowLabelWidth + 'px' }"> </div>
  <div 
    class="header-line" 
    v-for="deviceGroup in allDeviceGroups || []"
    :key="deviceGroup?.groupId"
    :style="{ width: columnWidth + 'px' }"
  >
    {{ deviceGroup?.groupName }}  <!-- 可选链 -->
  </div>
</div>

<!-- 2. 看板行：给productionLines/allDevices加兜底 + 可选链 -->
<div class="board-row" v-for="line in productionLines || []" :key="line?.line_id">
  <div class="row-label" :style="{ width: rowLabelWidth + 'px' }">{{ line?.line_name }}</div>
  <!-- 遍历所有设备 -->
  <div
    class="row-column"
    v-for="device in allDevices || []"
    :key="device?.device_id" 
    v-if="device?.line_id === line?.line_id"
    @drop="(e) => {e.stopPropagation(); handleDrop(line?.line_id, device?.device_id)}"
    @dragover="(e) => {e.preventDefault(); e.stopPropagation()}"
    @dragenter="(e) => {e.stopPropagation(); handleDragEnter(line?.line_id, device?.device_id)}"
    @dragleave="(e) => {e.stopPropagation(); handleDragLeave()}"
    :class="{ 'column-hover': hoveredLineId === line?.line_id && hoveredDeviceId === device?.device_id }"
    :style="{ width: columnWidth + 'px' }"
  >
    <!-- 已排程工单：加可选链 -->
    <div
      class="scheduled-order"
      v-for="order in getScheduledOrders(line?.line_id, device?.device_id)"
      :key="order?.work_order_id"
      draggable="true"
      @dragstart="handleScheduledDragStart(order)"
      @dragend="handleDragEnd"
    >
      {{ order?.name }}
    </div>
  </div>
  <!-- 非当前生产线设备列：加样式宽度 -->
  <div 
    class="row-column disabled-column" 
    v-else
    :style="{ width: columnWidth + 'px' }"
  ></div>
</div>
</div>
        <!-- 操作按钮 -->
        <div class="operation-btns">
          <el-button type="default" size="mini" @click="resetSchedule">重置</el-button>
          <el-button type="success" size="mini" @click="saveSchedule">保存排程</el-button>
        </div>
      </div>

        
      </div>
    </div>
  
</template>

<script setup>
import { ref, onMounted, computed,nextTick } from 'vue'
import { ElMessage, ElLoading } from 'element-plus'
import axios from 'axios'

// 后端接口基础路径
const baseUrl = 'http://localhost:8080/api/work-order-schedules'
// 加载状态
const loading = ref(null)


// 新增：设备列表数据（适配device表）
const deviceList = ref([])
// 悬浮的设备ID（替代原col，用于动态列）
const hoveredDeviceId = ref(null)

// 工单列表数据（适配work_order表字段）
const workOrderList = ref([])
// 选中的工单
const selectedWorkOrder = ref(null)
// 悬浮的列（视觉反馈）
const hoveredLineId = ref(null)
const hoveredCol = ref(null)
// 生产线数据（适配production_line表）
const productionLines = ref([])
// 已排程工单
const scheduledOrders = ref([])

// // 获取对应生产线+列的工单
// const getScheduledOrders = (lineId, col) => {
//   return scheduledOrders.value.filter(item => item.line_id === lineId && item.col === col)
// }

// 动态计算看板尺寸（根据数据量自适应）
const rowLabelWidth = 150  // 行标签宽度固定
const columnCount = computed(() => deviceList.value.length > 0 ? deviceList.value.length : 3) // 列数=设备总数
const columnWidth = computed(() => {
  // 兜底：若获取不到right-panel，用默认宽度800
  const rightPanelWidth = document.querySelector('.right-panel')?.clientWidth || 800
  // 避免除以0：columnCount至少为1
  const count = columnCount.value || 1
  return Math.floor((rightPanelWidth - rowLabelWidth) / count) - 2
})
onMounted(async () => {
  await Promise.all([
    getWorkOrderList(),
    getProductionLineList(),
    getScheduledList(),
    getDeviceList()
  ])
  // 等待DOM更新后再计算列宽（避免DOM未加载）
  await nextTick()
})
const rowHeight = 100  // 行高固定（可根据需要动态调整）

// 获取所有设备组（用于表头）
const allDeviceGroups = computed(() => {
  return deviceList.value.map(device => ({
    groupId: device.device_id,
    groupName: device.device_name  // 用设备名称作为列标题
  }))
})

// 获取指定生产线的设备列表
const getLineDevices = (lineId) => {
  return deviceList.value.filter(device => device.line_id === lineId)
}

// 适配动态列：根据生产线ID和设备ID筛选已排程工单
const getScheduledOrders = (lineId, deviceId) => {
  return scheduledOrders.value.filter(item => item.line_id === lineId && item.device_id === deviceId)
}

// ========== 接口请求封装 ==========
const request = async (url, method = 'GET', data = null) => {
  try {
    loading.value = ElLoading.service({
      lock: true,
      text: '处理中...',
      background: 'rgba(0, 0, 0, 0.1)'
    })
    const res = await axios({
      url: `${baseUrl}${url}`,
      method,
      data,
      headers: { 'Content-Type': 'application/json' }
    })
    if (res.data.code === 200) {
      return res.data.data
    } else {
      ElMessage.error(res.data.msg || '操作失败')
      return null
    }
  } catch (error) {
    ElMessage.error('接口请求失败：' + error.message)
    return null
  } finally {
    if (loading.value) loading.value.close()
  }
}

// ========== 数据加载 ==========
// 获取未排程工单列表（适配work_order表，筛选status=未排程）
const getWorkOrderList = async () => {
  const data = await request('/workOrder/list?status=未排程')
  if (data) {
    // 格式化数据：适配前端展示
    workOrderList.value = data.map(item => ({
      work_order_id: item.work_order_id,
      order_id: item.order_id,
      product_id: item.product_id,
      product_name: item.product_name, // 关联product表查询
      quantity: item.quantity,
      material_qty: item.material_quantity, // 关联material表
      recommend_device: item.recommend_device || 'A',
      name: `工单${item.product_id.charAt(0)}-${item.work_order_id.slice(-2)}`,
      materialStatus: '充足'
    }))
  }
}

// ========== 新增：获取设备列表接口 ==========
const getDeviceList = async () => {
  const data = await request('/device/list') // 新增后端接口：获取所有设备
  if (data) {
    deviceList.value = data
  } else {
    // 兜底数据：模拟3条生产线，每条2台设备（与你数据库数据匹配）
    deviceList.value = [
      { device_id: 'D004', device_name: '检测机B2', line_id: 'L001' },
      { device_id: 'D005', device_name: '包装机C1', line_id: 'L002' },
      { device_id: 'D006', device_name: '码垛机C2', line_id: 'L003' }
    ]
  }
}

// 获取生产线列表
const getProductionLineList = async () => {
  const data = await request('/productionLine/list')
  if (data) {
    productionLines.value = data
  } else {
    // 兜底数据
    productionLines.value = [
      { line_id: 'L001', line_name: '生产线A' }, 
      { line_id: 'L002', line_name: '生产线B' }, 
      { line_id: 'L003', line_name: '生产线C' }]
  }
}

// 获取已排程工单
const getScheduledList = async () => {
  const data = await request('/schedule/list')
  if (data) {
    scheduledOrders.value = data.map(item => ({
      work_order_id: item.work_order_id,
      name: `工单${item.product_id.charAt(0)}-${item.work_order_id.slice(-2)}`,
      line_id: item.line_id,
      col: item.col || 2,
      product_name: item.product_name
    }))
  }
}

// 刷新工单列表
const refreshWorkOrderList = async () => {
  await getWorkOrderList()
}

// ========== 交互逻辑 ==========
const handleOrderClick = (order) => {
  selectedWorkOrder.value = order
}

const handleOrderDragStart = (order) => {
  selectedWorkOrder.value = order
  selectedWorkOrder.value.isNew = true
  event.dataTransfer.setData('text/plain', JSON.stringify(order))
  event.dataTransfer.effectAllowed = 'move'
}

const handleScheduledDragStart = (order) => {
  selectedWorkOrder.value = order
  selectedWorkOrder.value.isNew = false
  event.dataTransfer.setData('text/plain', JSON.stringify(order))
}

// const handleDragEnter = (lineId, col) => {
//   hoveredLineId.value = lineId
//   hoveredCol.value = col
// }
const handleDragEnter = (lineId, deviceId) => {
  hoveredLineId.value = lineId
  hoveredDeviceId.value = deviceId
}

const handleDragLeave = () => {
  hoveredLineId.value = null
  hoveredCol.value = null
  hoveredDeviceId.value = null
}

const handleDragEnd = () => {
  hoveredLineId.value = null
}

// const handleDrop = (lineId, col) => {
//   event.preventDefault()
//   event.stopPropagation()

//   if (!selectedWorkOrder.value) return

//   // 新工单（从左侧拖拽）
//   if (selectedWorkOrder.value.isNew) {
//     const isDuplicate = scheduledOrders.value.some(item => item.work_order_id === selectedWorkOrder.value.work_order_id)
//     if (isDuplicate) {
//       ElMessage.warning(`【${selectedWorkOrder.value.name}】已排程，请勿重复添加`)
//       return
//     }
//     const newOrder = {
//       ...selectedWorkOrder.value,
//       line_id: lineId,
//       col
//     }
//     scheduledOrders.value.push(newOrder)
//     ElMessage.success(`【${newOrder.name}】已排至${productionLines.value.find(l => l.line_id === lineId)?.line_name}列${col}`)
//   } 
//   // 已排程工单（看板内拖拽）
//   else {
//     const targetIndex = scheduledOrders.value.findIndex(item => item.work_order_id === selectedWorkOrder.value.work_order_id)
//     if (targetIndex > -1) {
//       scheduledOrders.value[targetIndex].line_id = lineId
//       scheduledOrders.value[targetIndex].col = col
//       ElMessage.success(`【${selectedWorkOrder.value.name}】已移至${productionLines.value.find(l => l.line_id === lineId)?.line_name}列${col}`)
//     }
//   }

//   hoveredLineId.value = null
//   hoveredCol.value = null
// }


const handleDrop = (lineId, deviceId) => {
  event.preventDefault()
  event.stopPropagation()

  if (!selectedWorkOrder.value) return

  // 新工单（从左侧拖拽）
  if (selectedWorkOrder.value.isNew) {
    const isDuplicate = scheduledOrders.value.some(item => item.work_order_id === selectedWorkOrder.value.work_order_id)
    if (isDuplicate) {
      ElMessage.warning(`【${selectedWorkOrder.value.name}】已排程，请勿重复添加`)
      return
    }
    const newOrder = {
      ...selectedWorkOrder.value,
      line_id: lineId,
      device_id: deviceId  // 记录设备ID，替代原col
    }
    scheduledOrders.value.push(newOrder)
    const lineName = productionLines.value.find(l => l.line_id === lineId)?.line_name
    const deviceName = deviceList.value.find(d => d.device_id === deviceId)?.device_name
    ElMessage.success(`【${newOrder.name}】已排至${lineName}-${deviceName}`)
  } 
  // 已排程工单（看板内拖拽）
  else {
    const targetIndex = scheduledOrders.value.findIndex(item => item.work_order_id === selectedWorkOrder.value.work_order_id)
    if (targetIndex > -1) {
      scheduledOrders.value[targetIndex].line_id = lineId
      scheduledOrders.value[targetIndex].device_id = deviceId
      const lineName = productionLines.value.find(l => l.line_id === lineId)?.line_name
      const deviceName = deviceList.value.find(d => d.device_id === deviceId)?.device_name
      ElMessage.success(`【${selectedWorkOrder.value.name}】已移至${lineName}-${deviceName}`)
    }
  }

  hoveredLineId.value = null
  hoveredDeviceId.value = null
}

// 快捷添加
const quickAddToBoard = async (order) => {
  const targetLine = productionLines.value.find(l => l.line_name.includes(order.recommend_device))
  const targetLineId = targetLine?.line_id || '1'
  const targetCol = 1

  const isDuplicate = scheduledOrders.value.some(item => item.work_order_id === order.work_order_id)
  if (isDuplicate) {
    ElMessage.warning(`【${order.name}】已排程，请勿重复添加`)
    return
  }

  // 调用后端快捷添加接口
  const res = await request('/schedule/quickAdd', 'POST', {
    work_order_id: order.work_order_id,
    line_id: targetLineId,
    order_id: order.order_id,
    product_id: order.product_id,
    material_id: order.product_id // 物料编号暂用产品编号，可根据实际调整
  })
  
  if (res) {
    scheduledOrders.value.push({
      ...order,
      line_id: targetLineId,
      col: targetCol
    })
    ElMessage.success(`【${order.name}】已快速排至${targetLine?.line_name || '生产线A'}列${targetCol}`)
    selectedWorkOrder.value = order
  }
}

// 重置排程
const resetSchedule = async () => {
  const res = await request('/schedule/reset', 'POST')
  if (res) {
    scheduledOrders.value = []
    selectedWorkOrder.value = null
    ElMessage.info('排程已重置')
  }
}

// // 保存排程（适配production_schedule表字段）
// const saveSchedule = async () => {
//   if (scheduledOrders.value.length === 0) {
//     ElMessage.warning('暂无排程数据，无需保存')
//     return
//   }

//   // 格式化排程数据，匹配production_schedule表字段
//   const scheduleData = scheduledOrders.value.map(item => ({
//     schedule_id: `SCH_${Date.now()}${Math.floor(Math.random() * 1000)}`,
//     work_order_id: item.work_order_id,
//     order_id: item.order_id,
//     product_id: item.product_id,
//     line_id: item.line_id,
//     material_id: item.product_id, // 需替换为真实物料编号
//     update_time: new Date()
//   }))

//   const res = await request('/schedule/save', 'POST', scheduleData)
//   if (res) {
//     ElMessage.success('排程已保存成功')
//     console.log('保存的排程数据：', scheduleData)
//   }
// }
const saveSchedule = async () => {
  if (scheduledOrders.value.length === 0) {
    ElMessage.warning('暂无排程数据，无需保存')
    return
  }

  const scheduleData = scheduledOrders.value.map(item => ({
    schedule_id: `SCH_${Date.now()}${Math.floor(Math.random() * 1000)}`,
    work_order_id: item.work_order_id,
    order_id: item.order_id,
    product_id: item.product_id,
    line_id: item.line_id,
    device_id: item.device_id,  // 新增：关联设备ID
    material_id: item.product_id,
    update_time: new Date()
  }))

  const res = await request('/schedule/save', 'POST', scheduleData)
  if (res) {
    ElMessage.success('排程已保存成功')
  }
}

// // 页面加载初始化
// onMounted(async () => {
//   await Promise.all([
//     getWorkOrderList(),
//     getProductionLineList(),
//     getScheduledList(),
//     getDeviceList()
//   ])
// })
</script>

<style scoped>
/* 原有样式完全保留，仅适配变量名修改 */
.dispatch-page {
  padding: 10px;
  font-size: 14px;
}
.breadcrumb {
  margin-bottom: 15px;
  color: #666;
}
.dispatch-container {
  display: flex;
  gap: 20px;
  width: 100%;
  height: calc(100vh - 20px);
  box-sizing: border-box;
}
.left-panel {
  flex: 1;
  flex-shrink: 0;
  max-width: calc(100% / 3);
  box-sizing: border-box;
}
.right-panel {
  flex: 2;
  flex-shrink: 0;
  max-width: calc(200% / 3);
  box-sizing: border-box;
}
.work-order-pool {
  border: 1px solid #e6e6e6;
  border-radius: 4px;
  margin-bottom: 15px;
}
.pool-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 10px;
  border-bottom: 1px solid #e6e6e6;
  background: #f5f7fa;
  font-weight: 500;
}
.refresh-btn {
  padding: 2px 8px;
}
.order-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 15px;
  border-bottom: 1px dashed #e6e6e6;
  cursor: grab;
  transition: background 0.2s;
}
.order-active {
  background: #e8f4ff;
  border-left: 3px solid #409eff;
}
.order-item:hover {
  background: #f5f7fa;
}
.order-item:active {
  cursor: grabbing;
}
.order-info {
  line-height: 1.5;
  flex: 1;
}
.order-name {
  font-weight: 500;
}
.order-product {
  font-size: 12px;
  color: #666;
}
.order-btn-group {
  display: flex;
  gap: 5px;
}
.material-tip {
  border: 1px solid #e6e6e6;
  border-radius: 4px;
  padding: 10px;
}
.tip-header {
  font-weight: 500;
  margin-bottom: 8px;
}
.tip-item {
  font-size: 12px;
  margin-bottom: 4px;
}
.schedule-board {
  border: 1px solid #e6e6e6;
  border-radius: 4px;
  margin-bottom: 15px;
  width: 100%;
}
.board-header {
  display: flex;
  height: 60px;
  line-height: 60px;
  overflow-x: auto; /* 列数过多时可横向滚动 */
}
.header-empty {
  /* width: v-bind(rowLabelWidth + 'px'); 绑定动态行标签宽度 */
  flex-shrink: 0; /* 不收缩 */
  text-align: center;
  border-right: 1px solid #e6e6e6;
  background: #f5f7fa;
}
.header-line {
  flex-shrink: 0; /* 不收缩 */
  text-align: center;
  border-right: 1px solid #e6e6e6;
  background: #f5f7fa;
  font-weight: 500;
}
.board-row {
  display: flex;
  height: 100px;
  border-top: 1px solid #e6e6e6;
  overflow-x: auto; /* 列数过多时可横向滚动 */
}
.row-label {
  flex-shrink: 0; /* 不收缩 */
  display: flex;
  align-items: center;
  justify-content: center;
  border-right: 1px solid #e6e6e6;
  background: #f9f9f9;
}
.row-column {
  flex-shrink: 0; /* 不收缩 */
  border-right: 1px solid #e6e6e6;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background 0.2s;
  min-height: 100%;
}
/* 非当前生产线的设备列：灰色、不可交互 */
.disabled-column {
  flex-shrink: 0;
  border-right: 1px solid #e6e6e6;
  background: #f5f5f5;  /* 灰色背景 */
  min-height: 100%;
  min-width: 120px;
}
.column-hover {
  background: #e8f4ff;
  border: 1px dashed #409eff;
  box-shadow: 0 0 8px rgba(64, 158, 255, 0.2);
}
.scheduled-order {
  width: 150px;
  height: 40px;
  line-height: 40px;
  background: #409eff;
  color: #fff;
  text-align: center;
  border-radius: 4px;
  cursor: grab;
  transition: transform 0.2s;
}
.scheduled-order:hover {
  transform: scale(1.02);
}
.scheduled-order:active {
  cursor: grabbing;
}
.operation-btns {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>