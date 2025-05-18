<template>
  <div class="contact-chatPanel">
    <header class="contact-chatDetail">
      <p class="contact-chatDetailContent">{{receiverProfile.nikeName}}</p>
    </header>
    <main class="contact-chatRecord" ref="chatContainer" @scroll="onScroll">
    <component
          v-for="msg in allMessages"
          :key="msg.messageId"
          :is="msg.receiverName === currentUserName ? ReceiveMessageContent : SendMessageContent"
          :message-id="msg.messageId"
          :message="msg.message"
          :send-time="msg.sendTime"
          :receive-time="msg.sendTime"
          :active-send-time="activeSendTime"
          :active-receive-time="activeReceiveTime"
          :avatar-src="currentUserAvatar"
          :receiver-profile="receiverProfile"
          :receive-receiver-name="currentUserName"
          :send-receiver-name="currentChatObject"
          @deleteSendMessage="deleteMessage"
          @deleteReceiveMessage="deleteMessage"
          @updateActiveReceiveTime="updateActiveReceiveTime"
          @updateActiveSendTime="updateActiveSendTime"
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
import {onMounted, ref, getCurrentInstance, watch, nextTick} from "vue";
import test from  "axios";
import axios from "@/axios";
import SendMessageContent from "@/components/ContactMe/SendMessageContent";
import ReceiveMessageContent from "@/components/ContactMe/ReceiveMessageContent";

const instance = getCurrentInstance();
const currentChatObject = instance.appContext.config.globalProperties.$currentChatObject;
const currentUserName = ref("");
const sendMessage = ref("");
// 定义响应式变量 messages，用于存储聊天记录
const receiveMessages = ref([]);
const allMessages = ref([]);
const activeSendTime = ref("");
const activeReceiveTime = ref("");
const currentUserAvatar = ref();
const receiverProfile = ref([]);
const lastNewTime = ref();
const chatContainer = ref(null);
const loadingOlder = ref(false);          // 防抖 + 避免并发
const finishedAll = ref(false);           // 是否已加载完全部历史
function scrollToBottom() {
  nextTick(() => {
    if (chatContainer.value) {
      chatContainer.value.scrollTop = chatContainer.value.scrollHeight;
    }
  });
}

function onScroll() {
  if (!chatContainer.value || loadingOlder.value || finishedAll.value) return;
  if (chatContainer.value.scrollTop === 0) {
    loadOlderMessages();
  }
}

async function loadOlderMessages() {
  loadingOlder.value = true;
  // 1) 找到当前最早一条消息的时间/ID，作为游标
  const firstMsg = allMessages.value[0];
  if (!firstMsg) { loadingOlder.value = false; return; }
  const cursorTime = firstMsg.sendTime;
  // 2) 记录插入前的 scrollHeight
  const prevHeight = chatContainer.value.scrollHeight;
  try {
    // 3) 调接口拿更早 10 条
    const res = await axios.get("http://localhost:8081/getOlderMessages", {
      params: {
        friendName: currentChatObject.value,
        beforeTime: cursorTime,
      }
    });
    const older = res.data.data;          // 后端返回按 sendTime 升序/降序均可
    console.log(older)
    if (!older || older.length === 0) {
      finishedAll.value = true;           // 再无旧数据
    } else {
      // 4) 将旧消息插入到数组头部
      //    如果后端降序返回，可先 reverse()
      allMessages.value.unshift(...older.reverse());
      allMessages.value.sort((a, b) => new Date(a.sendTime) - new Date(b.sendTime));
      await nextTick();                   // 等 DOM 更新完
      // 5) 还原滚动位置，避免抖动
      const newHeight = chatContainer.value.scrollHeight;
      chatContainer.value.scrollTop = newHeight - prevHeight;
    }
  } catch (err) {
    console.error("加载旧消息失败", err);
  } finally {
    loadingOlder.value = false;
  }
}

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

