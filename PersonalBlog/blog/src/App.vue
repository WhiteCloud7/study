<template>
  <blog-nav></blog-nav>
  <router-view></router-view>
</template>

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