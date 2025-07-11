<template>
  <a-modal
    :open="visible"
    :title="file.fileName"
    @cancel="handleCancel"
    :footer="null"
    width="800px"
    :destroyOnClose="true"
    :maskClosable="true"
  >
    <div class="file-preview-container">
      <!-- 图片预览 -->
      <div v-if="isImage" class="preview-content image-preview">
        <a-image :src="file.fileUrl" :alt="file.fileName" />
      </div>

      <!-- 视频预览 -->
      <div v-else-if="isVideo" class="preview-content video-preview">
        <video controls style="width: 100%">
          <source :src="file.fileUrl" :type="`video/${getExtension(file.fileName)}`">
          您的浏览器不支持视频播放
        </video>
      </div>

      <!-- 音频预览 -->
      <div v-else-if="isAudio" class="preview-content audio-preview">
        <audio controls style="width: 100%">
          <source :src="file.fileUrl" :type="`audio/${getExtension(file.fileName)}`">
          您的浏览器不支持音频播放
        </audio>
      </div>

      <!-- PDF预览 -->
      <div v-else-if="isPdf" class="preview-content pdf-preview">
        <iframe :src="file.fileUrl" width="100%" height="500"></iframe>
      </div>

      <!-- 其他文件预览 -->
      <div v-else class="preview-content other-preview">
        <div class="no-preview">
          <file-outlined style="font-size: 64px; margin-bottom: 16px;" />
          <p>无法预览该文件类型</p>
          <a-button type="primary" @click="handleDownload">
            <template #icon><download-outlined /></template>
            下载文件
          </a-button>
        </div>
      </div>

      <!-- 文件信息 -->
      <div class="file-info">
        <a-descriptions title="文件信息" bordered :column="1">
          <a-descriptions-item label="文件名">{{ file.fileName }}</a-descriptions-item>
          <a-descriptions-item label="文件类型">{{ file.fileType }}</a-descriptions-item>
          <a-descriptions-item label="文件大小">{{ formatFileSize(file.fileSize) }}</a-descriptions-item>
          <a-descriptions-item label="上传者">{{ file.createBy }}</a-descriptions-item>
          <a-descriptions-item label="上传时间">{{ file.createTime }}</a-descriptions-item>
        </a-descriptions>
      </div>
    </div>
  </a-modal>
</template>

<script setup>
import {computed} from 'vue'
import {DownloadOutlined, FileOutlined} from '@ant-design/icons-vue'

const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  file: {
    type: Object,
    default: () => ({})
  }
})

const emit = defineEmits(['update:visible', 'close'])

// 判断文件类型
const isImage = computed(() => props.file.fileType === 'image')
const isVideo = computed(() => props.file.fileType === 'video')
const isAudio = computed(() => props.file.fileType === 'audio')
const isPdf = computed(() => {
  return props.file.fileName && props.file.fileName.toLowerCase().endsWith('.pdf')
})

// 获取文件扩展名
const getExtension = (filename) => {
  return filename.split('.').pop().toLowerCase()
}

// 格式化文件大小
const formatFileSize = (size) => {
  if (!size) return '0 B'
  
  if (size < 1024) {
    return size + ' B'
  } else if (size < 1024 * 1024) {
    return (size / 1024).toFixed(2) + ' KB'
  } else if (size < 1024 * 1024 * 1024) {
    return (size / (1024 * 1024)).toFixed(2) + ' MB'
  } else {
    return (size / (1024 * 1024 * 1024)).toFixed(2) + ' GB'
  }
}

// 关闭弹窗
const handleCancel = () => {
  emit('update:visible', false)
  emit('close')
}

// 下载文件
const handleDownload = () => {
  const link = document.createElement('a')
  link.href = props.file.fileUrl
  link.download = props.file.fileName
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
}
</script>

<style lang="scss" scoped>
.file-preview-container {
  .preview-content {
    margin-bottom: 20px;
    
    &.other-preview {
      .no-preview {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        height: 300px;
        background-color: #f5f5f5;
        border-radius: 4px;
        
        p {
          margin-bottom: 16px;
          color: #666;
        }
      }
    }
  }
  
  .file-info {
    margin-top: 20px;
  }
}
</style> 