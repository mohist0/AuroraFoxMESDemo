<template>
  <div class="page-container">

    <!-- 顶部工具栏 -->
    <div class="toolbar">
      <el-select v-model="selectedDevice" placeholder="选择设备 (可选)" clearable style="width: 200px">
        <el-option
            v-for="d in deviceList"
            :key="d"
            :label="d"
            :value="d"
        />
      </el-select>

      <el-button type="primary" @click="loadData" :loading="loading">
        刷新数据
      </el-button>
    </div>

    <!-- 甘特图 -->
    <GanttChart :tasks="filteredTasks" v-if="!loading" />

    <!-- 加载中 -->
    <div v-else class="loading-box">
      <el-spin tip="加载排程中..." />
    </div>

  </div>
</template>

<script>
export default {
  name: "PlanGantt"
};
</script>

<script setup>
import { ref, computed, onMounted } from "vue";
import { ElMessage } from "element-plus";
import GanttChart from "@/components/GanttChart.vue";
import { getSchedulePlan } from "@/api/schedule";

const tasks = ref([]);
const loading = ref(false);
const selectedDevice = ref("");

const loadData = async () => {
  try {
    loading.value = true;
    const res = await getSchedulePlan();
    tasks.value = res.data || [];
  } catch {
    ElMessage.error("获取排程失败");
  } finally {
    loading.value = false;
  }
};

onMounted(loadData);

const deviceList = computed(() => {
  const set = new Set(tasks.value.map(t => t.equipment));
  return [...set].sort();
});

const filteredTasks = computed(() => {
  if (!selectedDevice.value) return tasks.value;
  return tasks.value.filter(t => t.equipment === selectedDevice.value);
});
</script>

<style scoped>
.page-container {
  padding: 20px;
}

.toolbar {
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
}

.loading-box {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 500px;
}
</style>
