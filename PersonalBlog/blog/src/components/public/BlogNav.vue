<template>
  <div class="h-6" />
  <el-affix>
    <el-menu
        :default-active="activeIndex"
        class="el-menu-demo"
        mode="horizontal"
        background-color="#545c64"
        text-color="#fff"
        active-text-color="#ffd04b"
        @select="handleSelect"
        popper-class="menu"
        router
    >
      <el-menu-item index="/index" >首页</el-menu-item>
      <el-menu-item index="/resume">个人简历</el-menu-item>
      <el-menu-item index="/project">项目</el-menu-item>
      <el-menu-item index="/article">文章</el-menu-item>
      <el-menu-item index="/contactMe">与我联系</el-menu-item>
      <el-menu-item class="custom-menu-item">
        <div v-if="isLogin" class="blogNav-user">
          <el-popover
              placement="top-start"
              :width="200"
              trigger="hover"
          ><template #reference>
              <el-avatar :src="avatar"></el-avatar>
            </template>
            <div style="display: flex; flex-direction: column;">
              <el-button type="default" size="small" @click="goToProfile">个人中心</el-button>
              <el-button type="danger" size="small" @click="logout" :style="{marginLeft:'0'}">退出登录</el-button>
            </div>
          </el-popover>
        </div>
        <p v-else @click="goToLogin" title="点击前往登录">未登录</p>
      </el-menu-item>
    </el-menu>
  </el-affix>
</template>
<script lang="js" setup>
import {ref, getCurrentInstance, onMounted, watch} from 'vue'
import {useRouter} from "vue-router";
import axios from "@/axios";
import cookie from 'js-cookie';

const avatar = ref('');
const activeIndex = ref('1');
const instance = getCurrentInstance();
const isLogin = instance?.appContext.config.globalProperties.$isLogin;
const username = ref("");
const router = useRouter();

function logout(){
  isLogin.value = false;
  sessionStorage.removeItem("token");
  cookie.remove("refreshToken");

  axios.get("http://59.110.48.56:8081/logout",{
    params:{username:username.value}
  }).catch(console.log);
}

const handleSelect = (key, keyPath) => {
  if(isLogin.value===false&&key==="/contactMe"){
    router.push("/login");
  }else{
    router.push(key);
  }
}

function goToLogin(){
  location.assign("/login");
}

function goToProfile(){
  location.assign("/profile");
}

function initLogin(){
  if(isLogin.value===true){
    axios.get("http://59.110.48.56:8081/profile", {
      responseType: "json"
    }).then(res => {
      const data = res.data.data;
      username.value = data.username;
      avatar.value = data.avatar_src;
      console.log(data)
    }).catch(err => {
      console.log(err);
    });
  }
}

watch(isLogin, (newVal) => {
  if (newVal === true) {
    initLogin();
  }
});

onMounted(()=>{
  initLogin();
})
</script>
<style scoped>
.el-menu-demo{
  margin-top: 0px;
  height: 60px;
}
.el-menu-demo .el-menu-item {
  width: 220px;
}
.custom-menu-item {
  width: 40px !important;
}
.blogNav-user{
  display: flex;
  align-items: center;
  justify-items: center;
}
</style>