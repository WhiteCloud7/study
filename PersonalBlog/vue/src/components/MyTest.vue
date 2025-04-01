<template>
  <div class="div">
    <p>我是你爹:{{name}}</p> <!-- 插值表达式 -->
    <p>我：<span v-text="age"></span></p> <!-- v-text -->
    <span v-html="html"></span><br> <!-- v-html -->
    <input type="text" v-model="username"><br> <!-- 绑定文本框 -->
    <input type="checkbox" v-model="agree">哈哈 <!-- 绑定单个复选框 -->
    <input type="checkbox" value="math" v-model="subject">数学 <!-- 绑定多个复选框,value传递值 -->
    <input type="checkbox" value="Chinese" v-model="subject">语文
    <input type="checkbox" value="English" v-model="subject">英语<br>
    <select v-model="selectOption"> <!-- 静态下拉框，动态在v-for介绍 -->
      <option value="A">我是天才</option>
      <option value="B">你是蠢材</option>
      <option value="C">我说得对</option>
    </select><br>
    <button @click="click()">点我</button><br>
    <button @click="testIf()">点我</button><br>
    <input v-if='test=="1"'>{{test}}<br>
    <button @click="testBind()">点我</button><br>
    <input :value="t"><br>
    <div  v-for="info in names" :key="info.id" class="test">
      {{info.name}}
      {{info.phone}}
    </div>
    <router-link :to="{ name: 'About' }" >跳转到 About 页面</router-link><br>
    <span @mouseenter="mouseenter()">鼠标移入就跳转到 About 页面</span><br>
    <input :value="birth">
  </div>
</template>

<script>
import {defineComponent} from 'vue';
import {MyTest2} from './MyTest2';
// import {useRouter} from 'vue-router';
//setup 语法糖更简单 如下是重写的一些内容
// let name = "刘文杰";
// let age="21";
// function click(){
//        console.log("test");
//    }
// function mouseenter(){
//   const router = useRouter();
//   router.push({ name: 'About' })
// }
export default defineComponent({
  name:"MyTest",
  data() {
    return {
        name: "刘文杰",
        age: "21岁",
        html: '<a href="https://blog.csdn.net/">点击访问 CSDN</a>',
        username:"云白",
        agree: true,
        subject:["math","English"],
        selectOption:"A",
        test:"",
        t:"",
        names:[{id:1,name:"刘文杰",phone:"18671551575"}
          ,{id:2,name:"冯杰伟",phone:"186715ds"}
          ,{id:3,name:"张俊伟",phone:"18671551ds5"}],
    };
  },
  computed: {
    birth() {
      const date = new Date();
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      return `${year}-${month}-${day}`;
    }
  },
  watch:{
      names(newValue, oldValue) {
        console.log('对象发生变化', newValue, oldValue);
      },
  },
  props:{
    
  },
  methods:{
    click(){
      console.log("test");
    },
    testIf(){
      this.$clickCount.value++;
      console.log(this.$clickCount.value);
      if(this.$clickCount.value%2==0)
        this.test = "2";
      else
        this.test = "1";
    },
    testBind(){
      this.$clickCount.value++;
      console.log(this.$clickCount.value);
      if(this.$clickCount.value%2==0)
        this.t = "2";
      else
        this.t = "1";
    },
    mouseenter(){
      this.$router.push({ name: 'About' });
    }
  },
  components:{
    MyTest2
  }
});
</script>
<style>
html{
  display: flex;
  align-items: center;
  flex-direction: column;
  width: 100%;
}
.div{
  display: block;
}
.test{
  display: block;
}
</style>