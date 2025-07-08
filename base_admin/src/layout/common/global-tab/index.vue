<template>
  <div class="global-tab">
    <a-tabs v-model:activeKey="activeKey" type="editable-card" size="small" :hideAdd="true" @edit="handleTabEdit" @change="handleTabChange">
      <a-tab-pane v-for="item in tabList" :key="item.key" :closable="item.closable">
        <template #tab>
          <div class="tab-content">
            <Icon :name="item.icon" size="12" />
            <span class="tab-text">{{ item.tab }}</span>
          </div>
        </template>
      </a-tab-pane>
    </a-tabs>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  AppstoreOutlined,
  CloseCircleOutlined,
  CloseOutlined,
  CloseSquareOutlined,
  ReloadOutlined
} from '@ant-design/icons-vue'
import { useAuthStore } from '@/stores/auth'
import Icon from '@/components/common/icon.vue'
import { HOME_PATH } from '@/config'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()
const tabList = ref([])
const activeKey = ref('/home')

// 查找菜单项
const findMenuItem = (menus, path) => {
  for (const item of menus) {
    if (item.path === path) {
      return item
    }
    if (item.children && item.children.length > 0) {
      const found = findMenuItem(item.children, path)
      if (found) {
        return found
      }
    }
  }
  return null
}

// 处理标签页编辑
const handleTabEdit = (targetKey, action) => {
  console.log(targetKey, action);
  if (action === 'remove') {
    handleTabClose(targetKey)
  }
}

// 处理标签页关闭
const handleTabClose = (key) => {
  const targetIndex = tabList.value.findIndex(item => item.key === key)
  if(targetIndex === -1){
    return
  }
  tabList.value.splice(targetIndex, 1)

  if(activeKey.value === key){
    if(tabList.value.length){
      tabList.value.length
      activeKey.value = tabList.value[tabList.value.length - 1].key
      router.push(activeKey.value)
    }else{
      activeKey.value = HOME_PATH
      router.push(HOME_PATH)
    }
  }
}

// 处理标签页切换
const handleTabChange = (key) => {
  // 使用编程式导航跳转，避免页面刷新
  router.push(key)
}

watch(()=>route.path,(newPath)=>{
  activeKey.value = newPath
  const menuItem = findMenuItem(authStore.showMenus, newPath)
  if(!menuItem){
    return
  }

  if(tabList.value.find(item=>item.key===newPath)){
    return
  }
  tabList.value.push({
    key: newPath,
    tab: menuItem.menuName,
    icon: menuItem.icon,
    closable: true
  })
},
{
  immediate: true
}
)
</script>

<style lang="scss" scoped>
.global-tab {
  background-color: #fff;
  padding: 0 16px;

  :deep(.ant-tabs-nav) {
    margin: 0;
  }

  :deep(.ant-tabs-tab) {
    padding: 8px 16px;
    cursor: pointer;
  }
  
  .tab-content {
    display: flex;
    align-items: center;
    
    .tab-text {
      
    }
  }
}
</style>