<template>
  <div class="editNotice">
    <div class="editContext">
      <el-input type="text" v-model="noticeTitle" :readonly="isReadOnly" :input-style="{cursor:cursorType }"></el-input>
      <el-input type="textarea" v-model="messageText" :readonly="isReadOnly" :input-style="{cursor:cursorType }"></el-input>
    </div>
    <el-button-group class="button-set">
      <el-button class="edit-button" @click="edit">编辑</el-button>
      <el-button class="edit-button" @click="save" v-if="route.path!=='/index/newNotice'">保存</el-button>
      <el-button class="edit-button" @click="newNotice" v-if="route.path==='/index/newNotice'">发布</el-button>
      <el-button class="edit-back" @click="editBack">返回</el-button>
    </el-button-group>
  </div>
</template>
<script setup>
import {defineEmits, defineProps, onBeforeUnmount, onMounted, ref} from "vue";
import axios from "@/axios";
import {useRouter,useRoute} from "vue-router";
const props = defineProps({
  message: String,
  noticeId: Number,
  title: String
});
const messageText = ref(props.message);
const emit = defineEmits(["editBack"]);
const isReadOnly = ref(true);
const cursorType = ref("default");
const noticeTitle = ref(props.title);
const router = useRouter();
const route = useRoute();

function edit(){
  isReadOnly.value = !isReadOnly.value;
  cursorType.value = isReadOnly.value ? "default" : "text";
}

function save(){
  const params = new URLSearchParams();
  params.append("noticeId", props.noticeId);
  params.append("title",noticeTitle.value);
  params.append("messageText", messageText.value);
  axios.post("http://localhost:8081/saveNotice",params,{
    headers: {
      "Content-Type": "application/x-www-form-urlencoded"
    }
  }).then(res =>{
    if(res.data.message=="保存成功"){
      confirm("保存成功！");
      window.location.reload();
    }
    else
      alert("保存失败!");
  }).catch(err =>{
    console.log(err);
  });
}

function newNotice(){
  const params = new URLSearchParams();
  params.append("title",noticeTitle.value);
  params.append("noticeContent", messageText.value);
  axios.post("http://localhost:8081/newNotice",params,{
    headers: {
      "Content-Type": "application/x-www-form-urlencoded"
    }
  }).then(res =>{
    if(res.data.message=="发布成功"){
      confirm("发布成功！");
      router.push("/index");
      window.location.reload();
    }
    else
      alert("发布失败!");
  }).catch(err =>{
    console.log(err);
  });
}

function editBack(){
  if(route.path === "/index/newNotice")
    router.push("/index");
  emit('editBack')
}
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
  padding: 8px;
  width: 100%;
  min-height: 90%;
  background-color: rgb(216.8, 235.6, 255);
  box-shadow: var(--el-box-shadow);
  word-break: break-word;
  overflow-y: hidden;
}

.editContext textarea{
  height: 375px !important;
  resize: none !important;
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
  left: 560px;
}
</style>