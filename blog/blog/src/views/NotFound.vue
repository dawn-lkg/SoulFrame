<script setup>
import {useRouter} from 'vue-router'
import GlassCard from '../components/GlassCard.vue'

const router = useRouter()

const goHome = () => {
  router.push('/')
}
</script>

<template>
  <div class="not-found">
    <GlassCard class="not-found-card">
      <div class="not-found-content">
        <h1>404</h1>
        <h2>页面不存在</h2>
        <p>您访问的页面不存在或已被删除</p>
        <el-button class="home-button" type="primary" @click="goHome">返回首页</el-button>
      </div>
    </GlassCard>
  </div>
</template>

<style scoped>
.not-found {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  min-height: 500px;
  text-align: center;
  padding: 1.5rem;
}

.not-found-card {
  max-width: 500px;
  width: 100%;
  animation: fadeInUp 0.6s ease forwards;
  opacity: 0;
  transform: translateY(20px);
}

@keyframes fadeInUp {
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.not-found-content h1 {
  font-size: 80px;
  background: linear-gradient(135deg, #7877c6, #ff77c4);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin-bottom: 0;
}

.not-found-content h2 {
  font-size: 30px;
  color: var(--text-primary);
  margin-top: 0;
}

.not-found-content p {
  color: var(--text-secondary);
  margin-bottom: 30px;
}

.home-button {
  background: linear-gradient(135deg, #7877c6, #ff77c4);
  border: none;
  color: white;
  font-weight: 600;
  position: relative;
  overflow: hidden;
  transition: all 0.3s ease;
}

.home-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(120, 119, 198, 0.4);
}

.home-button::before {
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

.home-button:hover::before {
  left: 100%;
}
</style>