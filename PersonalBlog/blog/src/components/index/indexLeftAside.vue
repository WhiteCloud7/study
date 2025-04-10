<template>
  <div class="video-wrapper">
    <video
        v-if="!videoError"
        class="video"
        :src="require('@/assets/photo/aside.mp4')"
        autoplay
        loop
        muted
        playsinline
        preload="auto"
        @error="handleVideoError"
    ></video>
    <div v-else class="fallback">
      <PictureFilled class="fallback-icon" />
      <p class="fallback-text">视频加载失败</p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const videoError = ref(false)

function handleVideoError() {
  videoError.value = true
}
</script>

<style scoped>
.video-wrapper {
  width: 220px;
  height: 520px;
  overflow: hidden;
  border-radius: 12px;
  background: black;
  position: relative;
  top:7px;
}

.video {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

.fallback {
  width: 100%;
  height: 100%;
  background-color: #000;
  color: #fff;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.fallback-icon {
  font-size: 48px;
  color: #aaa;
}

.fallback-text {
  margin-top: 12px;
  font-size: 14px;
  color: #ccc;
}
</style>
