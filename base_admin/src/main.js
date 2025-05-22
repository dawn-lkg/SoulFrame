import { createApp } from 'vue'
import Antd from 'ant-design-vue'
import 'ant-design-vue/dist/reset.css'
import zhCN from 'ant-design-vue/es/locale/zh_CN'
import { ConfigProvider } from 'ant-design-vue'
import './style.css'
import App from './App.vue'

const app = createApp(App)
app.use(Antd)
app.config.globalProperties.$antdLocale = zhCN
app.component('ConfigProvider', ConfigProvider)
app.mount('#app')
