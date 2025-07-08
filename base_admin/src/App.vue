<script setup>
import { inject } from 'vue'
import { useAppStore } from '@/stores/app'

const appStore = useAppStore()
const locale = inject('antLocale')
</script>

<template>
  <a-config-provider :locale="locale" :component-size="appStore.componentSize">
    <router-view />
  </a-config-provider>
</template>

<style scoped>

</style>
