<script setup>
import {computed, nextTick, onMounted, onUnmounted, reactive, ref, watch} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import {useArticleStore} from '../store'
import {useUserStore} from '../store/user'
import {ElMessage, ElMessageBox} from 'element-plus'
import {marked} from 'marked'
import hljs from 'highlight.js'
import 'highlight.js/styles/atom-one-dark.css'
import {
  Bell,
  Check,
  Close,
  Document,
  EditPen,
  FullScreen,
  Link,
  MagicStick,
  Picture,
  Plus,
  Reading,
  ScaleToOriginal,
  Timer,
  View
} from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const articleStore = useArticleStore()
const userStore = useUserStore()

const editMode = ref('split') // split, edit, preview
const isLoading = ref(false)
const isEditing = computed(() => !!route.query.id)
const isFullscreen = ref(false) // 全屏状态
const isFocusMode = ref(false) // 专注模式
const lastSaved = ref(new Date()) // 上次保存时间
const showSaveIndicator = ref(false) // 显示保存指示器
const wordCount = ref(0) // 字数统计
const charCount = ref(0) // 字符统计
const readingTime = ref('0 分钟') // 阅读时间

const showShortcutsHelp = ref(false) // 显示快捷键帮助

// 配置 marked 
marked.setOptions({
  renderer: new marked.Renderer(),
  highlight: function (code, lang) {
    if (lang && hljs.getLanguage(lang)) {
      try {
        return hljs.highlight(code, {language: lang}).value
      } catch (err) {
      }
    }
    return hljs.highlightAuto(code).value
  },
  langPrefix: 'hljs language-',
  pedantic: false,
  gfm: true,
  breaks: true,
  sanitize: false,
  smartLists: true,
  smartypants: false,
  xhtml: false
})

const article = reactive({
  id: '',
  title: '',
  content: '',
  html: '',
  description: '',
  excerpt: '',
  category: '',
  tags: [],
  status: 'draft',
  visibility: 'public',
  password: '',
  allowComments: true,
  featured: false,
  seoTitle: '',
  keywords: '',
  canonical: '',
  customSlug: '',
  coverImage: '',
  publishDate: '',
  readTime: '',
  viewCount: 0,
  createdAt: '',
  updatedAt: '',
  author: userStore.userInfo.name
})

const tagInputVisible = ref(false)
const tagInputValue = ref('')
const tagInputRef = ref(null)
const contentTextarea = ref(null)
const sidebarCollapsed = ref(true) // 默认收起状态

// 计算Markdown HTML
const htmlContent = computed(() => {
  if (!article.content) return '<p class="empty-hint">开始写作吧...</p>'
  try {
    return marked(article.content)
  } catch (error) {
    console.error('Markdown parsing error:', error)
    return '<p class="error-hint">Markdown解析错误</p>'
  }
})

// 监听内容变化，实时更新HTML
watch(() => article.content, (newContent) => {
  article.html = htmlContent.value

  // 更新字数统计
  updateWordCount(newContent)
}, {immediate: true})

// 更新字数统计和阅读时间
const updateWordCount = (content) => {
  if (!content) {
    wordCount.value = 0
    charCount.value = 0
    readingTime.value = '0 分钟'
    return
  }

  // 移除Markdown标记以获得更准确的文本
  const plainText = content.replace(/```[\s\S]*?```/g, '') // 移除代码块
      .replace(/`[^`]*`/g, '') // 移除行内代码
      .replace(/!\[.*?\]\(.*?\)/g, '') // 移除图片
      .replace(/\[.*?\]\(.*?\)/g, '') // 移除链接
      .replace(/[#*`\-\[\]()>]/g, '') // 移除其他Markdown标记
      .trim()

  // 计算字符数（包括空格）
  charCount.value = plainText.length

  // 计算单词数（中文按字符计算，英文按空格分隔计算）
  // 中文字符范围
  const chineseCharsCount = (plainText.match(/[\u4e00-\u9fa5]/g) || []).length

  // 英文单词数
  const englishWords = plainText.replace(/[\u4e00-\u9fa5]/g, '') // 移除中文字符
      .split(/\s+/)
      .filter(word => word.length > 0)

  // 总字数：中文字符 + 英文单词
  wordCount.value = chineseCharsCount + englishWords.length

  // 计算阅读时间（假设平均阅读速度：中文300字/分钟，英文200词/分钟）
  const chineseReadingTime = chineseCharsCount / 300
  const englishReadingTime = englishWords.length / 200
  const totalReadingTime = Math.max(1, Math.ceil(chineseReadingTime + englishReadingTime))

  readingTime.value = `${totalReadingTime} 分钟`
  article.readTime = totalReadingTime.toString()
}

const categories = [
  '技术', '教程', '设计', '生活', '思考', '分享'
]

const handleTagClose = (tag) => {
  article.tags.splice(article.tags.indexOf(tag), 1)
}

const showTagInput = () => {
  tagInputVisible.value = true
  nextTick(() => {
    tagInputRef.value?.focus()
  })
}

const handleTagInputConfirm = () => {
  if (tagInputValue.value.trim()) {
    if (article.tags.indexOf(tagInputValue.value.trim()) === -1) {
      article.tags.push(tagInputValue.value.trim())
    }
  }
  tagInputVisible.value = false
  tagInputValue.value = ''
}

// 插入Markdown语法
const insertMarkdown = (syntax) => {
  const textarea = contentTextarea.value?.$el?.querySelector('textarea') || contentTextarea.value
  if (!textarea) return

  const start = textarea.selectionStart
  const end = textarea.selectionEnd
  const selectedText = article.content.substring(start, end)

  let insertText = ''
  let cursorOffset = 0

  switch (syntax) {
    case 'bold':
      insertText = `**${selectedText || '粗体文本'}**`
      cursorOffset = selectedText ? 0 : -2
      break
    case 'italic':
      insertText = `*${selectedText || '斜体文本'}*`
      cursorOffset = selectedText ? 0 : -1
      break
    case 'code':
      insertText = `\`${selectedText || '代码'}\``
      cursorOffset = selectedText ? 0 : -1
      break
    case 'codeblock':
      insertText = `\`\`\`javascript\n${selectedText || '// 你的代码'}\n\`\`\``
      cursorOffset = selectedText ? 0 : -15
      break
    case 'link':
      insertText = `[${selectedText || '链接文本'}](http://)`
      cursorOffset = selectedText ? -7 : -1
      break
    case 'image':
      insertText = `![${selectedText || '图片描述'}](图片链接)`
      cursorOffset = selectedText ? -7 : -1
      break
    case 'quote':
      insertText = `> ${selectedText || '引用文本'}`
      cursorOffset = selectedText ? 0 : -4
      break
    case 'h1':
      insertText = `# ${selectedText || '一级标题'}`
      cursorOffset = selectedText ? 0 : -4
      break
    case 'h2':
      insertText = `## ${selectedText || '二级标题'}`
      cursorOffset = selectedText ? 0 : -4
      break
    case 'h3':
      insertText = `### ${selectedText || '三级标题'}`
      cursorOffset = selectedText ? 0 : -4
      break
    case 'ul':
      insertText = `- ${selectedText || '列表项'}`
      cursorOffset = selectedText ? 0 : -3
      break
    case 'ol':
      insertText = `1. ${selectedText || '有序列表项'}`
      cursorOffset = selectedText ? 0 : -5
      break
  }

  article.content = article.content.substring(0, start) + insertText + article.content.substring(end)

  nextTick(() => {
    const newPosition = start + insertText.length + cursorOffset
    textarea.focus()
    textarea.setSelectionRange(newPosition, newPosition)
  })
}

