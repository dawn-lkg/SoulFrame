<script setup>
import {computed, onMounted, ref} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import {useArticleStore} from '../store'
import {useUserStore} from '../store/user'
import {ElMessage, ElMessageBox} from 'element-plus'
import {marked} from 'marked'
import hljs from 'highlight.js'
import 'highlight.js/styles/atom-one-dark.css'
import {Back, Calendar, Delete, EditPen, Timer} from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const articleStore = useArticleStore()
const userStore = useUserStore()

const article = ref(null)
const loading = ref(true)
const toc = ref([])

// 配置 marked
const headings = []

// 辅助函数：安全地将任何值转换为字符串并生成slug
const createSlug = (text) => {
  const textStr = typeof text === 'string' ? text : String(text || '')
  return textStr.toLowerCase().replace(/[^\w\u4e00-\u9fa5]+/g, '-').replace(/^-+|-+$/g, '')
}

// 配置marked全局选项
marked.setOptions({
  highlight: function (code, lang) {
    if (!lang) return code;
    const language = hljs.getLanguage(lang) ? lang : 'plaintext';
    return hljs.highlight(code, {language}).value;
  },
  gfm: true,
  breaks: true,
  sanitize: false
})

const htmlContent = computed(() => {
  if (!article.value || !article.value.content) {
    return '';
  }

  try {
    // 重置标题列表
    headings.length = 0

    // 使用marked解析Markdown
    let html = marked(article.value.content)

    // 后处理：为标题添加id属性
    html = html.replace(/<h([1-6])>(.*?)<\/h[1-6]>/g, (match, level, text) => {
      const slug = createSlug(text)
      // 同时添加到目录
      if (parseInt(level) <= 3) {
        headings.push({
          level: parseInt(level),
          title: text,
          slug: slug
        })
      }
      return `<h${level} id="${slug}">${text}</h${level}>`
    })

    // 更新目录
    toc.value = headings.slice()

    return html
  } catch (error) {
    console.error('Markdown渲染错误:', error)
    // 如果渲染失败，使用简单的HTML替换
    return article.value.content
        .replace(/\n/g, '<br>')
        .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
        .replace(/\*(.*?)\*/g, '<em>$1</em>')
        .replace(/`(.*?)`/g, '<code>$1</code>')
  }
})

const getReadingTime = (content) => {
  if (!content) return '1 分钟';
  const wordsPerMinute = 300; // 假设平均阅读速度为每分钟300字
  const wordCount = content.length;
  const readingTime = Math.max(1, Math.ceil(wordCount / wordsPerMinute));
  return `${readingTime} 分钟`;
}

const formatDate = (dateString) => {
  const date = new Date(dateString)
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  })
}

const scrollToHeading = (slug) => {
  const el = document.getElementById(slug)
  if (el) {
    el.scrollIntoView({behavior: 'smooth'})
  }
}

const editArticle = () => {
  router.push(`/editor?id=${article.value.id}`)
}

const deleteArticle = () => {
  ElMessageBox.confirm('确定要删除这篇文章吗？此操作不可逆', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    const success = articleStore.deleteArticle(article.value.id)
    if (success) {
      ElMessage.success('文章已删除')
      router.push('/')
    } else {
      ElMessage.error('删除失败')
    }
  }).catch(() => {
  })
}

onMounted(async () => {
  try {
    loading.value = true
    const id = route.params.id

    const foundArticle = articleStore.getArticle(id)

    if (foundArticle) {
      article.value = foundArticle

      // 模拟一些加载时间，让页面有更好的体验
      setTimeout(() => {
        loading.value = false
      }, 300)
    } else {
      ElMessage.error('文章不存在')
      router.push('/')
    }
  } catch (error) {
    console.error('获取文章失败:', error)
    ElMessage.error('获取文章失败')
    router.push('/')
  }
})
</script>

