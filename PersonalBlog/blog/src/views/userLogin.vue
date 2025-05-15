<template>
  <div class="login-document">
    <div class="login-container">
      <h1>登录</h1>
      <form @submit.prevent="handleLogin">
        <div class="login-formGroup">
          <label for="username">用户名</label>
          <input type="text" id="username" v-model="username" placeholder="请输入用户名" required>
        </div>
        <div class="login-formGroup">
          <label for="password">密码</label>
          <input type="password" id="password" v-model="password" placeholder="请输入密码" required>
        </div>
        <button type="submit">登录</button>
        <p @click="goToRegister" class="login-register">还没有账号？点我注册</p>
      </form>
    </div>
  </div>
</template>
<script setup>
import { ref,getCurrentInstance} from 'vue';
import {useHead} from "@vueuse/head";
import axios from 'axios';
import cookie from "js-cookie";

const instance = getCurrentInstance();
const isLogin = instance?.appContext.config.globalProperties.$isLogin;
const username = ref('');
const password = ref('');

function goToRegister(){
  location.assign("/register");
}

function initUser(){
  isLogin.value = false;
  sessionStorage.removeItem("token");
  cookie.remove("refreshToken");
}

function handleLogin(){
  console.log(isLogin.value);
  axios.get("http://59.110.48.56:8081/login",{
    params:{
      username:username.value,
      password:password.value
    }
  }).then(res=> {
    const data = res.data;
    if (data.message === "密码错误")
      alert("密码错误，请重新登录!")
    else if (data.message === "账号不存在")
      alert("账号不存在")
    else {
      initUser();
      if(data.data!==null){
        isLogin.value = true;
        sessionStorage.setItem("token", data.data);
        location.assign("/index");
      }
    }
  }).catch(err=>{
    console.log(err);
  })
}

useHead({
  title:"登录"
})
</script>
<style>
.login-document {
  background-image: url("@/assets/photo/view.png");
  font-family: Arial, sans-serif;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  margin: 0;
  background-color: #f4f4f9;
}

.login-container {
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  width: 300px;
}

.login-container h1 {
  text-align: center;
  margin-bottom: 20px;
}

.login-formGroup {
  margin-bottom: 15px;
}

.login-formGroup label {
  display: block;
  margin-bottom: 5px;
}

.login-formGroup input {
  width: 100%;
  padding: 8px;
  box-sizing: border-box;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.login-container button {
  width: 100%;
  padding: 10px;
  background-color: #007BFF;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.login-container button:hover {
  background-color: #0056b3;
}

.login-register{
  margin-top: 10px;
  margin-left: 40px;
  cursor: pointer;
  color: rgba(0, 0, 0, 0.74);
}
</style>