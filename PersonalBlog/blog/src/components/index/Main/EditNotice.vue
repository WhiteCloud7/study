<template>
  <div class="editNotice">
    <div class="editContext">
      <p>哈哈哈</p>
    </div>
    <el-button-group class="button-set">
      <el-button class="edit-button">编辑</el-button>
      <el-button class="edit-button">保存</el-button>
      <el-button class="edit-back" @click="editBack">返回</el-button>
    </el-button-group>
  </div>
</template>
<script setup>
import {defineEmits} from "vue";

const emit = defineEmits(["editBack"]);
const editBack = () => {
  emit("editBack");
};
</script>
<style>
.editNotice{
  display: flex;
  position: absolute;
  left:15%;
  right: 15%;
  top: 75px;
  width: 70%;
  height: 460px;
  z-index: 600;
  background-color: rgba(255, 255, 255, 0.8);
  flex-direction: column;
  padding-right: 15px;
}
.editContext{
  padding-left: 8px;
  padding-right: 8px;
  width: 100%;
  min-height: 90%;
  overflow-y: auto;
  background-color: rgb(216.8, 235.6, 255);
  box-shadow: var(--el-box-shadow);
}
.button-set{
  background-color: rgba(255, 255, 255, 0.65);
  display: flex;
  flex-direction: row;
  margin-left: 8px;
  width: 100%;
  height: 10%;
}
.edit-button{
  width: 10%;
  margin-top: 1%;
  font-size: 2em;
  margin-left: 10px ;
}
.edit-back{
  width: 10%;
  margin-top: 1%;
  position: relative;
  left: 580px;
}
</style>