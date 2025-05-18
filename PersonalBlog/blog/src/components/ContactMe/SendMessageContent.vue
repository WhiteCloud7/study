<template>
  <div class="contact-send">
    <el-avatar class="contact-sendAvatar" :src="avatarSrc"></el-avatar>
    <p @contextmenu.prevent
       class="contact-sendContent"
       @mousedown.right="showOptions"
    >{{message}}</p>
    <div v-if="props.activeSendTime === props.sendTime" class="contact-messageOption">
      <el-button style="font-size: 12px;width: 30px;height: 20px;" @click="copyMessage">复制</el-button>
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
import {defineProps, onMounted, ref, defineEmits, onUnmounted} from "vue";
import axios from '@/axios';
import {ElMessage} from "element-plus";

const props = defineProps({
  messageId:Number,
  message: String,
  sendTime: String,
  activeSendTime: String,
  avatarSrc:String,
  sendReceiverName:String
})
const dialogVisible = ref(false);
const emit = defineEmits(["deleteSendMessage", "updateActiveSendTime"]);


function showOptions() {
  emit("updateActiveSendTime", props.sendTime);
}

function deleteMessage(){
  axios.get("http://localhost:8081/deleteMessage",{
    params:{
      messageId:props.messageId,
      receiveName:props.sendReceiverName,
      sendTime:props.sendTime
    },
  }).then(() => {
    emit("deleteSendMessage", props.messageId);
    emit("updateActiveSendTime", "");
    dialogVisible.value = false;
  }).catch(err => {
    console.log(err);
    alert("删除失败，建议刷新重试");
  });
}

function copyMessage() {
  navigator.clipboard.writeText(props.message).then(() => {
    ElMessage.success("已复制到剪贴板");
  }).catch(err => {
    console.error("复制失败:", err);
    ElMessage.error("复制失败，请手动复制");
  });
}

const handleGlobalClick = (e) => {
  const menu = e.target.closest(".contact-messageOption");
  const content = e.target.closest(".contact-sendContent");
  if (!menu && !content) {
    emit("updateActiveSendTime", "");
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
.contact-send{
  min-width: 1em;
  max-width: 600px;
  min-height: 40px;
  max-height: 20em;
  margin-left: 240px;
  display: inline-flex;
  flex-direction: row-reverse;
  margin-top: 8px;
  padding: 2px;
  position: relative;
}
.contact-sendContent{
  display: inline-block;
  padding: 6px 10px;
  background-color: #f5f5f5;
  border-radius: 6px;
  word-break: break-word;
  white-space: pre-wrap;
  max-width: 100%;
  margin: 0 0 0 6px;
}
.contact-sendContent:hover{
  cursor: pointer;
  background-color: rgba(245, 245, 245, 0.6);
}

.contact-messageOption {
  position: absolute;
  top: 100%;
  left: 500px;
  width: 80px;
  display: flex;
  gap: 1px;
  padding: 0;
  background-color: #fff;
  z-index: 200;
}
</style>