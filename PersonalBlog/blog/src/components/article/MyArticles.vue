<template>
  <article class="myArticles-article">
    <div class="myArticles-authorDetail">
      <el-avatar style="width: 20px;height: 20px;margin-top: 3px;  margin-left: 10px;"></el-avatar>
      <p style="margin-left: 8px">云白</p>
    </div>

    <div class="myArticles-articlePreview">
      <h1>欢迎</h1>
      <p>第一行</p>
      <p>最多二行</p>
    </div>

    <div class="myArticles-relativeInfo">
      <div class="myArticles-iconGroup">
        <el-icon class="myArticles-icon" id="myArticles-view"><View /></el-icon>
        <label for="myArticles-view" class="myArticles-iconLabel">{{ visitCount }}</label>
      </div>
      <div class="myArticles-iconGroup" @click="toggleLike">
        <el-icon class="myArticles-icon" id="myArticles-like">
          <img :src="likeIcon" class="myArticles-icon-img" />
        </el-icon>
        <label for="myArticles-like" class="myArticles-iconLabel">{{ likeCount }}</label>
      </div>
      <div class="myArticles-iconGroup" @click="toggleStar">
        <el-icon class="myArticles-icon" id="myArticles-star">
          <component :is="isStarred? StarFilled : Star" />
        </el-icon>
        <label for="myArticles-star" class="myArticles-iconLabel">{{ starCount }}</label>
      </div>
      <el-button @click="detail" class="myArticles-button"
                 style="font-size: 8px;width:45px;height:10px;margin-bottom: 0">查看详细</el-button>
      <p class="myArticles-time"></p>
    </div>

    <el-divider style="margin:6px"></el-divider>

    <div>
      <teleport to="body">
        <transition name="fade">
          <article-detail
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
    </div>
  </article>

  <global-mask v-if="isMask"></global-mask>
</template>

<script setup>
// 引入 Vue 的响应式和计算属性相关函数，以及 Element Plus 的图标组件
import { ref, computed } from 'vue';
import { Star, StarFilled, View } from '@element-plus/icons-vue';
// 引入自定义的全局遮罩组件和文章详情组件
import GlobalMask from "@/components/public/GlobalMask";
import ArticleDetail from "@/components/article/articleDetail";

// 定义响应式数据，用于存储点赞、收藏、浏览状态及对应的数量
const isLike = ref(false); // 是否已点赞
const isStarred = ref(false); // 是否已收藏
const likeCount = ref(0); // 点赞数量
const starCount = ref(0); // 收藏数量
const visitCount = ref(0); // 浏览数量

// 计算属性，根据点赞状态动态获取点赞图标路径
const likeIcon = computed(() => {
  return require(`@/assets/photo/${isLike.value? 'likefill.png' : 'like.png'}`);
});

// 点赞按钮的点击事件处理函数
const toggleLike = () => {
  likeCount.value += isLike.value? -1 : 1;
  isLike.value =!isLike.value;
};

// 收藏按钮的点击事件处理函数
const toggleStar = () => {
  starCount.value += isStarred.value? -1 : 1;
  isStarred.value =!isStarred.value;
};

// 更新点赞状态和数量的函数，用于接收子组件传递的更新数据
const updateLike = (newLikeCount, newIsLiked) => {
  likeCount.value = newLikeCount;
  isLike.value = newIsLiked;
};

// 更新收藏状态和数量的函数，用于接收子组件传递的更新数据
const updateStar = (newStarCount, newIsStarred) => {
  starCount.value = newStarCount;
  isStarred.value = newIsStarred;
};

// 定义响应式数据，用于控制全局遮罩的显示和隐藏
const isMask = ref(false);

// 定义响应式数据，用于存储编辑文章组件的引用
const editNotice = ref(null);
// 编辑文章的异步函数，用于动态引入编辑文章组件并显示遮罩
async function edit() {
  const { default: edit } = await import("@/components/index/Main/EditNotice.vue");
  editNotice.value = edit;
  isMask.value = true;
}

// 定义响应式数据，用于存储文章详情组件的引用
const noticeDetail = ref(null);
// 查看文章详情的异步函数，用于动态引入文章详情组件、增加浏览数量并显示遮罩
async function detail() {
  const { default: notice } = await import("@/components/index/Main/noticeDetail.vue");
  visitCount.value++;
  noticeDetail.value = notice;
  isMask.value = true;
}

// 返回按钮的点击事件处理函数，用于关闭文章详情组件并隐藏遮罩
const back = () => {
  noticeDetail.value = null;
  isMask.value = false;
};
</script>

<style>
/* 全局样式重置，去除默认的 margin 和 padding，设置盒模型为 border-box */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

/* 文章整体容器样式，设置为弹性盒模型，垂直排列 */
.myArticles-article {
  display: flex;
  flex-direction: column;
  overflow-x: hidden;
}

/* 文章作者详情容器样式，设置高度、宽度、弹性盒模型，水平排列并居中对齐内容 */
.myArticles-authorDetail {
  height: 20%;
  width: 100%;
  display: flex;
  flex-direction: row;
  align-items: center;
}

/* 文章预览内容容器样式，设置高度、宽度和字体大小 */
.myArticles-articlePreview {
  height: 60%;
  width: 100%;
  display: inline;
  font-size: 12px;
  padding-left: 10px;
}

/* 文章相关信息容器样式，设置高度、宽度、弹性盒模型，水平排列并居中对齐内容，允许换行 */
.myArticles-relativeInfo {
  height: 20%;
  width: 100%;
  display: flex;
  align-items: center;
  padding-left: 10px;
  flex-wrap: wrap;
}

/* 文章图标组容器样式，设置为弹性盒模型，水平排列并居中对齐内容，设置右边距 */
.myArticles-iconGroup {
  display: flex;
  align-items: center;
  margin-right: 12px;
}

/* 文章图标样式，设置为指针样式，鼠标悬停时显示手型 */
.myArticles-icon {
  cursor: pointer;
}

/* 文章图标标签样式，设置左边距和字体大小 */
.myArticles-iconLabel {
  margin-left: 5px;
  font-size: 12px;
}

/* 文章点赞图标图片样式，设置显示方式、宽度和高度 */
.myArticles-icon-img {
  display: inline-block;
  width: 16px;
  height: 16px;
}

/* 文章查看详细按钮样式，设置左边距 */
.myArticles-button {
  margin-left: 40px;
}
</style>