<script setup>
import { inject } from 'vue'

const locale = inject('antLocale')
</script>

<template>
  <a-config-provider :locale="locale">
    <router-view />
  </a-config-provider>
</template>

<style scoped>

</style>
