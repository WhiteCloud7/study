<template>
  <div class="contact-receive">
    <el-avatar class="contact-receiveAvatar" :src="receiverProfile.avatar_src"></el-avatar>
    <p @contextmenu.prevent
       class="contact-receiveContent"
       @mousedown.right="showOptions"
    >{{message}}</p>
    <div v-if="props.activeReceiveTime === props.receiveTime" class="contact-receive-messageOption">
    <el-button style="font-size: 12px;width: 30px;height: 20px;">复制</el-button>
      <el-button type="danger" style="font-size: 12px;width: 30px;height: 20px" @click="dialogVisible = true;
">删除</el-button>
    </div>
  </div>

  <el-dialog
      v-model="dialogVisible"
      title="Tips"
      width="30%"
  >
    <span>确定要删除这条消息吗？删除后无法找回</span>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="deleteMessage">确定</el-button>
      </span>
    </template>
  </el-dialog>
</template>
<script setup>
import {defineProps, onMounted, ref, defineEmits, onUnmounted, inject} from "vue";
import axios from '@/axios';

const props = defineProps({
  messageId:Number,
  message: String,
  receiveTime: String,
  activeReceiveTime: String,
  receiverProfile:Object,
  receiveReceiverName:String
})
const dialogVisible = ref(false);
const emit = defineEmits(["deleteReceiveMessage", "updateActiveReceiveTime"]);
const userId = inject("userId");

function showOptions() {
  emit("updateActiveReceiveTime", props.receiveTime);
}

function deleteMessage(){
  axios.get("http://59.110.48.56:8081/deleteMessage",{
    params:{
      messageId:props.messageId,
      receiveName:props.receiveReceiverName,
      sendTime:props.receiveTime
    },
  }).then(() => {
    emit("deleteReceiveMessage", props.messageId);
    emit("updateActiveReceiveTime", "");
    dialogVisible.value = false;
  }).catch(err => {
    console.log(err);
    alert("删除失败，建议刷新重试");
  });
}

const handleGlobalClick = (e) => {
  const menu = e.target.closest(".contact-receive-messageOption");
  const content = e.target.closest(".contact-receiveContent");
  if (!menu && !content) {
    emit("updateActiveReceiveTime", "");
  }
};

onMounted(() => {
  document.addEventListener("click", handleGlobalClick);
});

onUnmounted(() => {
  document.removeEventListener("click", handleGlobalClick);
});
</script>
<style>
.contact-receive {
  display: inline-flex;
  align-items: center;
  max-width: 600px;
  margin-left: 10px;
  margin-top: 8px;
  border-radius: 6px;
  padding: 4px;
  position: relative;
}

.contact-receiveAvatar {
  flex-shrink: 0;
}

.contact-receiveContent {
  display: inline-block;
  padding: 6px 10px;
  background-color: #f5f5f5;
  border-radius: 6px;
  word-break: break-word;
  white-space: pre-wrap;
  max-width: 100%;
  margin: 0 0 0 6px;
}
.contact-receiveContent:hover{
  cursor: pointer;
  background-color: rgba(245, 245, 245, 0.6);
}

.contact-receive-messageOption {
  position: absolute;
  top: 100%;
  left: 40px;
  width: 80px;
  display: flex;
  gap: 1px;
  padding: 0;
  background-color: #fff;
  z-index: 200;
}
</style>