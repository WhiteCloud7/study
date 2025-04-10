<template>
  <div class="nLevelDirectory">
    <el-icon><Folder /></el-icon>
    <p style="margin-left: 10px">一级目录</p>
  </div>
</template>
<script setup>
import {Folder} from '@element-plus/icons-vue';
</script>
<style>
.nLevelDirectory {
  width: 95%;
  padding: 4px;
  display: flex;
  align-items: center;
  transition: background-color 0.3s ease;
  cursor: pointer;
  margin-left: 2.5%;
}

.nLevelDirectory:hover {
  background-color: lightgray;
}

.nLevelDirectory el-icon {
  display: inline-flex;
  margin-right: 4px;
}
</style>