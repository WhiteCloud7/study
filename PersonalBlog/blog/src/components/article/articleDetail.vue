<template>
  <div class="myArticles-articleDetail" v-loading.fullscreen.lock="isLoading">
      <Editor
          api-key="lifs7sk02yr110bbdvq7tcxiwlnr1p29cqe9sz0j1do78teq"
          :init="editorInit"
          @init="onEditorInit"
      />
    <div class="myArticles-articleDetailIconGroup">
      <el-icon class="myArticles-articleDetailIcon" @click="toggleLike">
        <img :src="likeIcon" class="myArticles-articleDetailIconImg" />
      </el-icon>
      <label class="myArticles-articleDetailIconLabel">{{ likeCount }}</label>
      <el-icon class="myArticles-articleDetailIcon" @click="toggleStar">
        <component :is="isStar? StarFilled : Star" />
      </el-icon>
      <label class="myArticles-articleDetailIconLabel">{{ starCount }}</label>
      <el-icon class="myArticles-articleDetailIcon"><ChatDotRound /></el-icon>
      <el-button class="myArticles-back" @click="back">返回</el-button>
    </div>
  </div>
</template>

<script setup>
// 引入必要的 Vue 函数和 Element Plus 图标
import {computed, ref, onMounted} from "vue";
import { Star, StarFilled, ChatDotRound } from '@element-plus/icons-vue';
import axiosToken from "@/axios";
import {useRouter} from "vue-router";
import { marked } from 'marked'
import Editor from '@tinymce/tinymce-vue'
// 基于 props 初始化响应式数据
// 点赞数量
const likeCount = ref(0);
// 收藏数量
const starCount = ref(0);
// 是否已点赞状态
const isLike = ref(false);
// 是否已收藏状态
const isStar = ref(false);
const content = ref("" );
const editorRef = ref(null);
const htmlContent = ref();
const isLoading = ref(false);
const editorInit = {
  menubar: false,
  toolbar: false,
  statusbar: false,
  plugins: 'codesample',
  height: 500,
  readonly: 1,
  content_style: 'body { font-size:16px; padding:10px; }',
};


const router = useRouter();
const props = defineProps({
  articleId: Number
});

// 计算点赞图标路径的计算属性
const likeIcon = computed(() => {
  return require(`@/assets/photo/${isLike.value? 'likefill.png' : 'like.png'}`);
});

// 点赞按钮点击处理函数
const toggleLike = () => {
  axiosToken.get("http://localhost:8081/updateArticleLikeCount",{
    params:{
      articleId:props.articleId,
    },
    responseType:"json"
  }).then(res=>{
    if (isLike.value) {
      likeCount.value--;
    } else {
      likeCount.value++;
    }
    isLike.value =!isLike.value;
  }).catch(console.log)
};

// 收藏按钮点击处理函数
const toggleStar = () => {
  axiosToken.get("http://localhost:8081/updateArticleStarCount",{
    params:{
      articleId:props.articleId,
    },
    responseType:"json"
  }).then(res=>{
    if (isStar.value) {
      starCount.value--;
    } else {
      starCount.value++;
    }
    isStar.value =!isStar.value;
  }).catch(console.log)
};
// 返回按钮点击处理函数
const back = () => {
  router.push("/article");
};

function initCurrentArticleInfo() {
  axiosToken.get("http://localhost:8081/initCurrentArticleInfo", {
    params: {articleId: props.articleId},
    responseType: "json"
  }).then(res => {
    const data = res.data.data;
    isLike.value = data.like;
    isStar.value = data.star;
  }).catch(err => {
    console.log(err);
  });
}

const onEditorInit = (evt, editor) => {
  editorRef.value = editor;
  if (htmlContent.value) {
    editor.setContent(htmlContent.value);
  }
};

function initCurrentArticle() {
  axiosToken.get("http://localhost:8081/initCurrentArticle", {
    params: { articleId: props.articleId },
    responseType: "json"
  }).then(res => {
    const data = res.data.data;
    console.log(editorInit);
    likeCount.value = data.likeCount;
    starCount.value = data.starCount;
    const html = marked.parse(data.articleContent);
    htmlContent.value = html;
    if (editorRef.value) {
      editorRef.value.setContent(html);
    } else {
      const timer = setInterval(() => {
        if (editorRef.value) {
          editorRef.value.setContent(html);
          clearInterval(timer);
        }
      }, 100);
    }
  }).catch(console.log);
}

function loading(){
  isLoading.value = true;
  setTimeout(()=>{
    isLoading.value = false;
  },1000);
}

onMounted(()=>{
  initCurrentArticle(),
  initCurrentArticleInfo()
  loading();
})
</script>

<style>
/* 文章详情容器样式 */
.myArticles-articleDetail {
  display: flex;
  position: absolute;
  top: 0;
  width: 100%;
  height: 100%;
  z-index: 600;
  background-color: rgba(255, 255, 255, 0.8);
  flex-direction: column;
  overflow-x: hidden;
}

/* 文章内容区域样式 */
.myArticles-content {
  padding: 22px;
  width: 100%;
  min-height: 90%;
  overflow-y: auto;
  background-color: rgb(216.8, 235.6, 255);
  box-shadow: var(--el-box-shadow);
}

/* 文章详情图标组样式 */
.myArticles-articleDetailIconGroup {
  background-color: rgba(255, 255, 255, 0.65);
  display: flex;
  flex-direction: row;
  width: 100%;
  height: 10%;
}

/* 文章详情单个图标样式 */
.myArticles-articleDetailIcon {
  width: 5%;
  margin-top: 1%;
  cursor: pointer;
  font-size: 2em !important;
  margin-right: 6px;
  margin-left: 20px;
}

/* 文章详情图标内图片样式 */
.myArticles-articleDetailIconImg {
  width: 1em;
  height: 1em;
}

/* 文章详情图标标签样式 */
.myArticles-articleDetailIconLabel {
  font-size: 2em;
  margin-right: 20px;
  margin-top: 3px;
}

/* 返回按钮样式 */
.myArticles-back {
  width: 20%;
  margin-top: 1%;
  margin-left: 60%;
}
</style>