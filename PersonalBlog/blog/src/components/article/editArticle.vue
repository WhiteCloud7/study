<template>
  <div class="myArticles-articleEdit" v-loading.fullscreen.lock="isLoading">
    <div class="myArticles-editContent">
      <el-input
          type="text"
          v-model="articleTitle"
          :readonly="isReadOnly"
          placeholder="请输入标题"
          :input-style="{ cursor: cursorType }"
      />
      <Editor
          api-key="lifs7sk02yr110bbdvq7tcxiwlnr1p29cqe9sz0j1do78teq"
          v-model="articleText"
          :init="{
          height: 480,
          resize:false,
          menubar: false,
          plugins: 'link image code lists',
          toolbar: 'undo redo | bold italic | alignleft aligncenter alignright | bullist numlist | code',
          placeholder: '请输入正文内容',
          branding: false,
          promotion: false,
          readonly: isReadOnly,
        }"
      />
    </div>
    <div class="myArticles-editButtonGroup">
      <el-button class="myArticles-editButton" @click="edit">编辑</el-button>
      <el-button class="myArticles-editButton" @click="save">保存</el-button>
      <el-button class="myArticles-editButton" @click="newArticle" v-if="route.path.slice(route.path.lastIndexOf('/'))==='/add'">发布</el-button>
      <el-button class="myArticles-editButton" @click="back">返回</el-button>
    </div>
  </div>
</template>

<script setup>
import {defineProps, defineEmits, ref, onMounted} from 'vue'
import axios from 'axios'
import {useRouter} from "vue-router";
import Editor from '@tinymce/tinymce-vue'
import axiosToken from "@/axios";
import {useRoute} from "vue-router";

const props = defineProps({
  articleId: Number,
})
const router = useRouter();
const articleTitle = ref()
const articleText = ref()
const isReadOnly = ref(true)
const cursorType = ref("default")
const isLoading = ref(false);
const route = useRoute();

function edit() {
  isReadOnly.value = !isReadOnly.value
  cursorType.value = isReadOnly.value ? "default" : "text"
}

function htmlToPlainText(html) {
  // 替换 p 标签为换行，避免多段文字合并成一行
  html = html.replace(/<p[^>]*>/gi, '').replace(/<\/p>/gi, '\r\n');
  html = html.replace(/<br\s*\/?>/gi, '\r\n');

  // 创建一个 DOM 元素来解析剩余标签
  const tempDiv = document.createElement("div");
  tempDiv.innerHTML = html;
  return tempDiv.innerText;  // 自动去除剩余标签
}

function save() {
  if(route.path!=="/article/add"){
    const params = new URLSearchParams()
    params.append("articleId", props.articleId)
    params.append("title", articleTitle.value)
    params.append("content", htmlToPlainText(articleText.value))
    axiosToken.post("http://localhost:8081/saveArticle", params, {
      headers: {
        "Content-Type": "application/x-www-form-urlencoded"
      }
    }).then(res => {
      confirm("保存成功！")
      window.location.reload()
    }).catch(err => {
      console.error(err)
    })
  }else{
    const params = new URLSearchParams()
    params.append("title", articleTitle.value)
    params.append("content", htmlToPlainText(articleText.value))
    axiosToken.post("http://localhost:8081/saveArticleToDraft", params, {
      headers: {
        "Content-Type": "application/x-www-form-urlencoded"
      }
    }).then(res => {
      confirm("保存成功！")
    }).catch(err => {
      console.error(err)
    })
  }
}

function newArticle(){
  const params = new URLSearchParams()
  params.append("title", articleTitle.value)
  params.append("content", htmlToPlainText(articleText.value))
  axiosToken.post("http://localhost:8081/newArticle", params, {
    headers: {
      "Content-Type": "application/x-www-form-urlencoded"
    }
  }).then(res => {
    confirm("发布成功！")
    router.push("/article")
  }).catch(err => {
    console.error(err)
  })
}

function initCurrentArticle() {
  if(route.path!=="/article/add"){
    axiosToken.get("http://localhost:8081/initCurrentArticle", {
      params: { articleId: props.articleId },
      responseType: "json"
    }).then(res => {
      const data = res.data.data;
      articleTitle.value = data.title;
      articleText.value = articleText.value === "undefined"?"":
          data.articleContent
          .replace(/\r\n/g, '<br>')
          .replace(/\n/g, '<br>');
    }).catch(err => {
      console.log(err);
    });
  }else {
    initDraft();
  }
}

function initDraft(){
  axiosToken.get("http://localhost:8081/articleDraft").then(res=>{
    const data = res.data.data;
    articleTitle.value = data.title==="undefined"?"":data.title;
    articleText.value = data.articleContent===undefined?"":data.articleContent;
  }).catch(console.log)
}

function loading(){
  isLoading.value = true;
  setTimeout(()=>{
    isLoading.value = false;
  },1000);
}
function back() {
  router.push("/article");
}

onMounted(()=>{
  initCurrentArticle();
  loading();
})
</script>

<style>
.myArticles-articleEdit {
  display: flex;
  position: absolute;
  top: 0;
  width: 100%;
  height: 100%;
  z-index: 600;
  background-color: rgba(255, 255, 255, 0.8);
  flex-direction: column;
}

.myArticles-editContent {
  padding: 16px;
  width: 100%;
  min-height: 90%;
  background-color: rgb(216.8, 235.6, 255);
  box-shadow: var(--el-box-shadow);
}

.myArticles-editContent textarea {
  height: 400px !important;
  resize: none !important;
}

.myArticles-editButtonGroup {
  background-color: rgba(255, 255, 255, 0.65);
  display: flex;
  flex-direction: row;
  gap: 30px;
  width: 100%;
  height: 10%;
}

.myArticles-editButton {
  width: 10%;
  margin-top: 1%;
  margin-left: 40px;
}
</style>
