<template>
  <template v-for="item in menuList" :key="item.menuId">
    <a-sub-menu v-if="item.menuType==='M'" :key="item.path">
      <template #icon>
        <Icon :name="item.icon"/>
      </template>
      <template #title>
        <span class="menu-name">{{ item.menuName }}</span>
      </template>
      <subMenu :menuList="item.children" />
    </a-sub-menu>
    <a-menu-item v-if="item.menuType==='C'" @click="handleClickMenu(item)" :key="item.path">
      <template #icon>
        <Icon :name="item.icon"/>
      </template>
      <span class="menu-name">{{ item.menuName }}</span>
    </a-menu-item>
  </template>
</template>

<script setup>
import Icon from "@/components/common/icon.vue";
import { useRouter } from 'vue-router'
const router = useRouter()
const props = defineProps({
  menuList: {
    type: Array,
    required: true,
  },
});
const handleClickMenu = (item) => {
  console.log(item);
  
  router.push(item.path)
}
</script>

<style scoped>
.menu-name{
  user-select: none;
  transition: color 0.3s ease;
}

:deep(.ant-menu-item:hover .menu-name),
:deep(.ant-menu-submenu-title:hover .menu-name) {
  color: var(--ant-primary-color, #1890ff);
}

:deep(.ant-menu-dark .ant-menu-item:hover .menu-name),
:deep(.ant-menu-dark .ant-menu-submenu-title:hover .menu-name) {
  color: #fff;
  opacity: 0.85;
}
</style>
