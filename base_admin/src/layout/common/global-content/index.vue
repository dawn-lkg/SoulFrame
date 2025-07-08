<template>
  <router-view v-slot="{ Component }">
    <transition name="content-animation" mode="out-in">
      <keep-alive>
        <component :is="Component" />
      </keep-alive>
      </transition>
  </router-view>
</template>


<script setup>
import { useRoute } from 'vue-router'

const route = useRoute()


</script>

<style scoped> 

.content-animation-enter-active,
.content-animation-leave-active {
  transition: all 0.25s cubic-bezier(0.645, 0.045, 0.355, 1);
}

.content-animation-enter-from {
  opacity: 0;
  transform: translateX(-20px);
}

.content-animation-leave-to {
  opacity: 0;
  transform: translateX(10px);
}
</style>
