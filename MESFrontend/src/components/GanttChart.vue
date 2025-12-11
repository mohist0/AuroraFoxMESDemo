<template>
  <div ref="chartRef" class="gantt-box"></div>
</template>

<script setup>
import * as echarts from "echarts";
import { ref, onMounted, watch } from "vue";

const chartRef = ref(null);
let chart = null;

const props = defineProps({
  tasks: { type: Array, default: () => [] }
});

function getColor(index) {
  const palette = [
    "#4A90E2", "#7B68EE", "#3CB371", "#FF8C00", "#FF6B6B",
    "#26A69A", "#8E44AD", "#2E86C1", "#AF7AC5", "#F4D03F"
  ];
  return palette[index % palette.length];
}

function formatData(tasks, devices) {
  return tasks.map(t => {
    const deviceIndex = devices.indexOf(t.equipment);
    const color = getColor(deviceIndex);

    return {
      name: t.workOrderId,
      value: [
        t.equipment,
        new Date(t.startTime).getTime(),
        new Date(t.endTime).getTime()
      ],
      raw: t,
      itemStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
          { offset: 0, color },
          { offset: 1, color + "cc" }
        ])
      }
    };
  });
}

function render() {
  if (!chart) return;

  const devices =
      props.tasks.length > 0
          ? [...new Set(props.tasks.map(t => t.equipment))]
          : ["暂无设备"];

  const option = {
    title: { text: "生产排程甘特图", left: "center" },

    grid: { top: 70, left: 150, right: 40, bottom: 40 },

    tooltip: {
      formatter(p) {
        const t = p.data.raw;
        return `
        <b>⏳ 工单：${t.workOrderId}</b><br/>
        📦 产品：${t.productName || "无"} <br/>
        🏭 设备：${t.equipment}<br/>
        👤 操作员：${t.operator || "无"} <br/>
        🕒 开始：${new Date(t.startTime).toLocaleString()}<br/>
        🕒 结束：${new Date(t.endTime).toLocaleString()}<br/>
        `;
      }
    },

    xAxis: {
      type: "time",
      axisLabel: { rotate: 45 }
    },

    yAxis: {
      type: "category",
      data: devices
    },

    series: [
      {
        type: "custom",
        encode: { x: [1, 2], y: 0 },

        renderItem(params, api) {
          const catIndex = api.value(0);
          const start = api.coord([api.value(1), catIndex]);
          const end = api.coord([api.value(2), catIndex]);
          const height = api.size([0, 1])[1] * 0.6;

          return {
            type: "rect",
            shape: {
              x: start[0],
              y: start[1] - height / 2,
              width: end[0] - start[0],
              height
            },
            style: api.style()
          };
        },

        data: formatData(props.tasks, devices)
      }
    ]
  };

  chart.setOption(option);
}

onMounted(() => {
  chart = echarts.init(chartRef.value);
  render();
  window.addEventListener("resize", () => chart.resize());
});

watch(() => props.tasks, () => render(), {deep: true});
</script>

<style scoped>
.gantt-box {
  width: 100%;
  height: 650px;
}
</style>
