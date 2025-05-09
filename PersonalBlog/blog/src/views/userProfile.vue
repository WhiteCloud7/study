<template>
  <div class="personal-center">
    <div class="sidebar">
      <el-menu
          mode="vertical"
          router
      ><el-menu-item index="/profile">个人资料</el-menu-item>
        <el-menu-item index="/profile/star">个人收藏</el-menu-item>
        <el-menu-item index="/profile/setting">编辑资料</el-menu-item>
      </el-menu>
    </div>
    <div class="content">
      <router-view></router-view>
    </div>
  </div>
</template>
<style scoped>
.personal-center {
  display: flex;
  height: 62vh;
  width: 1200px;
}

.sidebar {
  width: 200px;
  border-right: 1px solid #e4e7ed;
}

.content {
  width: 730px;
  display: flex;
  flex-direction: column;
  padding: 20px;
  overflow-y: auto;
  overflow-x: hidden;
  -ms-overflow-style: none;
  transform: scale(1.4); /* 放大 1.2 倍 */
  transform-origin: top left; /* 从左上角缩放，不影响布局 */
}

.content::-webkit-scrollbar {  /*隐藏滚动条*/
  display: none;
}
</style>