<template>
  <div class="article-container">
    <el-skeleton v-if="loading" :rows="10" animated/>

    <div v-else-if="article" class="article-layout">
      <aside v-if="toc.length > 1" class="article-sidebar">
        <div class="toc-container">
          <h3 class="toc-title">目录</h3>
          <div class="toc-list">
            <div
                v-for="heading in toc"
                :key="heading.slug"
                :class="`level-${heading.level}`"
                class="toc-item"
                @click="scrollToHeading(heading.slug)"
            >
              {{ heading.title }}
            </div>
          </div>
        </div>
      </aside>

      <div class="article-main">
        <div class="article-header">
          <h1 class="article-title">{{ article.title }}</h1>

          <div class="article-info">
            <div class="article-meta">
              <div class="meta-item">
                <el-avatar :size="36" :src="userStore.userInfo.avatar"/>
                <span>{{ userStore.userInfo.name }}</span>
              </div>

              <div class="meta-item">
                <el-icon>
                  <Calendar/>
                </el-icon>
                {{ formatDate(article.updatedAt || article.createdAt) }}
              </div>

              <div class="meta-item">
                <el-icon>
                  <Timer/>
                </el-icon>
                阅读时间：{{ getReadingTime(article.content) }}
              </div>
            </div>

            <div class="article-categories">
              <div v-if="article.category" class="category-item">
                <el-tag effect="light" round size="large">
                  {{ article.category }}
                </el-tag>
              </div>

              <div v-if="article.tags && article.tags.length" class="tag-list">
                <el-tag
                    v-for="tag in article.tags"
                    :key="tag"
                    class="article-tag"
                    effect="plain"
                    size="small"
                >
                  {{ tag }}
                </el-tag>
              </div>
            </div>
          </div>

          <div class="article-actions">
            <el-button plain type="primary" @click="editArticle">
              <el-icon>
                <EditPen/>
              </el-icon>
              编辑文章
            </el-button>
            <el-button plain type="danger" @click="deleteArticle">
              <el-icon>
                <Delete/>
              </el-icon>
              删除文章
            </el-button>
            <el-button plain @click="router.push('/')">
              <el-icon>
                <Back/>
              </el-icon>
              返回首页
            </el-button>
          </div>
        </div>

        <div class="article-content-wrapper">
          <div
              v-if="htmlContent"
              class="markdown-body"
              v-html="htmlContent"
          ></div>
          <div
              v-else-if="article && article.content"
              class="raw-content"
          >
            <pre>{{ article.content }}</pre>
          </div>
          <div v-else class="no-content">
            <p>暂无内容</p>
          </div>
        </div>

        <div class="article-footer">
          <div class="article-nav">
            <div class="nav-item prev">
              <!-- 可以添加上一篇文章链接 -->
            </div>
            <div class="nav-item next">
              <!-- 可以添加下一篇文章链接 -->
            </div>
          </div>
        </div>
      </div>
    </div>

    <el-empty
        v-else
        description="文章不存在"
    >
      <el-button @click="router.push('/')">返回首页</el-button>
    </el-empty>
  </div>
</template>

<style scoped>
.article-container {
  width: 100%;
  margin: 0 auto;
  min-height: calc(100vh - 200px);
  background: transparent;
}

.article-layout {
  display: flex;
  gap: 2.5rem;
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 1rem;
}

.article-sidebar {
  width: 280px;
  flex-shrink: 0;
  position: sticky;
  top: 100px;
  align-self: flex-start;
  max-height: calc(100vh - 140px);
  overflow-y: auto;
}

.toc-container {
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 16px;
  padding: 1.5rem;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
  transition: all 0.3s ease;
}

.toc-container:hover {
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.4);
  transform: translateY(-2px);
}

