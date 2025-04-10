<template>
  <div class="container">
    <div class="time">
      <current-time></current-time>
    </div>
    <img :src="require(`@/assets/photo/canlendarImg-${year}.gif`)">
    <div class="calendar">
      <my-calendar></my-calendar>
    </div>
    <div class="footer">
      <p class="passage">
        <span class="line1">海蓝见鲸，</span><br />
        <span class="line2">林深见鹿，</span><br />
        <span class="line3">梦醒见你。</span>
      </p>
    </div>
  </div>
</template>

<script setup>
import CurrentTime from "@/components/index/Right/CurrentTime";
import MyCalendar from "@/components/index/Right/MyCalendar";
import {onMounted, onUnmounted, ref} from "vue";
const year = ref(2025);
const updateYear = () => {
  const date = new Date();
  year.value = date.getFullYear();
}
onMounted(()=>{
  const interval = setInterval(updateYear,1000 * 24 * 60 *60);
  onUnmounted(()=>{
    clearInterval(interval);
  });
})
</script>

<style>
.container {
  display: flex;
  flex-direction: column;
  width: inherit;
  height: inherit;
}

.time {
  width: 100%;
  height: 20%;
  display: flex;
  justify-content: center;
}

img {
  position: relative;
  width: 100%;
  height: 25%; /* 设置你想要的高度 */
  object-fit: cover; /* 使图片完全覆盖容器，可能会裁剪 */
}

.calendar {
  height: 40%;
  width: 100%;
}
.footer {
  height: 15%;
  width: 100%;
  background: rgba(236, 245, 255, 0.6); /* 半透明蓝底 */
  backdrop-filter: blur(6px);           /* 毛玻璃效果 */
  display: flex;
  align-items: center; /* 垂直居中 */
  justify-content: center; /* 水平居中 */
  padding: 0 15px; /* 为了避免文字过于贴近左右边缘，加入左右内边距 */
}

.footer .passage {
  font-size: 15px; /* 调整字体大小 */
  font-weight: 500;
  font-family: 'KaiTi', 'STKaiti', 'Songti SC', serif;
  line-height: 0.75; /* 更小的行高，减少行与行之间的间距 */
  background: linear-gradient(135deg, #4a90e2, #87e5dc);
  -webkit-background-clip: text;
  color: transparent;
  animation: fadeIn 2s ease-in-out;
  white-space: pre-line;
  text-align: left;
  display: flex;
  flex-direction: column; /* 纵向排列 */
  align-items: flex-start;
  margin: 0; /* 去掉 margin，避免影响布局 */
}

.footer .line1 {
  margin-left: 0; /* 第一行不缩进 */
}

.footer .line2 {
  margin-left: 2em; /* 第二行缩进 2em */
}

.footer .line3 {
  margin-left: 4em; /* 第三行缩进 4em */
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

</style>
