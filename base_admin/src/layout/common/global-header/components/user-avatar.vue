<template>
  <a-dropdown>
    <hover-container class="user-avatar">
      <a-avatar :style="{ backgroundColor: color, verticalAlign: 'middle' }" :size="32" :gap="12"
        src="http://120.27.215.0:8081/files/970c267a-ada5-4096-a709-4489a16f885c.gif">
      </a-avatar>
      <span class="username">admin</span>
    </hover-container>
    <template #overlay>
      <a-menu>
        <a-menu-item key="1">
          <SettingOutlined />
          个人中心
        </a-menu-item>
        <a-menu-item key="2">
          <LogoutOutlined />
          退出登录
        </a-menu-item>
      </a-menu>
    </template>
  </a-dropdown>
</template>

<script setup>
import {
  LogoutOutlined,
  SettingOutlined,
} from '@ant-design/icons-vue'
</script>

<style lang="scss" scoped>
.user-avatar {
  padding: 0 12px;

  .username {
    font-weight: 600;
    font-size: 16px;
    margin-left: 8px;
  }
}
</style>