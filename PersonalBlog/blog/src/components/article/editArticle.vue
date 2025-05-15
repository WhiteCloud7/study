<template>
  <div class="myArticles-articleEdit">
    <div class="myArticles-editContent">
      <el-input
          type="text"
          v-model="articleTitle"
          :readonly="isReadOnly"
          placeholder="请输入标题"
          :input-style="{ cursor: cursorType }"
      />
      <el-input
          type="textarea"
          v-model="articleText"
          :readonly="isReadOnly"
          :input-style="{ cursor: cursorType }"
          placeholder="请输入正文内容"
      />
    </div>
    <div class="myArticles-editButtonGroup">
      <el-button class="myArticles-editButton" @click="edit">编辑</el-button>
      <el-button class="myArticles-editButton" @click="save">保存</el-button>
      <el-button class="myArticles-editBack" @click="back">返回</el-button>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits, ref } from 'vue'
import axios from 'axios'

const props = defineProps({
  articleId: Number,
  title: String,
  content: String
})

const emit = defineEmits(['back'])
const articleTitle = ref(props.title)
const articleText = ref(props.content)

const isReadOnly = ref(true)
const cursorType = ref("default")

function edit() {
  isReadOnly.value = !isReadOnly.value
  cursorType.value = isReadOnly.value ? "default" : "text"
}

function save() {
  const params = new URLSearchParams()
  params.append("articleId", props.articleId)
  params.append("title", articleTitle.value)
  params.append("content", articleText.value)

  axios.post("http://59.110.48.56:8081/saveArticle", params, {
    responseType: 'text',
    headers: {
      "Content-Type": "application/x-www-form-urlencoded"
    }
  }).then(res => {
    if (res.data === "保存成功") {
      confirm("保存成功！")
      window.location.reload()
    } else {
      alert("保存失败！")
    }
  }).catch(err => {
    console.error(err)
  })
}

function back() {
  emit("back")
}
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
  width: 100%;
  height: 10%;
}

.myArticles-editButton {
  width: 10%;
  margin-top: 1%;
  font-size: 2em;
  margin-left: 10px;
}

.myArticles-editBack {
  width: 10%;
  margin-top: 1%;
  margin-left: auto;
  margin-right: 20px;
}
</style>
