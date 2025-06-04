export const useThemeStore = defineStore('theme', {
  state: () =>
    reactive({
      primaryColor: '#1890ff',
      isDarkMode: false,
      menuTheme: 'dark',
      layout: 'side', // side | top | mix
      layoutConfig: {
        showBreadcrumb: false,
        fixedHeader: true,
        fixedSidebar: true,
        splitMenus: false,
        showLogo: true,
        showTagsView: true,
        showFooter: false,
        contentWidth: 'fluid', // fluid | fixed
      },
    }),
  actions: {
    // 切换主题色
    changePrimaryColor(color) {
      this.primaryColor = color
      // 这里可以添加动态修改主题色的逻辑
    },
    //切换主题模式
    toggleDarkMode() {
      this.isDarkMode = !this.isDarkMode
    },
    changeLayout(layout) {
      this.layout = layout
    },
    changeMenuTheme(theme) {
      this.menuTheme = theme
    },
    // 设置面包屑显示
    setShowBreadcrumb(showBreadcrumb){
      this.layoutConfig.showBreadcrumb=showBreadcrumb;
    }
  },
})
