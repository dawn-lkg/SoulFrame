<template>
  <div class="global-tab">
    <a-tabs v-model:activeKey="activeKey" type="editable-card" size="small" :hideAdd="true">
      <a-tab-pane key="1" :closable="false">
        <template #tab>
          <AppstoreOutlined />
          <span class="tab-text">首页</span>
        </template>
      </a-tab-pane>
      <a-tab-pane key="2" tab="用户管理" :closable="false"></a-tab-pane>
      <a-tab-pane key="3" tab="菜单管理" :closable="false"></a-tab-pane>
    </a-tabs>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import {
  AppstoreOutlined,
  CloseCircleOutlined,
  CloseOutlined,
  CloseSquareOutlined,
  ReloadOutlined
} from '@ant-design/icons-vue'

const activeKey = ref('1')
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

    .anticon {
      margin-right: 4px;
    }
  }
}
</style>