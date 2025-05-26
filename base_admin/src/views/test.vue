<template>
  <div class="container">
    <a-card title="测试页面" :bordered="false">
      <a-result
        status="success"
        title="成功安装和配置 Ant Design Vue!"
        sub-title="您已成功安装了 Sass 并配置了 Vite 路径别名"
      >
        <template #extra>
          <a-space>
            <a-button type="primary" @click="$router.push('/')">
              返回首页
            </a-button>
            <a-button @click="$router.push('/about')">
              关于页面
            </a-button>
          </a-space>
        </template>
      </a-result>
    </a-card>
  </div>
</template>

<script setup>
</script>

<style scoped lang="scss">
.container {
  padding: 24px;
  
  .ant-card {
    margin-bottom: 24px;
  }
}
</style>

