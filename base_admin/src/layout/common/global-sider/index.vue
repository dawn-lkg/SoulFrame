<template>
  <div class="global-sider">
    <transition name="fade">
      <a-menu v-model:openKeys="state.openKeys" v-model:selectedKeys="state.selectedKeys" :inline-collapsed="state.collapsed"
              :theme="themeStore.menuTheme" class="custom-menu" mode="inline"
              @openChange="handleOpenChange">
        <sub-menu :menuList="menuList"/>
      </a-menu>
    </transition>
  </div>
</template>
<script setup>
import {onMounted, reactive, watch} from 'vue';
import {useThemeStore} from '@/stores/theme';
import {useAuthStore} from '@/stores/auth';
import SubMenu from './components/sub-menu.vue';
import {useRoute} from 'vue-router';

const themeStore = useThemeStore();
const authStore = useAuthStore();
const route = useRoute();
const state = reactive({
  collapsed: false,
  selectedKeys: [],
  openKeys: [],
  preOpenKeys: [],
});
const menuList = reactive(authStore.showMenus);

// 监听打开的菜单
watch(
  () => state.openKeys,
  (_val, oldVal) => {
    state.preOpenKeys = oldVal;
  },
);

//查找父级菜单Path
const findParentPath = (menuItems,path,parentPath=[]) => {
  for(const item of menuItems){
    if(item.path === path){
      return [...parentPath];
    }
    if(item.children&&item.children.length>0){
      const parentPathList = findParentPath(item.children,path,[...parentPath,item.path]);
      if(parentPathList.length>0){
        return parentPathList;
      }
    }
  }
  return [];
}
// 监听路由
watch(()=>route.path,(val,oldVal)=>{
  state.selectedKeys = [val];
  state.openKeys = [...new Set([...findParentPath(menuList,val),...state.openKeys])];
},
{
  immediate: true
}
)
// 监听菜单
onMounted(()=>{
  state.selectedKeys = [route.path];
  state.openKeys = [...new Set([...findParentPath(menuList,route.path),...state.openKeys])];
})
// 监听菜单收缩
const toggleCollapsed = () => {
  state.collapsed = !state.collapsed;
  state.openKeys = state.collapsed ? [] : state.preOpenKeys;
};
// 监听菜单展开
const handleOpenChange = (openKeys) => {
  state.openKeys = openKeys;
};
</script>

<style lang="scss" scoped>
.global-sider {
  height: 100%;
  display: flex;
  flex-direction: column;
  
  :deep(.custom-menu) {
    height: 100%;
    border-right: none;
    overflow-y: auto;
    overflow-x: hidden;
    flex: 1;

    // 自定义滚动条样式
    &::-webkit-scrollbar {
      width: 6px;
      height: 6px;
    }

    &::-webkit-scrollbar-thumb {
      background-color: rgba(0, 0, 0, 0.2);
      border-radius: 4px;
    }

    &::-webkit-scrollbar-track {
      background-color: transparent;
    }
    
    // 添加图标过渡效果
    .anticon {
      transition: all 0.3s cubic-bezier(0.645, 0.045, 0.355, 1);
    }
    
    .ant-menu-item:hover .anticon,
    .ant-menu-submenu-title:hover .anticon {
      transform: scale(1.15);
      color: var(--ant-primary-color, #1890ff);
    }
    
    &.ant-menu-dark .ant-menu-item:hover .anticon,
    &.ant-menu-dark .ant-menu-submenu-title:hover .anticon {
      color: #fff;
    }
    
    .ant-menu-item,.ant-menu-submenu-title{
      border-radius: 4px;
      transition: all 0.3s cubic-bezier(0.645, 0.045, 0.355, 1);
      position: relative;
      overflow: hidden;
      
      &::before {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background-color: var(--ant-primary-1, rgba(24, 144, 255, 0.1));
        opacity: 0;
        transition: opacity 0.3s cubic-bezier(0.645, 0.045, 0.355, 1);
        z-index: -1;
      }
      
      &:hover {
        background-color: transparent;
        &::before {
          opacity: 1;
        }
      }
      
      &:active {
        transform: scale(0.98);
      }
    }
    //选中样式
    .ant-menu-item-selected {
      background-color: rgba(0, 0, 0, 0);
      &::after{
        opacity: 1;
        transform: scaleY(1);
        transition: transform 0.15s cubic-bezier(0.645, 0.045, 0.355, 1), 
                   opacity 0.15s cubic-bezier(0.645, 0.045, 0.355, 1);
      }
      
      &::before {
        opacity: 0.5;
        background-color: var(--ant-primary-2, rgba(24, 144, 255, 0.2));
      }
      
      .anticon {
        color: var(--ant-primary-color, #1890ff);
      }
    }

    //暗色
    &.ant-menu-dark {
      .ant-menu-item, .ant-menu-submenu-title {
        &::before {
          background-color: rgba(255, 255, 255, 0.08);
        }
        
        &:hover {
          background-color: transparent;
        }
      }
      
      .ant-menu-item-selected {
        background-color: rgba(0, 0, 0, 0);
        &::after{
          opacity: 1;
          transform: scaleY(1);
          transition: transform 0.15s cubic-bezier(0.645, 0.045, 0.355, 1), 
                     opacity 0.15s cubic-bezier(0.645, 0.045, 0.355, 1);
        }
        
        &::before {
          opacity: 0.6;
          background-color: var(--ant-primary-color, #177ddc);
        }
        
        .anticon {
          color: #fff;
        }
      }

      // 暗色模式滚动条
      &::-webkit-scrollbar-thumb {
        background-color: rgba(255, 255, 255, 0.2);
      }
    }
  }
}

</style>
