<template>
  <div class="production-order-container">
    <h2>创建生产订单（模拟ERP传递订单）</h2>
    <el-form 
      ref="orderFormRef" 
      :model="orderForm" 
      label-width="120px" 
      class="order-form"
      :rules="formRules"
    >
      <!-- 订单编号 -->
      <el-form-item label="生产订单编号" prop="orderId">
        <el-input 
          v-model="orderForm.orderId" 
          placeholder="请输入订单编号（如O002）"
        ></el-input> <!-- 补充闭合标签 -->
      </el-form-item>

      <!-- 产品编号 -->
      <el-form-item label="产品编号" prop="productId">
        <el-input 
          v-model="orderForm.productId" 
          placeholder="请输入产品编号（如PR001）"
        ></el-input> <!-- 补充闭合标签 -->
      </el-form-item>

      <!-- 产品名称 -->
      <el-form-item label="产品名称" prop="productName">
        <el-input 
          v-model="orderForm.productName" 
          placeholder="请输入产品名称（如高性能芯片A）"
        ></el-input> <!-- 补充闭合标签 -->
      </el-form-item>

      <!-- 订单数量（修复核心） -->
      <el-form-item label="订单数量" prop="orderQuantity">
        <el-input-number 
          v-model="orderForm.orderQuantity" 
          :min="1" 
          placeholder="请输入订单数量"
          :precision="0"
        ></el-input-number> <!-- 补充闭合标签，属性规范 -->
      </el-form-item>

      <!-- 交期 -->
      <el-form-item label="交期">
        <el-date-picker 
          v-model="orderForm.deliveryDate" 
          type="date" 
          placeholder="选择交期（可选）"
          value-format="yyyy-MM-dd"
        ></el-date-picker> <!-- 补充闭合标签 -->
      </el-form-item>

      <!-- 备注 -->
      <el-form-item label="备注">
        <el-input 
          v-model="orderForm.remark" 
          type="textarea" 
          placeholder="请输入备注（可选）"
        ></el-input> <!-- 补充闭合标签 -->
      </el-form-item>

      <!-- 操作按钮 -->
      <el-form-item>
        <el-button type="primary" @click="submitOrder" :loading="submitLoading">提交订单</el-button>
        <el-button @click="resetForm">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue';
// 局部引入Element Plus组件（若未全局引入则必须加）
import { ElInputNumber, ElForm, ElFormItem, ElInput, ElDatePicker, ElButton, ElMessage, ElMessageBox } from 'element-plus';
import axios from 'axios';

// 配置axios基础路径（根据后端实际部署地址调整）
axios.defaults.baseURL = 'http://localhost:8080'; 
axios.defaults.headers.post['Content-Type'] = 'application/json;charset=utf-8';

// 表单数据
const orderForm = reactive({
  orderId: '',
  productId: '',
  productName: '',
  orderQuantity: 1,
  deliveryDate: null,
  remark: ''
});

// 表单校验规则
const formRules = reactive({
  orderId: [
    { required: true, message: '请输入生产订单编号', trigger: 'blur' },
    { pattern: /^[A-Za-z0-9]+$/, message: '订单编号仅支持字母和数字', trigger: 'blur' }
  ],
  productId: [
    { required: true, message: '请输入产品编号', trigger: 'blur' },
    { pattern: /^[A-Za-z0-9]+$/, message: '产品编号仅支持字母和数字', trigger: 'blur' }
  ],
  productName: [
    { required: true, message: '请输入产品名称', trigger: 'blur' }
  ],
  orderQuantity: [
    { required: true, message: '请输入订单数量', trigger: 'blur' },
    { type: 'number', min: 1, message: '订单数量必须大于0', trigger: 'blur' }
  ]
});

// 表单引用
const orderFormRef = ref(null);
// 提交加载状态
const submitLoading = ref(false);

// 提交订单
const submitOrder = async () => {
  try {
    await orderFormRef.value.validate();
  } catch (error) {
    ElMessage.warning('表单填写有误，请检查！');
    return;
  }

  submitLoading.value = true;
  try {
    const response = await axios.post(
      '/api/productionorder/addProductionOrder',
      JSON.parse(JSON.stringify(orderForm))
    );

    if (response.status === 200) {
      ElMessage.success(`订单创建成功！订单编号：${response.data.orderId}`);
      ElMessageBox.confirm(
        '订单创建成功，是否继续创建新订单？',
        '提示',
        {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'success'
        }
      ).then(() => {
        resetForm();
      }).catch(() => {});
    }
  } catch (error) {
    let errorMsg = '订单创建失败：';
    if (error.response) {
      errorMsg += error.response.data?.message || `HTTP状态码：${error.response.status}`;
    } else if (error.request) {
      errorMsg += '无法连接到后端服务器，请检查后端是否启动！';
    } else {
      errorMsg += error.message;
    }
    ElMessage.error(errorMsg);
  } finally {
    submitLoading.value = false;
  }
};

// 重置表单
const resetForm = () => {
  orderFormRef.value.resetFields();
  orderForm.orderId = '';
  orderForm.productId = '';
  orderForm.productName = '';
  orderForm.orderQuantity = 1;
  orderForm.deliveryDate = null;
  orderForm.remark = '';
};
</script>

<style scoped>
.production-order-container {
  max-width: 800px;
  margin: 20px auto;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
}

.order-form {
  margin-top: 20px;
}

:deep(.el-form-item) {
  margin-bottom: 20px;
}
</style>