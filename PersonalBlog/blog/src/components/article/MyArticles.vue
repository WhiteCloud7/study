<template>
  <article class="myArticles-article"  v-for="article in articles" :key="article.articleId">
    <div class="myArticles-authorDetail">
      <el-avatar :src="article.avatar_src" style="width: 20px;height: 20px;margin-top: 3px;  margin-left: 10px;"></el-avatar>
      <p style="margin-left: 8px">{{article.nikeName}}</p>
    </div>

    <div class="myArticles-articlePreview">
      <h1>{{article.title}}</h1>
      <p v-for="articleSub in sub(articleContent(article.articleContent))" :key="articleSub">{{ articleSub }}</p>
    </div>

    <div class="myArticles-relativeInfo">
      <div class="myArticles-iconGroup">
        <el-icon class="myArticles-icon" id="myArticles-view"><View /></el-icon>
        <label for="myArticles-view" class="myArticles-iconLabel">{{ getVisitCount(article.articleId) }}</label>
      </div>
      <div class="myArticles-iconGroup" @click="toggleLike(article.articleId)">
        <el-icon class="myArticles-icon" id="myArticles-like">
          <img :src="likeIconValue(article.articleId)" class="myArticles-icon-img" />
        </el-icon>
        <label for="myArticles-like" class="myArticles-iconLabel">{{ getLikeCount(article.articleId) }}</label>
      </div>
      <div class="myArticles-iconGroup" @click="toggleStar(article.articleId)">
        <el-icon class="myArticles-icon" id="myArticles-star">
          <component :is="isStarredValue(article.articleId)? StarFilled : Star" />
        </el-icon>
        <label for="myArticles-star" class="myArticles-iconLabel">{{ getStarCount(article.articleId) }}</label>
      </div>
      <el-button @click="detail(article.articleId)" class="myArticles-button"
                 style="font-size: 8px;width:45px;height:10px;margin-bottom: 0">查看详细</el-button>
      <el-button @click="edit(article.articleId)" class="myArticles-button"
                 style="font-size: 8px;width:45px;height:10px;margin-bottom: 0">编辑</el-button>
      <el-button @click="deleteArticle(article.articleId)" class="myArticles-button"
                 style="font-size: 8px;width:45px;height:10px;margin-bottom: 0">删除</el-button>
      <p class="myArticles-time"></p>
    </div>
  </article>
</template>

<script setup>
// 引入 Vue 的响应式和计算属性相关函数，以及 Element Plus 的图标组件
import {ref, computed, onMounted, getCurrentInstance, nextTick} from 'vue';
import {useRouter} from "vue-router";
import axios from 'axios';
import axiosToken from '@/axios';
import { Star, StarFilled, View } from '@element-plus/icons-vue';

// 定义响应式数据，用于存储点赞、收藏、浏览状态及对应的数量
const isLike = ref([]); // 是否已点赞
const isStarred = ref([]); // 是否已收藏
const likeCount = ref([]); // 点赞数量
const starCount = ref([]); // 收藏数量
const visitCount = ref([]); // 浏览数量
const articles = ref([]);
const operationValue = ref(0);
const operationStatus = ref(false);
const instance = getCurrentInstance();
const isLogin = instance?.appContext.config.globalProperties.$isLogin;
const router = useRouter();

// 计算属性，根据点赞状态动态获取点赞图标路径
const likeIconValue = computed(() => {
    return (articleId) => {
      if(isLogin.value===false||!isLike.value.find(i => i.articleId === articleId)){
        return require("@/assets/photo/like.png");
      }else {
          const likeItem = isLike.value.find(i => i.articleId === articleId);
          return likeItem? require(`@/assets/photo/${likeItem.like? 'likefill.png' : 'like.png'}`) : '';
      }
    };
})

// 计算属性，获取是否收藏状态
const isStarredValue = computed(() => {
  return (articleId) => {
    const starredItem = isStarred.value.find(i => i.articleId === articleId);
    return starredItem? starredItem.star : false;
  };
});

// 点赞按钮的点击事件处理函数
function toggleLike(articleId){
  if(isLogin.value===true){
    axiosToken.get("http://localhost:8081/updateArticleLikeCount",{
      params:{
        articleId:articleId,
      },
      responseType:"json"
    }).then(res=>{
      if(!isLike.value.some(i => i.articleId === articleId))
        initCurrentArticleInfo(articleId);
      operationStatus.value = isLike.value.find(i => i.articleId === articleId).like;
      operationValue.value = likeCount.value.find(l => l.articleId === articleId).likeCount;

      operationValue.value += operationStatus.value? -1 : 1;
      likeCount.value.find(l => l.articleId === articleId).likeCount = operationValue.value;
      isLike.value.find(i => i.articleId === articleId).like = !operationStatus.value;
    }).catch(console.log)
  }else {
    router.push("/login");
  }
}

