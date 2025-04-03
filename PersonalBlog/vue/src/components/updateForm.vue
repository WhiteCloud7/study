<template class="template">
  <form class="form">
    <div class="input">
      <label for="is">编号：     </label>
      <input id="id" v-model="formData.userId" required="required">
    </div>
    <div class="input">
      <label for="name">姓名：     </label>
      <input id="name" v-model="formData.nikename">
    </div>
    <div class="input">
      <label for="sex">性别：     </label>
      <input id="sex" v-model="formData.sex">
    </div>
    <div class="input">
      <label for="phone">电话：     </label>
      <input id="phone" v-model="formData.phone">
    </div>
    <div class="input">
      <label for="email">邮箱：     </label>
      <input id="email" v-model="formData.email">
    </div>
    <button type="submit" @click.prevent="updateForm" class="submit">提交</button>
  </form>
</template>
<script setup>
import {reactive} from "vue";
import axios from "axios";
let formData = reactive({
  userId: 0,
  nikename: "",
  sex: "",
  phone: "",
  email: ""
});
function updateForm() {
  // const form = JSON.stringify(formData);
  let form = new FormData();
  for(let key in formData){
    form.append(key,formData[key]);
  }
  axios.post("http://localhost:8081/form/updateUserInfo", form, {
  }).then(response => {
    console.log(response.data);
  }).catch(error => {
    console.error("Error:", error);
  });
}

</script>
<style>
.form{
  font-family: "Bauhaus 93";
  font-size: 20px;
  width: 40%;
  right: 30%;
  left: 30%;
  top: 15%;
  position: fixed;
  display: flex;
  flex-direction: column;
  padding: 10px;
  border: 1px solid black;
}
.input{
  margin-top: 20px;
}
.input input{
  width: 50%;
}
.submit{
  width: 20%;
  margin-left: 40%;
  margin-top: 20px;
}
</style>