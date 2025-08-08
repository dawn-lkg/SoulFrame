<script setup>
import {computed, onMounted, ref} from 'vue'
import {useRouter} from 'vue-router'
import {useArticleStore} from '../store'
import {useUserStore} from '../store/user'
import GlassCard from '../components/GlassCard.vue'
import {ElEmpty} from 'element-plus'
import {Calendar, Document} from '@element-plus/icons-vue'

const router = useRouter()
const articleStore = useArticleStore()
const userStore = useUserStore()
const loading = ref(true)

// 初始化示例数据
onMounted(() => {
  articleStore.initSampleData()
  // 模拟加载效果
  setTimeout(() => {
    loading.value = false
  }, 300)
})

const articles = computed(() => {
  const sortedArticles = articleStore.articles.slice().sort((a, b) => {
    const dateA = a.updatedAt || a.createdAt
    const dateB = b.updatedAt || b.createdAt
    return new Date(dateB) - new Date(dateA)
  })
  return sortedArticles
})

const viewArticle = (id) => {
  router.push(`/article/${id}`)
}

const formatDate = (dateString) => {
  const date = new Date(dateString)
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit'
  })
}

const getReadingTime = (content) => {
  if (!content) return '1 分钟';
  const wordsPerMinute = 300; // 假设平均阅读速度为每分钟300字
  const wordCount = content.length;
  const readingTime = Math.max(1, Math.ceil(wordCount / wordsPerMinute));
  return `${readingTime} 分钟`;
}

const navigateTo = (path) => {
  router.push(path)
}
</script>

<template>
  <div class="home">
    <!-- Hero区域 -->
    <section class="hero">
      <div class="hero-content">
        <h1 class="hero-title floating">SoulFrame</h1>
        <p class="hero-subtitle floating floating-delay-1">探索技术的无限可能</p>
        <button class="hero-btn floating floating-delay-2" @click="navigateTo('/editor')">
          开始写作
        </button>
      </div>
    </section>

    <!-- 博客内容区域 -->
    <section class="blog-content">
      <div class="blog-container">
        <div class="blog-main">
          <div class="section-header">
            <h2 class="section-title">最新文章</h2>
          </div>

          <div class="article-list">
            <GlassCard
                v-for="article in articles"
                :key="article.id"
                class="article-card"
                @click="viewArticle(article.id)"
            >
              <div class="article-content">
                <div class="article-info">
                  <h3 class="article-title">{{ article.title }}</h3>
                  <p class="article-desc">{{ article.description || article.content?.substring(0, 150) + '...' }}</p>
                  <div class="article-meta">
                    <span class="meta-item">
                      <el-icon><Calendar/></el-icon>
                      {{ formatDate(article.createdAt) }}
                    </span>
                    <span class="meta-item">
                      <el-icon><Document/></el-icon>
                      {{ getReadingTime(article.content) }}
                    </span>
                  </div>
                  <div v-if="article.tags && article.tags.length" class="article-tags">
                    <span
                        v-for="tag in article.tags"
                        :key="tag"
                        class="tag-item"
                    >
                      {{ tag }}
                    </span>
                  </div>
                </div>
              </div>
            </GlassCard>

            <el-empty
                v-if="articles.length === 0 && !loading"
                :image-size="200"
                description="暂无文章"
            />
          </div>
        </div>

        <!-- 侧边栏 -->
        <aside class="blog-sidebar">
          <GlassCard class="profile-card">
            <div class="profile-header">
              <img :src="userStore.userInfo.avatar" alt="Profile" class="profile-avatar">
              <h3 class="profile-name">{{ userStore.userInfo.name }}</h3>
              <p class="profile-bio">{{ userStore.userInfo.bio }}</p>
            </div>

            <div class="profile-stats">
              <div class="stat-item">
                <span class="stat-value">{{ userStore.userInfo.articlesCount || articles.length }}</span>
                <span class="stat-label">文章</span>
              </div>
              <div class="stat-item">
                <span class="stat-value">{{ userStore.userInfo.readCount }}+</span>
                <span class="stat-label">阅读</span>
              </div>
              <div class="stat-item">
                <span class="stat-value">{{ userStore.userInfo.followers }}</span>
                <span class="stat-label">粉丝</span>
              </div>
            </div>

            <div class="profile-links">
              <el-button class="profile-button" plain type="primary" @click="navigateTo('/editor')">
                写文章
              </el-button>
            </div>
          </GlassCard>

          <GlassCard class="sidebar-card">
            <h4 class="sidebar-title">快速链接</h4>
            <div class="sidebar-links">
              <a class="sidebar-link" href="#">技术博客</a>
              <a class="sidebar-link" href="#">学习笔记</a>
              <a class="sidebar-link" href="#">项目分享</a>
              <a class="sidebar-link" href="#">关于我</a>
            </div>
          </GlassCard>

          <GlassCard class="sidebar-card">
            <h4 class="sidebar-title">公告</h4>
            <div class="announcement-content">
              <p>🎉 欢迎来到 SoulFrame 博客！</p>
              <p>这里记录技术成长的每一步，分享编程路上的思考与感悟。</p>
            </div>
          </GlassCard>
        </aside>
      </div>
    </section>
  </div>
