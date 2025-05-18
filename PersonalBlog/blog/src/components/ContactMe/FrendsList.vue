<template>
  <h1 class="contact-friendListHeader">好 友 列 表</h1>
  <div class="contact-friendList" v-for="friend in friendList" :key="friend" @click="chatChecked(friend[2])">
    <div class="contact-friendContent">
      <el-avatar class="friend-avatar" style="height:40px" :src="friend[1]"></el-avatar>
      <div class="friend-preview">
        <p class="friend-name">{{friend[0]}}</p>
        <p class="friend-newMessage">欢迎！</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import axios from "@/axios";
import { onMounted, ref, getCurrentInstance } from "vue";

const friendList = ref([]);
const instance = getCurrentInstance();
const currentChatObject = instance?.appContext.config.globalProperties.$currentChatObject;

const getFriendBasicInfo = () => {
  axios.get("http://localhost:8081/getFriendBasicInfo", {
    responseType: "json"
  }).then(res => {
    friendList.value = res.data.data;
    currentChatObject.value = res.data.data[0][2];
    instance.emit('loaded');
  }).catch(err => {
    console.log(err);
  });
};

function chatChecked(username) {
  currentChatObject.value = username;
}

onMounted(() => {
  getFriendBasicInfo();
});
</script>

<style>
.contact-friendListHeader {
  margin-top: 8px;
  width: 50%;
  margin-left: 25%;
  text-align: center;
  height: 30px;
  padding-left: 0;
  padding-right: 0;
  margin-bottom: 15px;
}
.contact-friendList {
  display: flex;
  flex-direction: column;
  cursor: pointer;
}
.contact-friendContent {
  margin-top: 0;
  display: flex;
  flex-direction: row;
  border-radius: 4px;
  width: 100%;
  height: 60px;
  background-color: #ecfcff;
  box-shadow: var(--el-box-shadow);
  margin-bottom: 2px;
}
.contact-friendContent:hover {
  background-color: #d9d9d9;
  border: 1px solid black;
}
.friend-avatar {
  border: 1px solid black;
  height: 10px;
  margin-top: 10px;
  margin-left: 6px;
  margin-right: 8px;
}
.friend-preview {
  display: flex;
  flex-direction: column;
  width: 80%;
  padding: 1px;
  height: 98%;
}
.friend-name {
  font-size: 20px;
  width: 100%;
  height: 50%;
  margin-top: 1px;
  margin-bottom: 0;
}
.friend-newMessage {
  font-size: 14px;
  width: 100%;
  height: 50%;
  margin-top: 4px;
}
</style>