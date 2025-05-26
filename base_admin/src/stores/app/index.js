export const useAppStore = defineStore('app',{
    state:()=> reactive({
        settingDrawerVisible: false,
    }),
    actions: {
        // 打开设置抽屉
        openSettingDrawer(){
            this.settingDrawerVisible = true
        },
        // 关闭设置抽屉
        closeSettingDrawer(){
            this.settingDrawerVisible = false
        },
        // 切换设置抽屉
        toggleSettingDrawer(){
            this.settingDrawerVisible = !this.settingDrawerVisible
        }
    }
})