</template>

<style scoped>
.home {
  max-width: 100%;
  margin: 0 auto;
}

.hero {
  height: 400px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  position: relative;
  text-align: center;
  margin-bottom: 3rem;
  border-radius: 20px;
  overflow: hidden;
  background: linear-gradient(135deg, rgba(120, 119, 198, 0.1) 0%, rgba(255, 119, 198, 0.1) 50%, rgba(120, 219, 255, 0.1) 100%);
  border: 1px solid rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
}

.hero::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: radial-gradient(circle at 20% 80%, rgba(120, 119, 198, 0.3) 0%, transparent 50%),
  radial-gradient(circle at 80% 20%, rgba(255, 119, 198, 0.3) 0%, transparent 50%),
  radial-gradient(circle at 40% 40%, rgba(120, 219, 255, 0.2) 0%, transparent 50%);
  z-index: -1;
}

.hero-content {
  position: relative;
  z-index: 1;
  max-width: 800px;
  padding: 0 1rem;
}

.hero-title {
  font-size: 3rem;
  font-weight: 800;
  margin-bottom: 1.5rem;
  background: linear-gradient(135deg, #7877c6, #ff77c4);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.hero-subtitle {
  font-size: 1.25rem;
  color: var(--text-secondary);
  margin-bottom: 2rem;
  opacity: 0.9;
}

.hero-btn {
  padding: 0.75rem 2rem;
  border: none;
  border-radius: 12px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  text-decoration: none;
  display: inline-block;
  background: linear-gradient(135deg, #7877c6, #ff77c4);
  color: white;
  box-shadow: 0 4px 15px rgba(120, 119, 198, 0.3);
  position: relative;
  overflow: hidden;
}

.hero-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(120, 119, 198, 0.4);
}

.hero-btn::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(
      90deg,
      transparent,
      rgba(255, 255, 255, 0.2),
      transparent
  );
  transition: left 0.5s ease;
}

.hero-btn:hover::before {
  left: 100%;
}

.blog-container {
  max-width: 1280px;
  margin: 0 auto;
  display: flex;
  gap: 2rem;
  padding: 0 1rem;
}

.blog-main {
  flex: 1;
}

.section-header {
  margin-bottom: 1.5rem;
}

.section-title {
  font-size: 1.5rem;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0;
}

.blog-sidebar {
  width: 320px;
  flex-shrink: 0;
}

