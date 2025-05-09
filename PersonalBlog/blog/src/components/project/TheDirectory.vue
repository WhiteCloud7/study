<template>
  <div class="project-directory"
       :style="{backgroundColor:isClick?backgroundColor:''}"
       @click="isChecked"
  ><el-icon>
    <Folder v-if="type==='文件夹'"></Folder>
    <i class="fa-regular fa-file  " v-else></i>
  </el-icon>
    <input v-if="isRename" class="project-rename" v-model="newFileName" @keydown.enter="rename">
    <p class="project-directoryName" v-else>{{fileName}}</p>
    <p class="project-modifyTime">{{modifyTime}}</p>
    <p class="project-type">{{type}}</p>
  </div>
</template>
<script setup>
import {Folder} from '@element-plus/icons-vue';
import {defineProps, ref, onMounted, onUnmounted,defineEmits} from 'vue';
import '@fortawesome/fontawesome-free/css/all.css';

const props = defineProps({
  fileId:Number,
  fileName: String,
  modifyTime:String,
  type: String,
  dirLevel:String,
  utilsEnable:Boolean,
  isRename:Boolean,
  RenameNewFileName:String
})
const emit = defineEmits(['isCheck','rename']);

const backgroundColor = ref('');
const isClick = ref(false);
const newFileName = ref('');

function isChecked(e){
  if(!e.ctrlKey&&e.button===0){
    isClick.value = true;
    backgroundColor.value = 'lightgray';
  }else if(e.ctrlKey&&e.button===0){
    if(backgroundColor.value === 'white'){
      isClick.value = true;
      backgroundColor.value = 'lightgray';
    }else{
      isClick.value = false;
      backgroundColor.value = 'white';
    }
  }
  emit('isCheck',isClick.value,props.fileId);
}

function GlobalClick(event){
  const isDirectoryClick = event.target.closest(".project-directory");
  if (!isDirectoryClick) {
    isClick.value = false;
    backgroundColor.value = "white";
  }
  emit('isCheck',isClick.value,props.fileId)
}

function rename(){
  emit('rename',newFileName.value);
}

onMounted(()=>{
  document.addEventListener("click",GlobalClick);
})
onUnmounted(()=>{
  document.removeEventListener("click",GlobalClick);
})
</script>
<style>
.project-directory {
  width: 95%;
  padding: 4px;
  display: flex;
  align-items: center;
  transition: background-color 0.3s ease;
  cursor: pointer;
  margin-left: 2.5%;
}
.project-directory:hover{
  background-color: lightgray;
}

.project-directory el-icon {
  display: inline-flex;
  margin-right: 4px;
}

.project-directoryName,
.project-rename{
  display: inline-flex;
  margin-left: 10px;
  width: 405px;
}
.project-modifyTime{
  display: inline-flex;
  width: 200px;
  margin-left: 240px;
}
.project-type{
  display: inline-flex;
  width: 200px;
  margin-left: 80px;
}
</style>