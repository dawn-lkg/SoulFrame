<template>
  <div>
    <!-- <a-card title="文件管理" :bordered="false"> -->
    <a-row :gutter="16">
      <!-- 左侧筛选区域 -->
      <a-col :span="5">
        <a-card
          title="文件类型"
          :bordered="false"
          class="filter-card"
          style="height: 100vh"
        >
          <a-menu
            v-model:selectedKeys="selectedFileTypes"
            mode="inline"
            @select="handleFileTypeSelect"
          >
            <a-menu-item key="all">
              <template #icon><folder-outlined /></template>
              全部文件
            </a-menu-item>
            <a-menu-item key="image">
              <template #icon><picture-outlined /></template>
              图片
            </a-menu-item>
            <a-menu-item key="document">
              <template #icon><file-text-outlined /></template>
              文档
            </a-menu-item>
            <a-menu-item key="video">
              <template #icon><video-camera-outlined /></template>
              视频
            </a-menu-item>
            <a-menu-item key="audio">
              <template #icon><sound-outlined /></template>
              音频
            </a-menu-item>
            <a-menu-item key="other">
              <template #icon><file-unknown-outlined /></template>
              其他
            </a-menu-item>
          </a-menu>

          <a-divider />

          <div class="storage-info">
            <h4>存储空间</h4>
            <a-progress
              :percent="storageUsage"
              :stroke-color="getStorageColor()"
            />
            <div class="storage-text">
              <span>已用: {{ formatFileSize(usedStorage) }}</span>
              <span>总共: {{ formatFileSize(totalStorage) }}</span>
            </div>
          </div>
        </a-card>
      </a-col>

      <!-- 右侧主内容区域 -->
      <a-col :span="19">
        <div class="common-container">
          <!-- 搜索表单 -->
          <a-form layout="inline" :model="queryParams" class="search-form">
            <a-form-item label="文件名称">
              <a-input
                v-model:value="queryParams.fileName"
                placeholder="请输入文件名称"
              />
            </a-form-item>
            <a-form-item label="上传时间">
              <a-range-picker v-model:value="queryParams.dateRange" />
            </a-form-item>
            <a-form-item>
              <a-space>
                <a-button type="primary" @click="handleQuery">
                  <template #icon><search-outlined /></template>
                  查询
                </a-button>
                <a-button @click="resetQuery">
                  <template #icon><reload-outlined /></template>
                  重置
                </a-button>
              </a-space>
            </a-form-item>
          </a-form>

          <!-- 操作按钮 -->
          <div class="action-buttons">
            <a-space>
              <a-upload
                name="file"
                :multiple="true"
                :show-upload-list="false"
                :customRequest="customUpload"
              >
                <a-button type="primary">
                  <template #icon><upload-outlined /></template>
                  上传文件
                </a-button>
              </a-upload>
              <a-button
                danger
                :disabled="!selectedRowKeys.length"
                @click="handleBatchDelete"
              >
                <template #icon><delete-outlined /></template>
                批量删除
              </a-button>
            </a-space>

            <!-- 视图切换 -->
            <a-radio-group v-model:value="viewMode" button-style="solid">
              <a-radio-button value="table">
                <template #icon><table-outlined /></template>
                表格视图
              </a-radio-button>
              <a-radio-button value="card">
                <template #icon><appstore-outlined /></template>
                卡片视图
              </a-radio-button>
            </a-radio-group>
          </div>

          <!-- 表格视图 -->
          <a-table
            v-if="viewMode === 'table'"
            :columns="columns"
            :data-source="filteredFileList"
            :row-key="(record) => record.fileId"
            :pagination="pagination"
            :loading="loading"
            :row-selection="{ selectedRowKeys, onChange: onSelectChange }"
            @change="handleTableChange"
          >
            <template #bodyCell="{ column, record }">
              <template v-if="column.key === 'preview'">
                <a-image
                  v-if="isImage(record.fileType)"
                  :width="40"
                  :src="record.fileUrl"
                  :preview="{ src: record.fileUrl }"
                />
                <file-outlined v-else />
              </template>
              <template v-else-if="column.key === 'fileSize'">
                {{ formatFileSize(record.fileSize) }}
              </template>
              <template v-else-if="column.key === 'status'">
                <a-tag :color="FILE_STATUS[record.status].color">
                  {{ FILE_STATUS[record.status].text }}
                </a-tag>
              </template>
              <template v-else-if="column.key === 'action'">
                <a-space>
                  <a @click="handlePreview(record)">预览</a>
                  <a @click="handleDownload(record)">下载</a>
                  <a-popconfirm
                    title="确定要删除该文件吗？"
                    @confirm="handleDelete(record)"
                    ok-text="确定"
                    cancel-text="取消"
                  >
                    <a>删除</a>
                  </a-popconfirm>
                </a-space>
              </template>
            </template>
          </a-table>

          <!-- 卡片视图 -->
          <div v-else class="file-cards">
            <a-row :gutter="16">
              <a-col
                :span="4"
                v-for="file in filteredFileList"
                :key="file.fileId"
              >
                <a-card hoverable class="file-card">
                  <template #cover>
                    <div class="file-preview" @click="handlePreview(file)">
                      <a-image
                        v-if="isImage(file.fileType)"
                        :src="file.fileUrl"
                        :preview="false"
                        height="120"
                      />
                      <div v-else class="file-icon">
                        <file-outlined />
                        <div class="file-type">{{ file.fileType }}</div>
                      </div>
                    </div>
                  </template>
                  <a-card-meta :title="file.fileName">
                    <template #description>
                      <div>{{ formatFileSize(file.fileSize) }}</div>
                      <div>{{ file.createTime }}</div>
                    </template>
                  </a-card-meta>
                  <template #actions>
                    <eye-outlined key="preview" @click="handlePreview(file)" />
                    <download-outlined
                      key="download"
                      @click="handleDownload(file)"
                    />
                    <delete-outlined
                      key="delete"
                      @click="confirmDelete(file)"
                    />
                  </template>
                </a-card>
              </a-col>
            </a-row>
          </div>
        </div>
      </a-col>
    </a-row>
    <!-- </a-card> -->

    <!-- 预览弹窗 -->
    <file-preview-modal
      v-model:visible="previewVisible"
      :file="currentFile"
      @close="previewVisible = false"
    />
  </div>