.toc-title {
  font-size: 1.2rem;
  font-weight: 700;
  margin-bottom: 1.2rem;
  padding-bottom: 0.8rem;
  border-bottom: 2px solid rgba(120, 119, 198, 0.3);
  color: #ffffff;
  background: linear-gradient(135deg, #7877c6, #ff77c4);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.toc-list {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.toc-item {
  cursor: pointer;
  padding: 0.6rem 1rem;
  border-radius: 10px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  color: rgba(255, 255, 255, 0.8);
  font-size: 0.95rem;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  position: relative;
  border: 1px solid transparent;
}

.toc-item::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  width: 0;
  height: 100%;
  background: linear-gradient(135deg, rgba(120, 119, 198, 0.2), rgba(255, 119, 198, 0.2));
  border-radius: 10px;
  transition: width 0.3s ease;
  z-index: -1;
}

.toc-item:hover {
  color: #ffffff;
  border-color: rgba(120, 119, 198, 0.5);
  transform: translateX(4px);
}

.toc-item:hover::before {
  width: 100%;
}

.toc-item.level-1 {
  font-weight: 600;
  padding-left: 1rem;
}

.toc-item.level-2 {
  padding-left: 1.5rem;
  font-size: 0.9rem;
}

.toc-item.level-3 {
  padding-left: 2rem;
  font-size: 0.85rem;
  opacity: 0.8;
}

.article-main {
  flex-grow: 1;
  max-width: 100%;
  animation: fadeInUp 0.6s ease forwards;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.article-header {
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 20px;
  padding: 2.5rem;
  margin-bottom: 2rem;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
  position: relative;
  overflow: hidden;
}

.article-header::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 4px;
  background: linear-gradient(135deg, #7877c6, #ff77c4, #78dbff);
}

.article-title {
  font-size: 2.5rem;
  font-weight: 800;
  line-height: 1.2;
  color: #ffffff;
  margin-bottom: 2rem;
  word-break: break-word;
}

.article-info {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  align-items: flex-start;
  gap: 1.5rem;
  margin-bottom: 2rem;
}

.article-meta {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 2rem;
  color: rgba(255, 255, 255, 0.8);
  font-size: 1rem;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 1rem;
  background: rgba(120, 119, 198, 0.2);
  border-radius: 12px;
  border: 1px solid rgba(120, 119, 198, 0.3);
  transition: all 0.3s ease;
  color: rgba(255, 255, 255, 0.9);
}

.meta-item:hover {
  background: rgba(120, 119, 198, 0.3);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(120, 119, 198, 0.4);
  color: #ffffff;
}

.article-categories {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 1rem;
}

.tag-list {
  display: flex;
  flex-wrap: wrap;
  gap: 0.75rem;
}

.article-tag {
  margin: 0;
  background: linear-gradient(135deg, rgba(66, 153, 225, 0.2), rgba(120, 119, 198, 0.2));
  border: 1px solid rgba(66, 153, 225, 0.4);
  color: rgba(255, 255, 255, 0.9);
  transition: all 0.3s ease;
}

.article-tag:hover {
  background: linear-gradient(135deg, rgba(66, 153, 225, 0.3), rgba(120, 119, 198, 0.3));
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(120, 119, 198, 0.3);
  color: #ffffff;
}

.article-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
  justify-content: flex-end;
  margin-top: 2rem;
  padding-top: 2rem;
  border-top: 2px solid rgba(120, 119, 198, 0.2);
}

.article-actions .el-button {
  border-radius: 12px;
  padding: 0.75rem 1.5rem;
  font-weight: 600;
  transition: all 0.3s ease;
  border-width: 2px;
}

.article-actions .el-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.3);
}

.article-content-wrapper {
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 20px;
  padding: 3rem;
  margin-bottom: 2rem;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
  overflow: hidden;
  position: relative;
}

.article-content-wrapper::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 2px;
  background: linear-gradient(135deg, rgba(120, 119, 198, 0.5), rgba(255, 119, 198, 0.5));
}

