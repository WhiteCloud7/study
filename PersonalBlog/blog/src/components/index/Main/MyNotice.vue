<template>
  <div class="article-container">
    <article class="article" v-for="notice in notices" :key="notice.noticeId">
      <h1 style="margin-top: 0;margin-bottom: 5px;">{{notice.title}}</h1>
      <div class="passage">
        <p v-for="subNotice in sub(messageContent(notice.noticeMessage))" :key="subNotice">{{ subNotice }}</p>
      </div>
      <div style="font-size: 1.5em">
        <el-icon class="icon"><View /></el-icon>
        <label class="iconLabel">{{ formatCount(notice.visitCount) }}</label>

        <el-icon class="icon" @click="toggleLike(notice.noticeId)">
          <img :src="getIcon(notice.noticeId)" class="icon-img" />
        </el-icon>
        <label class="iconLabel">{{ formatCount(notice.likeCount) }}</label>

        <el-button @click="edit(notice.noticeId)" class="button">编辑</el-button>
        <el-button @click="detail(notice.noticeId)" class="button">查看详细</el-button>
        <el-button @click="deleteNotice(notice.noticeId)" class="button">删除</el-button>
      </div>

      <teleport to="body">
        <transition name="fade">
          <notice-detail
              v-if="noticeDetail"
              :notice-id="currentNoticeId"
              :like-count="currentLike"
              :is-liked="currentIsLike"
              :message-array="messageContent(currentMessage)"
              :title="currentNoticeTitle"
              @updateLike="updateLike"
              @back="back"
          />
        </transition>
      </teleport>

      <teleport to="body">
        <transition name="fade">
          <edit-notice
              v-if="editNotice"
              @editBack="editBack"
              :message="currentMessage"
              :title="currentNoticeTitle"
              :notice-id="currentNoticeId"
          /></transition>
      </teleport>
    </article>
  </div>
  <global-mask v-if="isMask" />
</template>

<script setup>
import {ref, onMounted, nextTick, getCurrentInstance, defineProps, watch} from 'vue';
import axios from 'axios';
import axiosToken from '@/axios';
import { View } from '@element-plus/icons-vue';
import GlobalMask from '@/components/public/GlobalMask.vue';
import EditNotice from '@/components/index/Main/EditNotice.vue';
import NoticeDetail from '@/components/index/Main/noticeDetail.vue';
import {useRouter} from "vue-router";
import {useRoute} from "vue-router";

const instance = getCurrentInstance();
const isLogin = instance?.appContext.config.globalProperties.$isLogin;
const router = useRouter();
const notices = ref([]);
const isLike = ref({});
const route = useRoute();

const currentLike = ref(0);
const currentIsLike = ref(false);
const currentMessage = ref('');
const currentNoticeId = ref(null);
const currentNoticeTitle = ref('');

const isMask = ref(false);
const editNotice = ref(null);
const noticeDetail = ref(null);

function messageContent(message) {
  return message ? message.split('\r\n') : [];
}

function sub(messageArray) {
  let currentRow = 0;
  const messageArraySub = [];
  for (let i = 0; i < messageArray.length; i++) {
    const rows = Math.ceil((messageArray[i].length + 2) / 20);
    if (rows + currentRow >= 8) {
      const maxRow = 8 - currentRow;
      let maxContent = messageArray[i].substr(0, 20 * maxRow - 4) + '..';
      messageArraySub.push(maxContent);
      break;
    } else {
      messageArraySub.push(messageArray[i]);
      currentRow += rows;
    }
  }
  return messageArraySub;
}

function getIcon(noticeId) {
  return require(`@/assets/photo/${isLike.value[noticeId] ? 'likefill.png' : 'like.png'}`);
}

function getCurrentNotice(noticeId) {
  return notices.value.find(n => n.noticeId === noticeId);
}

function formatCount(count){
  return count > 9999 ? '9999+' : count;
}
function toggleLike(noticeId) {
  if(isLogin.value===true){
    const notice = getCurrentNotice(noticeId);
    if (!notice) return;
    notice.likeCount += isLike.value[noticeId] ? -1 : 1;
    isLike.value[noticeId] = !isLike.value[noticeId];

    axiosToken.get("http://localhost:8081/updateLikeCount", {
      params: { noticeId }
    }).catch(console.log);
  }else{
    router.push("login");
  }
}

function updateLike(id, count, liked) {
  const notice = getCurrentNotice(id);
  if (notice) {
    notice.likeCount = count;
    isLike.value[id] = liked;
  }
}

