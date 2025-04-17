<template>
  <div>
    <el-avatar class="avatar" :src="`${avatarSrc}`" @error="handleError"></el-avatar>
  </div>
  <div class="introduce">
    <p style="font-size: 18px">云白<a href="https://blog.csdn.net/qq_73715061?spm=1010.2135.3001.10640" class="like" title="转到我的CSDN主页">+关注</a></p>
    <p style="font-size: 12px">年龄：{{age}}&emsp;关注：{{likes}}</p>
  </div>
  <p class="motto">淡 泊 明 志，宁 静 致 远</p>
</template>

<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";

const likes = ref(0);
const age = ref(0);
const showDefaultIcon = ref(false);

const avatarSrc = ref("");
const handleError = () => true;

const getAge = () => {
  const birthDate = new Date(2004, 2, 8); 
  const currentDate = new Date();
  let calculatedAge = currentDate.getFullYear() - birthDate.getFullYear();
  const monthDiff = currentDate.getMonth() - birthDate.getMonth();
  if (
    monthDiff < 0 ||
    (monthDiff === 0 && currentDate.getDate() < birthDate.getDate())
  ) {
    calculatedAge--;
  }
  age.value = calculatedAge; 
}

const getAvatarSrc = () => {
  axios.get("http://localhost:8081/adminAvatar?userId=1",{
    responseType: 'text'
  }).then((res) => {
    avatarSrc.value = res.data;
    console.log(avatarSrc.value);
  }).catch((err) => {
    console.log(err);
  });
}

onMounted(() => {
  getAge(),
  getAvatarSrc();
});
</script>

<style>
.avatar.el-avatar {
  width: 120px;
  height: 120px;
  display: inherit;
  position: relative;
  top: 100px;
  left: 10px;
}

.introduce {
  color: white;
  width: 150px;
  height: 50px;
  display: inherit;
  position: relative;
  left: 160px;
  text-align: left;
}

.introduce .like {
  color: #dcdcdc;
  font-size: 12px;
  margin-left: 8px;
  position: relative;
  bottom: 2px;
  background-color: #3434c5;
  border-color: #3434c5;
  padding: 3px;
  text-decoration: none;
  box-shadow: var(--el-box-shadow);
  border-radius: 4px;
}

.motto {
  color: rgba(255, 255, 255, 0.74);
  font-family: 'Caveat', cursive;
  display: inherit;
  position: relative;
  bottom: 130px;
  left: 480px;
}
</style>    