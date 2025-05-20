<template>
  <div class="register-document">
    <div class="register-container">
      <h1>注册</h1>
      <form @submit.prevent="handleRegister">
        <div class="register-formGroup">
          <label for="username">用户名</label>
          <input type="text" id="username" v-model="username" placeholder="请输入用户名" required>
          <label class="register-check" v-if="usernameLengthIllegality">账号为6-16位的字母数字组合</label>
          <label class="register-check" v-if="usernamePattenIllegality">账号不能包含特殊字符</label>
        </div>
        <div class="register-formGroup">
          <label for="password">密码</label>
          <input type="password" id="password" v-model="password" placeholder="请输入密码" required>
          <label class="register-check" v-if="passwordLengthIllegality">密码为6-20位的字母、数字、合法字符组合</label>
          <label class="register-check" v-if="passwordPattenIllegality">密码不能包含非法字符</label>
        </div>
        <div class="register-formGroup">
          <label for="email">再此输入密码</label>
          <input type="password" id="passwordCheck" v-model="passwordCheck" placeholder="再次输入密码" required>
          <label class="register-check" v-if="passwordCheckError">两次密码不一致</label>
        </div>
        <button type="submit">注册</button>
      </form>
    </div>
  </div>
</template>

<script setup>
import {ref,getCurrentInstance} from 'vue';
import { useHead } from "@vueuse/head";
import axios from "axios";

useHead({
  title: "注册"
});

const username = ref('');
const passwordCheck = ref('');
const password = ref('');

const usernameLengthIllegality = ref(false);
const usernamePattenIllegality = ref(false);
const passwordLengthIllegality = ref(false);
const passwordPattenIllegality = ref(false);
const  passwordCheckError = ref(false);
const instance = getCurrentInstance();
const illegalityStr = instance.appContext.config.globalProperties.$illegalityStr;

const handleRegister = () => {
  //passwordCheckError.value = false;
  // usernameLengthIllegality.value = false;
  // usernamePattenIllegality.value = false;
  // passwordLengthIllegality.value = false;
  // passwordLengthIllegality.value = false;
  //
  // const regex1 = /^[a-zA-Z0-9]{6,12}$/
  // const escapedChars = illegalityStr.map(char => /[.*+?^${}()|[\]\\]/.test(char) ? `\\${char}` : char);
  // const regex2 = new RegExp(`[${escapedChars.join('')}]`);
  //
  // if(username.value.length<6||username.value.length>16){
  //   usernameLengthIllegality.value = true;
  //   if(password.value.length<6||password.value.length>20){
  //     passwordLengthIllegality.value = true;
  //   }else if(regex2.test(password.value)){
  //     passwordPattenIllegality.value = true;
  //   }
  //   return;
  // }
  //
  // if(!regex1.test(username.value)){
  //   usernamePattenIllegality.value = true;
  //   if(password.value.length<6||password.value.length>20){
  //     passwordLengthIllegality.value = true;
  //   }else if(regex2.test(password.value)){
  //     passwordPattenIllegality.value = true;
  //   }
  //   return;
  // }

  if(passwordCheck.value===password.value) {
    axios.get("http://localhost:8081/register", {
      params: {
        username: username.value,
        password: password.value
      }
    }).then(res => {
      const data = res.data;
      if (data === "用户已存在")
        alert("用户已存在!")
      else if(data === "账号或密码校验错误，请重新输入")
        alert("账号或密码校验错误，请重新输入!")
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
    passwordCheckError.value = true;
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
  margin-bottom: 7px;
}

.register-check{
  color: red;
  font-size: 8px;
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