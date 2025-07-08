<template>
  <div class="common-container">
    <!-- <a-card :bordered="false"> -->
    <!-- 搜索区域 -->
    <a-form
      layout="inline"
      :model="queryParams"
      @finish="handleQuery"
      class="search-form"
    >
      <a-form-item label="用户名称" name="userName">
        <a-input
          v-model:value="queryParams.userName"
          placeholder="请输入用户名称"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="手机号码" name="phone">
        <a-input
          v-model:value="queryParams.phone"
          placeholder="请输入手机号码"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="状态" name="status">
        <a-select
          v-model:value="queryParams.status"
          placeholder="用户状态"
          allow-clear
          style="width: 200px"
        >
          <a-select-option value="0">正常</a-select-option>
          <a-select-option value="1">停用</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item>
        <a-space>
          <a-button type="primary" html-type="submit">
            <template #icon>
              <SearchOutlined />
            </template>
            搜索
          </a-button>
          <a-button @click="resetQuery">
            <template #icon>
              <ReloadOutlined />
            </template>
            重置
          </a-button>
        </a-space>
      </a-form-item>
    </a-form>

    <div class="common-table-container" ref="tableContainerRef">
      <!-- 操作按钮区域 -->
      <div style="margin-bottom: 16px" class="action-buttons">
        <a-space>
          <a-button type="primary" @click="handleAdd">
            <template #icon>
              <PlusOutlined />
            </template>
            新增
          </a-button>
          <a-button
            type="danger"
            :disabled="selectedRowKeys.length === 0"
            @click="handleBatchDelete"
          >
            <template #icon>
              <DeleteOutlined />
            </template>
            批量删除
          </a-button>
        </a-space>
        <!-- 表格工具 -->
        <TableTool
          @refresh="getList"
          v-model:tableSize="tableConfig.size"
          :tableColumns="tableConfig.columns"
          :fullScreenElement="tableContainerRef"
        />
      </div>

      <!-- 表格区域 -->
      <a-table
        :columns="tableColumns"
        :data-source="userList"
        :row-key="(record) => record.userId"
        :size="tableConfig.size"
        :pagination="{
          total: pagination.total,
          current: queryParams.pageNum,
          pageSize: queryParams.pageSize,
          showSizeChanger: true,
          showQuickJumper: true,
          showTotal: (total) => `共 ${total} 条`,
        }"
        :loading="loading"
        :row-selection="{
          selectedRowKeys: selectedRowKeys,
          onChange: onSelectChange,
        }"
        @change="handleTableChange"
        :scroll="{ x: 1300 }"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.dataIndex === 'status'">
            <a-tag :color="record.status === '0' ? 'green' : 'red'">
              {{ record.status === "0" ? "正常" : "停用" }}
            </a-tag>
          </template>
          <template v-else-if="column.dataIndex === 'sex'">
            {{ record.sex === "0" ? "男" : "女" }}
          </template>
          <template v-else-if="column.dataIndex === 'action'">
            <a-space>
              <a @click="handleEdit(record)">编辑</a>
              <a-divider type="vertical" />
              <a @click="handleDelete(record)">删除</a>
              <a-divider type="vertical" />
              <a @click="handleResetPassword(record)">重置密码</a>
            </a-space>
          </template>
        </template>
      </a-table>
      <!-- 引入用户表单组件 -->
    </div>
    <UserForm
      :open="open"
      :title="title"
      :userData="currentUser"
      :roleOptions="roleOptions"
      ref="UserFormRef"
      @update:open="open = $event"
      @success="handleFormSuccess"
    />
    
    <!-- </a-card> -->
  </div>
</template>
  
  <script setup>
import { h, onMounted, reactive, ref,getCurrentInstance } from "vue";
import { message, Modal } from "ant-design-vue";
import {
  DeleteOutlined,
  ExclamationCircleOutlined,
  PlusOutlined,
  ReloadOutlined,
  SearchOutlined,
} from "@ant-design/icons-vue";
import UserForm from "./components/UserForm.vue";
import {
  batchDeleteUser,
  deleteUser,
  getUserPage,
  resetPassword,
} from "@/api/modules/user";
import { getRoleList } from "@/api/modules/role";
import {
  handleDeletePagination,
  handleSingleDeletePagination,
} from "@/utils/pagination";
import { useAppStore } from "@/stores/app";

const appStore = useAppStore();

const tableContainerRef = ref(null)

