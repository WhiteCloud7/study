<template>
  <div class="contact-chatPanel">
    <header class="contact-chatDetail">
      <p class="contact-chatDetailContent">云白</p>
    </header>
    <main class="contact-chatRecord">
      <send-message-content
          v-for="(msg, index) in messages"
          :key="index"
          :message-id="msg[0]"
          :message="msg[1]"
          :send-time="msg[2]"
          :active-send-time="activeSendTime"
          @updateActiveSendTime="val => activeSendTime = val"
          @deleteSendMessage="deleteSendMessage"
      ></send-message-content>
      <receive-message-content
          v-for="(msg, index) in receiveMessages"
          :key="index"
          :message-id="msg[0]"
          :message="msg[1]"
          :re="msg[2]"
          :active-receive-time="activeReceiveTime"
          @updateActiveReceiveTime="val => activeReceiveTime = val"
          @deleteReceiveMessage="deleteReceiveMessage"
      ></receive-message-content>
    </main>
    <footer class="contact-editMessage">
      <el-input
          placeholder="输入信息"
          type="textarea"
          v-model="sendMessage"
          :autosize="{ minRows: 1, maxRows: 20}"
          :style="{ height: '80%', marginTop: '7px', resize: 'none', flex: 1, marginLeft: '4px' }"
          :active-send-time="activeSendTime"
          @keydown="handleKeyDown"
      ></el-input>
      <el-button class="contact-sendMessage" @click="sendMessageHandler">发送</el-button>
    </footer>
  </div>
</template>

<script setup>
import {onMounted, ref} from "vue";
import axios from "axios";
import SendMessageContent from "@/components/ContactMe/SendMessageContent";
import ReceiveMessageContent from "@/components/ContactMe/ReceiveMessageContent";

const sendMessage = ref("");
// 定义响应式变量 messages，用于存储聊天记录
const messages = ref([]);
const receiveMessages = ref([]);

const activeSendTime = ref("");
const activeReceiveTime = ref("");

// 键盘按下事件处理函数
const handleKeyDown = (event) => {
  if (event.shiftKey && event.key === "Enter") {
    // 阻止默认的换行行为
    event.preventDefault();
    // 手动添加换行符
    sendMessage.value += "\n";
  } else if (event.key === "Enter") {
    // 阻止默认行为并防止冒泡
    event.preventDefault();
    event.stopPropagation();
    // 按下Enter键触发发送消息
    sendMessageHandler();
  }
};

function formatDateToMySQL(date) {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  const seconds = String(date.getSeconds()).padStart(2, '0');
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}

const sendMessageHandler = () => {
  if (sendMessage.value.trim() !== "") {
    const sendTime = new Date();
    const params = {senderId: 1, receiverId: 2, message: sendMessage.value, sendTime: sendTime.toLocaleString()}
    axios.post("http://localhost:8081/sendMessage", JSON.stringify(params), {
      headers:{
        "Content-Type":"application/json"
      },
      responseType: "text"
    }).then(res => {
      if(res.data == "发送成功") {
        axios.get("http://localhost:8081/getSendMessageId",{
          params:{
            userId:1,
            friendId:2,
            Time:formatDateToMySQL(sendTime)
          },
          responseType: "json"
        }).then(res=>{
          const messageId = res.data;
          messages.value.push([messageId,sendMessage.value, params.sendTime]);
          sendMessage.value = "";
        }).catch(err=>{
          console.log(err);
        });
      }
    else
      alert("发送失败");
    }).catch(err => {
      console.log(err)
    });
  }else
    confirm("消息不能为空！");
};

function receiveMessage(){
  axios.get("http://localhost:8081/getReceiveMessage",{
    params:{
      friendId:2,
      userId:1
    },
    responseType: "json"
  }).then(res=>{
    const gotMessages = res.data;
    console.log(gotMessages);
    receiveMessages.value = gotMessages;
  }).catch(err=>{
    console.log(err);
  });
}

function deleteSendMessage(sendTime){
  const deletedIndex = messages.value.indexOf(sendTime);
  messages.value.splice(deletedIndex,1);
}
function deleteReceiveMessage(sendTime){
  const deletedIndex = messages.value.indexOf(sendTime);
  receiveMessages.value.splice(deletedIndex,1);
}

function initMessage(){
  axios.get("http://localhost:8081/getSentMessage",{
    params:{
      userId:1,
      friendId:2
    },
    responseType: "json"
  }).then(res=>{
    const gotMessages = res.data;
    console.log(gotMessages);
    messages.value = gotMessages;
  }).catch(err=>{
    console.log(err);
  });
  receiveMessage();
}

onMounted(()=>{
  initMessage();
  setInterval(receiveMessage,1000)
});
</script>

<style>
/* 聊天面板整体样式 */
.contact-chatPanel {
  display: flex;
  flex-direction: column;
  width: 100%;
  height: 100%;
  overflow: hidden;
  box-sizing: border-box;
  box-shadow: var(--el-box-shadow);
}

/* 聊天详情头部样式 */
.contact-chatDetail {
  height: 5%;
  width: 100%;
  box-sizing: border-box;
  text-align: center;
  background-color: #ecfcff;
}

/* 聊天详情头部内容样式 */
.contact-chatDetailContent {
  margin: 0;
}

/* 聊天记录主体样式 */
.contact-chatRecord {
  min-height: 85%;
  width: 100%;
  overflow-y: auto;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  padding: 5px;
}

/* 编辑消息底部样式 */
.contact-editMessage {
  background-color: #edecff;
  min-height: 10%;
  width: 100%;
  box-sizing: border-box;
  display: flex;
  flex-direction: row;
}

/* 编辑消息区域的 textarea 样式（覆盖 Element Plus 样式） */
.contact-editMessage textarea {
  resize: none !important;
}

/* 发送消息按钮样式 */
.contact-sendMessage {
  height: 80%;
  width: 10%;
  margin-left: 4px;
  margin-top: 7px;
  margin-right: 4px;
}
</style>