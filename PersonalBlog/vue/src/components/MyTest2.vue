<template>
  <p>这时跳转的页面</p>
  <router-link :to="{ name: 'Home' }">跳回去</router-link>
  <p>我是你爹: {{ message }}</p>
  <button @click="load">懒加载</button>
  <component :is="Form" v-if="Form" @sentData="handleSentData"></component><br>
  <button @click="load2">直接跳</button>
  <button @click="add">测试组合 API 响应式</button><br>
  {{ n.name }}<br>{{ n.value }}<br>
  <p>得到子组件信息:{{ childMessage }}</p><br>
  <p>用provide和inject来得到:</p>{{childMessage2}}
</template>

<script setup>
import {defineProps, provide, reactive, ref, toRef} from 'vue';
import { useRouter } from "vue-router";
const childMessage2 = "我提供过去了";
provide("childMessage2",childMessage2)
let childMessage = ref("");
const props = defineProps({
  message: {
    type: String,
    required: true
  }
});
console.log(props.message);

const router = useRouter();
let Form = ref(null);
let clickCount = ref(0);

async function load() {
  clickCount.value++;
  const { default: myForm } = await import('./myForm');
  Form.value = clickCount.value % 2 !== 0 ? myForm : null;
}

function load2(){
  router.push("/form");
}

const nn = { name: 'num', value: 0 };
const n = reactive(nn);
let num = toRef(n, 'value');

function add() {
  num.value++;
  console.log(n);
}

function handleSentData(data) {
  childMessage.value = data;
}
</script>
