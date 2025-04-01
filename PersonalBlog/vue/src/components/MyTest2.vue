<template>
  <p>这时跳转的页面</p>
  <router-link :to="{name:'Home'}">跳回去</router-link>
  <p>接收到的消息：{{message}}}</p>
</template>
<script>
import { defineComponent} from 'vue';

export default defineComponent({
  name:"MyTest2",
  props:{
    message:{
      type:String,
      require:true
    }
  }
});
</script>