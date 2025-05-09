<template>
  <blog-nav v-if="$route.meta.showNav"></blog-nav>
  <router-view></router-view>
</template>
<script setup>
import {onMounted} from "vue";

onMounted(() => {
  let link = document.querySelector("link[rel~='icon']");
  if (!link) {
    link = document.createElement('link');
    link.rel = 'icon';
    document.getElementsByTagName('head')[0].appendChild(link);
  }
  link.href = '/photo/logo.png';
});
</script>
<style scoped>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
  background-color: rgb(235.9, 245.3, 255);
}
</style>
<script>
import BlogNav from "@/components/public/BlogNav";
export default {
  components: {BlogNav}
}
</script>