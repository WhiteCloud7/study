<template>
  <div class="contact-chatPanel">
    <header class="contact-chatDetail">
      <p class="contact-chatDetailContent">云白</p>
    </header>
    <main class="contact-chatRecord">
      <component
          v-for="msg in allMessages"
          :key="msg.messageId"
          :is="msg.senderId === currentUserId ? SendMessageContent : ReceiveMessageContent"
          :message-id="msg.messageId"
          :message="msg.message"
          :send-time="msg.sendTime"
          :receive-time="msg.sendTime"
          :active-send-time="activeSendTime"
          :active-receive-time="activeReceiveTime"
          @updateActiveSendTime="val => activeSendTime = val"
          @updateActiveReceiveTime="val => activeReceiveTime = val"
          @deleteSendMessage="deleteMessage"
          @deleteReceiveMessage="deleteMessage"
      />
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
const receiveMessages = ref([]);
const allMessages = ref([]);
const currentUserId = 1;

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
        axios.get("http://localhost:8081/getSendMessageId", {
          params: {
            userId: 1,
            friendId: 2,
            Time: formatDateToMySQL(sendTime)
          }
        }).then(res => {
          const messageId = res.data;
          allMessages.value.push({
            messageId,
            senderId: 1,
            receiverId: 2,
            message: sendMessage.value,
            sendTime: params.sendTime
          });
          allMessages.value.sort((a, b) => a.messageId - b.messageId);
          sendMessage.value = "";
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
      friendId: 2,
      userId: 1
    },
    responseType: "json"
  }).then(res => {
    const gotMessages = res.data;

    for (const message of gotMessages) {
      if (!allMessages.value.some(m => m.messageId === message[0])) {
        allMessages.value.push({
          messageId: message[0],
          senderId: 2,
          receiverId: 1,
          message: message[1],
          sendTime: message[2]
        });
      }
    }
    allMessages.value.sort((a, b) => a.messageId - b.messageId);
  }).catch(err => {
    console.log("接收消息出错", err);
  });
}

function deleteMessage(messageId) {
  const index = allMessages.value.findIndex(msg => msg.messageId === messageId);
  if (index !== -1) {
    allMessages.value.splice(index, 1);
  }
}

function initMessage() {
  axios.get("http://localhost:8081/getAllMessages", {
    params: {
      userId: 1,
      friendId: 2
    },
    responseType: "json"
  }).then(res => {
    const gotMessages = res.data;
    allMessages.value = gotMessages.sort((a, b) => a.messageId - b.messageId);
  }).catch(err => {
    console.log(err);
  });
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