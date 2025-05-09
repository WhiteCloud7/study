<template>
  <el-container class="index-container">
    <el-aside class="index-left-aside">
      <index-left-aside></index-left-aside>
    </el-aside>
    <el-main class="index-main">
      <index-main></index-main>
    </el-main>
    <el-aside class="index-right-aside">
      <index-right-aside></index-right-aside>
    </el-aside>
  </el-container>
</template>

<script setup>
import IndexLeftAside from "@/components/index/indexLeftAside";
import IndexMain from "@/components/index/IndexMain";
import IndexRightAside from "@/components/index/IndexRightAside";
import {useHead} from "@vueuse/head";

useHead({
  title:"云白的个人博客",
})
</script>

<style scoped>
html,
body {
  height: 100%;
  margin: 0;
  padding: 0;
}

.index-container {
  height: 89vh;
  display: flex;
}

.index-left-aside {
  width: 220px;
  height: 100%;
  overflow: hidden;
}

.index-main {
  padding-top: 0;
  min-height: 89vh;
  overflow-y: auto;
  overflow-x: hidden;
  flex: 1;
  -ms-overflow-style: none; /*隐藏滚动条*/

}

.index-main::-webkit-scrollbar {  /*隐藏滚动条*/
  display: none;
}

.index-right-aside {
  display: flex;
  flex-direction: column;
  width: 220px;
  height: 100%;
  overflow: hidden;
}
</style>