</template>
  
  <script setup>
  import {computed, onMounted, reactive, ref} from "vue";
  import {message} from "ant-design-vue";
  import {
    AppstoreOutlined,
    DeleteOutlined,
    DownloadOutlined,
    EyeOutlined,
    FileOutlined,
    FileTextOutlined,
    FileUnknownOutlined,
    FolderOutlined,
    PictureOutlined,
    ReloadOutlined,
    SearchOutlined,
    SoundOutlined,
    TableOutlined,
    UploadOutlined,
    VideoCameraOutlined,
  } from "@ant-design/icons-vue";
  import FilePreviewModal from "./components/FilePreviewModal.vue";
  import {getFilePage, getFileUrl, uploadFile} from "@/api/modules/file";
  import {FILE_STATUS} from "@/config";

  // 视图模式
const viewMode = ref("table");

// 查询参数
const queryParams = reactive({
  fileName: "",
  fileType: undefined,
  dateRange: [],
  pageNum: 1,
  pageSize: 10,
});

// 文件类型筛选
const selectedFileTypes = ref(["all"]);

// 存储空间信息
const usedStorage = ref(1024 * 1024 * 1024 * 2.5); // 2.5GB
const totalStorage = ref(1024 * 1024 * 1024 * 10); // 10GB
const storageUsage = computed(() => {
  return Math.round((usedStorage.value / totalStorage.value) * 100);
});

// 获取存储空间颜色
const getStorageColor = () => {
  const usage = storageUsage.value;
  if (usage < 60) {
    return "#52c41a"; // 绿色
  } else if (usage < 80) {
    return "#faad14"; // 黄色
  } else {
    return "#f5222d"; // 红色
  }
};

// 加载状态
const loading = ref(false);

// 选中行的键
const selectedRowKeys = ref([]);

// 当前预览的文件
const currentFile = ref({});

// 预览弹窗可见性
const previewVisible = ref(false);

// 分页配置
const pagination = reactive({
  current: 1,
  pageSize: 10,
  total: 0,
  showSizeChanger: true,
  showQuickJumper: true,
  showTotal: (total) => `共 ${total} 条`,
});

