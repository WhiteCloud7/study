<template>
  <div class="notice">
    <div class="notice-article">
      <h1>{{title}}</h1>
      <p v-for="message in messageArray" :key="message">&emsp;&emsp;{{ message }}</p>
    </div>
    <div class="iconGroup2">
      <el-icon class="icon2" @click="toggleLike">
        <img :src="likeIcon" class="icon-img" />
      </el-icon>
      <label class="iconLabel2">{{ getLabelFormat(likeCount) }}</label>

      <el-icon class="icon2"><ChatDotRound /></el-icon>
      <el-button class="back" @click="emit('back')">返回</el-button>
    </div>
  </div>
</template>

<script setup>
import {ref, computed, watch, onMounted, nextTick, getCurrentInstance} from "vue";
import { defineProps, defineEmits } from "vue";
import {ChatDotRound } from "@element-plus/icons-vue";
import axios from "axios";
import axiosToken from "@/axios";
import {useRouter} from "vue-router";

const props = defineProps({
  noticeId: Number,
  likeCount: Number,
  isLiked: Boolean,
  messageArray: Array,
  title: String
});

const emit = defineEmits(["updateLike","back"]);
const instance = getCurrentInstance();
const isLogin = instance?.appContext.config.globalProperties.$isLogin;
const router = useRouter();
const likeCount = ref(props.likeCount);
const isLike = ref(props.isLiked);

// 防止 prop 更新失效
watch(() => props.likeCount, val => (likeCount.value = val));
watch(() => props.isLiked, val => (isLike.value = val));

const likeIcon = computed(() =>
    require(`@/assets/photo/${isLike.value ? "likefill.png" : "like.png"}`)
);

function toggleLike() {
  if(isLogin.value===true){
    isLike.value = !isLike.value;
    likeCount.value += isLike.value ? 1 : -1;

    axiosToken.get("http://59.110.48.56:8081/updateLikeCount", {
      params: { noticeId: props.noticeId}
    }).catch(console.log);

    emit("updateLike", props.noticeId, likeCount.value, isLike.value);
  }else {
    router.push("/login");
  }
}

const getLabelFormat = (count) => {
    if(count>9999&&count<99999999){
      return (count/10000).toString().concat("W");
    }else if(count>99999999){
      return "1亿+";
    }else{
      return count;
    }
};

function getLabelStyle(){
  const iconLabels = document.querySelectorAll(".iconLabel2");
  for (let iconLabel of iconLabels) {
    let text = getLabelFormat(iconLabel.textContent);
    let fontsize;
    let marginTop;
    switch (text.length) {
      case 7:
        fontsize = "18px";marginTop = "9px"
        break;
      case 8:
        fontsize = "16px";marginTop = "8px"
        break;
      case 9:
        fontsize = "14px";marginTop = "7px"
        break;
      case 10:
        fontsize = "13px";marginTop = "6px"
        break;
      default:
        marginTop = "10px"
        fontsize = "21px";
        break;
    }
    iconLabel.style.fontSize = fontsize;
    iconLabel.style.marginTop = "10px";
  }
}

onMounted(async () => {
  await nextTick();
  getLabelStyle();
});
</script>

<style>
.notice {
  display: flex;
  position: absolute;
  left:15%;
  right: 15%;
  top: 75px;
  width: 70%;
  height: 460px;
  z-index: 600;
  background-color: rgba(255, 255, 255, 0.8);
  flex-direction: column;
  padding-right: 15px;
}

.notice-article {
  font-family: "PingFang SC";
  font-weight: 100;
  padding: 8px;
  margin-left: 8px;
  width: 100%;
  min-height: 90%;
  overflow-y: auto;
  overflow-x: hidden;
  background-color: rgb(216.8, 235.6, 255);
  box-shadow: var(--el-box-shadow);
  word-break: break-word;
}

.iconGroup2{
  background-color: rgba(255, 255, 255, 0.65);
  display: flex;
  flex-direction: row;
  margin-left: 8px;
  width: 100%;
  height: 10%;
}
.icon2{
  width: 5%;
  margin-top: 1%;
  cursor: pointer;
  font-size: 2em !important;
  margin-right: 6px;
  margin-left: 10px;
}
.icon-img{
  width: 1em;
  height: 1em;
}
.iconLabel2{
  display: inline-flex;
  max-height: 30px;
  width: 160px;
  align-items: center;
  justify-items: center;
  margin-right: 20px;
  overflow: hidden;
}
.back{
  width: 20%;
  margin-top: 1%;
  margin-left: 460px;
}
</style>