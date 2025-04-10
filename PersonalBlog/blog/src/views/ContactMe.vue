<template>
  <div class="contact-container">
    <el-aside class="contact-aside">
      <frends-list></frends-list>
    </el-aside>
    <el-main class="contact-main">
      <contact-main></contact-main>
    </el-main>
  </div>
</template>
<script>
import ContactMain from "@/components/ContactMe/ContactMain";
import FrendsList from "@/components/ContactMe/FrendsList";
export default {
  components: {FrendsList, ContactMain}
}
</script>
<style>
.contact-container{
  display: flex;
  flex-direction: row;
  height: 89vh;
  background-color: #d9ecff;
  box-sizing: border-box;
}
.contact-aside{
  border: 1px solid black;
  border-radius: 10px;
  padding: 8px;
  background-color: #ecf5ff;
  box-sizing: border-box;
  overflow: hidden;
}
.contact-main{
  margin-left: 8px;
  border: 1px solid black;
  border-radius: 10px;
  background-color: #ecf5ff;
  box-sizing: border-box;
  height: 100%;
  padding: 0;
}
</style>