const saveArticle = async () => {
  if (!article.title.trim()) {
    ElMessage.warning('请输入文章标题')
    return
  }

  if (!article.content.trim()) {
    ElMessage.warning('请输入文章内容')
    return
  }

  try {
    isLoading.value = true

    // 自动生成描述（如果没有的话）
    if (!article.description.trim()) {
      const textContent = article.content.replace(/[#*`\-\[\]()]/g, '').trim()
      article.description = textContent.substring(0, 150) + (textContent.length > 150 ? '...' : '')
    }

    const savedArticle = articleStore.saveArticle({
      ...article,
      html: htmlContent.value
    })

    ElMessage.success(isEditing.value ? '文章已更新' : '文章已保存')
    if (!isEditing.value) {
      router.push(`/article/${savedArticle.id}`)
    }
  } catch (error) {
    console.error('保存文章失败:', error)
    ElMessage.error('保存失败，请重试')
  } finally {
    isLoading.value = false
  }
}

const discardChanges = () => {
  ElMessageBox.confirm('确定要放弃更改吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    router.push('/')
  }).catch(() => {
  })
}

// 全屏功能
const toggleFullscreen = () => {
  isFullscreen.value = !isFullscreen.value

  if (isFullscreen.value) {
    // 进入全屏模式
    const editorElement = document.querySelector('.editor-page')
    if (editorElement.requestFullscreen) {
      editorElement.requestFullscreen()
    } else if (editorElement.webkitRequestFullscreen) {
      editorElement.webkitRequestFullscreen()
    } else if (editorElement.mozRequestFullScreen) {
      editorElement.mozRequestFullScreen()
    } else if (editorElement.msRequestFullscreen) {
      editorElement.msRequestFullscreen()
    }
  } else {
    // 退出全屏模式
    if (document.exitFullscreen) {
      document.exitFullscreen()
    } else if (document.webkitExitFullscreen) {
      document.webkitExitFullscreen()
    } else if (document.mozCancelFullScreen) {
      document.mozCancelFullScreen()
    } else if (document.msExitFullscreen) {
      document.msExitFullscreen()
    }
  }
}

// 监听全屏状态变化
const handleFullscreenChange = () => {
  const isCurrentlyFullscreen = !!(
      document.fullscreenElement ||
      document.webkitFullscreenElement ||
      document.mozFullScreenElement ||
      document.msFullscreenElement
  )
  isFullscreen.value = isCurrentlyFullscreen
}

// 专注模式
const toggleFocusMode = () => {
  isFocusMode.value = !isFocusMode.value

  // 如果进入专注模式，自动收起侧边栏
  if (isFocusMode.value) {
    sidebarCollapsed.value = true

    // 如果不是全屏，自动进入全屏
    if (!isFullscreen.value) {
      toggleFullscreen()
    }
  }
}

// 自动保存草稿
let autoSaveTimer = null
const startAutoSave = () => {
  if (autoSaveTimer) clearInterval(autoSaveTimer)
  autoSaveTimer = setInterval(() => {
    if (article.title.trim() || article.content.trim()) {
      localStorage.setItem('blog_draft', JSON.stringify(article))

      // 更新最后保存时间
      lastSaved.value = new Date()

      // 显示保存指示器
      showSaveIndicator.value = true
      setTimeout(() => {
        showSaveIndicator.value = false
      }, 2000)
    }
  }, 30000) // 30秒自动保存一次
}

// 手动保存草稿
const saveDraft = () => {
  if (article.title.trim() || article.content.trim()) {
    localStorage.setItem('blog_draft', JSON.stringify(article))

    // 更新最后保存时间
    lastSaved.value = new Date()

    // 显示保存指示器
    showSaveIndicator.value = true
    setTimeout(() => {
      showSaveIndicator.value = false
    }, 2000)

    ElMessage.success('草稿已保存')
  }
}

const loadDraft = () => {
  const draft = localStorage.getItem('blog_draft')
  if (draft && !isEditing.value) {
    try {
      const draftData = JSON.parse(draft)
      ElMessageBox.confirm('检测到未保存的草稿，是否恢复？', '提示', {
        confirmButtonText: '恢复',
        cancelButtonText: '忽略',
        type: 'info'
      }).then(() => {
        Object.assign(article, draftData)
      }).catch(() => {
        localStorage.removeItem('blog_draft')
      })
    } catch (error) {
      localStorage.removeItem('blog_draft')
    }
  }
}

const toggleSidebar = () => {
  sidebarCollapsed.value = !sidebarCollapsed.value
}

const autoFillMeta = () => {
  const textContent = article.content.replace(/[#*`\-\[\]()]/g, '').trim()
  article.title = textContent.substring(0, 50) + (textContent.length > 50 ? '...' : '')
  article.description = textContent.substring(0, 150) + (textContent.length > 150 ? '...' : '')
}

const getReadingTime = (content) => {
  const wordsPerMinute = 200; // 假设每分钟阅读200字
  const words = content.split(/\s+/).filter(word => word.length > 0);
  const minutes = Math.ceil(words.length / wordsPerMinute);
  return `${minutes} 分钟`;
}

// 显示快捷键帮助
const toggleShortcutsHelp = () => {
  showShortcutsHelp.value = !showShortcutsHelp.value
}

// 键盘快捷键处理
const handleKeyboardShortcuts = (e) => {
  // Ctrl+/ 显示快捷键帮助
  if (e.ctrlKey && e.key === '/') {
    e.preventDefault()
    toggleShortcutsHelp()
  }

  // Ctrl+S 保存文章/草稿
  if (e.ctrlKey && e.key === 's') {
    e.preventDefault()
    if (article.title.trim() && article.content.trim()) {
      saveArticle()
    } else {
      saveDraft()
    }
  }

  // Ctrl+D 保存草稿
  if (e.ctrlKey && e.key === 'd') {
    e.preventDefault()
    saveDraft()
  }

  // Ctrl+Shift+F 切换专注模式
  if (e.ctrlKey && e.shiftKey && e.key === 'F') {
    e.preventDefault()
    toggleFocusMode()
  }

  // F11 或 Ctrl+Enter 切换全屏
  if (e.key === 'F11' || (e.ctrlKey && e.key === 'Enter')) {
    e.preventDefault()
    toggleFullscreen()
  }

  // ESC 退出全屏或专注模式
  if (e.key === 'Escape') {
    if (isFullscreen.value) {
      isFullscreen.value = false
    }

    if (isFocusMode.value) {
      isFocusMode.value = false
    }
  }

  // Ctrl+1 编辑模式
  if (e.ctrlKey && e.key === '1') {
    e.preventDefault()
    editMode.value = 'edit'
  }

  // Ctrl+2 分屏模式
  if (e.ctrlKey && e.key === '2') {
    e.preventDefault()
    editMode.value = 'split'
  }

  // Ctrl+3 预览模式
  if (e.ctrlKey && e.key === '3') {
    e.preventDefault()
    editMode.value = 'preview'
  }
}

onMounted(() => {
  // 检查是否是编辑模式
  const id = route.query.id
  if (id) {
    const existingArticle = articleStore.getArticleById(id)
    if (existingArticle) {
      Object.assign(article, existingArticle)
    } else {
      ElMessage.error('文章不存在')
      router.push('/')
    }
  } else {
    loadDraft()
  }

  startAutoSave()

  // 添加全屏事件监听
  document.addEventListener('fullscreenchange', handleFullscreenChange)
  document.addEventListener('webkitfullscreenchange', handleFullscreenChange)
  document.addEventListener('mozfullscreenchange', handleFullscreenChange)
  document.addEventListener('msfullscreenchange', handleFullscreenChange)

  // 添加ESC键退出全屏的监听
  document.addEventListener('keydown', handleKeyboardShortcuts)
})

// 页面卸载时清理定时器
onUnmounted(() => {
  if (autoSaveTimer) {
    clearInterval(autoSaveTimer)
  }

  // 移除全屏事件监听
  document.removeEventListener('fullscreenchange', handleFullscreenChange)
  document.removeEventListener('webkitfullscreenchange', handleFullscreenChange)
  document.removeEventListener('mozfullscreenchange', handleFullscreenChange)
  document.removeEventListener('msfullscreenchange', handleFullscreenChange)

  // 移除键盘快捷键监听
  document.removeEventListener('keydown', handleKeyboardShortcuts)
})
</script>

<template>
  <div :class="{ 'focus-mode': isFocusMode }" class="editor-page">
    <div class="editor-header">
      <div class="header-left">
        <h1 class="page-title">{{ isEditing ? '编辑文章' : '写文章' }}</h1>
      </div>

      <div class="header-actions">
        <el-button-group class="view-toggle">
          <el-button
              :type="editMode === 'edit' ? 'primary' : ''"
              size="small"
              @click="editMode = 'edit'"
          >
            <el-icon>
              <EditPen/>
            </el-icon>
            编辑
          </el-button>
          <el-button
              :type="editMode === 'split' ? 'primary' : ''"
              size="small"
              @click="editMode = 'split'"
          >
            <el-icon>
              <Document/>
            </el-icon>
            分屏
          </el-button>
          <el-button
              :type="editMode === 'preview' ? 'primary' : ''"
              size="small"
              @click="editMode = 'preview'"
          >
            <el-icon>
              <View/>
            </el-icon>
            预览
          </el-button>
        </el-button-group>

        <el-button
            :title="isFullscreen ? '退出全屏' : '全屏编写'"
            size="small"
            @click="toggleFullscreen"
        >
          <el-icon>
            <ScaleToOriginal v-if="isFullscreen"/>
            <FullScreen v-else/>
          </el-icon>
          {{ isFullscreen ? '退出全屏' : '全屏' }}
        </el-button>

        <el-button
            :title="isFocusMode ? '退出专注模式' : '专注模式'"
            :type="isFocusMode ? 'primary' : ''"
            size="small"
            @click="toggleFocusMode"
        >
          <el-icon>
            <Reading/>
          </el-icon>
          专注
        </el-button>

        <el-button
            size="small"
            title="保存草稿 (Ctrl+D)"
            @click="saveDraft"
        >
          <el-icon>
            <Document/>
          </el-icon>
          草稿
        </el-button>

        <el-button size="small" @click="discardChanges">
          <el-icon>
            <Close/>
          </el-icon>
          取消
        </el-button>
        <el-button
            :loading="isLoading"
            size="small"
            type="primary"
            @click="saveArticle"
        >
          <el-icon>
            <Check/>
          </el-icon>
          {{ isEditing ? '更新' : '发布' }}
        </el-button>
      </div>
    </div>

    <div class="editor-container">
      <!-- 文章信息面板 -->
      <div :class="{ collapsed: sidebarCollapsed }" class="editor-sidebar">
        <div :title="sidebarCollapsed ? '展开文章信息' : '收起文章信息'" class="sidebar-toggle" @click="toggleSidebar">
          <el-icon>
            <Document v-if="sidebarCollapsed"/>
            <Close v-else/>
          </el-icon>
        </div>

        <div v-show="!sidebarCollapsed" class="sidebar-content">
          <div class="article-meta-compact">
            <div class="meta-header">
              <h4 class="meta-title">文章设置</h4>
              <div class="quick-actions">
                <el-button size="small" title="自动填充" type="text" @click="autoFillMeta">
                  <el-icon>
                    <EditPen/>
                  </el-icon>
                </el-button>
              </div>
            </div>

            <div class="meta-form">
              <!-- 基础信息 -->
              <div class="meta-section">
                <div class="section-title">基础信息</div>

                <div class="meta-row">
                  <el-input
                      v-model="article.title"
                      :maxlength="100"
                      clearable
                      placeholder="文章标题"
                      show-word-limit
                      size="small"
                  />
                </div>

                <div class="meta-row">
                  <el-input
                      v-model="article.description"
                      :maxlength="200"
                      :rows="3"
                      placeholder="文章描述（SEO简介）"
                      resize="none"
                      show-word-limit
                      size="small"
                      type="textarea"
                  />
                </div>

                <div class="meta-row">
                  <el-input
                      v-model="article.excerpt"
                      :maxlength="150"
                      :rows="2"
                      placeholder="文章摘要（首页展示）"
                      resize="none"
                      size="small"
                      type="textarea"
                  />
                </div>
              </div>

              <!-- 分类和标签 -->
              <div class="meta-section">
                <div class="section-title">分类标签</div>

                <div class="meta-row-split">
                  <el-select
                      v-model="article.category"
                      allow-create
                      clearable
                      filterable
                      placeholder="选择分类"
                      size="small"
                      style="width: 48%"
                  >
                    <el-option
                        v-for="cat in categories"
                        :key="cat"
                        :label="cat"
                        :value="cat"
                    />
                  </el-select>

                  <el-select
                      v-model="article.status"
                      placeholder="状态"
                      size="small"
                      style="width: 48%"
                  >
                    <el-option label="草稿" value="draft"/>
                    <el-option label="发布" value="published"/>
                    <el-option label="私密" value="private"/>
                  </el-select>
                </div>

                <div class="meta-row">
                  <div class="tags-section-full">
                    <div class="tags-display">
                      <el-tag
                          v-for="tag in article.tags"
                          :key="tag"
                          class="tag-mini"
                          closable
                          size="small"
                          type="info"
                          @close="handleTagClose(tag)"
                      >
                        {{ tag }}
                      </el-tag>

                      <el-button
                          v-if="article.tags.length < 8"
                          class="add-tag-mini"
                          plain
                          size="small"
                          type="primary"
                          @click="showTagInput"
                      >
                        <el-icon>
                          <Plus/>
                        </el-icon>
                        添加标签
                      </el-button>
                    </div>

                    <el-input
                        v-if="tagInputVisible"
                        ref="tagInputRef"
                        v-model="tagInputValue"
                        placeholder="输入标签名"
                        size="small"
                        style="width: 100%; margin-top: 4px;"
                        @blur="handleTagInputConfirm"
                        @keyup.enter="handleTagInputConfirm"
                    />
                  </div>
                </div>
              </div>

              <!-- 发布设置 -->
              <div class="meta-section">
                <div class="section-title">发布设置</div>

                <div class="meta-row-split">
                  <el-select
                      v-model="article.visibility"
                      placeholder="可见性"
                      size="small"
                      style="width: 48%"
                  >
                    <el-option label="公开" value="public"/>
                    <el-option label="仅自己" value="private"/>
                    <el-option label="密码保护" value="password"/>
                  </el-select>

                  <el-input
                      v-model="article.readTime"
                      placeholder="阅读时间（分钟）"
                      size="small"
                      style="width: 48%"
                      type="number"
                  />
                </div>

                <div v-if="article.visibility === 'password'" class="meta-row">
                  <el-input
                      v-model="article.password"
                      placeholder="访问密码"
                      size="small"
                      type="password"
                  />
                </div>

                <div class="meta-row">
                  <el-checkbox v-model="article.allowComments" size="small">
                    允许评论
                  </el-checkbox>
                  <el-checkbox v-model="article.featured" size="small" style="margin-left: 1rem;">
                    推荐文章
                  </el-checkbox>
                </div>
              </div>

              <!-- SEO设置 -->
              <div class="meta-section">
                <div class="section-title">SEO优化</div>

                <div class="meta-row">
                  <el-input
                      v-model="article.seoTitle"
                      :maxlength="60"
                      placeholder="SEO标题（搜索引擎显示）"
                      size="small"
                  />
                </div>

                <div class="meta-row">
                  <el-input
                      v-model="article.keywords"
                      :maxlength="100"
                      placeholder="关键词（用逗号分隔）"
                      size="small"
                  />
                </div>

                <div class="meta-row">
                  <el-input
                      v-model="article.canonical"
                      placeholder="规范链接（可选）"
                      size="small"
                  />
                </div>
              </div>

              <!-- 高级设置 -->
              <div class="meta-section">
                <div class="section-title">高级设置</div>

                <div class="meta-row">
                  <el-input
                      v-model="article.customSlug"
                      :maxlength="50"
                      placeholder="自定义URL（英文）"
                      size="small"
                  />
                </div>

                <div class="meta-row">
                  <el-input
                      v-model="article.coverImage"
                      placeholder="封面图片链接"
                      size="small"
                  />
                </div>

                <div class="meta-row">
                  <el-date-picker
                      v-model="article.publishDate"
                      format="YYYY-MM-DD HH:mm"
                      placeholder="发布时间"
                      size="small"
                      style="width: 100%"
                      type="datetime"
                      value-format="YYYY-MM-DD HH:mm:ss"
                  />
                </div>
              </div>

              <!-- 统计信息 -->
              <div class="meta-stats">
                <span class="stat-item">
                  <el-icon><Document/></el-icon>
                  {{ article.content.length }} 字
                </span>
                <span class="stat-item">
                  <el-icon><Timer/></el-icon>
                  {{ getReadingTime(article.content) }}
                </span>
                <span class="stat-item">
                  <el-icon><View/></el-icon>
                  {{ article.viewCount || 0 }} 浏览
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 编辑器主体 -->
      <div class="editor-main">
        <!-- 改进的工具栏 -->
        <div v-if="editMode !== 'preview'" class="toolbar">
          <div class="toolbar-section">
            <span class="section-label">标题</span>
            <div class="toolbar-group">
              <el-button
                  class="toolbar-btn"
                  size="small"
                  title="一级标题"
                  @click="insertMarkdown('h1')"
              >
                H1
              </el-button>
              <el-button
                  class="toolbar-btn"
                  size="small"
                  title="二级标题"
                  @click="insertMarkdown('h2')"
              >
                H2
              </el-button>
              <el-button
                  class="toolbar-btn"
                  size="small"
                  title="三级标题"
                  @click="insertMarkdown('h3')"
              >
                H3
              </el-button>
            </div>
          </div>

          <div class="toolbar-divider"></div>

          <div class="toolbar-section">
            <span class="section-label">样式</span>
            <div class="toolbar-group">
              <el-button
                  class="toolbar-btn"
                  size="small"
                  title="粗体"
                  @click="insertMarkdown('bold')"
              >
                <strong>B</strong>
              </el-button>
              <el-button
                  class="toolbar-btn"
                  size="small"
                  title="斜体"
                  @click="insertMarkdown('italic')"
              >
                <em>I</em>
              </el-button>
              <el-button
                  class="toolbar-btn"
                  size="small"
                  title="行内代码"
                  @click="insertMarkdown('code')"
              >
                <code>&lt;/&gt;</code>
              </el-button>
            </div>
          </div>

          <div class="toolbar-divider"></div>

          <div class="toolbar-section">
            <span class="section-label">插入</span>
            <div class="toolbar-group">
              <el-button
                  class="toolbar-btn"
                  size="small"
                  title="链接"
                  @click="insertMarkdown('link')"
              >
                <el-icon>
                  <Link/>
                </el-icon>
              </el-button>
              <el-button
                  class="toolbar-btn"
                  size="small"
                  title="图片"
                  @click="insertMarkdown('image')"
              >
                <el-icon>
                  <Picture/>
                </el-icon>
              </el-button>
              <el-button
                  class="toolbar-btn"
                  size="small"
                  title="引用"
                  @click="insertMarkdown('quote')"
              >
                ❝
              </el-button>
            </div>
          </div>

          <div class="toolbar-divider"></div>

          <div class="toolbar-section">
            <span class="section-label">列表</span>
            <div class="toolbar-group">
              <el-button
                  class="toolbar-btn"
                  size="small"
                  title="无序列表"
                  @click="insertMarkdown('ul')"
              >
                ●
              </el-button>
              <el-button
                  class="toolbar-btn"
                  size="small"
                  title="有序列表"
                  @click="insertMarkdown('ol')"
              >
                1.
              </el-button>
              <el-button
                  class="toolbar-btn"
                  size="small"
                  title="代码块"
                  @click="insertMarkdown('codeblock')"
              >
                { }
              </el-button>
            </div>
          </div>

          <div class="toolbar-spacer"></div>

          <div class="toolbar-section">
            <div class="current-time">
              {{ new Date().toLocaleTimeString('zh-CN', {hour: '2-digit', minute: '2-digit'}) }}
            </div>
          </div>

          <div class="toolbar-section">
            <div class="stats-container">
              <div class="stat-item" title="字数统计">
                <el-icon>
                  <Document/>
                </el-icon>
                {{ wordCount }} 字
              </div>
              <div class="stat-item" title="预计阅读时间">
                <el-icon>
                  <Timer/>
                </el-icon>
                {{ readingTime }}
              </div>
              <div v-if="showSaveIndicator" class="save-indicator">
                <el-icon>
                  <Bell/>
                </el-icon>
                已保存
              </div>
            </div>
          </div>

          <div class="toolbar-section">
            <el-button
                class="help-btn"
                size="small"
                title="查看键盘快捷键 (Ctrl+/)"
                @click="toggleShortcutsHelp"
            >
              <el-icon>
                <MagicStick/>
              </el-icon>
            </el-button>
          </div>
        </div>

        <div :class="editMode" class="editor-content">
          <!-- 编辑区域 -->
          <div v-if="editMode === 'edit' || editMode === 'split'" class="edit-area">
            <el-input
                ref="contentTextarea"
                v-model="article.content"
                class="content-editor"
                placeholder="# 开始写作吧！

在这里输入你的文章内容...

## 支持的Markdown语法
- **粗体文本**
- *斜体文本*  
- `代码`
- [链接](https://example.com)
- ![图片](图片地址)

> 引用内容

```javascript
// 代码块
console.log('Hello World');
```

1. 有序列表
2. 第二项

- 无序列表
- 另一项"
                resize="none"
                type="textarea"
            />
          </div>

          <!-- 预览区域 -->
          <div v-if="editMode === 'preview' || editMode === 'split'" class="preview-area">
            <div class="preview-content" v-html="htmlContent"></div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- 快捷键帮助对话框 -->
  <el-dialog
      v-model="showShortcutsHelp"
      align-center
      title="键盘快捷键"
      width="400px"
  >
    <div class="shortcuts-help">
      <div class="shortcut-section">
        <h3>编辑操作</h3>
        <div class="shortcut-item">
          <span class="shortcut-key">Ctrl + S</span>
          <span class="shortcut-desc">保存文章/草稿</span>
        </div>
        <div class="shortcut-item">
          <span class="shortcut-key">Ctrl + D</span>
          <span class="shortcut-desc">保存草稿</span>
        </div>
      </div>

      <div class="shortcut-section">
        <h3>视图操作</h3>
        <div class="shortcut-item">
          <span class="shortcut-key">Ctrl + 1</span>
          <span class="shortcut-desc">编辑模式</span>
        </div>
        <div class="shortcut-item">
          <span class="shortcut-key">Ctrl + 2</span>
          <span class="shortcut-desc">分屏模式</span>
        </div>
        <div class="shortcut-item">
          <span class="shortcut-key">Ctrl + 3</span>
          <span class="shortcut-desc">预览模式</span>
        </div>
        <div class="shortcut-item">
          <span class="shortcut-key">F11 / Ctrl + Enter</span>
          <span class="shortcut-desc">切换全屏</span>
        </div>
        <div class="shortcut-item">
          <span class="shortcut-key">Ctrl + Shift + F</span>
          <span class="shortcut-desc">切换专注模式</span>
        </div>
        <div class="shortcut-item">
          <span class="shortcut-key">ESC</span>
          <span class="shortcut-desc">退出全屏/专注模式</span>
        </div>
        <div class="shortcut-item">
          <span class="shortcut-key">Ctrl + /</span>
          <span class="shortcut-desc">显示此帮助</span>
        </div>
      </div>
    </div>
  </el-dialog>
</template>

<style scoped>
.editor-page {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: var(--bg-color);
  transition: all 0.3s ease;
}

/* 全屏模式样式 */
.editor-page:fullscreen {
  background: #0a0a0a;
}

.editor-page:-webkit-full-screen {
  background: #0a0a0a;
}

.editor-page:-moz-full-screen {
  background: #0a0a0a;
}

.editor-page:-ms-fullscreen {
  background: #0a0a0a;
}

/* 全屏模式下的调整 */
.editor-page:fullscreen .editor-header,
.editor-page:-webkit-full-screen .editor-header,
.editor-page:-moz-full-screen .editor-header,
.editor-page:-ms-fullscreen .editor-header {
  background: rgba(0, 0, 0, 0.9);
  backdrop-filter: blur(20px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.editor-page:fullscreen .editor-container,
.editor-page:-webkit-full-screen .editor-container,
.editor-page:-moz-full-screen .editor-container,
.editor-page:-ms-fullscreen .editor-container {
  background: transparent;
}

.editor-page:fullscreen .editor-sidebar,
.editor-page:-webkit-full-screen .editor-sidebar,
.editor-page:-moz-full-screen .editor-sidebar,
.editor-page:-ms-fullscreen .editor-sidebar {
  background: rgba(0, 0, 0, 0.8);
  backdrop-filter: blur(20px);
  border-right: 1px solid rgba(255, 255, 255, 0.1);
}

.editor-page:fullscreen .editor-main,
.editor-page:-webkit-full-screen .editor-main,
.editor-page:-moz-full-screen .editor-main,
.editor-page:-ms-fullscreen .editor-main {
  background: transparent;
}

.editor-page:fullscreen .editor-content,
.editor-page:-webkit-full-screen .editor-content,
.editor-page:-moz-full-screen .editor-content,
.editor-page:-ms-fullscreen .editor-content {
  background: rgba(0, 0, 0, 0.6);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.editor-page:fullscreen .content-editor,
.editor-page:-webkit-full-screen .content-editor,
.editor-page:-moz-full-screen .content-editor,
.editor-page:-ms-fullscreen .content-editor {
  background: transparent;
  color: #ffffff;
  caret-color: var(--primary-color);
}

.editor-page:fullscreen .content-editor::placeholder,
.editor-page:-webkit-full-screen .content-editor::placeholder,
.editor-page:-moz-full-screen .content-editor::placeholder,
.editor-page:-ms-fullscreen .content-editor::placeholder {
  color: rgba(255, 255, 255, 0.5);
}

.editor-page:fullscreen .preview-area,
.editor-page:-webkit-full-screen .preview-area,
.editor-page:-moz-full-screen .preview-area,
.editor-page:-ms-fullscreen .preview-area {
  background: rgba(0, 0, 0, 0.4);
  backdrop-filter: blur(20px);
}

/* 全屏模式下隐藏一些不必要的UI元素 */
.editor-page:fullscreen .editor-sidebar.collapsed,
.editor-page:-webkit-full-screen .editor-sidebar.collapsed,
.editor-page:-moz-full-screen .editor-sidebar.collapsed,
.editor-page:-ms-fullscreen .editor-sidebar.collapsed {
  display: none;
}

/* 全屏模式下的工具栏调整 */
.editor-page:fullscreen .toolbar,
.editor-page:-webkit-full-screen .toolbar,
.editor-page:-moz-full-screen .toolbar,
.editor-page:-ms-fullscreen .toolbar {
  background: rgba(0, 0, 0, 0.8);
  backdrop-filter: blur(20px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  padding: 0.75rem 1.5rem;
}

/* 全屏模式下的按钮样式 */
.editor-page:fullscreen .toolbar button,
.editor-page:-webkit-full-screen .toolbar button,
.editor-page:-moz-full-screen .toolbar button,
.editor-page:-ms-fullscreen .toolbar button {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  color: rgba(255, 255, 255, 0.9);
}

.editor-page:fullscreen .toolbar button:hover,
.editor-page:-webkit-full-screen .toolbar button:hover,
.editor-page:-moz-full-screen .toolbar button:hover,
.editor-page:-ms-fullscreen .toolbar button:hover {
  background: rgba(255, 255, 255, 0.2);
  color: #ffffff;
}

/* 全屏模式下的文章元信息样式 */
.editor-page:fullscreen .article-meta-compact,
.editor-page:-webkit-full-screen .article-meta-compact,
.editor-page:-moz-full-screen .article-meta-compact,
.editor-page:-ms-fullscreen .article-meta-compact {
  background: rgba(0, 0, 0, 0.7);
  backdrop-filter: blur(20px);
}

.editor-page:fullscreen .article-meta-compact .el-input__wrapper,
.editor-page:-webkit-full-screen .article-meta-compact .el-input__wrapper,
.editor-page:-moz-full-screen .article-meta-compact .el-input__wrapper,
.editor-page:-ms-fullscreen .article-meta-compact .el-input__wrapper {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.editor-page:fullscreen .article-meta-compact .el-input__inner,
.editor-page:-webkit-full-screen .article-meta-compact .el-input__inner,
.editor-page:-moz-full-screen .article-meta-compact .el-input__inner,
.editor-page:-ms-fullscreen .article-meta-compact .el-input__inner {
  color: #ffffff;
}

/* 全屏模式下提供专注写作提示 */
.editor-page:fullscreen::before {
  content: "专注写作模式 • 按 ESC 退出全屏";
  position: fixed;
  top: 20px;
  right: 20px;
  background: rgba(0, 0, 0, 0.8);
  color: rgba(255, 255, 255, 0.7);
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-size: 0.8rem;
  z-index: 9999;
  animation: fadeInOut 3s ease-in-out;
}

@keyframes fadeInOut {
  0%, 100% {
    opacity: 0;
  }
  50% {
    opacity: 1;
  }
}

/* 全屏模式下的滚动条美化 */
.editor-page:fullscreen ::-webkit-scrollbar,
.editor-page:-webkit-full-screen ::-webkit-scrollbar,
.editor-page:-moz-full-screen ::-webkit-scrollbar,
.editor-page:-ms-fullscreen ::-webkit-scrollbar {
  width: 8px;
}

.editor-page:fullscreen ::-webkit-scrollbar-track,
.editor-page:-webkit-full-screen ::-webkit-scrollbar-track,
.editor-page:-moz-full-screen ::-webkit-scrollbar-track,
.editor-page:-ms-fullscreen ::-webkit-scrollbar-track {
  background: rgba(255, 255, 255, 0.1);
}

.editor-page:fullscreen ::-webkit-scrollbar-thumb,
.editor-page:-webkit-full-screen ::-webkit-scrollbar-thumb,
.editor-page:-moz-full-screen ::-webkit-scrollbar-thumb,
.editor-page:-ms-fullscreen ::-webkit-scrollbar-thumb {
  background: linear-gradient(135deg, var(--primary-color), #ff77c4);
  border-radius: 4px;
}

.editor-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.5rem 2rem;
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
  background: rgba(255, 255, 255, 0.03);
  backdrop-filter: blur(20px);
  box-shadow: 0 2px 20px rgba(0, 0, 0, 0.1);
}

.page-title {
  font-size: 1.75rem;
  font-weight: 800;
  margin: 0;
  background: linear-gradient(135deg, var(--primary-color), #ff77c4);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.header-actions {
  display: flex;
  gap: 1.5rem;
  align-items: center;
}

.view-toggle {
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.view-toggle .el-button {
  border: none;
  background: transparent;
  color: var(--text-secondary);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  font-weight: 500;
  padding: 0.5rem 1rem;
}

.view-toggle .el-button.el-button--primary {
  background: linear-gradient(135deg, var(--primary-color), #ff77c4);
  color: white;
  box-shadow: 0 2px 8px rgba(120, 119, 198, 0.3);
}

.view-toggle .el-button:not(.el-button--primary):hover {
  background: rgba(255, 255, 255, 0.1);
  color: var(--text-primary);
}

.editor-container {
  flex: 1;
  display: flex;
  height: calc(100vh - 90px);
  overflow: hidden;
}

.editor-sidebar {
  width: 280px;
  padding: 0;
  border-right: 1px solid rgba(255, 255, 255, 0.08);
  overflow: hidden;
  background: rgba(255, 255, 255, 0.02);
  backdrop-filter: blur(10px);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
}

.editor-sidebar.collapsed {
  width: 28px;
}

.sidebar-toggle {
  position: absolute;
  top: 10px;
  right: 4px;
  z-index: 10;
  width: 18px;
  height: 18px;
  background: rgba(120, 119, 198, 0.15);
  border: 1px solid rgba(120, 119, 198, 0.25);
  border-radius: 3px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease;
  color: var(--primary-color);
  font-size: 10px;
}

.sidebar-toggle:hover {
  background: rgba(120, 119, 198, 0.25);
  transform: scale(1.05);
  box-shadow: 0 2px 8px rgba(120, 119, 198, 0.2);
}

.sidebar-content {
  padding: 0.8rem;
  height: 100%;
  overflow-y: auto;
}

.article-meta-compact {
  background: rgba(255, 255, 255, 0.02);
  border: 1px solid rgba(255, 255, 255, 0.06);
  border-radius: 8px;
  padding: 0.6rem;
  backdrop-filter: blur(20px);
}

.meta-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.6rem;
  padding-bottom: 0.3rem;
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
}

.meta-title {
  font-size: 0.8rem;
  font-weight: 600;
  margin: 0;
  color: var(--text-primary);
}

.quick-actions .el-button {
  padding: 2px;
  min-height: unset;
  color: var(--text-secondary);
  font-size: 12px;
}

.quick-actions .el-button:hover {
  color: var(--primary-color);
}

.meta-form {
  display: flex;
  flex-direction: column;
  gap: 0.6rem;
}

.meta-section {
  background: rgba(255, 255, 255, 0.02);
  border: 1px solid rgba(255, 255, 255, 0.06);
  border-radius: 6px;
  padding: 0.6rem;
  backdrop-filter: blur(20px);
}

.section-title {
  font-size: 0.75rem;
  font-weight: 600;
  margin-bottom: 0.5rem;
  padding-bottom: 0.3rem;
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
  color: var(--primary-color);
  text-transform: uppercase;
  letter-spacing: 0.3px;
}

.meta-row {
  width: 100%;
  margin-bottom: 0.5rem;
}

.meta-row:last-child {
  margin-bottom: 0;
}

.meta-row-split {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 0.3rem;
  margin-bottom: 0.5rem;
}

.tags-section-full {
  width: 100%;
}

.tags-display {
  display: flex;
  flex-wrap: wrap;
  gap: 0.3rem;
  align-items: center;
  min-height: 24px;
  margin-bottom: 0.4rem;
}

.tag-mini {
  font-size: 11px;
  height: 22px;
  padding: 0 8px;
  background: linear-gradient(135deg, rgba(120, 119, 198, 0.1), rgba(255, 119, 198, 0.1));
  border: 1px solid rgba(120, 119, 198, 0.2);
  color: var(--primary-color);
  border-radius: 12px;
}

.add-tag-mini {
  height: 22px;
  padding: 0 8px;
  font-size: 11px;
  background: rgba(120, 119, 198, 0.1);
  border: 1px solid rgba(120, 119, 198, 0.2);
  color: var(--primary-color);
  border-radius: 12px;
}

.add-tag-mini:hover {
  background: rgba(120, 119, 198, 0.2);
}

.tag-more {
  font-size: 10px;
  color: var(--text-secondary);
  padding: 2px 4px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 8px;
}

.meta-stats {
  display: flex;
  justify-content: space-around;
  padding: 0.5rem;
  border-top: 1px solid rgba(255, 255, 255, 0.08);
  margin-top: 0.5rem;
  background: rgba(255, 255, 255, 0.02);
  border-radius: 6px;
  flex-wrap: wrap;
  gap: 0.3rem;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 0.2rem;
  font-size: 0.65rem;
  color: var(--text-secondary);
}

.stat-item .el-icon {
  font-size: 10px;
}

/* 简化输入框样式 */
.meta-form .el-input :deep(.el-input__wrapper) {
  background: rgba(255, 255, 255, 0.02);
  border: 1px solid rgba(255, 255, 255, 0.06);
  border-radius: 6px;
  transition: all 0.3s ease;
  box-shadow: none;
  min-height: 28px;
}

.meta-form .el-input :deep(.el-input__wrapper):hover {
  border-color: rgba(120, 119, 198, 0.2);
}

.meta-form .el-input :deep(.el-input__wrapper.is-focus) {
  border-color: var(--primary-color);
  box-shadow: 0 0 0 1px rgba(120, 119, 198, 0.1);
}

.meta-form .el-textarea :deep(.el-textarea__inner) {
  background: rgba(255, 255, 255, 0.02);
  border: 1px solid rgba(255, 255, 255, 0.06);
  border-radius: 6px;
  color: var(--text-primary);
  transition: all 0.3s ease;
  font-size: 12px;
  line-height: 1.4;
  min-height: 60px;
}

.meta-form .el-textarea :deep(.el-textarea__inner):hover {
  border-color: rgba(120, 119, 198, 0.2);
}

.meta-form .el-textarea :deep(.el-textarea__inner):focus {
  border-color: var(--primary-color);
  box-shadow: 0 0 0 1px rgba(120, 119, 198, 0.1);
}

.meta-form .el-select :deep(.el-input__wrapper) {
  background: rgba(255, 255, 255, 0.02);
  border: 1px solid rgba(255, 255, 255, 0.06);
  border-radius: 6px;
  transition: all 0.3s ease;
  min-height: 28px;
}

.meta-form .el-select :deep(.el-input__wrapper):hover {
  border-color: rgba(120, 119, 198, 0.2);
}

.meta-form .el-date-editor :deep(.el-input__wrapper) {
  background: rgba(255, 255, 255, 0.02);
  border: 1px solid rgba(255, 255, 255, 0.06);
  border-radius: 6px;
  min-height: 28px;
}

.editor-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  background: rgba(255, 255, 255, 0.01);
  min-width: 0;
}

.toolbar {
  display: flex;
  align-items: center;
  padding: 0.5rem 1rem;
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
  background: rgba(255, 255, 255, 0.03);
  backdrop-filter: blur(10px);
  gap: 1rem;
  box-shadow: 0 2px 20px rgba(0, 0, 0, 0.05);
  flex-wrap: wrap;
  min-height: 45px;
}

.toolbar-section {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.section-label {
  font-size: 0.75rem;
  color: var(--text-secondary);
  font-weight: 600;
  letter-spacing: 0.5px;
  white-space: nowrap;
  min-width: fit-content;
}

.toolbar-group {
  display: flex;
  gap: 0.15rem;
  padding: 0.1rem;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 6px;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.toolbar-btn {
  border: none !important;
  background: transparent !important;
  color: var(--text-secondary) !important;
  border-radius: 3px !important;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1) !important;
  font-weight: 500 !important;
  min-width: 26px !important;
  height: 26px !important;
  padding: 0 !important;
  display: flex !important;
  align-items: center !important;
  justify-content: center !important;
  font-size: 11px !important;
}

.toolbar-btn:hover {
  background: rgba(120, 119, 198, 0.15) !important;
  color: var(--primary-color) !important;
  transform: translateY(-1px) !important;
  box-shadow: 0 2px 8px rgba(120, 119, 198, 0.15) !important;
}

.toolbar-btn:active {
  transform: translateY(0) !important;
}

.toolbar-divider {
  width: 1px;
  height: 14px;
  background: rgba(255, 255, 255, 0.1);
  margin: 0 0.05rem;
}

.toolbar-spacer {
  flex-grow: 1;
}

.current-time {
  font-size: 0.75rem;
  color: var(--text-secondary);
  font-weight: 500;
  letter-spacing: 0.5px;
  background: rgba(255, 255, 255, 0.05);
  padding: 0.25rem 0.5rem;
  border-radius: 12px;
  border: 1px solid rgba(255, 255, 255, 0.1);
  white-space: nowrap;
}

.stats-container {
  display: flex;
  gap: 0.5rem;
  align-items: center;
  font-size: 0.75rem;
  color: var(--text-secondary);
  background: rgba(255, 255, 255, 0.05);
  padding: 0.25rem 0.5rem;
  border-radius: 12px;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.save-indicator {
  display: flex;
  align-items: center;
  gap: 0.2rem;
  font-size: 0.75rem;
  color: var(--primary-color);
  background: rgba(120, 119, 198, 0.1);
  padding: 0.25rem 0.5rem;
  border-radius: 12px;
  border: 1px solid rgba(120, 119, 198, 0.2);
}

.editor-content {
  flex: 1;
  display: flex;
  overflow: hidden;
  margin: 0.4rem;
  border-radius: 10px;
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.editor-content.edit {
  flex-direction: column;
}

.editor-content.split {
  flex-direction: row;
}

.editor-content.preview {
  flex-direction: column;
}

.edit-area {
  flex: 1;
  overflow: hidden;
  background: rgba(255, 255, 255, 0.02);
  backdrop-filter: blur(10px);
  display: flex;
  flex-direction: column;
}

.editor-content.split .edit-area {
  border-right: 1px solid rgba(255, 255, 255, 0.1);
}

.content-editor {
  height: 100%;
  flex: 1;
}

.content-editor :deep(.el-textarea__inner) {
  height: 100% !important;
  resize: none;
  border: none;
  background: transparent;
  color: var(--text-primary);
  font-family: 'JetBrains Mono', 'Fira Code', 'Consolas', 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
  font-size: 17px;
  line-height: 1.9;
  padding: 1.2rem;
  font-weight: 400;
  outline: none !important;
  box-shadow: none !important;
}

.content-editor :deep(.el-textarea__inner)::placeholder {
  color: var(--text-secondary);
  opacity: 0.5;
  font-style: italic;
  line-height: 1.6;
}

.preview-area {
  flex: 1;
  overflow-y: auto;
  padding: 1.2rem;
  background: rgba(255, 255, 255, 0.02);
  backdrop-filter: blur(10px);
}

.preview-content {
  max-width: none;
  color: var(--text-primary);
  line-height: 1.8;
  font-size: 16px;
}

/* 自定义滚动条 */
.preview-area::-webkit-scrollbar,
.editor-sidebar::-webkit-scrollbar {
  width: 6px;
}

.preview-area::-webkit-scrollbar-track,
.editor-sidebar::-webkit-scrollbar-track {
  background: transparent;
}

.preview-area::-webkit-scrollbar-thumb,
.editor-sidebar::-webkit-scrollbar-thumb {
  background: rgba(120, 119, 198, 0.3);
  border-radius: 3px;
}

.preview-area::-webkit-scrollbar-thumb:hover,
.editor-sidebar::-webkit-scrollbar-thumb:hover {
  background: rgba(120, 119, 198, 0.5);
}

/* 改进的Markdown样式 */
.preview-content :deep(h1),
.preview-content :deep(h2),
.preview-content :deep(h3),
.preview-content :deep(h4),
.preview-content :deep(h5),
.preview-content :deep(h6) {
  color: var(--text-primary);
  font-weight: 700;
  margin: 2em 0 1em 0;
  line-height: 1.3;
  letter-spacing: -0.02em;
}

.preview-content :deep(h1) {
  font-size: 2.5rem;
  border-bottom: 3px solid;
  border-image: linear-gradient(135deg, var(--primary-color), #ff77c4) 1;
  padding-bottom: 0.75rem;
  margin-bottom: 1.5rem;
}

.preview-content :deep(h2) {
  font-size: 2rem;
  border-bottom: 2px solid rgba(120, 119, 198, 0.3);
  padding-bottom: 0.5rem;
  margin-bottom: 1.25rem;
}

.preview-content :deep(h3) {
  font-size: 1.5rem;
  color: var(--primary-color);
  margin-bottom: 1rem;
}

.preview-content :deep(h4) {
  font-size: 1.25rem;
  margin-bottom: 0.75rem;
}

.preview-content :deep(p) {
  margin: 1.25em 0;
  text-align: justify;
}

.preview-content :deep(code) {
  background: linear-gradient(135deg, rgba(120, 119, 198, 0.15), rgba(255, 119, 198, 0.15));
  padding: 0.25em 0.5em;
  border-radius: 6px;
  font-family: 'JetBrains Mono', 'Fira Code', 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
  font-size: 0.9em;
  color: #e06c75;
  border: 1px solid rgba(120, 119, 198, 0.2);
  font-weight: 500;
}

.preview-content :deep(pre) {
  background: linear-gradient(135deg, rgba(0, 0, 0, 0.4), rgba(20, 20, 40, 0.4));
  border-radius: 12px;
  padding: 1.5rem;
  overflow-x: auto;
  margin: 1.5em 0;
  border: 1px solid rgba(255, 255, 255, 0.1);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
  position: relative;
}

.preview-content :deep(pre)::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 30px;
  background: linear-gradient(135deg, rgba(120, 119, 198, 0.1), rgba(255, 119, 198, 0.1));
  border-radius: 12px 12px 0 0;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.preview-content :deep(pre code) {
  background: none;
  padding: 0;
  color: inherit;
  border: none;
  font-size: 14px;
  line-height: 1.6;
}

.preview-content :deep(blockquote) {
  border-left: 4px solid var(--primary-color);
  margin: 1.5em 0;
  color: var(--text-secondary);
  background: linear-gradient(135deg, rgba(120, 119, 198, 0.05), rgba(255, 119, 198, 0.05));
  padding: 1rem 1.5rem;
  border-radius: 0 12px 12px 0;
  position: relative;
  font-style: italic;
}

.preview-content :deep(blockquote)::before {
  content: '"';
  position: absolute;
  top: -10px;
  left: 10px;
  font-size: 3rem;
  color: var(--primary-color);
  opacity: 0.3;
  font-family: serif;
}

.preview-content :deep(ul),
.preview-content :deep(ol) {
  padding-left: 2rem;
  margin: 1.5em 0;
}

.preview-content :deep(li) {
  margin: 0.75em 0;
  line-height: 1.6;
}

.preview-content :deep(ul li)::marker {
  color: var(--primary-color);
}

.preview-content :deep(ol li)::marker {
  color: var(--primary-color);
  font-weight: 600;
}

.preview-content :deep(a) {
  color: var(--primary-color);
  text-decoration: none;
  border-bottom: 1px solid transparent;
  transition: all 0.3s ease;
  font-weight: 500;
}

.preview-content :deep(a:hover) {
  border-bottom-color: var(--primary-color);
  color: #ff77c4;
}

.preview-content :deep(img) {
  max-width: 100%;
  height: auto;
  border-radius: 12px;
  margin: 2em 0;
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.3);
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.preview-content :deep(table) {
  width: 100%;
  border-collapse: collapse;
  margin: 2em 0;
  border-radius: 12px;
  overflow: hidden;
  border: 1px solid rgba(255, 255, 255, 0.1);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
}

.preview-content :deep(th),
.preview-content :deep(td) {
  padding: 1rem;
  text-align: left;
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
}

.preview-content :deep(th) {
  background: linear-gradient(135deg, rgba(120, 119, 198, 0.1), rgba(255, 119, 198, 0.1));
  font-weight: 700;
  color: var(--primary-color);
}

.preview-content :deep(tr:hover) {
  background: rgba(255, 255, 255, 0.02);
}

.preview-content :deep(.empty-hint) {
  text-align: center;
  color: var(--text-secondary);
  font-style: italic;
  padding: 6rem 2rem;
  font-size: 1.2rem;
  opacity: 0.6;
}

.preview-content :deep(.error-hint) {
  color: #ff6b6b;
  text-align: center;
  padding: 3rem;
  font-size: 1.1rem;
  background: rgba(255, 107, 107, 0.1);
  border-radius: 12px;
  border: 1px solid rgba(255, 107, 107, 0.2);
}

/* 响应式改进 */
@media (max-width: 1024px) {
  .editor-container {
    flex-direction: column;
  }

  .editor-sidebar {
    width: 100% !important;
    height: auto;
    max-height: 180px;
    border-right: none;
    border-bottom: 1px solid rgba(255, 255, 255, 0.08);
  }

  .editor-sidebar.collapsed {
    height: 30px;
    max-height: 30px;
  }

  .sidebar-toggle {
    top: 6px;
    right: 10px;
  }

  .sidebar-content {
    padding: 0.6rem;
  }

  .article-meta-compact {
    padding: 0.6rem;
  }

  .meta-form {
    gap: 0.4rem;
  }

  .meta-section {
    padding: 0.5rem;
  }

  .meta-row-split {
    flex-direction: column;
    gap: 0.4rem;
  }

  .tags-section-full {
    width: 100%;
  }

  .editor-content.split {
    flex-direction: column;
  }

  .edit-area {
    border-right: none;
  }

  .editor-content.split .edit-area {
    border-right: none;
    border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  }

  .toolbar {
    padding: 0.6rem 0.8rem;
    gap: 0.8rem;
  }

  .toolbar-section {
    gap: 0.4rem;
  }

  .section-label {
    font-size: 0.7rem;
  }
}

@media (max-width: 768px) {
  .editor-header {
    padding: 1rem;
    flex-direction: column;
    gap: 1rem;
    align-items: stretch;
  }

  .page-title {
    font-size: 1.5rem;
    text-align: center;
  }

  .header-actions {
    justify-content: center;
    gap: 1rem;
  }

  .view-toggle {
    flex: 1;
    max-width: 250px;
  }

  .toolbar {
    flex-direction: column;
    gap: 0.6rem;
    padding: 0.8rem;
    align-items: stretch;
  }

  .toolbar-section {
    justify-content: center;
  }

  .toolbar-group {
    justify-content: center;
  }

  .current-time {
    text-align: center;
  }

  .content-editor :deep(.el-textarea__inner) {
    padding: 0.8rem;
    font-size: 15px;
    line-height: 1.7;
  }

  .preview-area {
    padding: 0.8rem;
  }

  .editor-content {
    margin: 0.3rem;
    border-radius: 8px;
  }

  .meta-title {
    font-size: 0.75rem;
  }

  .section-title {
    font-size: 0.7rem;
  }
}

@media (max-width: 480px) {
  .editor-header {
    padding: 0.8rem;
  }

  .page-title {
    font-size: 1.3rem;
  }

  .toolbar {
    padding: 0.6rem;
  }

  .toolbar-section {
    flex-direction: column;
    gap: 0.3rem;
  }

  .section-label {
    font-size: 0.65rem;
    text-align: center;
  }

  .content-editor :deep(.el-textarea__inner) {
    padding: 0.6rem;
    font-size: 14px;
    line-height: 1.6;
  }

  .preview-area {
    padding: 0.6rem;
  }

  .preview-content {
    font-size: 14px;
  }

  .editor-content {
    margin: 0.2rem;
  }

  .sidebar-content {
    padding: 0.5rem;
  }

  .meta-section {
    padding: 0.4rem;
  }
}

/* 动画效果 */
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes slideInLeft {
  from {
    opacity: 0;
    transform: translateX(-30px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

@keyframes slideInRight {
  from {
    opacity: 0;
    transform: translateX(30px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

.article-meta {
  animation: slideInLeft 0.6s ease-out;
}

.toolbar {
  animation: fadeInUp 0.8s ease-out;
}

.editor-content {
  animation: slideInRight 1s ease-out;
}

/* 悬浮效果 */
.article-meta:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.toolbar-group:hover {
  box-shadow: 0 3px 12px rgba(120, 119, 198, 0.1);
  transition: all 0.3s ease;
}

/* 加载状态 */
.editor-page.loading {
  pointer-events: none;
}

.editor-page.loading::after {
  content: '';
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(2px);
  z-index: 9999;
}

/* 专注模式样式 */
.editor-page.focus-mode .editor-header,
.editor-page.focus-mode .toolbar,
.editor-page.focus-mode .editor-sidebar {
  opacity: 0;
  pointer-events: none;
  transition: opacity 0.5s ease;
}

.editor-page.focus-mode .editor-main {
  padding: 2rem;
}

.editor-page.focus-mode .editor-content {
  border: none;
  box-shadow: none;
  background: transparent;
}

.editor-page.focus-mode .content-editor :deep(.el-textarea__inner) {
  font-size: 18px;
  line-height: 2;
  padding: 2rem;
}

.editor-page.focus-mode .preview-area {
  padding: 2rem;
}

.editor-page.focus-mode:hover .editor-header,
.editor-page.focus-mode:hover .toolbar {
  opacity: 0.3;
  pointer-events: auto;
}

.editor-page.focus-mode:hover .editor-header:hover,
.editor-page.focus-mode:hover .toolbar:hover {
  opacity: 1;
}

/* 快捷键帮助样式 */
.shortcuts-help {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.shortcut-section {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.shortcut-section h3 {
  font-size: 1rem;
  margin: 0 0 0.5rem 0;
  padding-bottom: 0.5rem;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  color: var(--primary-color);
}

.shortcut-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.5rem 0;
}

.shortcut-key {
  background: rgba(255, 255, 255, 0.1);
  padding: 0.3rem 0.6rem;
  border-radius: 4px;
  font-family: 'JetBrains Mono', monospace;
  font-size: 0.9rem;
  color: var(--primary-color);
  border: 1px solid rgba(120, 119, 198, 0.3);
}

.shortcut-desc {
  color: var(--text-primary);
  font-size: 0.9rem;
}

/* 帮助按钮样式 */
.help-btn {
  background: rgba(120, 119, 198, 0.1) !important;
  border: 1px solid rgba(120, 119, 198, 0.2) !important;
  color: var(--primary-color) !important;
  border-radius: 50% !important;
  width: 28px !important;
  height: 28px !important;
  padding: 0 !important;
  display: flex !important;
  align-items: center !important;
  justify-content: center !important;
  transition: all 0.3s ease !important;
}

.help-btn:hover {
  background: rgba(120, 119, 198, 0.2) !important;
  transform: rotate(15deg) !important;
  box-shadow: 0 0 12px rgba(120, 119, 198, 0.3) !important;
}
</style>