// 表格列定义
const columns = [
  {
    title: "预览",
    dataIndex: "preview",
    key: "preview",
    width: 80,
  },
  {
    title: "文件名",
    dataIndex: "originalName",
    key: "originalName",
    ellipsis: true,
  },
  {
    title: "文件类型",
    dataIndex: "fileExtension",
    key: "fileExtension",
    width: 100,
  },
  {
    title: "文件大小",
    dataIndex: "fileSize",
    key: "fileSize",
    width: 120,
    sorter: true,
  },
  {
    title: "上传者",
    dataIndex: "userName",
    key: "userName",
    width: 120,
  },
  {
    title: "上传时间",
    dataIndex: "createTime",
    key: "createTime",
    width: 180,
    sorter: true,
  },
  {
    title: "状态",
    dataIndex: "status",
    key: "status",
    width: 80,
  },
  {
    title: "操作",
    dataIndex: "action",
    key: "action",
    width: 180,
    fixed: "right",
  },
];

// 模拟文件列表数据
const fileList = ref([]);

// 分页获取文件列表
const getFileList = async () => {
  loading.value = true;
  try { 
    const data = await getFilePage(queryParams);
    fileList.value = data.records;
    pagination.total = data.total;
  } catch (error) {
    message.error(error.message || '获取文件列表失败')
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  getFileList();
});

const customUpload = (options) => {
  console.log(options);
  const { file, onSuccess, onError } = options;
  // const formData = new FormData();
  // formData.append('file', file);
  // formData.append('fileName', file.name);
  // formData.append('fileType', file.type);
  uploadFile(file).then(data => {
    console.log(data);
  })
};

// 根据选择的文件类型过滤文件列表
const filteredFileList = computed(() => {
  if (selectedFileTypes.value.includes("all")) {
    return fileList.value;
  }
  return fileList.value.filter((file) =>
    selectedFileTypes.value.includes(file.fileType)
  );
});

// 处理文件类型选择
const handleFileTypeSelect = ({ selectedKeys }) => {
  selectedFileTypes.value = selectedKeys;
  // 重置分页
  pagination.current = 1;
};

// 组件挂载时执行
onMounted(() => {
  // 在实际项目中，这里应该调用API获取文件列表
  // getFileList()
  pagination.total = filteredFileList.value.length;

  // 计算已用存储空间
  calculateUsedStorage();
});

// 计算已用存储空间
const calculateUsedStorage = () => {
  usedStorage.value = fileList.value.reduce(
    (total, file) => total + file.fileSize,
    0
  );
};

// 判断是否为图片文件
const isImage = (fileType) => {
  return fileType === "image";
};

// 格式化文件大小
const formatFileSize = (size) => {
  if (size < 1024) {
    return size + " B";
  } else if (size < 1024 * 1024) {
    return (size / 1024).toFixed(2) + " KB";
  } else if (size < 1024 * 1024 * 1024) {
    return (size / (1024 * 1024)).toFixed(2) + " MB";
  } else {
    return (size / (1024 * 1024 * 1024)).toFixed(2) + " GB";
  }
};

// 处理查询
const handleQuery = () => {
  queryParams.pageNum = 1;
  // 实际项目中应该调用API
  // getFileList()
  message.success("查询成功");
};

// 重置查询
const resetQuery = () => {
  queryParams.fileName = "";
  queryParams.fileType = undefined;
  queryParams.dateRange = [];
  queryParams.pageNum = 1;
  // 实际项目中应该调用API
  // getFileList()
  message.success("重置成功");
};

// 表格变化事件
const handleTableChange = (pag, filters, sorter) => {
  pagination.current = pag.current;
  pagination.pageSize = pag.pageSize;
  queryParams.pageNum = pag.current;
  queryParams.pageSize = pag.pageSize;

  // 处理排序
  if (sorter.field) {
    queryParams.orderByColumn = sorter.field;
    queryParams.isAsc = sorter.order === "ascend" ? "asc" : "desc";
  } else {
    queryParams.orderByColumn = undefined;
    queryParams.isAsc = undefined;
  }

  // 实际项目中应该调用API
  // getFileList()
};

// 选择行变化
const onSelectChange = (keys) => {
  selectedRowKeys.value = keys;
};

// 处理文件预览
const handlePreview = (file) => {
  currentFile.value = file;
  previewVisible.value = true;
};

