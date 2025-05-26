<template>
  <a-layout :class="layoutClass">
    <!-- 左侧菜单 -->
    <a-layout-sider v-model:collapsed="collapsed" :style="siderStyle" :theme="themeStore.menuTheme" collapsible>
      <global-logo />
      <global-sider />
    </a-layout-sider>
    <!-- 右侧内容 -->
    <a-layout>
      <!-- 头部 -->
      <a-layout-header class="layout-header">
        <global-header />
      </a-layout-header>
      <!-- 内容 -->
      <a-layout-content class="layout-content">
        <router-view />
      </a-layout-content>
    </a-layout>
    <!-- <a-layout-footer :style="footerStyle">Footer</a-layout-footer> -->
  </a-layout>
</template>

<script setup>
import { ref, computed } from "vue";
import { useThemeStore } from "@/stores/theme";
import { BellOutlined } from '@ant-design/icons-vue';
import globalSider from "../common/global-sider/index.vue";
import globalLogo from "../common/global-logo/index.vue";
import GlobalHeader from "../common/global-header/index.vue";

const themeStore = useThemeStore();
const collapsed = ref(false);

// 计算布局样式
const layoutClass = computed(() => ({
  'layout-container': true,
  'dark-mode': themeStore.isDarkMode
}));

// 侧边栏样式
const siderStyle = computed(() => ({
  borderRight: `1px solid ${themeStore.isDarkMode ? '#303030' : '#f0f0f0'}`,
  overflow: 'auto',
  height: '100vh',
  position: 'fixed',
  left: 0,
  top: 0,
  bottom: 0,
  backgroundColor: themeStore.isDarkMode ? '#141414' : '#fff'
}));
</script>

<style lang="scss" scoped>
@use '@/styles/variables' as *;

.layout-container {
  :deep(.ant-layout-sider) {
    background: $component-bg;
    transition: all $animation-duration-base;
  }

  .layout-header {
    background: $component-bg;
    padding: 0;
    display: flex;
    align-items: center;
    justify-content: space-between;
    border-bottom: 1px solid $border-color;
    height: $header-height;
    position: sticky;
    top: 0;
    z-index: 100;
    transition: all $animation-duration-base;

    .header-left {
      .ant-breadcrumb {
        line-height: 64px;
      }
    }

    .header-right {
      display: flex;
      align-items: center;
    }
  }

  .layout-content {
    margin: 24px 24px 0;
    padding: 24px;
    background: $component-bg;
    border-radius: $border-radius-base;
    min-height: calc(100vh - $header-height - 48px);
    transition: all $animation-duration-base;
  }

  :deep(.ant-layout) {
    margin-left: $sidebar-width;
    transition: margin-left $animation-duration-base;

    &.ant-layout-has-sider {
      margin-left: $sidebar-collapsed-width;
    }
  }
}
</style>
