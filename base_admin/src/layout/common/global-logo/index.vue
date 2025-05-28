<template>
  <div class="global-logo">
    <span class="logo-icon">B</span>
    <h1 v-if="!app.siderCollapse">Base Admin</h1>
  </div>
</template>

<script setup>
import { useAppStore } from "@/stores";
const app = useAppStore();
</script>

<style scoped lang="scss">
.global-logo {
  height: 64px;
  padding: 16px;
  text-align: center;
  margin-bottom: 4px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;

  h1 {
    margin: 0;
    font-size: 18px;
    font-weight: 600;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    -webkit-background-clip: text;
    // -webkit-text-fill-color: transparent;
  }
  .logo-icon {
    height: 32px;
    width: 32px;
    display: flex;
    align-items: center;
    justify-content: center;
    background-color: var(--primary-color, #1890ff);
    color: #fff;
    font-size: 20px;
    font-weight: bold;
    border-radius: 4px;
  }
}
</style>
