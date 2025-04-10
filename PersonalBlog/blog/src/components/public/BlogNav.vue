<template>
  <div class="h-6" />
  <el-affix>
    <el-menu
        :default-active="activeIndex"
        class="el-menu-demo"
        mode="horizontal"
        background-color="#545c64"
        text-color="#fff"
        active-text-color="#ffd04b"
        @select="handleSelect"
        popper-class="menu"
        router
    >
      <el-menu-item index="index" >首页</el-menu-item>
      <el-menu-item index="resume">个人简历</el-menu-item>
      <el-menu-item index="project">项目</el-menu-item>
      <el-menu-item index="article">文章</el-menu-item>
      <el-menu-item index="contactMe">与我联系</el-menu-item>
    </el-menu>
  </el-affix>
</template>
<script lang="ts" setup>
import {ref} from 'vue'
const activeIndex = ref('1')
const handleSelect = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
}
</script>
<style>
.el-menu-demo{
  margin-top: 0px;
  height: 60px;
}
.el-menu-demo .el-menu-item {
  width: 220px;
}
</style>