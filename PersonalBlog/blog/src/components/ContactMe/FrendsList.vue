<template>
  <h1 class="contact-friendListHeader">好 友 列 表</h1>
  <div class="contact-friendList">
    <div class="contact-friendContent">
      <el-avatar class="friend-avatar" style="height:40px" :src="userAvator"></el-avatar>
      <div class="friend-preview">
        <p class="friend-name">云白</p>
        <p class="friend-newMessage">欢迎！</p>
      </div>
    </div>
  </div>
</template>
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