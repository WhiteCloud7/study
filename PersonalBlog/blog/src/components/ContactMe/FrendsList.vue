<template>
  <h1 class="contact-friendListHeader">好 友 列 表</h1>
  <div class="contact-friendList" v-for="friend in friendList" :key="friend" @click="showDetail">
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
import axios from "axios";
import {onMounted,ref} from "vue";

const friendList = ref([]);

const getFriendBasicInfo = () =>{
  axios.get("http://localhost:8081/getFriendBasicInfo",{
    params:{
      userId: 1
    },
    responseType: "json"
  }).then(res =>{
    friendList.value = res.data;
  }).catch(err=>{
    console.log(err);
  })
};

onMounted(()=>{
  getFriendBasicInfo();
});
</script>
<style>
.contact-friendListHeader{
  margin-top: 8px;
  width: 50%;
  margin-left: 25%;
  text-align: center;
  height: 30px;
  padding-left:0 ;
  padding-right: 0;
}
.contact-friendList{
  display: flex;
  flex-direction: column;
  cursor: pointer;
}
.contact-friendContent{
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
.contact-friendContent:hover{
  background-color: #d9d9d9;
  border: 1px solid black;
}
.friend-avatar{
  border: 1px solid black;
  height: 10px;
  margin-top: 10px;
  margin-left: 6px;
  margin-right: 8px;
}
.friend-preview{
  display: flex;
  flex-direction: column;
  width: 80%;
  padding: 1px;
  height: 98%;
}
.friend-name{
  font-size: 20px;
  width: 100%;
  height: 50%;
  margin-top:1px;
  margin-bottom: 0;
}
.friend-newMessage{
  font-size: 14px;
  width: 100%;
  height: 50%;
  margin-top: 4px;
}
</style>