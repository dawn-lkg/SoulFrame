<template>
  <transition name="fade" mode="out-in">
    <hover-container @click="app.toggleSiderCollapse">
      <div class="menu-collapse" :class="{ 'menu-collapse-collapsed': collapsed }">
        <MenuFoldOutlined v-if="collapsed"  />
        <MenuUnfoldOutlined v-else />
      </div>
    </hover-container>
  </transition>
</template>

<script setup>  
import { MenuFoldOutlined, MenuUnfoldOutlined } from '@ant-design/icons-vue';
import { ref } from 'vue';
import { useAppStore } from '@/stores';
const app = useAppStore();

const collapsed = ref(false);


</script>

<style lang="scss" scoped>
.menu-collapse {
  font-size: 20px;
  cursor: pointer;
  width: 40px;
  text-align: center;
}
</style>