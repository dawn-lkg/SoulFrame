import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'layout',
    component: () => import('../layout/silderLayout/index.vue')
  },
  {
    path: '/about',
    name: 'About',
    component: () => import('../views/About.vue')
  },
  {
    path: '/test',
    name: 'Test',
    component: () => import('../views/test.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router 