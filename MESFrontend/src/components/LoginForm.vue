<template>
  <form @submit.prevent="handleSubmit" class="login-form">
    <div class="form-item">
      <label>账号：</label>
      <input v-model="form.username" placeholder="请输入账号" />
    </div>

    <div class="form-item">
      <label>密码：</label>
      <input type="password" v-model="form.password" placeholder="请输入密码" />
    </div>

    <!-- 角色下拉 -->
    <div class="form-item">
      <label>角色：</label>
      <select v-model="form.roleId"
        style="width:100%;height:36px;padding:0 10px;border:1px solid #ccc;border-radius:6px">
        <option disabled value="">
          {{ roles.length ? '请选择角色' : '角色加载中…' }}
        </option>
        <option v-for="r in roles" :key="r.roleId" :value="r.roleId">
          {{ r.roleName }}
        </option>
      </select>
    </div>

    <button class="login-btn" type="submit" :disabled="loading">
      {{ loading ? '登录中…' : '登录' }}
    </button>
  </form>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { useUserStore } from '../stores/user'
import { useRouter } from 'vue-router'
import axios from 'axios'

const userStore = useUserStore()
const router = useRouter()

const loading = ref(false)
const roles = ref([])

const form = reactive({
  username: '',
  password: '',
  roleId: ''
})

/* 挂载时拉取角色 */
onMounted(async () => {
  try {
    const { data } = await axios.get('http://localhost:8080/api/role')
    roles.value = data || []
  } catch (e) {
    alert('获取角色失败：' + (e.response?.data || e.message))
  }
})

function validateForm() {
  if (!form.username.trim()) { alert('请输入账号'); return false }
  if (!form.password) { alert('请输入密码'); return false }
  if (!form.roleId) { alert('请选择角色'); return false }
  return true
}

async function handleSubmit() {
  if (!validateForm()) return
  loading.value = true
  try {
    await userStore.loginUser({
      username: form.username,
      password: form.password,
      roleId: form.roleId
    })
    router.replace('/')
  } catch (e) {
    console.error(e)
    alert('登录失败，详情看控制台 F12')
  } finally {
    loading.value = false
  }
}
</script>
<style scoped>
.login-form {
  display: flex;
  flex-direction: column;
  max-width: 350px;
  margin: 100px auto;
}

.form-item {
  margin-bottom: 15px;
}

input {
  width: 100%;
  height: 36px;
  padding: 0 10px;
  border: 1px solid #ccc;
  border-radius: 6px;
}

.login-btn {
  height: 36px;
  background: #409eff;
  color: #fff;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}

.login-btn:disabled {
  background: #a0cfff;
}
</style>