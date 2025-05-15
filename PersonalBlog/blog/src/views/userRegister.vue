<template>
  <div class="register-document">
    <div class="register-container">
      <h1>注册</h1>
      <form @submit.prevent="handleRegister">
        <div class="register-formGroup">
          <label for="username">用户名</label>
          <input type="text" id="username" v-model="username" placeholder="请输入用户名" required>
        </div>
        <div class="register-formGroup">
          <label for="password">密码</label>
          <input type="password" id="password" v-model="password" placeholder="请输入密码" required>
        </div>
        <div class="register-formGroup">
          <label for="email">再此输入密码</label>
          <input type="password" id="passwordCheck" v-model="passwordCheck" placeholder="再次输入密码" required>
        </div>
        <button type="submit">注册</button>
      </form>
    </div>
  </div>
</template>

<script setup>
import {ref} from 'vue';
import { useHead } from "@vueuse/head";
import axios from "axios";

useHead({
  title: "注册"
});

const username = ref('');
const passwordCheck = ref('');
const password = ref('');

const handleRegister = () => {
  console.log(password.value);
  if(passwordCheck.value===password.value) {
    axios.get("http://59.110.48.56:8081/register", {
      params: {
        username: username.value,
        password: password.value
      }
    }).then(res => {
      const data = res.data;
      if (data === "用户已存在")
        alert("用户已存在!")
      else {
        const isConfirm = confirm("注册成功，确认前往登录！");
        if(isConfirm)
          location.assign("/login");
      }
    }).catch(err => {
      console.log(err);
    })
  }
  else
    alert("两次密码不一致！");
}
</script>

<style scoped>
.register-document {
  background-image: url("@/assets/photo/view.png");
  font-family: Arial, sans-serif;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  margin: 0;
  background-color: #f4f4f9;
}

.register-container {
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  width: 300px;
}

.register-container h1 {
  text-align: center;
  margin-bottom: 20px;
}

.register-formGroup {
  margin-bottom: 15px;
}

.register-formGroup label {
  display: block;
  margin-bottom: 5px;
}

.register-formGroup input {
  width: 100%;
  padding: 8px;
  box-sizing: border-box;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.register-container button {
  width: 100%;
  padding: 10px;
  background-color: #007BFF;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.register-container button:hover {
  background-color: #0056b3;
}
</style>