// 查询参数
const queryParams = reactive({
  userName: "",
  phone: "",
  status: undefined,
  pageNum: 1,
  pageSize: 10,
});
//表格配置
const tableConfig = reactive({
  size: appStore.tableSize,
  columns: [
    {
      title: "id",
      dataIndex: "userId",
      key: "userId",
      width: 50,
      visible: true,
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
      width: 180,
      visible: true,
    },
    {
      title: "最后登录时间",
      dataIndex: "loginDate",
      key: "loginDate",
      width: 180,
      visible: true,
    },
    {
      title: "操作",
      dataIndex: "action",
      key: "action",
      fixed: "right",
      width: 220,
      visible: true,
    },
  ],
});

const tableColumns = computed(() => {
  return tableConfig.columns.filter((column) => column.visible);
});

// 用户列表数据
const userList = ref();

// 角色选项
const roleOptions = ref([]);

// 分页配置
const pagination = reactive({
  current: 1,
  pageSize: 10,
  total: 0,
  showSizeChanger: true,
  showTotal: (total) => `共 ${total} 条`,
});

// 加载状态
const loading = ref(false);

// 选中的行
const selectedRowKeys = ref([]);

// 弹窗相关
const open = ref(false);
const title = ref("添加用户");
const currentUser = ref({}); // 当前选中的用户数据

// 组件挂载时获取数据
onMounted(() => {
  getList();
  getRoles();
});

// 获取角色列表
const getRoles = () => {
  getRoleList().then((data) => {
    roleOptions.value = data;
  });
};

// 获取用户列表
const getList = () => {
  loading.value = true;
  getUserPage(queryParams)
    .then((data) => {
      loading.value = false;
      userList.value = data.records;
      pagination.total = data.total;
    })
    .catch((e) => {
      loading.value = false;
      message.error(e.message || "获取用户列表失败");
    });
};

// 处理查询
const handleQuery = () => {
  pagination.current = 1;
  getList();
};

// 重置查询
const resetQuery = () => {
  queryParams.username = "";
  queryParams.phone = "";
  queryParams.status = undefined;
  handleQuery();
};

// 表格变化事件
const handleTableChange = (pag) => {
  queryParams.pageNum = pag.current;
  queryParams.pageSize = pag.pageSize;
  getList();
};

// 选择行变化
const onSelectChange = (keys) => {
  selectedRowKeys.value = keys;
};

// 新增用户
const handleAdd = () => {
  title.value = "添加用户";
  currentUser.value = {};
  setTimeout(() => {
    open.value = true;
  }, 100);
};

// 编辑用户
const handleEdit = (record) => {
  title.value = "修改用户";
  currentUser.value = { ...record }; // 模拟角色数据
  setTimeout(() => {
    open.value = true;
  }, 100);
};

// 处理表单成功提交
const handleFormSuccess = (formData) => {
  console.log("表单数据:", formData);

  // 重新加载列表数据
  getList();
};

// 删除用户
const handleDelete = (record) => {
  Modal.confirm({
    title: "确认删除",
    icon: () => h(ExclamationCircleOutlined),
    content: `是否确认删除用户 "${record.userName}" ？`,
    okText: "确定",
    cancelText: "取消",
    onOk() {
      return deleteUser(record.userId)
        .then((res) => {
          message.success("删除成功");
          return handleSingleDeletePagination(
            userList.value,
            queryParams,
            getList
          );
        })
        .catch((error) => {
          message.error(error.message || "删除失败，请重试");
        });
    },
  });
};

// 批量删除
const handleBatchDelete = () => {
  const userIds = selectedRowKeys.value;
  Modal.confirm({
    title: "确认删除",
    icon: () => h(ExclamationCircleOutlined),
    content: `是否确认删除选中的 ${userIds.length} 个用户？`,
    okText: "确定",
    cancelText: "取消",
    onOk() {
      return batchDeleteUser(userIds)
        .then((res) => {
          message.success("批量删除成功");
          selectedRowKeys.value = [];
          return handleDeletePagination(
            userList.value,
            userIds,
            queryParams,
            getList
          );
        })
        .catch((error) => {
          message.error(error.message || "批量删除失败，请重试");
        });
    },
  });
};

// 重置密码
const handleResetPassword = (record) => {
  resetPassword(record.userId)
    .then((res) => {
      message.success(`重置密码：${record.userName}`);
    })
    .catch((error) => {
      message.error(error.message || "重置密码失败，请重试");
    });
};
</script>

<style lang="scss" scoped>

</style>

