<template>
  <i :class="['iconfont', `icon-${name}`]" :style="styles"></i>
</template>

<script setup>
defineProps({
  // 图标名称
  name: {
    type: String,
    required: true
  },
  // 图标大小
  size: {
    type: [Number, String],
    default: 16
  },
  // 图标颜色
  color: {
    type: String,
    default: ''
  }
})

const styles = computed(() => {
  return {
    fontSize: `${size}px`,
    color: color
  }
})
</script>

<style>
@import '@/assets/icons/iconfont.css';
</style>