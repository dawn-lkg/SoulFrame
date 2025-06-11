<template>
  <div class="global-header">
    <div class="header-left">
      <MenuCollapse />
      <GlobalBreadcrumb v-if="themeStore.layoutConfig.showBreadcrumb" />
    </div>
    <div class="header-right">
      <ThemeMode />
      <SettingButton />
      <user-avatar />
    </div>
  </div>
</template>

<script setup>
import MenuCollapse from './components/menu-collapse.vue';
import GlobalBreadcrumb from './components/global-breadcrumb.vue';
import SettingButton from './components/setting-button.vue';
import ThemeMode from './components/theme-mode.vue';
import userAvatar from './components/user-avatar.vue';
import { useThemeStore, useAppStore } from "@/stores";
const themeStore = useThemeStore();
</script>

<style lang="scss" scoped>
@use '@/styles/variables' as *;
.global-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0;
  height: $header-height;
  width: 100%;
  background-color: #fff;
  // border-bottom: 1px solid #f0f0f0;

  .header-left {
    height: 100%;
    display: flex;
    align-items: center;
  }

  .header-right {
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
  }
}
</style>
