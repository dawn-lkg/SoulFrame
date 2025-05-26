<template>
  <a-drawer
    v-model:open="app.settingDrawerVisible"
    title="主题设置"
    placement="right"
  >
  </a-drawer>
</template>

<script setup>
import { useAppStore } from '@/stores/app';

const app = useAppStore();
</script>

<style lang="scss" scoped>

</style>
