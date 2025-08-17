<template>
  <div class="common-container">
    <CommonTable
        v-model:columns="columns"
        v-model:dataSource="data"
        v-model:pagination="tablePagination"
        v-model:selectedRowKeys="selectedRowKeys"
        :loading="loading"
        rowKey="userId"
        @change="handleTableChange"
        @query="handleQuery"
        @reset="handleReset">
      <template #searchForm>
        <a-form-item label="用户名称" name="userName">
          <a-input
              v-model:value="queryParams.userName"
              allow-clear
              placeholder="请输入用户名称"
          />
        </a-form-item>
        <a-form-item label="手机号码" name="phone">
          <a-input
              v-model:value="queryParams.phone"
              allow-clear
              placeholder="请输入手机号码"
          />
        </a-form-item>
        <a-form-item label="状态" name="status">
          <DictSelect
              v-model:value="queryParams.status"
              allow-clear
              dictType="sys_user_status"
              placeholder="用户状态"
              style="width: 200px"
          />
        </a-form-item>
      </template>
      <template #action-buttons>
        <a-button size="small" type="primary" @click="handleAdd">新增</a-button>
        <a-button
            :disabled="!selectedRowKeys.length"
            size="small"
            type="danger"
            @click="handleBatchDelete"
        >批量删除
        </a-button>
      </template>
      <template #bodyCell="{ column, record }">
        <span v-if="column.dataIndex === 'action'">
          <a-button size="small" type="link" @click="handleEdit(record)">编辑</a-button>
          <a-popconfirm
              cancel-text="取消"
              ok-text="确定"
              title="确定删除该用户吗？"
              @confirm="handleDelete(record)"
          >
            <a-button danger size="small" type="link">删除</a-button>
          </a-popconfirm>
          <a-button size="small" type="link" @click="handleResetPassword(record)">重置密码</a-button>
        </span>
      </template>
    </CommonTable>
  </div>
</template>

<script setup>
import {batchDeleteUser, deleteUser, getUserPage, resetPassword,} from "@/api/modules/user";
import {onMounted, reactive, ref, watch} from 'vue';
import {message} from 'ant-design-vue';

const columns = [
  {
    title: "id",
    dataIndex: "userId",
    key: "userId",
    width: 50,
    visible: false,
  },
  {
    title: "用户名称",
    dataIndex: "userName",
    key: "userName",
    width: 150,
    visible: true,
  },
  {
    title: "用户昵称",
    dataIndex: "nickName",
    key: "nickName",
    width: 150,
    visible: true,
  },
  {
    title: "邮箱",
    dataIndex: "email",
    key: "email",
    width: 150,
    ellipsis: true,
    visible: true,
  },
  {
    title: "性别",
    dataIndex: "sex",
    key: "sex",
    width: 100,
    visible: true,
  },
  {
    title: "手机号码",
    dataIndex: "phone",
    key: "phone",
    width: 150,
    visible: true,
  },
  {
    title: "状态",
    dataIndex: "status",
    key: "status",
    width: 100,
    visible: true,
  },
  {
    title: "最后登录ip",
    dataIndex: "loginIp",
    key: "loginIp",
    width: 150,
    visible: true,
  },
  {
    title: "最后登录时间",
    dataIndex: "loginDate",
    key: "loginDate",
    width: 150,
    visible: true,
  },
  {
    title: "操作",
    dataIndex: "action",
    key: "action",
    fixed: "right",
    width: 240,
    visible: true,
  },
];
const data = ref([]);
const loading = ref(false);
const selectedRowKeys = ref([]);
const tablePagination = reactive({
  current: 1,
  pageSize: 10,
  total: 0,
  showSizeChanger: true,
  showTotal: (total) => `共 ${total} 条`,
});
const queryParams = ref({
  userName: '',
  phone: '',
  status: undefined,
  pageNum: tablePagination.current,
  pageSize: tablePagination.pageSize,
});


watch(selectedRowKeys, (newVal) => {
  console.log('selectedRowKeys', newVal);
})

const getList = () => {
  console.log('queryParams', queryParams.value);

  loading.value = true;
  getUserPage(queryParams.value).then(res => {
    console.log(res);
    if (res && res.records) {
      data.value = res.records;
      tablePagination.total = res.total || 0;
    } else {
      console.error('获取数据失败或数据格式不正确', res);
      data.value = [];
      tablePagination.total = 0;
    }
  }).catch(error => {
    console.error('获取数据出错:', error);
    data.value = [];
    tablePagination.total = 0;
  }).finally(() => {
    loading.value = false;
  });
}

// 处理表格变化（排序、筛选、分页等）
const handleTableChange = ({pagination}) => {
  console.log('表格变化', pagination);
  queryParams.value.pageNum = pagination.current;
  queryParams.value.pageSize = pagination.pageSize;
  getList();
};

// 处理查询事件
const handleQuery = (params) => {
  console.log('父组件收到查询事件', params);
  queryParams.value.pageNum = 1;
  tablePagination.current = 1;
  getList();
}

// 处理重置事件
const handleReset = () => {
  console.log('父组件收到重置事件');
  queryParams.value = {
    userName: '',
    phone: '',
    status: undefined,
    pageNum: 1,
    pageSize: tablePagination.pageSize,
  };
  tablePagination.current = 1;
  selectedRowKeys.value = [];
  getList();
}

// 处理添加
const handleAdd = () => {
  console.log('添加');
  // TODO: 实现添加用户的逻辑
}

// 处理编辑
const handleEdit = (record) => {
  console.log('编辑', record);
  // TODO: 实现编辑用户的逻辑
}

// 处理删除
const handleDelete = (record) => {
  console.log('删除', record);
  deleteUser(record.userId).then(res => {
    if (res.code === 200) {
      message.success('删除成功');
      getList();
    } else {
      message.error(res.msg || '删除失败');
    }
  }).catch(error => {
    console.error('删除出错:', error);
    message.error('删除失败');
  });
}

// 处理批量删除
const handleBatchDelete = () => {
  if (!selectedRowKeys.value.length) {
    message.warning('请选择要删除的用户');
    return;
  }

  batchDeleteUser(selectedRowKeys.value).then(res => {
    if (res.code === 200) {
      message.success('批量删除成功');
      selectedRowKeys.value = [];
      getList();
    } else {
      message.error(res.msg || '批量删除失败');
    }
  }).catch(error => {
    console.error('批量删除出错:', error);
    message.error('批量删除失败');
  });
}

// 处理重置密码
const handleResetPassword = (record) => {
  console.log('重置密码', record);
  resetPassword(record.userId).then(res => {
    if (res.code === 200) {
      message.success('密码重置成功');
    } else {
      message.error(res.msg || '密码重置失败');
    }
  }).catch(error => {
    console.error('密码重置出错:', error);
    message.error('密码重置失败');
  });
}

onMounted(() => {
  getList();
})
</script>

<style scoped>
.common-container {
  padding: 16px;
  background-color: #fff;
  border-radius: 4px;
  margin: 16px;
}
</style>