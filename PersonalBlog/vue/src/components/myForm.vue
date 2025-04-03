<template class="temp">
  <button @click="sendDataEvent">自定义事件传递数据</button><br>
<!--  <p>接收到了:{{childMessage2}}</p>-->
  <label for="search" class="label">搜索：</label>
  <input id="search" placeholder="按编号搜索" v-model="Id" @keydown.enter="searchById">
  <button @click="openUpdateFrom">点我更新用户</button>
  <table class="table">
    <thead class="head">
    <tr>
      <th>编号</th>
      <th>姓名</th>
      <th>性别</th>
      <th>电话</th>
      <th>邮箱</th>
    </tr>
    </thead>
    <tbody class="data">
    <tr v-for="userInfo in userInfos" :key="userInfo.userId">
      <td>{{userInfo.userId}}</td>
      <td>{{userInfo.nikename}}</td>
      <td>{{userInfo.sex}}</td>
      <td>{{userInfo.phone}}</td>
      <td>{{userInfo.email}}</td>
    </tr>
    </tbody>
  </table>
</template>
<script>
import { defineComponent } from "vue";
import axios from "axios";
export default defineComponent({
  emits: ['sentData'],
  provide(){
    return{
      loadUser:this.loadUser()
    }
  },
  data() {
    return {
      userInfos: [],
      Id: 0,
      childMessage: "我要传给父组件",
    };
  },
  inject:['childMessage2'],
  mounted() {
    this.loadUser();
  },
  methods: {
    sendDataEvent() {
      this.$emit('sentData', this.childMessage);
    },
    loadUser() {
      axios.get("http://localhost:8081/form/userInfoInit", {
        responseType: "json"
      }).then(response => {
        this.userInfos = response.data;
      }).catch(err => {
        console.log(err);
      });
    },
    searchById() {
      axios.get("http://localhost:8081/form/userInfoInitById", {
        params: {
          userId: this.Id
        },
        responseType: "json"
      }).then(response => {
        this.userInfos = response.data;
      }).catch(err => {
        console.log(err);
      });
    },
    openUpdateFrom() {
      this.$router.push("/updateForm");
    }
  }
});
</script>
<style>
.temp {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-content: center;
}

.table {
  table-layout: fixed;
  position: relative;
  padding: 0;
  left: 10%;
  width: 80%;
  border-collapse: collapse;
}

th,
td {
  border: 1px solid black;
}

td:nth-child(1),
th:nth-child(1) {
  width: 10%;
}
</style>