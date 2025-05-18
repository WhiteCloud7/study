<template>
  <div class="profile-content">
    <h1>个人资料</h1>
    <div class="nickname">
      <el-avatar :src="avatar" size="100"></el-avatar>
      <span>{{ nickname }}</span>
    </div>
    <div class="profile-item">
      <span>性别:</span>
      <span class="profile-info">{{ sex }}</span>
    </div>
    <div class="profile-item">
      <span>年龄:</span>
      <span class="profile-info">{{ age }}</span>
      <span style="margin-left: 10px">生日:</span>
      <span style="margin-left: 10px">{{ birthday }}</span>
    </div>
    <div class="profile-item">
      <span>电话:</span>
      <span class="profile-info">{{ phone }}</span>
    </div>
    <div class="profile-item">
      <span>QQ:</span>
      <span class="profile-info">{{ qq }}</span>
    </div>
    <div class="profile-item">
      <span>微信:</span>
      <span class="profile-info">{{ wechat }}</span>
    </div>
    <div class="profile-item">
      <span>毕业院校:</span>
      <span class="profile-info">{{ school }}</span>
    </div>
    <el-button v-if="route.path.slice(0,route.path.lastIndexOf('/'))==='userProfile'" style="width: 100px" @click="addFriend(username)">添加好友</el-button>
  </div>
</template>

<script setup>
import { onMounted, ref, watchEffect } from "vue";
import axios from '@/axios';
import {getCurrentInstance} from "vue";
import {useRouter,useRoute} from "vue-router";

const router = useRouter();
const route = useRoute();
const avatar = ref('');
const nickname = ref('');
const sex = ref('');
const age = ref('');
const birthday = ref('');
const phone = ref('');
const qq = ref('');
const wechat = ref('');
const school = ref('');
const instance = getCurrentInstance();
const isLogin = instance?.appContext.config.globalProperties.$isLogin;
const props = defineProps({
  username:String
})
// 计算年龄的函数
function calculateAge(birthdayStr) {
  const birthDate = new Date(birthdayStr);
  const currentDate = new Date();
  let calculatedAge = currentDate.getFullYear() - birthDate.getFullYear();
  const monthDiff = currentDate.getMonth() - birthDate.getMonth();
  if (monthDiff < 0 || (monthDiff === 0 && currentDate.getDate() < birthDate.getDate())) {
    calculatedAge--;
  }
  return calculatedAge;
}

function initProfile() {
  if(props.username===null||props.username===undefined){
    axios.get("http://localhost:8081/profile", {
      responseType: "json"
    }).then(res => {
      const data = res.data.data;
      avatar.value = data.avatar_src;
      nickname.value = data.nikeName;
      sex.value = data.sex;
      birthday.value = data.birthday;
      phone.value = data.phone;
      qq.value = data.qq;
      wechat.value = data.wechat;
      school.value = data.school;
      // 初始化年龄
      age.value = calculateAge(birthday.value);
    }).catch(err => {
      console.log(err);
    });
  }else{
    axios.get("http://localhost:8081/friendProfile", {
      params:{
        username:props.username
      },
      responseType: "json"
    }).then(res => {
      const data = res.data.data;
      avatar.value = data.avatar_src;
      nickname.value = data.nikeName;
      sex.value = data.sex;
      birthday.value = data.birthday;
      phone.value = data.phone;
      qq.value = data.qq;
      wechat.value = data.wechat;
      school.value = data.school;
      // 初始化年龄
      age.value = calculateAge(birthday.value);
    }).catch(err => {
      console.log(err);
    });
  }
}

function addFriend(username){
  if(isLogin.value===true){
    axios.get("http://localhost:8081/addFriend",{
      params:{
        username:username,
      }
    }).then(()=>{
      if(confirm("添加成功！确认返回聊天界面"))
        router.push("/contactMe")
    }).catch(console.log)
  }else {
    router.push("/login");
  }
}

onMounted(() => {
  initProfile();
});

// 每天更新一次年龄
watchEffect(() => {
  const now = new Date();
  const midnight = new Date(now.getFullYear(), now.getMonth(), now.getDate() + 1, 0, 0, 0);
  const timeUntilMidnight = midnight - now;

  setTimeout(() => {
    age.value = calculateAge(birthday.value);
    // 每天更新一次
    setInterval(() => {
      age.value = calculateAge(birthday.value);
    }, 24 * 60 * 60 * 1000);
  }, timeUntilMidnight);
});
</script>

<style>
.profile-content {
  display: flex;
  flex-direction: column;
}

.profile-item {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

.nickname {
  display: flex;
  align-items: center;
  justify-items: center;
  font-size: 20px;
  font-weight: bold;
  margin-top: 10px;
  margin-bottom: 10px;
}
.profile-info{
  margin-left: 10px;
}
</style>