// 处理文件下载
const handleDownload =async (file) => {
  message.success(`正在下载: ${file.fileName}`);
  // 实际项目中应该调用API
  // downloadFile(file.fileId)
  try {
    const data = await getFileUrl(file.fileId)
    const link = document.createElement("a");
    link.href = data;
    link.download = file.originalName;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  } catch (error) {
    message.error(error.message || '获取文件url失败')
  }
};

// 处理文件删除
const handleDelete = (file) => {
  message.success(`删除文件: ${file.fileName}`);
  // 实际项目中应该调用API
  // deleteFile(file.fileId).then(() => {
  //   getFileList()
  // })

  // 模拟删除
  fileList.value = fileList.value.filter((item) => item.fileId !== file.fileId);
  pagination.total = fileList.value.length;
  calculateUsedStorage(); // 更新已用存储空间
};

// 批量删除确认
const handleBatchDelete = () => {
  if (selectedRowKeys.value.length === 0) {
    message.warning("请至少选择一个文件");
    return;
  }

  message.success(`批量删除文件: ${selectedRowKeys.value.join(", ")}`);
  // 实际项目中应该调用API
  // batchDeleteFiles(selectedRowKeys.value).then(() => {
  //   getFileList()
  // })

  // 模拟批量删除
  fileList.value = fileList.value.filter(
    (item) => !selectedRowKeys.value.includes(item.fileId)
  );
  selectedRowKeys.value = [];
  pagination.total = fileList.value.length;
  calculateUsedStorage(); // 更新已用存储空间
};

// 确认删除（卡片视图使用）
const confirmDelete = (file) => {
  handleDelete(file);
};

// 处理上传变化
// const handleUploadChange = (info) => {
//   if (info.file.status === "uploading") {
//     loading.value = true;
//     return;
//   }

//   if (info.file.status === "done") {
//     loading.value = false;
//     message.success(`${info.file.name} 上传成功`);
//     // 实际项目中应该调用API刷新列表
//     // getFileList()

//     // 模拟添加新文件
//     const newFile = {
//       fileId: fileList.value.length + 1,
//       fileName: info.file.name,
//       originalName: info.file.name,
//       fileType: getFileType(info.file.name),
//       fileSize: info.file.size || 1024 * 1024 * Math.random() * 10, // 随机大小
//       fileUrl: URL.createObjectURL(info.file.originFileObj),
//       createBy: "当前用户",
//       createTime: new Date().toLocaleString(),
//       status: 0,
//     };
//     fileList.value.unshift(newFile);
//     pagination.total = fileList.value.length;
//     calculateUsedStorage(); // 更新已用存储空间
//   } else if (info.file.status === "error") {
//     loading.value = false;
//     message.error(`${info.file.name} 上传失败`);
//   }
// };

// 获取文件类型
const getFileType = (fileName) => {
  const extension = fileName.split(".").pop().toLowerCase();
  const imageExts = ["jpg", "jpeg", "png", "gif", "bmp", "webp"];
  const docExts = ["doc", "docx", "xls", "xlsx", "ppt", "pptx", "pdf", "txt"];
  const videoExts = ["mp4", "avi", "mov", "wmv", "flv", "mkv"];
  const audioExts = ["mp3", "wav", "ogg", "flac", "aac"];

  if (imageExts.includes(extension)) return "image";
  if (docExts.includes(extension)) return "document";
  if (videoExts.includes(extension)) return "video";
  if (audioExts.includes(extension)) return "audio";
  return "other";
};
</script>
  
  <style lang="scss" scoped>
.action-buttons {
  display: flex;
  justify-content: space-between;
  margin-bottom: 16px;
}

.file-cards {
  margin-top: 16px;
}

.file-card {
  margin-bottom: 16px;

  .file-preview {
    height: 120px;
    display: flex;
    align-items: center;
    justify-content: center;
    background-color: #f5f5f5;
    cursor: pointer;
  }

  .file-icon {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;

    .anticon {
      font-size: 36px;
      margin-bottom: 8px;
    }

    .file-type {
      font-size: 12px;
      color: #999;
    }
  }
}

.filter-card {
  margin-bottom: 16px;
}

.storage-info {
  margin-top: 16px;
  padding: 16px;
  background-color: #f5f5f5;
  border-radius: 4px;

  h4 {
    margin-bottom: 10px;
    font-size: 16px;
  }

  .storage-text {
    font-size: 14px;
    color: #666;
    margin-top: 10px;
  }
}
</style>