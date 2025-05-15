<template>
  <div class="common-layout">
    <el-container class="indexMain-container">
      <el-header>
        <personal-card></personal-card>
      </el-header>
      <el-main>
        <my-notice></my-notice>
      </el-main>
      <el-footer>
        <contact-methods></contact-methods>
      </el-footer>
    </el-container>
  </div>
</template>
<style>

</style>
<script>
import PersonalCard from "@/components/index/Main/PersonalCard";
import MyNotice from "@/components/index/Main/MyNotice";
import ContactMethods from "@/components/index/Main/ContactMethods";
export default {
  components: {ContactMethods, MyNotice, PersonalCard}
}
</script>
<style>
.indexMain-container{
  min-height: 95vh;
  border: 1px solid black;
}
.indexMain-container .el-header{
  height: 45vh ;
  background-image: url("@/assets/photo/background.jpg");
  background-size: cover;
}
.indexMain-container .el-main{
  min-height: 50vh;
}
.indexMain-container .el-footer{
  min-height: 50px;
  background-color: rgba(58, 121, 208, 0.67);
}
</style>