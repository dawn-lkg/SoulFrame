<template>
  <span class="file-icon">
    <component :is="iconComponent" :style="{ color: iconColor }"/>
  </span>
</template>

<script setup>
import {computed} from 'vue';
import {
  FileExcelOutlined,
  FileImageOutlined,
  FilePdfOutlined,
  FileTextOutlined,
  FileUnknownOutlined,
  FileWordOutlined,
  FileZipOutlined,
  FolderOutlined,
  SoundOutlined,
  VideoCameraOutlined
} from '@ant-design/icons-vue';

const props = defineProps({
  fileType: {
    type: String,
    required: true
  },
  isFolder: {
    type: Boolean,
    default: false
  }
});

// 根据文件类型返回对应的图标组件
const iconComponent = computed(() => {
  if (props.isFolder) return FolderOutlined;

  switch (props.fileType) {
    case 'image':
      return FileImageOutlined;
    case 'document':
      return FileTextOutlined;
    case 'pdf':
      return FilePdfOutlined;
    case 'excel':
      return FileExcelOutlined;
    case 'word':
      return FileWordOutlined;
    case 'video':
      return VideoCameraOutlined;
    case 'audio':
      return SoundOutlined;
    case 'zip':
      return FileZipOutlined;
    default:
      return FileUnknownOutlined;
  }
});

// 根据文件类型返回对应的图标颜色
const iconColor = computed(() => {
  if (props.isFolder) return '#ffc53d';

  switch (props.fileType) {
    case 'image':
      return '#13c2c2';
    case 'document':
      return '#1677ff';
    case 'pdf':
      return '#f5222d';
    case 'excel':
      return '#52c41a';
    case 'word':
      return '#2f54eb';
    case 'video':
      return '#722ed1';
    case 'audio':
      return '#eb2f96';
    case 'zip':
      return '#fa8c16';
    default:
      return '#8c8c8c';
  }
});
</script>

<style scoped>
.file-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  margin-right: 8px;
}
</style> 