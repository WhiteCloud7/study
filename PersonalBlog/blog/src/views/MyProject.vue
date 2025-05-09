<template>
  <div class="myProject-project">
    <my-projects></my-projects>
  </div>
</template>
<script setup>
import MyProjects from "@/components/project/MyProjects";
import {useHead} from "@vueuse/head";
useHead({
  title:"项目",
})
</script>
<style>
.myProject-project{
  width: 100%;
  height: 89vh;
}
</style>