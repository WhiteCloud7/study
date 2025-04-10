<template>
  <div class="current-time">
    <span class="digit" :class="{ 'digit-flash': isFlash }">{{ hour }}</span>
    <span class="colon">:</span>
    <span class="digit" :class="{ 'digit-flash': isFlash }">{{ min }}</span>
    <span class="colon">:</span>
    <span class="digit" :class="{ 'digit-flash': isFlash }">{{ sec }}</span>
  </div>
</template>

<script setup>
import { ref, onUnmounted, nextTick } from "vue";

// 初始化时间
const hour = ref(0);
const min = ref('00');
const sec = ref('00');
// 用于控制数字闪烁的状态
const isFlash = ref(false);

// 更新时间的函数
const updateTime = () => {
  const date = new Date();
  hour.value = date.getHours();
  const minutes = date.getMinutes();
  const seconds = date.getSeconds();
  min.value = minutes < 10? `0${minutes}` : minutes.toString();
  sec.value = seconds < 10? `0${seconds}` : seconds.toString();

  // 每一秒闪烁一次
  nextTick(() => {
    isFlash.value = true;
    setTimeout(() => {
      isFlash.value = false;
    }, 500);
  });
};

// 初始调用一次更新时间函数
updateTime();

// 使用 setInterval 每秒更新一次时间
const intervalId = setInterval(updateTime, 1000);

// 组件卸载时清除定时器，避免内存泄漏
onUnmounted(() => {
  clearInterval(intervalId);
});
</script>

<style scoped>
.current-time {
  font-family: 'Dancing Script', cursive;
  font-size: 40px;
  width: 190px;
  height: 75px;
  padding: 20px;
  border-radius: 16px;
  box-shadow: var(--el-box-shadow);
  display: flex;
  justify-content: center;
  align-items: center;
  color: #333;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.1);
  background-color: #ecf5ff;
  margin-top: 30px;
}

.digit {
  display: inline-flex;
  justify-content: center;
  align-items: center;
  min-width: 40px;
  height: 58px;
  margin: 0 3px; /* 减小数字块的左右外边距 */
  padding: 0 8px;
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.06),
  inset 0 2px 2px rgba(255, 255, 255, 0.8);
  font-weight: 300;
  transition: all 0.3s ease;
}

.digit-flash {
  animation: softFlash 1s linear infinite;
}

.colon {
  margin: 0 2px; /* 减小冒号的左右外边距 */
  color: #666;
  font-size: 42px;
  font-weight: 300;
}

@keyframes softFlash {
  50% {
    opacity: 0.9;
    transform: scale(0.98);
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  }
}
</style>