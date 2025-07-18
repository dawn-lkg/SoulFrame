import {createPinia} from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
import {useAppStore} from './app'
import {useAuthStore} from './auth'
import {useConfigStore} from './config'
import {useDictStore} from './dict'
import {useThemeStore} from './theme'

const pinia = createPinia()

pinia.use(piniaPluginPersistedstate)

export {
    useAppStore,
    useAuthStore,
    useConfigStore,
    useDictStore,
    useThemeStore
}

export default pinia
