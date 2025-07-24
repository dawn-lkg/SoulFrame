<template>
  <router-view v-slot="{ Component }">
      <keep-alive>
        <component :is="Component" />
      </keep-alive>
  </router-view>
</template>


<script setup>

</script>

<style scoped> 

.content-animation-enter-active,
.content-animation-leave-active {
  transition: all 0.3s cubic-bezier(0.645, 0.045, 0.355, 1);
}

.content-animation-enter-from {
  opacity: 0;
  transform: translateX(-30px);
}

.content-animation-leave-to {
  opacity: 0;
  transform: translateX(30px);
}
</style>
