<template>
  <div class="contact-chatPanel">
    <header class="contact-chatDetail">
      <p class="contact-chatDetailContent">云白</p>
    </header>
    <main class="contact-chatRecord">
      <send-message-content v-for="(msg, index) in messages" :key="index" :message="msg"></send-message-content>
    </main>
    <footer class="contact-editMessage">
      <el-input
          placeholder="输入信息"
          type="textarea"
          v-model="sendMessage"
          :autosize="{ minRows: 1, maxRows: 20}"
          :style="{ height: '80%', marginTop: '7px', resize: 'none', flex: 1, marginLeft: '4px' }"
          @keydown="handleKeyDown"
      ></el-input>
      <el-button class="contact-sendMessage" @click="sendMessageHandler">发送</el-button>
    </footer>
  </div>
</template>

<script setup>
// 导入 Vue 的响应式函数 ref
import { ref } from "vue";
// 导入发送消息内容组件
import SendMessageContent from "@/components/ContactMe/SendMessageContent";
// 导入接收消息内容组件（虽然当前未使用，但保持导入顺序清晰）
import ReceiveMessageContent from "@/components/ContactMe/ReceiveMessageContent";

// 定义响应式变量 sendMessage，用于存储用户输入的消息
const sendMessage = ref("");
// 定义响应式变量 messages，用于存储聊天记录
const messages = ref([]);

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

// 发送消息处理函数
const sendMessageHandler = () => {
  if (sendMessage.value.trim()!== "") {
    messages.value.push(sendMessage.value);
    sendMessage.value = "";
  }
};
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