.article-list {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.article-card {
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
  animation: fadeInUp 0.6s ease forwards;
  opacity: 0;
  transform: translateY(20px);
}

.article-card:nth-child(1) {
  animation-delay: 0.1s;
}

.article-card:nth-child(2) {
  animation-delay: 0.2s;
}

.article-card:nth-child(3) {
  animation-delay: 0.3s;
}

@keyframes fadeInUp {
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.article-card:hover {
  transform: translateY(-4px);
}

.article-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(
      135deg,
      rgba(120, 119, 198, 0.05),
      rgba(255, 119, 198, 0.05)
  );
  opacity: 0;
  transition: opacity 0.3s ease;
  z-index: -1;
}

.article-card:hover::before {
  opacity: 1;
}

.article-content {
  display: flex;
  gap: 1.5rem;
}

.article-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.article-title {
  font-size: 1.25rem;
  font-weight: 700;
  margin: 0;
  color: var(--text-primary);
  line-height: 1.4;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
}

.article-card:hover .article-title {
  color: var(--primary-color);
}

.article-title::after {
  content: '';
  position: absolute;
  bottom: -2px;
  left: 0;
  width: 0;
  height: 2px;
  background: linear-gradient(90deg, var(--primary-color), #ff77c4);
  transition: width 0.3s ease;
}

.article-card:hover .article-title::after {
  width: 100%;
}

.article-desc {
  color: var(--text-secondary);
  font-size: 0.95rem;
  line-height: 1.6;
  margin: 0;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  transition: color 0.3s ease;
}

.article-card:hover .article-desc {
  color: var(--text-primary);
}

.article-meta {
  display: flex;
  gap: 1.5rem;
  color: var(--text-secondary);
  font-size: 0.85rem;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 0.25rem;
}

.article-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.tag-item {
  display: flex;
  align-items: center;
  gap: 0.25rem;
  background: linear-gradient(
      135deg,
      rgba(66, 153, 225, 0.15),
      rgba(120, 119, 198, 0.15)
  );
  padding: 0.2rem 0.5rem;
  border-radius: 12px;
  font-size: 0.8rem;
  color: var(--primary-color);
  transition: all 0.3s ease;
  border: 1px solid rgba(66, 153, 225, 0.2);
}

.tag-item:hover {
  background: linear-gradient(
      135deg,
      rgba(66, 153, 225, 0.25),
      rgba(120, 119, 198, 0.25)
  );
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(120, 119, 198, 0.2);
}

/* 侧边栏样式 */
.profile-card, .sidebar-card {
  margin-bottom: 1.5rem;
  animation: fadeIn 0.6s ease forwards;
  opacity: 0;
}

.profile-card {
  animation-delay: 0.4s;
}

.sidebar-card:nth-child(2) {
  animation-delay: 0.5s;
}

.sidebar-card:nth-child(3) {
  animation-delay: 0.6s;
}

@keyframes fadeIn {
  to {
    opacity: 1;
  }
}

.profile-header {
  text-align: center;
  padding: 1.5rem 1.5rem 1rem;
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);
}

.profile-avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  object-fit: cover;
  margin-bottom: 1rem;
  border: 3px solid var(--primary-color);
}

.profile-name {
  font-size: 1.25rem;
  font-weight: 700;
  margin: 0 0 0.5rem;
  color: var(--text-primary);
}

.profile-bio {
  color: var(--text-secondary);
  font-size: 0.9rem;
  margin: 0;
  line-height: 1.5;
}

.profile-stats {
  display: flex;
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);
}

.stat-item {
  flex: 1;
  text-align: center;
  padding: 1rem 0;
}

.stat-value {
  display: block;
  font-size: 1.25rem;
  font-weight: 700;
  color: var(--text-primary);
}

.stat-label {
  font-size: 0.85rem;
  color: var(--text-secondary);
}

.profile-links {
  padding: 1.25rem;
}

.profile-button {
  width: 100%;
  background: linear-gradient(135deg, #7877c6, #ff77c4);
  border: none;
  color: white;
  font-weight: 600;
  position: relative;
  overflow: hidden;
  transition: all 0.3s ease;
}

.profile-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(120, 119, 198, 0.4);
}

.profile-button::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(
      90deg,
      transparent,
      rgba(255, 255, 255, 0.2),
      transparent
  );
  transition: left 0.5s ease;
}

.profile-button:hover::before {
  left: 100%;
}

.sidebar-title {
  font-size: 1.1rem;
  font-weight: 700;
  padding: 1rem 1rem 0.5rem;
  margin: 0;
  color: var(--text-primary);
}

.sidebar-links {
  padding: 0 1rem 1rem;
}

.sidebar-link {
  display: block;
  color: var(--text-secondary);
  text-decoration: none;
  padding: 0.5rem 0;
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);
  transition: color 0.3s ease;
}

.sidebar-link:hover {
  color: var(--primary-color);
}

.sidebar-link:last-child {
  border-bottom: none;
}

.announcement-content {
  padding: 0 1rem 1rem;
  color: var(--text-secondary);
  font-size: 0.9rem;
  line-height: 1.6;
}

.announcement-content p {
  margin-bottom: 0.75rem;
}

.announcement-content p:last-child {
  margin-bottom: 0;
}

@media (max-width: 1024px) {
  .blog-container {
    flex-direction: column;
  }

  .blog-sidebar {
    width: 100%;
  }
}

@media (max-width: 768px) {
  .hero {
    height: 300px;
  }

  .hero-title {
    font-size: 2.25rem;
  }

  .hero-subtitle {
    font-size: 1rem;
  }

  .article-content {
    flex-direction: column;
  }

  .article-info {
    gap: 1rem;
  }

  .article-meta {
    flex-wrap: wrap;
  }

  .profile-header {
    padding: 1rem;
  }

  .profile-stats {
    flex-wrap: wrap;
  }

  .stat-item {
    flex: 0 0 33.333%;
  }
}
</style>