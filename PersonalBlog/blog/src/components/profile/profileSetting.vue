<template>
  <div class="edit-profile-content">
    <h1>编辑资料</h1>
    <form @submit.prevent="saveProfile">
      <div class="profile-avatar">
        <span style="position: relative;top: 8px">头像:</span>
        <el-avatar :src="avatar" size="100" style="margin-left: 48px"></el-avatar>
        <el-upload
            class="avatar-uploader"
            action="http://localhost:8081/uploadAvatar"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :headers="uploadHeaders"
            :before-upload="beforeAvatarUpload"
        >
          <i class="el-icon-upload"></i>
          <el-button style="margin-left: 20px">点击上传</el-button>
        </el-upload>
      </div>
      <div class="profile-item">
        <span>昵称:</span>
        <el-input v-model="nickname" placeholder="请输入昵称"></el-input>
      </div>
      <div class="profile-item">
        <span>性别:</span>
        <el-select v-model="sex" placeholder="请选择性别">
          <el-option label="男" value="男"></el-option>
          <el-option label="女" value="女"></el-option>
        </el-select>
      </div>
      <div class="profile-item">
        <span>生日:</span>
        <el-date-picker
            v-model="birthday"
            type="date"
            placeholder="选择生日"
        ></el-date-picker>
        <!-- 年龄根据生日自动计算，不提供编辑 -->
        <span style="margin-left: 10px">年龄:</span>
        <span>{{ age }}</span>
      </div>
      <div class="profile-item">
        <span>电话:</span>
        <el-input v-model="phone" placeholder="请输入电话号码"></el-input>
      </div>
      <div class="profile-item">
        <span>QQ:</span>
        <el-input v-model="qq" placeholder="请输入 QQ 号"></el-input>
      </div>
      <div class="profile-item">
        <span>微信:</span>
        <el-input v-model="wechat" placeholder="请输入微信号"></el-input>
      </div>
      <div class="profile-item">
        <span>毕业院校:</span>
        <el-input v-model="school" placeholder="请输入毕业院校"></el-input>
      </div>
      <div class="button-group" style="margin-left: 160px">
        <el-button type="primary" @click="saveProfile" style="margin-right: 40px">保存</el-button>
        <el-button @click="cancelEdit">取消</el-button>
      </div>
    </form>
  </div>
</template>

<script setup>
import { onMounted, ref, watch } from "vue";
import axios from '@/axios';
import {useRouter} from "vue-router";

const avatar = ref('');
const nickname = ref('');
const sex = ref('');
const age = ref('');
const birthday = ref('');
const phone = ref('');
const qq = ref('');
const wechat = ref('');
const school = ref('');

// 保存原始数据，用于取消编辑时恢复
const originalNickname = ref('');
const originalSex = ref('');
const originalBirthday = ref('');
const originalPhone = ref('');
const originalQQ = ref('');
const originalWechat = ref('');
const originalGraduationSchool = ref('');
const router = useRouter();

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

    // 保存原始数据
    originalNickname.value = nickname.value;
    originalSex.value = sex.value;
    originalBirthday.value = birthday.value;
    originalPhone.value = phone.value;
    originalQQ.value = qq.value;
    originalWechat.value = wechat.value;
    originalGraduationSchool.value = school.value;
  }).catch(err => {
    console.log(err);
  });
}

onMounted(() => {
  initProfile();
});

// 监控生日变化，实时更新年龄
watch(birthday, (newBirthday) => {
  if (newBirthday) {
    age.value = calculateAge(newBirthday);
  }
});

// 保存资料
function saveProfile() {
  axios.post("http://localhost:8081/saveProfile", {
    nikeName: nickname.value,
    sex: sex.value,
    birthday: birthday.value===null?null:birthday.value.toLocaleString(),
    phone: phone.value,
    qq: qq.value,
    wechat: wechat.value,
    school: school.value,
    avatar: avatar.value
  }).then(res => {
    if(res.data.data==="昵称已被使用！")
      alert("昵称已被使用！")
    else
      alert('资料保存成功');
  }).catch(err => {
    console.log(err);
    alert('资料保存失败，请稍后重试');
  });
}

// 取消编辑
function cancelEdit() {
  nickname.value = originalNickname.value;
  sex.value = originalSex.value;
  birthday.value = originalBirthday.value;
  phone.value = originalPhone.value;
  qq.value = originalQQ.value;
  wechat.value = originalWechat.value;
  school.value = originalGraduationSchool.value;
  router.push("/profile");
}

// 处理头像上传成功
const handleAvatarSuccess = (res) => {
  if(res.data==="上传文件为空")
    alert("上传文件为空!");
  else if(res.data==="上传失败")
    alert("上传失败!");
  else{
    avatar.value = res.data.avatar_url;
    alert('头像上传成功!');
  }
};

// 上传头像前的钩子
const beforeAvatarUpload = (file) => {
  const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';
  if (!isJpgOrPng) {
    alert('只能上传 JPG/PNG 格式的图片');
    return false;
  }
  const isLt2M = file.size / 1024 / 1024 < 2;
  if (!isLt2M) {
    alert('图片大小不能超过 2MB');
    return false;
  }
  return isJpgOrPng && isLt2M;
};

const uploadHeaders = { Authorization: `Bearer ${sessionStorage.getItem('token')}`};
</script>

<style scoped>
.edit-profile-content {
  width: 600px;
  display: flex;
  flex-direction: column;
  justify-items: center;
  align-items: center;
}

.profile-item {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

.profile-item span {
  width: 100px;
}

.button-group {
  margin-top: 20px;
}

.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 100px;
  height: 100px;
  line-height: 100px;
  text-align: center;
}

.avatar {
  width: 100px;
  height: 100px;
  display: block;
}

.profile-avatar{
  display: flex;
  flex-direction: row;
  margin-right: 10px;
  margin-bottom: 10px;
  margin-top: 20px;
}
</style>