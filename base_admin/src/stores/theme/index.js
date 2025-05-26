import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useThemeStore = defineStore('theme', () => {
  // 主题相关状态
  const primaryColor = ref('#1890ff')
  const isDarkMode = ref(false)
  const menuTheme = ref('light')
  const layout = ref('side') // side | top | mix

  // 布局配置
  const layoutConfig = ref({
    showBreadcrumb: true,
    fixedHeader: true,
    fixedSidebar: true,
    splitMenus: false,
    showLogo: true,
    showTagsView: true,
    showFooter: false,
    contentWidth: 'fluid', // fluid | fixed
  })

  // 修改主题色
  function changePrimaryColor(color) {
    primaryColor.value = color
    // 这里可以添加动态修改主题色的逻辑
  }

  // 切换暗黑模式
  function toggleDarkMode() {
    isDarkMode.value = !isDarkMode.value
  }

  // 修改布局配置
  function updateLayoutConfig(config) {
    layoutConfig.value = {
      ...layoutConfig.value,
      ...config
    }
  }

  return {
    primaryColor,
    isDarkMode,
    menuTheme,
    layout,
    layoutConfig,
    changePrimaryColor,
    toggleDarkMode,
    updateLayoutConfig
  }
}) 