.markdown-body {
  font-size: 1.1rem;
  line-height: 1.8;
  color: #ffffff;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

.markdown-body p {
  margin-bottom: 1.8rem;
  text-align: justify;
  color: rgba(255, 255, 255, 0.9);
}

.markdown-body h1,
.markdown-body h2,
.markdown-body h3,
.markdown-body h4,
.markdown-body h5,
.markdown-body h6 {
  scroll-margin-top: 120px;
  position: relative;
  color: #ffffff;
}

.markdown-body h1,
.markdown-body h2 {
  border-bottom: 3px solid rgba(120, 119, 198, 0.4);
  padding-bottom: 1rem;
  margin-top: 3rem;
  margin-bottom: 2rem;
}

.markdown-body h1 {
  font-size: 2.2rem;
  font-weight: 800;
}

.markdown-body h2 {
  font-size: 1.9rem;
  font-weight: 700;
}

.markdown-body h3 {
  font-size: 1.6rem;
  margin-top: 2.5rem;
  margin-bottom: 1.5rem;
  font-weight: 600;
  color: #7877c6;
}

.markdown-body h4 {
  font-size: 1.3rem;
  margin-top: 2rem;
  margin-bottom: 1rem;
  font-weight: 600;
  color: rgba(255, 255, 255, 0.9);
}

.markdown-body blockquote {
  padding: 1.5rem 2rem;
  border-left: 4px solid #7877c6;
  background: linear-gradient(135deg, rgba(120, 119, 198, 0.15), rgba(255, 119, 198, 0.15));
  border-radius: 0 12px 12px 0;
  margin: 2rem 0;
  position: relative;
  font-style: italic;
  color: rgba(255, 255, 255, 0.9);
}

.markdown-body blockquote::before {
  content: '"';
  position: absolute;
  top: -10px;
  left: 15px;
  font-size: 3rem;
  color: #7877c6;
  opacity: 0.5;
}

.markdown-body code {
  padding: 0.3em 0.6em;
  margin: 0;
  font-size: 0.9em;
  background: rgba(120, 119, 198, 0.2);
  border: 1px solid rgba(120, 119, 198, 0.4);
  border-radius: 6px;
  font-family: 'JetBrains Mono', 'Fira Code', 'SF Mono', Monaco, 'Cascadia Code', 'Roboto Mono', Consolas, 'Courier New', monospace;
  color: #ff77c4;
}

.markdown-body pre {
  margin: 2rem 0;
  border-radius: 12px;
  padding: 1.5rem;
  overflow: auto;
  background: rgba(0, 0, 0, 0.8) !important;
  border: 1px solid rgba(255, 255, 255, 0.2);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.5);
  position: relative;
}

.markdown-body pre::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 3px;
  background: linear-gradient(135deg, #7877c6, #ff77c4);
  border-radius: 12px 12px 0 0;
}

.markdown-body pre code {
  padding: 0;
  margin: 0;
  background-color: transparent;
  font-size: 0.95rem;
  line-height: 1.6;
  border: none;
  color: #e1e1e6;
}

.markdown-body ul, .markdown-body ol {
  margin: 1.5rem 0;
  padding-left: 2rem;
}

.markdown-body li {
  margin-bottom: 0.8rem;
  line-height: 1.7;
  color: rgba(255, 255, 255, 0.9);
}

.markdown-body ul li::marker {
  color: #7877c6;
}

.markdown-body ol li::marker {
  color: #7877c6;
  font-weight: 600;
}

.markdown-body table {
  border-collapse: collapse;
  width: 100%;
  margin: 2rem 0;
  overflow: auto;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
  background: rgba(255, 255, 255, 0.1);
}

.markdown-body table th {
  background: linear-gradient(135deg, rgba(120, 119, 198, 0.3), rgba(255, 119, 198, 0.3));
  font-weight: 700;
  color: #ffffff;
}

.markdown-body table th, .markdown-body table td {
  padding: 1rem 1.5rem;
  border: 1px solid rgba(120, 119, 198, 0.3);
  text-align: left;
  color: rgba(255, 255, 255, 0.9);
}

.markdown-body table tbody tr:hover {
  background: rgba(120, 119, 198, 0.15);
}

.markdown-body a {
  color: #7877c6;
  text-decoration: none;
  border-bottom: 2px solid transparent;
  transition: all 0.3s ease;
  font-weight: 500;
}

