import {createRouter, createWebHistory} from 'vue-router'

const routes = [
    {
        path: '/',
        name: 'Home',
        component: () => import('../views/Home.vue')
    },
    {
        path: '/editor',
        name: 'Editor',
        component: () => import('../views/Editor.vue')
    },
    {
        path: '/article/:id',
        name: 'Article',
        component: () => import('../views/Article.vue')
    },
    {
        path: '/manage',
        name: 'Manage',
        component: () => import('../views/Manage.vue')
    },
    {
        path: '/:pathMatch(.*)*',
        name: 'NotFound',
        component: () => import('../views/NotFound.vue')
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router 