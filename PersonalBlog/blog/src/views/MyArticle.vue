<template>
  <div class="myArticle-article">
    <my-articles></my-articles>
  </div>
</template>
<script setup>
import MyArticles from "@/components/article/MyArticles";
import {useHead} from "@vueuse/head";
useHead({
  title:"文章",
})
</script>
<style>
myArticle-article{
  width: 100%;
  height: 89vh;
  overflow-y: auto;
}
</style>