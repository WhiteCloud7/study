<template>
  <div class="myArticle-article">
    <my-articles></my-articles>
  </div>
</template>
<script>
import MyArticles from "@/components/article/MyArticles";
export default {
  components: {MyArticles}
}
</script>
<style>
myArticle-article{
  width: 100%;
  height: 89vh;
  overflow-y: auto;
}
</style>