.markdown-body a:hover {
  border-bottom-color: #7877c6;
  background: rgba(120, 119, 198, 0.2);
  padding: 2px 4px;
  border-radius: 4px;
  color: #ff77c4;
}

.markdown-body img {
  max-width: 100%;
  height: auto;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
  margin: 2rem 0;
  transition: transform 0.3s ease;
}

.markdown-body img:hover {
  transform: scale(1.02);
}

.raw-content {
  color: rgba(255, 255, 255, 0.9);
  font-family: 'JetBrains Mono', 'Fira Code', 'SF Mono', Monaco, 'Cascadia Code', 'Roboto Mono', Consolas, 'Courier New', monospace;
}

.raw-content pre {
  white-space: pre-wrap;
  word-wrap: break-word;
  background: rgba(0, 0, 0, 0.5);
  padding: 1.5rem;
  border-radius: 8px;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.no-content {
  text-align: center;
  color: rgba(255, 255, 255, 0.6);
  font-style: italic;
  padding: 2rem;
}

.article-footer {
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 16px;
  padding: 2rem;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
}

.article-nav {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

/* 自定义滚动条 */
.article-sidebar::-webkit-scrollbar {
  width: 6px;
}

.article-sidebar::-webkit-scrollbar-track {
  background: rgba(0, 0, 0, 0.2);
  border-radius: 3px;
}

.article-sidebar::-webkit-scrollbar-thumb {
  background: linear-gradient(135deg, #7877c6, #ff77c4);
  border-radius: 3px;
}

.article-sidebar::-webkit-scrollbar-thumb:hover {
  background: linear-gradient(135deg, #6366f1, #ec4899);
}

@media (max-width: 1200px) {
  .article-layout {
    gap: 2rem;
  }

  .article-sidebar {
    width: 240px;
  }

  .article-content-wrapper {
    padding: 2.5rem;
  }
}

@media (max-width: 1024px) {
  .article-layout {
    flex-direction: column;
    gap: 1.5rem;
  }

  .article-sidebar {
    width: 100%;
    position: static;
    max-height: none;
    margin-bottom: 1.5rem;
    order: -1;
  }

  .toc-container {
    padding: 1.2rem;
  }

  .article-header {
    padding: 2rem;
  }

  .article-title {
    font-size: 2rem;
  }

  .article-content-wrapper {
    padding: 2rem;
  }

  .markdown-body {
    font-size: 1.05rem;
  }
}

@media (max-width: 768px) {
  .article-layout {
    padding: 0 0.5rem;
  }

  .article-info {
    flex-direction: column;
    align-items: flex-start;
    gap: 1rem;
  }

  .article-meta {
    flex-direction: column;
    align-items: flex-start;
    gap: 1rem;
    margin-bottom: 1rem;
  }

  .article-title {
    font-size: 1.8rem;
    margin-bottom: 1.5rem;
  }

  .article-header {
    padding: 1.5rem;
  }

  .article-content-wrapper {
    padding: 1.5rem;
  }

  .article-actions {
    justify-content: center;
    flex-direction: column;
  }

  .article-actions .el-button {
    width: 100%;
  }

  .markdown-body {
    font-size: 1rem;
  }

  .markdown-body h1 {
    font-size: 1.8rem;
  }

  .markdown-body h2 {
    font-size: 1.6rem;
  }

  .markdown-body h3 {
    font-size: 1.4rem;
  }

  .markdown-body pre {
    margin: 1.5rem -0.5rem;
    border-radius: 8px;
  }

  .markdown-body table {
    font-size: 0.9rem;
  }

  .markdown-body table th, .markdown-body table td {
    padding: 0.75rem 1rem;
  }
}

@media (max-width: 480px) {
  .toc-item {
    font-size: 0.9rem;
    padding: 0.5rem 0.8rem;
  }

  .meta-item {
    padding: 0.4rem 0.8rem;
    font-size: 0.9rem;
  }

  .markdown-body {
    line-height: 1.7;
  }
}
</style>