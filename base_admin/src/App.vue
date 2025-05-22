<script setup>
import HelloWorld from './components/HelloWorld.vue'
import { ref, getCurrentInstance } from 'vue'

const { proxy } = getCurrentInstance()
const locale = proxy.$antdLocale
const visible = ref(false)
const showModal = () => {
  visible.value = true
}
const handleOk = () => {
  visible.value = false
}
</script>

<template>
  <a-config-provider :locale="locale">
    <div class="container">
      <a-row :gutter="16">
        <a-col :span="12">
          <a-card title="Ant Design Vue 示例" :bordered="false">
            <p>这是一个使用 Ant Design Vue 的简单示例</p>
            <a-space>
              <a-button type="primary" @click="showModal">打开对话框</a-button>
              <a-button>默认按钮</a-button>
              <a-button type="dashed">虚线按钮</a-button>
            </a-space>
            
            <a-divider />
            
            <a-alert message="成功提示" type="success" show-icon />
            
            <a-divider />
            
            <HelloWorld msg="Vite + Vue + Ant Design" />
          </a-card>
        </a-col>
      </a-row>
      
      <a-modal v-model:visible="visible" title="欢迎" @ok="handleOk">
        <p>欢迎使用 Ant Design Vue!</p>
      </a-modal>
    </div>
  </a-config-provider>
</template>

<style scoped>
.container {
  max-width: 1200px;
  margin: 50px auto;
  padding: 0 20px;
}
.logo {
  height: 6em;
  padding: 1.5em;
  will-change: filter;
  transition: filter 300ms;
}
.logo:hover {
  filter: drop-shadow(0 0 2em #646cffaa);
}
.logo.vue:hover {
  filter: drop-shadow(0 0 2em #42b883aa);
}
</style>
