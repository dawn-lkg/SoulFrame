<template>
  <div class="common-table-container">
    <a-form class="search-form" layout="inline" @finish="handleQuery">
      <slot name="searchForm"></slot>
      <a-form-item>
        <a-space>
          <a-button type="primary" @click="handleQuery">
            <template #icon>
              <search-outlined/>
            </template>
            搜索
          </a-button>
          <a-button @click="resetQuery">
            <template #icon>
              <redo-outlined/>
            </template>
            重置
          </a-button>
        </a-space>
      </a-form-item>
    </a-form>
    <div ref="tableContainerRef" class="common-table-container">
      <div class="action-buttons">
        <a-space>
          <slot name="action-buttons"></slot>
        </a-space>
        <TableTool
            v-model:tableSize="tableSize"
            :fullScreenElement="tableContainerRef"
            :tableColumns="tableColumns"
            @refresh="handleQuery"
        />
      </div>
      <a-table
          :columns="showColumns"
          :data-source="dataSource"
          :loading="loading"
          :pagination="pagination"
          :row-key="rowKey"
          :row-selection="{
          selectedRowKeys: selectedRowKeys,
          onChange: onSelectChange,
        }"
          :size="tableSize"
          @change="handleTableChange"
      >
        <template #bodyCell="{ column, record }">
          <slot :column="column" :record="record" name="bodyCell"></slot>
        </template>
      </a-table>
    </div>
  </div>
</template>

<script setup>
import {SearchOutlined, RedoOutlined} from "@ant-design/icons-vue";

const appStore = useAppStore();
const tableContainerRef = ref(null);
const tableSize = ref(appStore.componentSize);

const props = defineProps({
  dataSource: {
    type: Array,
    default: () => [],
  },
  loading: {
    type: Boolean,
    default: false,
  },
  columns: {
    type: Array,
    default: () => [],
  },
  searchForm: {
    type: Object,
    default: () => {
    },
  },
  pagination: {
    type: Object,
    default: () => {
      return {
        current: 1,
        pageSize: 10,
        total: 0,
        showSizeChanger: true,
        showTotal: (total) => `共 ${total} 条`,
      };
    },
  },
  rowKey: {
    type: String,
    default: () => "id",
  },
  selectedRowKeys: {
    type: Array,
    default: () => [],
  },
  scroll: {
    type: Object,
    default: () => {
    },
  },
});
const tableColumns = ref(props.columns);

const showColumns = computed(() => {
  return tableColumns.value.filter((item) => item.visible);
});

const emit = defineEmits([
  "query",
  "reset",
  "update:loading",
  "change",
  "update:selectedRowKeys",
  "update:scroll",
  "update:pagination",
  "update:columns",
]);

const handleQuery = () => {
  console.log("搜索");
  emit("query");
};

const resetQuery = () => {
  console.log("重置");
  emit("reset");
};

// 处理表格变化（排序、筛选、分页等）
const handleTableChange = (pagination, filters, sorter) => {
  emit("change", {pagination, filters, sorter});
  // 更新分页信息
  emit("update:pagination", pagination);
};

const onSelectChange = (selectedRowKeys) => {
  console.log("selectedRowKeys", selectedRowKeys);
  emit("update:selectedRowKeys", selectedRowKeys);
};

// 监听列变化
const updateColumns = () => {
  emit("update:columns", tableColumns.value);
};
</script>

<style scoped>
.common-table-container {
  padding: 16px;
  background-color: #fff;
  border-radius: 4px;
}

.action-buttons {
  display: flex;
  justify-content: space-between;
}
</style>