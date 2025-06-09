<template>
  <a-layout :class="layoutClass">
    <!-- 左侧菜单 -->
    <a-layout-sider v-model:collapsed="app.siderCollapse" :theme="themeStore.menuTheme"
      :collapsedWidth="app.collapsedWidth" collapsible :style="siderStyle" class="layout-sider">
      <global-logo />
      <global-sider />
    </a-layout-sider>
    <!-- 右侧内容 -->
    <a-layout :style="layoutStyle">
      <!-- 头部 -->
      <a-layout-header class="layout-header">
        <global-header />
      </a-layout-header>
      <!-- 头部tab-->
      <global-tab v-if="themeStore.layoutConfig.showTagsView" />
      <!-- 内容 -->
      <a-layout-content class="layout-content">
        <router-view />
      </a-layout-content>
    </a-layout>
    <!-- <a-layout-footer :style="footerStyle">Footer</a-layout-footer> -->
    <setting-drawer />
  </a-layout>
</template>

<script setup>
import { ref, computed } from "vue";
import { useThemeStore, useAppStore } from "@/stores";
import { BellOutlined } from '@ant-design/icons-vue';
import globalSider from "../common/global-sider/index.vue";
import globalLogo from "../common/global-logo/index.vue";
import GlobalHeader from "../common/global-header/index.vue";
import SettingDrawer from "../common/setting-drawer/index.vue";
import globalTab from "../common/global-tab/index.vue"

const themeStore = useThemeStore();
const app = useAppStore()

// 计算布局样式
const layoutClass = computed(() => ({
  'layout-container': true,
  'dark-mode': themeStore.isDarkMode
}));

// 侧边栏样式
const siderStyle = computed(() => ({
  backgroundColor: themeStore.menuTheme === 'dark' ? '#001529' : '#fff',
  borderRight: `1px solid ${themeStore.menuTheme === 'dark' ? '#1e1e1e' : '#f0f0f0'}`,
  color: themeStore.menuTheme === 'dark' ? '#fff' : '#000',
  position: themeStore.layoutConfig.fixedSidebar ? 'fixed' : 'static',
  left: themeStore.layoutConfig.fixedSidebar ? 0 : 'auto',
  width: app.siderCollapse ? `${app.collapsedWidth}px` : `${app.siderWidth}px`,
}));
// 布局样式
const layoutStyle = computed(() => ({
  marginLeft: themeStore.layoutConfig.fixedSidebar ? `${app.siderCollapse ? app.collapsedWidth : app.siderWidth}px` : '0',
}));
</script>

<style lang="scss" scoped>
@use '@/styles/variables' as *;

.layout-container {
  .layout-sider {
    height: 100vh;
    z-index: 101;
    transition: all $animation-duration-base;

    &:deep(.ant-layout-sider-children) {
      display: flex;
      flex-direction: column;
      height: 100%;
      background: inherit;
    }

    &:deep(.ant-menu) {
      flex: 1;
      overflow-x: hidden;
      overflow-y: auto;
      background: inherit;

      &::-webkit-scrollbar {
        width: 6px;
        height: 6px;
      }

      &::-webkit-scrollbar-thumb {
        background: rgba(0, 0, 0, 0.12);
        border-radius: 3px;

        .dark-mode & {
          background: rgba(255, 255, 255, 0.08);
        }
      }

      &::-webkit-scrollbar-track {
        background: transparent;
      }
    }

    &:deep(.ant-layout-sider-trigger) {
      border-right: 1px solid;
      border-color: inherit;
      background: inherit;
    }
  }

  .layout-header {
    background: $component-bg;
    padding: 0;
    display: flex;
    align-items: center;
    justify-content: space-between;
    height: $header-height;
    z-index: 100;
    transition: all $animation-duration-base;
    box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);

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
    // margin-left: $sidebar-width;
    transition: margin-left $animation-duration-base;

    &.ant-layout-has-sider {
      margin-left: $sidebar-collapsed-width;
    }
  }
}

// 暗黑模式特定样式
:deep(.dark-mode) {
  .layout-sider {
    box-shadow: 1px 0 8px rgba(0, 0, 0, 0.15);

    .ant-menu {

      &-item,
      &-submenu-title {
        &:hover {
          background-color: rgba(255, 255, 255, 0.08) !important;
        }

        &.ant-menu-item-selected {
          background-color: #177ddc !important;
        }
      }

      &-submenu-popup {
        background-color: #001529;
      }

      &-inline {
        background: inherit;
      }
    }
  }

  .layout-header {
    box-shadow: 0 1px 4px rgba(0, 0, 0, 0.15);
  }
}
</style>
