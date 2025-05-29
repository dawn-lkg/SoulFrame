<template>
  <a-dropdown>
    <hover-container class="user-avatar">
      <a-avatar
        :style="{ backgroundColor: color, verticalAlign: 'middle' }"
        :gap="gap"
      >
        {{ avatarValue }}
      </a-avatar>
      <span>admin</span>
    </hover-container>
    <template #overlay>
      <a-menu>
        <a-menu-item key="1">Option 1</a-menu-item>
        <a-menu-item key="2">Option 2</a-menu-item>
        <a-menu-item key="3">Option 3</a-menu-item>
        <a-menu-item key="4">Option 4</a-menu-item>
      </a-menu>
    </template>
  </a-dropdown>
</template>

<script>
</script>

<style>
.user-avatar{
    padding:0 12px;
}
</style>