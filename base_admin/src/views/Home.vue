<script setup>
import { ref } from 'vue'

const visible = ref(false)
const showModal = () => {
  visible.value = true
}
const handleOk = () => {
  visible.value = false
}
</script>

<template>
  <div class="container">
    <a-row :gutter="16">
      <a-col :span="12">
        <a-card title="Ant Design Vue 示例" :bordered="false">
          <p>这是一个使用 Ant Design Vue 的简单示例</p>
          <a-space>
            <a-button type="primary" @click="showModal">打开对话框</a-button>
            <a-button>默认按钮</a-button>
            <a-button type="dashed">虚线按钮</a-button>
            <router-link to="/about">
              <a-button type="link">关于页面</a-button>
            </router-link>
          </a-space>
          
          <a-divider />
          
          <a-alert message="成功提示" type="success" show-icon />
        </a-card>
      </a-col>
    </a-row>
    
    <a-modal v-model:visible="visible" title="欢迎" @ok="handleOk">
      <p>欢迎使用 Ant Design Vue!</p>
    </a-modal>
  </div>
</template>

<style scoped>
.container {
  max-width: 1200px;
  margin: 50px auto;
  padding: 0 20px;
}
</style> 