async function detail(noticeId) {
  if(isLogin.value===true){

  }
  const notice = getCurrentNotice(noticeId);
  if (!notice) return;

  notice.visitCount++;
  await axios.get("http://localhost:8081/updateVisitCount", {
    params: { noticeId }
  }).catch(console.log);

  currentNoticeId.value = noticeId;
  currentMessage.value = notice.noticeMessage;
  currentLike.value = notice.likeCount;
  currentIsLike.value = isLike.value[noticeId];
  currentNoticeTitle.value = notice.title;
  noticeDetail.value = NoticeDetail;
  isMask.value = true;
}

function deleteNotice(noticeId){
  if(notices.value.length > 1){
    axiosToken.get("http://localhost:8081/deleteNotice",{
      params:{
        noticeId:noticeId
      }
    }).then(()=>{
      alert('删除成功');
      location.reload();
    }).catch(console.log)
  }else{
    alert("至少保留一个通知！")
  }
}

async function edit(noticeId) {
  await axiosToken.get("http://localhost:8081/permissionCheck", null).then(res=>{
    const notice = getCurrentNotice(noticeId);
    if (!notice) return;

    currentNoticeId.value = noticeId;
    currentMessage.value = notice.noticeMessage;
    currentNoticeTitle.value = notice.title;

    editNotice.value = EditNotice;
    isMask.value = true;
  }).catch(console.log);
}

function back() {
  noticeDetail.value = null;
  isMask.value = false;
}

function editBack() {
  editNotice.value = null;
  isMask.value = false;
}

async function initNotice() {
    axios.get("http://localhost:8081/getNotice"
    ).then(res=>{
      notices.value = res.data;
      if(isLogin.value === true){
        for (let notice of notices.value) {
          const id = notice.noticeId;
          axiosToken.get("http://localhost:8081/getNoticeInfo", {
            params: { noticeId: id}
          }).then(info=>{
            notices.value.sort((a,b)=>a.noticeId-b.noticeId);
            if(info.data.data!==null)
              isLike.value[id] = info.data.data.like;
          }).catch(console.log);
        }
      }
    }).catch(console.log);
}

const getLabelFontSize = () => {
  const iconLabels = document.querySelectorAll(".iconLabel");
  for (let iconLabel of iconLabels) {
    let text = iconLabel.textContent;
    let fontsize;
    let bottom;
    switch (text.length) {
      case 1:
        fontsize = "22px";bottom = "3px"
        break;
      case 2:
        fontsize = "18px";bottom = "4px"
        break;
      case 3:
        fontsize = "12px";bottom = "6px"
        break;
      case 4:
        fontsize = "8px";bottom = "8px"
        break;
      default:
        iconLabel.textContent = "9999+";bottom = "10px"
        fontsize = "7px";
        break;
    }
    iconLabel.style.fontSize = fontsize;
    iconLabel.style.bottom = bottom;
  }
};

watch(()=>route.path,(newPath)=>{
  if(newPath==='/index/newNotice'){
    if(isLogin.value===true){
      editNotice.value = EditNotice;
      isMask.value = true;
    }
  }
})

onMounted(async () => {
  await initNotice();
  await nextTick();
  getLabelFontSize();
});
</script>

<style>
.article-container {
  margin: 5px;
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  max-height: 1150px;
  overflow: hidden;
  background-color: rgb(235.9, 245.3, 255);
  box-shadow: var(--el-box-shadow);
  box-sizing: border-box;
}

.article {
  background-color: rgb(216.8, 235.6, 255);
  width: calc(50% - 5px);
  height: 280px;
  box-sizing: border-box;
  padding: 10px;
  text-align: left;
}

.passage {
  text-indent: 2em;
  color: rgba(164, 181, 197, 0.84);
  margin-top: 0;
  height: 200px;
  word-break: break-word;
}
.icon {
  display: inline-block;
  margin-top: 0;
  margin-right: 4px;
  cursor: pointer;
  position: relative;
}

.icon-img {
  display: inline-block;
  width: 24px;
  height: 24px;
}

.iconLabel {
  display: inline-flex;
  margin-right: 8px;
  width: 20px;
  min-height: 10px;
  max-height: 25px;
  flex: 1;
  position: relative;
  bottom: 3px;
  align-items: center;
  justify-items: center;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 20px;
}

.button {
  position: relative;
  bottom: 3px;
  left: 10px;
  width: 68px;
  border: 1px solid black;
}

/* 动画淡入淡出 */
.fade-enter-active, .fade-leave-active {
  transition: opacity .3s ease;
}
.fade-enter-from, .fade-leave-to {
  opacity: 0;
}
</style>