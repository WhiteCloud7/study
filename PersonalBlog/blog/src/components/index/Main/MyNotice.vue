<template>
  <div class="article-container">
    <article class="article">
      <h1 style="margin-top: 0;margin-bottom: 5px;">欢迎访问！</h1>
      <p class="passage">欢迎访问我的博客！在校大学生一枚，在这里我会分享一些我学习过程中的一些总结和开源项目，欢迎各位和我一起交流学习！</p>
      <div style="font-size: 1.5em">
        <el-icon class="icon" id="view"><View></View></el-icon>
        <label for="view" class="iconLabel" >{{ visitCount }}</label>
        <el-icon class="icon" @click="toggleLike">
          <img :src="likeIcon" class="icon-img" />
        </el-icon>
        <label for="like" class="iconLabel">{{ likeCount }}</label>
        <el-icon class="icon" @click="toggleStar">
          <component :is="isStarred ? StarFilled : Star" />
        </el-icon>
        <label for="star" class="iconLabel">{{ starCount }}</label>
        <el-button @click="edit" class="button">编辑</el-button>
        <el-button @click="detail" class="button">查看详细</el-button>
      </div>
      <!-- detail 弹窗 -->
      <teleport to="body">
        <transition name="fade">
          <notice-detail
              :is="noticeDetail"
              v-if="noticeDetail"
              :like-count="likeCount"
              :star-count="starCount"
              :is-liked="isLike"
              :is-starred="isStarred"
              @updateStar="updateStar"
              @updateLike="updateLike"
              @back="back"
          />
        </transition>
      </teleport>
      <teleport to="body">
        <transition name="fade">
          <edit-notice
              :is="editNotice"
              v-if="editNotice"
              @editBack="editBack"
          />
        </transition>
      </teleport>
    </article>
  </div>
  <global-mask v-if="isMask"></global-mask>
</template>

<script setup>
import {ref, computed} from 'vue';
import { Star, StarFilled,View} from '@element-plus/icons-vue';
import GlobalMask from "@/components/public/GlobalMask";
import EditNotice from "@/components/index/Main/EditNotice";
import NoticeDetail from "@/components/index/Main/noticeDetail"

const isLike = ref(false);
const isStarred = ref(false);

const likeCount = ref(0);
const starCount = ref(0);
const visitCount = ref(0);

// 计算likeIcon的路径
const likeIcon = computed(() => {
  return require(`@/assets/photo/${isLike.value ? 'likefill.png' : 'like.png'}`);
});

const toggleLike = () => {
  if (isLike.value)
    likeCount.value--;
  else
    likeCount.value++;

  isLike.value = !isLike.value;
};

const toggleStar = () => {
  if (isStarred.value) {
    starCount.value--;
  } else {
    starCount.value++;
  }
  isStarred.value = !isStarred.value;
};

const updateLike = (newLikeCount,newIsLiked) =>{
  likeCount.value = newLikeCount;
  isLike.value = newIsLiked;
};

const updateStar = (newStarCount,newIsStarred) =>{
  starCount.value = newStarCount;
  isStarred.value = newIsStarred;
};

const isMask = ref(false);

const editNotice = ref(null);
async function edit() {
  const { default: edit } = await import("@/components/index/Main/EditNotice.vue");
  editNotice.value = edit;
  isMask.value = true;
}

const noticeDetail = ref(null);
async function detail() {
  const { default: notice } = await import("@/components/index/Main/noticeDetail.vue");
  visitCount.value++;
  noticeDetail.value = notice;
  isMask.value = true;
}

const back = () => {
  noticeDetail.value = null;
  isMask.value = false;
}

const editBack = () => {
  editNotice.value = null;
  isMask.value = false;
}
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
}

.icon {
  display: inline-block;
  margin-top: 95px;
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
  font-size: 20px;
  margin-right: 8px;
  position: relative;
  bottom: 3px;
}

.button {
  position: relative;
  bottom: 3px;
  left: 24px;
  width: 68px;
}

/* 动画淡入淡出 */
.fade-enter-active, .fade-leave-active {
  transition: opacity .3s ease;
}
.fade-enter-from, .fade-leave-to {
  opacity: 0;
}
</style>