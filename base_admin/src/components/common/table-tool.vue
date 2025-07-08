<template>
  <div class="table-tool">
    <a-space>
      <a-tooltip title="刷新">
        <Icon name="ReloadOutlined" size="18" @click="emitRefresh" class="table-tool-icon" />
      </a-tooltip>
      <a-dropdown>
      <a-tooltip title="密度">
          <Icon name="ColumnHeightOutlined" size="18" class="table-tool-icon" />
        </a-tooltip>
        <template #overlay>
          <a-menu :selectedKeys="[tableSize]" @click="$emit('update:tableSize', $event.key)">
            <a-menu-item key="default" class="table-tool-menu-item">默认</a-menu-item>
            <a-menu-item key="middle" class="table-tool-menu-item">中等</a-menu-item>
            <a-menu-item key="small" class="table-tool-menu-item">小</a-menu-item>
          </a-menu>
        </template>
      </a-dropdown>
      <a-popover title="列设置" placement="bottom" trigger="click">
        <template #content>
            <div v-for="column in tableColumns" :key="column.dataIndex" >
            <a-checkbox :checked="column.visible" @change="handleColumnVisible(column)">
                {{ column.title }}
            </a-checkbox>
        </div>
        </template>
        <a-tooltip title="设置">
        <Icon name="SettingOutlined" size="18" class="table-tool-icon" />
      </a-tooltip>
      </a-popover>
      
      <a-tooltip :title="isFull ? '退出全屏' : '全屏'">
        <Icon :name="isFull ? 'FullscreenExitOutlined' : 'FullscreenOutlined'" size="18" class="table-tool-icon" @click="handleFullScreen" />
      </a-tooltip>
    </a-space>
  </div>
</template>

<script setup>

// 定义事件
const emit = defineEmits(['refresh','update:tableSize','update:tableColumns'])
// 定义属性
const props = defineProps({
  //表格大小
  tableSize: {
    type: String,
  },
  //表格列设置
  tableColumns: {
    type: Array,
  },
  //需要全屏的元素
  fullScreenElement: {
    type: Object,
  },
})

// 全屏
const isFull = ref(false)

// 全屏
const handleFullScreen = () => {
  if(isFull.value){
    isFull.value = false
    props.fullScreenElement.classList.remove('fullscreen-active')
  }else{
    isFull.value = true
    props.fullScreenElement.classList.add('fullscreen-active')
  }
}

// 刷新 
const emitRefresh = () => {
  emit('refresh')
}

// 列设置
const handleColumnVisible = (column) => {
    column.visible = !column.visible
    emit('update:tableColumns',[...props.tableColumns])
}
</script>

<style lang="scss" scoped>
.table-tool {
  display: flex;
  justify-content: space-between;
  align-items: center;
  cursor: pointer;
}

// 增加菜单项宽度
:deep(.table-tool-menu-item) {
  min-width: 80px;
}

// 图标悬浮变色效果
.table-tool-icon {
  transition: color 0.3s;
  
  &:hover {
    color: #1890ff; 
  }
}
</style>
