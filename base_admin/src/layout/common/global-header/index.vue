<template>
  <div class="global-header">
    <div class="header-left">
      <MenuCollapse />
      <GlobalBreadcrumb />
    </div>
    <div class="header-right">
      <svg-icon name="icon-bell" />
      <SettingButton />
    </div>
  </div>
</template>

<script setup>
import MenuCollapse from './components/menu-collapse.vue';
import GlobalBreadcrumb from './components/global-breadcrumb.vue';
import SettingButton from './components/setting-button.vue';
</script>

<style lang="scss" scoped>
.global-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0;
  height: 64px;
  width: 100%;
  background-color: #fff;
  border-bottom: 1px solid #f0f0f0;

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
