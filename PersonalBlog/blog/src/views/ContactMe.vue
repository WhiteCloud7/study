<template>
  <div class="contact-container">
    <el-aside class="contact-aside">
      <frends-list @loaded="handleFrendsListLoaded"></frends-list>
    </el-aside>
    <el-main class="contact-main" v-if="isFrendsListLoaded">
      <contact-main></contact-main>
    </el-main>
  </div>
</template>

<script setup>
import ContactMain from "@/components/ContactMe/ContactMain";
import FrendsList from "@/components/ContactMe/FrendsList";
import { ref, watch } from "vue";
import { useHead } from "@vueuse/head";

useHead({
  title: "联系我",
});

// 定义一个响应式变量表示 FrendsList 组件是否加载完成
const isFrendsListLoaded = ref(false);

const handleFrendsListLoaded = () => {
  isFrendsListLoaded.value = true;
};

// 监听 isFrendsListLoaded 的变化
watch(isFrendsListLoaded, (newValue) => {
  if (newValue) {
  }
});
</script>

<style>
.contact-container {
  display: flex;
  flex-direction: row;
  height: 89vh;
  background-color: #d9ecff;
  box-sizing: border-box;
}
.contact-aside {
  border: 1px solid black;
  border-radius: 10px;
  padding: 8px;
  background-color: #ecf5ff;
  box-sizing: border-box;
  overflow: hidden;
}
.contact-main {
  margin-left: 8px;
  border: 1px solid black;
  border-radius: 10px;
  background-color: #ecf5ff;
  box-sizing: border-box;
  height: 100%;
  padding: 0;
}
</style>