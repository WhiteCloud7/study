<template>
  <div class="contact-send">
    <el-avatar class="contact-sendAvatar"></el-avatar>
    <p class="contact-sendContent">{{message}}</p>
  </div>
</template>
<script setup>
import {ref,defineProps} from "vue";

const props = defineProps({
  message: String
})
</script>
<style>
.contact-send {
  display: inline-flex;
  align-items: center;
  max-width: 600px;
  margin-left: 10px;
  margin-top: 8px;
  border-radius: 6px;
  padding: 4px;
}

.contact-sendAvatar {
  flex-shrink: 0;
}

.contact-sendContent {
  display: inline-block;
  padding: 6px 10px;
  background-color: #f5f5f5;
  border-radius: 6px;
  word-break: break-word;
  white-space: pre-wrap;
  max-width: 100%;
  margin: 0 0 0 6px;
}
</style>