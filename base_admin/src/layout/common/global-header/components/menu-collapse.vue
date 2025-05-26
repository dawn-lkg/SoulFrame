<template>
  <transition name="fade" mode="out-in">
    <div class="menu-collapse" :class="{ 'menu-collapse-collapsed': collapsed }">
      <MenuFoldOutlined v-if="collapsed" @click="toggleCollapsed" />
      <MenuUnfoldOutlined v-else @click="toggleCollapsed" />
    </div>
  </transition>
</template>

<script setup>  
import { MenuFoldOutlined, MenuUnfoldOutlined } from '@ant-design/icons-vue';
import { ref } from 'vue';

const collapsed = ref(false);

const toggleCollapsed = () => {
  collapsed.value = !collapsed.value;
};

</script>

<style lang="scss" scoped>
.menu-collapse {
  font-size: 20px;
  cursor: pointer;
  width: 40px;
  text-align: center;
}
.menu-collapse:hover {
  background-color: #f0f0f0;
}
</style>