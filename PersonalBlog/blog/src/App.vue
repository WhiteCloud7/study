<template>
  <blog-nav v-if="$route.meta.showNav"></blog-nav>
  <router-view></router-view>
</template>
<script setup>
import {onMounted,getCurrentInstance} from "vue";
import axios from "axios";
const instance = getCurrentInstance();
const isLogin = instance?.appContext.config.globalProperties.$isLogin;
axios.defaults.withCredentials = true;

function acquireToken(){
  const token = sessionStorage.getItem("token");
  if(token===null || token === undefined){
    axios.get("http://59.110.48.56:8081/refreshToken",{
    }).then(res=>{
      const data = res.data.data;
      if(data!==null){
        sessionStorage.setItem("token",data);
        isLogin.value = true;
      }
    }).catch(console.log)
  }else{
    isLogin.value = true;
  }
}

onMounted(() => {
  let link = document.querySelector("link[rel~='icon']");
  if (!link) {
    link = document.createElement('link');
    link.rel = 'icon';
    document.getElementsByTagName('head')[0].appendChild(link);
  }
  link.href = '/photo/logo.png';
  acquireToken();
  setInterval(acquireToken,1000*30*60);
});
</script>
<style scoped>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
  background-color: rgb(235.9, 245.3, 255);
}
</style>
<script>
import BlogNav from "@/components/public/BlogNav";
export default {
  components: {BlogNav}
}
</script>