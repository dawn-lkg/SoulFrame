<template>
  <div class="container">
    <a-page-header
      title="关于页面"
      sub-title="Ant Design Vue 示例"
      @back="goBack"
    />
    
    <a-card>
      <a-typography>
        <a-typography-title :level="2">关于 Ant Design Vue</a-typography-title>
        <a-typography-paragraph>
          这是一个使用 Ant Design Vue 的示例项目，展示了如何使用中文本地化配置。
        </a-typography-paragraph>
        <a-typography-paragraph>
          <a-button type="primary" @click="goBack">返回首页</a-button>
        </a-typography-paragraph>
      </a-typography>
    </a-card>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()
const goBack = () => {
  router.push('/')
}
</script>

<style scoped>
.container {
  max-width: 1200px;
  margin: 50px auto;
  padding: 0 20px;
}
</style> 