const sendMessageHandler = async () => {
  if (sendMessage.value.trim() !== "") {
    const params = {receiverName: currentChatObject.value, message: sendMessage.value}
    axios.post("http://localhost:8081/sendMessage", JSON.stringify(params), {
      headers: {
        "Content-Type": "application/json"
      },
      responseType: "json"
    }).then(res => {
      if (res.data.message === "发送失败") {
        alert("发送失败");
      } else {
        const {messageId, receiverName, sendTime, message} = res.data.data;
        console.log("发送的消息: ",res.data.data)
        allMessages.value.push({
          messageId: messageId,
          receiverName: receiverName,
          message: message,
          sendTime: sendTime
        });
        allMessages.value.sort((a, b) => new Date(a.sendTime) - new Date(b.sendTime));
        sendMessage.value = "";
        scrollToBottom();
      }
    });
  }else
    confirm("消息不能为空！");
};

function getLastNewTime() {
  axios.get("/getLastNewTime", { params: { friendName: currentChatObject.value } })
      .then(res => {
        lastNewTime.value = res.data.data;
      });
}

function receiveMessage(){
  allMessages.value.sort((a, b) => new Date(a.sendTime) - new Date(b.sendTime));
  if(allMessages.value.length === 0||lastNewTime.value===null){return}
  axios.get("http://localhost:8081/getReceiveMessage",{
    params:{
      friendName: currentChatObject.value,
      currentNewMessageTime: lastNewTime.value
    },
    responseType: "json"
  }).then(res => {
    const gotMessages = res.data.data;
    for (const message of gotMessages) {
      if (!allMessages.value.some(m => m.messageId === message.messageId)) {
        allMessages.value.push({
          messageId: message[0],
          receiverName: currentUserName.value,
          message: message[2],
          sendTime: message[3]
        });
      }
    }
    allMessages.value.sort((a, b) => new Date(a.sendTime) - new Date(b.sendTime));
    getLastNewTime();
  }).catch(err => {
    console.log("接收消息出错", err);
  });
}

const updateActiveReceiveTime = (newTime) => {
  activeReceiveTime.value = newTime;
};

const updateActiveSendTime = (newTime) => {
  activeSendTime.value = newTime;
};

function deleteMessage(messageId) {
  const index = allMessages.value.findIndex(msg => msg.messageId === messageId);
  if (index !== -1) {
    allMessages.value.splice(index, 1);
  }
  allMessages.value.sort((a, b) => new Date(a.sendTime) - new Date(b.sendTime));
}

watch(currentChatObject,()=>{
  initMessage();
  initCurrentChatObject();
})

function initMessage() {
  axios.get("http://localhost:8081/getAllMessages", {
    params: {
      friendName: currentChatObject.value
    },
    responseType: "json"
  }).then(res => {
    const gotMessages = res.data.data;
    if (gotMessages !== null) {
      const sorted = gotMessages.sort((a, b) => new Date(a.sendTime) - new Date(b.sendTime));
      allMessages.value = sorted;
      scrollToBottom();
    } else {
      allMessages.value = [];
    }
  }).catch(err => {
    console.log(err);
  });
}

function initCurrentUser(){
  axios.get("http://localhost:8081/profile", {
    responseType: "json"
  }).then(res => {
    const data = res.data.data;
    currentUserName.value = data.username;
    currentUserAvatar.value = data.avatar_src;
  }).catch(err => {
    console.log(err);
  });
}

function initCurrentChatObject(){
  axios.get("http://localhost:8081/getFriendBasicInfoByUsername", {
    params:{friendName:currentChatObject.value},
    responseType: "json"
  }).then(res => {
    receiverProfile.value = res.data.data;
  }).catch(err => {
    console.log(err);
  });
}

onMounted(()=>{
  initCurrentUser();
  initCurrentChatObject();
  initMessage();
  getLastNewTime();
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