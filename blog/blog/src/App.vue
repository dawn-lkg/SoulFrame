<script setup>
import {onMounted, ref} from 'vue'
import {useRouter} from 'vue-router'
import ParticleBackground from './components/ParticleBackground.vue'
import {useUserStore} from './store/user'
import {Close, EditPen, House, Management, Menu, Search} from '@element-plus/icons-vue'

const router = useRouter()
const isCollapse = ref(false)
const userStore = useUserStore()

const navigateTo = (path) => {
  router.push(path)
  isCollapse.value = false
}

const toggleMobileMenu = () => {
  isCollapse.value = !isCollapse.value
}

// 监听窗口大小变化，在大屏幕下自动展开菜单
onMounted(() => {
  const handleResize = () => {
    if (window.innerWidth > 768) {
      isCollapse.value = false
    }
  }

  window.addEventListener('resize', handleResize)

  return () => {
    window.removeEventListener('resize', handleResize)
  }
})
</script>

<template>
  <div class="blog-app">
    <!-- 粒子背景 -->
    <ParticleBackground/>

    <!-- 星空背景 -->
    <div class="star star-1" style="top: 10%; left: 20%;"></div>
    <div class="star star-2" style="top: 25%; left: 70%;"></div>
    <div class="star star-3" style="top: 40%; left: 40%;"></div>
    <div class="star star-1" style="top: 60%; left: 80%;"></div>
    <div class="star star-2" style="top: 75%; left: 30%;"></div>
    <div class="star star-3" style="top: 90%; left: 60%;"></div>

    <header class="blog-header">
      <div class="header-container">
        <div class="logo" @click="navigateTo('/')">
          <h1>Soul<span>Frame</span></h1>
        </div>

        <div class="header-search">
          <el-input
              :prefix-icon="Search"
              placeholder="搜索文章..."
              size="small"
          >
          </el-input>
        </div>

        <div class="mobile-toggle" @click="toggleMobileMenu">
          <el-icon size="24px">
            <Menu v-if="!isCollapse"/>
            <Close v-else/>
          </el-icon>
        </div>

        <nav :class="{ 'mobile-open': isCollapse }" class="nav-menu">
          <div class="nav-links">
            <el-button class="nav-link" text @click="navigateTo('/')">
              <el-icon>
                <House/>
              </el-icon>
              首页
            </el-button>
            <el-button class="nav-link" text @click="navigateTo('/editor')">
              <el-icon>
                <EditPen/>
              </el-icon>
              写文章
            </el-button>
            <el-button class="nav-link" text @click="navigateTo('/manage')">
              <el-icon>
                <Management/>
              </el-icon>
              管理
            </el-button>
            <div class="user-avatar">
              <el-avatar :size="32" :src="userStore.userInfo.avatar"/>
            </div>
          </div>
        </nav>
      </div>
    </header>

    <main class="blog-content">
      <router-view v-slot="{ Component }">
        <transition mode="out-in" name="fade">
          <component :is="Component"/>
        </transition>
      </router-view>
    </main>

    <footer class="blog-footer">
      <div class="footer-content">
        <p>© {{ new Date().getFullYear() }} SoulFrame Blog - 基于Vue3的简洁高效博客系统</p>
        <div class="footer-links">
          <a href="#" rel="noopener" target="_blank">GitHub</a>
          <a href="#" rel="noopener" target="_blank">关于我们</a>
          <a href="#" rel="noopener" target="_blank">联系我们</a>
        </div>
      </div>
    </footer>
  </div>
</template>

<style scoped>
.blog-app {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background: linear-gradient(135deg, #0c0c0c 0%, #1a1a2e 50%, #16213e 100%);
  position: relative;
}

.blog-app::before {
  content: '';
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: radial-gradient(circle at 20% 80%, rgba(120, 119, 198, 0.3) 0%, transparent 50%),
  radial-gradient(circle at 80% 20%, rgba(255, 119, 198, 0.3) 0%, transparent 50%),
  radial-gradient(circle at 40% 40%, rgba(120, 219, 255, 0.2) 0%, transparent 50%);
  z-index: -2;
}

.blog-header {
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 0 0 20px 20px;
  position: sticky;
  top: 10px;
  z-index: 100;
  margin: 0 20px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
}

.header-container {
  max-width: 1280px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 1.5rem;
  height: 64px;
}

.header-search {
  flex: 0 1 250px;
}

.header-search :deep(.el-input__wrapper) {
  background-color: rgba(45, 55, 72, 0.6);
  box-shadow: none;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.header-search :deep(.el-input__inner) {
  color: var(--text-primary);
}

.header-search :deep(.el-input__inner::placeholder) {
  color: var(--text-secondary);
}

.logo {
  cursor: pointer;
  display: flex;
  align-items: center;
}

.logo h1 {
  margin: 0;
  font-size: 1.6rem;
  font-weight: 700;
  letter-spacing: -0.5px;
  color: white;
}

.logo span {
  color: var(--primary-color);
  font-weight: 800;
}

.nav-menu {
  display: flex;
  align-items: center;
}

.nav-links {
  display: flex;
  gap: 12px;
  align-items: center;
}

.user-avatar {
  margin-left: 12px;
  cursor: pointer;
}

.nav-link {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #e2e8f0;
  font-size: 0.95rem;
  padding: 0.5rem 0.75rem;
}

.nav-link:hover {
  color: white;
  background-color: rgba(66, 153, 225, 0.15);
  border-radius: 6px;
}

.blog-content {
  flex-grow: 1;
  padding: 2rem 1.5rem;
  max-width: 1280px;
  width: 100%;
  margin: 0 auto;
}

.blog-footer {
  border-top: 1px solid var(--border-color);
  padding: 1.5rem 0;
  background-color: rgba(17, 24, 39, 0.8);
  backdrop-filter: blur(10px);
}

.footer-content {
  max-width: 1280px;
  margin: 0 auto;
  padding: 0 1.5rem;
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  align-items: center;
  color: var(--text-secondary);
  font-size: 0.9rem;
}

.footer-links {
  display: flex;
  gap: 1.5rem;
}

.mobile-toggle {
  display: none;
  cursor: pointer;
  color: white;
}

@media (max-width: 768px) {
  .header-container {
    height: 56px;
  }

  .mobile-toggle {
    display: block;
  }

  .header-search {
    display: none;
  }

  .nav-menu {
    position: fixed;
    top: 56px;
    left: 0;
    right: 0;
    background-color: var(--header-bg);
    padding: 1rem;
    box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
    display: none;
    animation: fadeIn 0.3s ease;
    backdrop-filter: blur(10px);
  }

  .nav-menu.mobile-open {
    display: block;
  }

  .nav-links {
    flex-direction: column;
    width: 100%;
  }

  .nav-link {
    width: 100%;
    justify-content: center;
  }

  .blog-content {
    padding: 1.5rem 1rem;
  }

  .footer-content {
    flex-direction: column;
    gap: 1rem;
    text-align: center;
  }

  .footer-links {
    justify-content: center;
  }
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
