<template>
  <div class="file-preview">
    <template v-if="isImage">
      <img :src="previewUrl" alt="图片预览" class="preview-image"/>
    </template>

    <template v-else-if="isVideo">
      <video :src="previewUrl" class="preview-video" controls>
        您的浏览器不支持视频播放
      </video>
    </template>

    <template v-else-if="isAudio">
      <audio :src="previewUrl" class="preview-audio" controls>
        您的浏览器不支持音频播放
      </audio>
    </template>

    <template v-else>
      <div class="unsupported-preview">
        <file-type-icon :file-type="fileType"/>
        <p>{{ file.originalName }}</p>
        <p class="hint">当前文件类型不支持预览，请下载后查看</p>
        <a-button type="primary" @click="handleDownload">
          <template #icon>
            <DownloadOutlined/>
          </template>
          下载文件
        </a-button>
      </div>
    </template>

    <!-- 文件信息 -->
    <div class="file-info">
      <a-descriptions :column="2" bordered size="small">
        <a-descriptions-item label="文件名">{{ file.originalName }}</a-descriptions-item>
        <a-descriptions-item label="文件类型">{{ fileTypeText }}</a-descriptions-item>
        <a-descriptions-item label="文件大小">{{ formatFileSize(file.fileSize) }}</a-descriptions-item>
        <a-descriptions-item label="上传时间">{{ file.createTime }}</a-descriptions-item>
      </a-descriptions>
    </div>
  </div>
</template>

<script setup>
import {computed} from 'vue';
import {DownloadOutlined} from '@ant-design/icons-vue';
import FileTypeIcon from './FileTypeIcon.vue';
import {getFileUrl} from '@/api/modules/file';
import {message} from 'ant-design-vue';

const props = defineProps({
  file: {
    type: Object,
    required: true
  }
});

// 获取文件类型
const fileType = computed(() => {
  const suffix = props.file.fileExtension?.toLowerCase();

  // 图片类型后缀
  const imageSuffix = ['jpg', 'jpeg', 'png', 'gif', 'bmp', 'webp'];
  // 文档类型后缀
  const documentSuffix = ['doc', 'pdf', 'xlsx', 'docx', 'xls', 'ppt', 'pptx', 'txt', 'md', 'csv'];
  // 视频类型后缀
  const videoSuffix = ['mp4', 'avi', 'mov', 'flv', 'wmv', 'mpeg', 'mpg', 'm4v', 'webm', 'mkv'];
  // 音频类型后缀
  const audioSuffix = ['mp3', 'wav', 'ogg', 'flac', 'aac', 'm4a'];

  if (imageSuffix.includes(suffix)) {
    return 'image';
  }
  if (documentSuffix.includes(suffix)) {
    return 'document';
  }
  if (videoSuffix.includes(suffix)) {
    return 'video';
  }
  if (audioSuffix.includes(suffix)) {
    return 'audio';
  }
  return 'other';
});

// 预览URL
const previewUrl = computed(() => {
  return props.file.fileUrl || '';
});

// 判断文件类型
const isImage = computed(() => fileType.value === 'image');
const isVideo = computed(() => fileType.value === 'video');
const isAudio = computed(() => fileType.value === 'audio');

// 文件类型文本
const fileTypeText = computed(() => {
  const typeMap = {
    'image': '图片',
    'document': '文档',
    'video': '视频',
    'audio': '音频',
    'other': '其他'
  };
  return typeMap[fileType.value] || '未知类型';
});

// 格式化文件大小
const formatFileSize = (size) => {
  if (!size) return '0 B';

  if (size < 1024) {
    return size + ' B';
  } else if (size < 1024 * 1024) {
    return (size / 1024).toFixed(2) + ' KB';
  } else if (size < 1024 * 1024 * 1024) {
    return (size / 1024 / 1024).toFixed(2) + ' MB';
  } else {
    return (size / 1024 / 1024 / 1024).toFixed(2) + ' GB';
  }
};

// 下载文件
const handleDownload = async () => {
  try {
    message.success(`正在下载: ${props.file.originalName}`);
    const url = await getFileUrl(props.file.fileId);
    const link = document.createElement('a');
    link.href = url;
    link.download = props.file.originalName;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  } catch (error) {
    message.error(error.message || '获取文件url失败');
  }
};
</script>

<style lang="scss" scoped>
.file-preview {
  .preview-image {
    max-width: 100%;
    max-height: 500px;
    margin: 0 auto 20px;
    display: block;
  }

  .preview-video, .preview-audio {
    width: 100%;
    margin-bottom: 20px;
  }

  .unsupported-preview {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 40px 0;
    margin-bottom: 20px;
    background-color: #f5f5f5;
    border-radius: 4px;

    .hint {
      color: #999;
      margin: 16px 0;
    }
  }

  .file-info {
    margin-top: 20px;
  }
}
</style> 