// 收藏按钮的点击事件处理函数
function toggleStar(articleId){
  if(isLogin.value===true){
    axiosToken.get("http://localhost:8081/updateArticleStarCount",{
      params:{
        articleId:articleId,
      },
      responseType:"json"
    }).then(res=>{
      if(!isStarred.value.some(i => i.articleId === articleId))
        initCurrentArticleInfo(articleId);
      nextTick();
      operationStatus.value = isStarred.value.find(i => i.articleId === articleId).star;
      operationValue.value = starCount.value.find(l => l.articleId === articleId).starCount;

      operationValue.value += operationStatus.value? -1 : 1;
      starCount.value.find(l => l.articleId === articleId).starCount = operationValue.value;
      isStarred.value.find(i => i.articleId === articleId).star = !operationStatus.value;
    }).catch(console.log)
  }else {
    router.push("login");
  }
}

function toggleVisit(articleId){
  axios.get("http://localhost:8081/updateArticleVisitCount",{
    params:{
      articleId:articleId,
    },
    responseType:"json"
  })
  operationValue.value = visitCount.value.find(l => l.articleId === articleId).visitCount;
  visitCount.value.find(l => l.articleId === articleId).visitCount = operationValue.value+1;
}

// 定义响应式数据，用于存储文章详情组件的引用
const articleDetailId = ref(null);
// 查看文章详情的异步函数，用于动态引入文章详情组件、增加浏览数量并显示遮罩
function detail(articleId) {
  toggleVisit(articleId);
  router.push(`/article/${articleId}`);
}
function edit(articleId) {
  axiosToken.get("http://localhost:8081/checkPremession")
      .then(()=>{
        router.push(`/article/edit/${articleId}`);
      }).catch(console.log)
}

function deleteArticle(articleId){
  axiosToken.get("http://localhost:8081/deleteArticle",{
    params:{
      articleId:articleId,
    }
  }).then(()=>{
    confirm("删除成功!");
    location.reload();
  }).catch(console.log)
}

function articleContent(article) {
  return article ? article.split('\r\n') : [];
}

function sub(article) {
  let currentRow = 0;
  const articleSub = [];
  for (let i = 0; i < article.length; i++) {
    const rows = Math.ceil((article[i].length + 2) / 132);
    if (rows + currentRow >= 2) {
      const maxRow = 2 - currentRow;
      let maxContent = article[i].substr(0, 132 * maxRow - 4) + '..';
      articleSub.push(maxContent);
      break;
    } else {
      articleSub.push(article[i]);
      currentRow += rows;
    }
  }
  return articleSub;
}

const getVisitCount = (articleId) => {
  const item = visitCount.value.find(l => l.articleId === articleId);
  return item? item.visitCount : 0;
};

const getLikeCount = (articleId) => {
  const item = likeCount.value.find(l => l.articleId === articleId);
  return item? item.likeCount : 0;
};

const getStarCount = (articleId) => {
  const item = starCount.value.find(l => l.articleId === articleId);
  return item? item.starCount : 0;
};

function initArticle(){
  axios.get("http://localhost:8081/initArticle",{
    responseType:"json"
  }).then(res=>{
    const data = res.data;
    articles.value = data;
    articles.value.map(a=>{a.title=a.title==="undefined"?"无标题":a.title;a.articleContent=a.articleContent==="undefined"?"无内容":a.articleContent})

    data.forEach(item => {
      const articleId = item.articleId;
      const visitCountValue = item.visitCount;
      visitCount.value.push({
        articleId: articleId,
        visitCount: visitCountValue
      });
    });

    data.forEach(item => {
      const articleId = item.articleId;
      const likeCountValue = item.likeCount;
      likeCount.value.push({
        articleId: articleId,
        likeCount: likeCountValue
      });
    });

    data.forEach(item => {
      const articleId = item.articleId;
      const StarCountValue = item.starCount;
      starCount.value.push({
        articleId: articleId,
        starCount: StarCountValue
      });
    });
    if(isLogin.value===true)
      initArticleInfo();
    articles.value.sort((a,b)=>a.articleId-b.articleId);
  }).catch(err=>{
    console.log(err);
  })
}

function initCurrentArticleInfo(articleId){
  axiosToken.get("http://localhost:8081/initCurrentArticleInfo", {
    params:{articleId:articleId},
    responseType: "json"
  }).then(res => {
    const data = res.data.data;
    isLike.value.find(l=>l.articleId === articleId).like = data.like;
    isStarred.value.find(s=>s.articleId === articleId).star = data.star;
  }).catch(err => {
    console.log(err);
  });
}

function initArticleInfo() {
  axiosToken.get("http://localhost:8081/getArticleInfo", {
    responseType: "json"
  }).then(res => {
    const data = res.data.data;

    data.forEach(item => {
      const articleId = item.articleId;
      const like = item.like;
      isLike.value.push({
        articleId: articleId,
        like: like
      });
    });

    data.forEach(item => {
      const articleId = item.articleId;
      const star = item.star;
      isStarred.value.push({
        articleId: articleId,
        star: star
      });
    });
  }).catch(err => {
    console.log(err);
  });
}

onMounted(()=>{
  initArticle();
})
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