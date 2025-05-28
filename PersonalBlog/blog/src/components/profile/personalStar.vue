<template>
  <h1 style="margin-bottom: 10px">收藏</h1>
  <div v-for="star in stars" :key="star.articleId">
    <div class="profile-star" @click="goToArticle(star.articleId)">
      <el-avatar :src="star.avatar_src"></el-avatar>
      <p class="profile-starNike">{{star.nikeName}}</p>
      <p class="profile-starTitle"><em><strong>{{star.title}}</strong></em></p>
    </div>
    <el-divider :style="{marginTop:'10px'}"></el-divider>
  </div>
</template>
<script setup>
import {onMounted,ref} from "vue";
import axios from "@/axios";
import {useRouter} from 'vue-router';

const router = new useRouter();
const stars = ref(null);

function goToArticle(articleId){
  router.push(`/article/${articleId}`);
}

function getStar(){
  axios.get("http://localhost:8081/getStar").then(res=>{
    const data = res.data.data;
    stars.value = data;
    console.log(stars.value);
  }).catch(console.log);
}
onMounted(()=>{
  getStar();
})
</script>
<style>
.profile-star{
  display: flex;
  flex-direction: row;
  justify-items: center;
  align-items: center;
}
.profile-star:hover{
  cursor: pointer;
  background: lightgrey;
}
.profile-starNike{
  margin-left: 10px;
}
.profile-starTitle{
  margin-left: 50px;
}
</style>