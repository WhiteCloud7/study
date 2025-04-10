<template>
  <div class="notice">
    <div class="notice-article">
      <p>哈哈哈</p>
    </div>
    <div class="iconGroup2">
      <el-icon class="icon2" @click="toggleLike">
        <img :src="likeIcon" class="icon-img" />
      </el-icon>
      <label class="iconLabel2">{{ likeCount }}</label>
      <el-icon class="icon2" @click="toggleStar">
        <component :is="isStar ? StarFilled : Star" />
      </el-icon>
      <label class="iconLabel2">{{ starCount }}</label>
      <el-icon class="icon2"><ChatDotRound /></el-icon>
      <el-button class="back" @click="back">返回</el-button>
    </div>
  </div>
</template>

<script setup>
import {computed, defineProps, ref, defineEmits} from "vue";
import { Star, StarFilled, ChatDotRound } from '@element-plus/icons-vue';

const emit = defineEmits(["back","updateLike","updateStar"])
const props = defineProps({
  likeCount: Number,
  starCount: Number,
  isLiked : Boolean,
  isStarred : Boolean
})

const likeCount = ref(props.likeCount);
const starCount = ref(props.starCount);

const isLike = ref(props.isLiked);
const isStar = ref(props.isStarred);

const likeIcon = computed(() => {
  return require(`@/assets/photo/${isLike.value ? 'likefill.png': 'like.png'}`);
});

const toggleLike = () =>{
  if(isLike.value)
    likeCount.value--;
  else
    likeCount.value++;
  isLike.value = !isLike.value;
  emit("updateLike",likeCount.value,isLike.value)
};

const toggleStar = () =>{
  if(isStar.value)
    starCount.value--;
  else
    starCount.value++;
  isStar.value = !isStar.value;
  emit("updateStar",starCount.value,isStar.value)
};

const back = () =>{
  emit("back");
};
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
  padding-left: 8px;
  padding-right: 8px;
  width: 100%;
  min-height: 90%;
  overflow-y: auto;
  background-color: rgb(216.8, 235.6, 255);
  box-shadow: var(--el-box-shadow);
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
}
.icon-img{
  width: 1em;
  height: 1em;
}
.iconLabel2{
  font-size: 2em;
  margin-right: 20px;
}
.back{
  width: 20%;
  margin-top: 1%;
  margin-left: 420px;
}
</style>