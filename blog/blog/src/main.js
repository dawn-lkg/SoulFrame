import {createApp} from 'vue'
import {createPinia} from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import router from './router'
import App from './App.vue'
import './style.css'
import 'mavon-editor/dist/css/index.css'
import {mavonEditor} from 'mavon-editor'
import {useUserStore} from './store/user'
import {useArticleStore} from './store'

// 创建应用实例
const app = createApp(App)

// 注册所有图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

// 注册mavon-editor组件
app.component('mavonEditor', mavonEditor)

// 使用插件
app.use(createPinia())
app.use(router)
app.use(ElementPlus)

// 初始化用户store
const userStore = useUserStore()
userStore.updateUserInfo({
    name: 'SoulFrame',
    avatar: '/src/assets/avatar.jpg',
    bio: '探索技术的无限可能，分享编程与生活的点滴感悟。',
    articlesCount: 0,
    readCount: 1200,
    followers: 89
})

// 初始化文章store
const articleStore = useArticleStore()
articleStore.initSampleData()

// 挂载应用
app.mount('#app')
