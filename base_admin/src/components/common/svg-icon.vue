<template>
    <svg class="icon" :style="styles">
      <use :xlink:href="`#icon-${name}`"></use>
    </svg>
    <!-- <i :class="['iconfont', `icon-${name}`]" :style="styles"></i> -->
</template>

<script setup>
const props = defineProps({
  // 图标名称
  name: {
    type: String,
    required: true,
  },
  // 图标大小
  size: {
    type: [Number, String],
    default: 16,
  },
  // 图标颜色
  color: {
    type: String,
    default: "",
  },
});

const styles = computed(() => {
  return {
    fontSize: `${props.size}px`,
    color: props.color,
    width:`${props.size}px`,
    height:`${props.size}px`
  };
});
</script>

<style>
@import "@/assets/icons/iconfont.css";
.icon {
  /* vertical-align: -1.15em; */
  fill: currentColor;
  overflow: hidden;
  display: flex;
}
</style>