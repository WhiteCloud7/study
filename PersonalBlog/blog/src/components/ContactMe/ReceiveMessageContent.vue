<template>
  <div class="contact-receive">
    <el-avatar class="contact-receiveAvatar"></el-avatar>
    <p class="contact-receiveContent">{{message}}</p>
  </div>
</template>
<style>
.contact-receive{
  min-width: 1em;
  max-width: 600px;
  min-height: 1em;
  max-height: 20em;
  margin-left: 240px;
  display: flex;
  flex-direction: row-reverse;
  margin-top: 8px;
  padding: 2px;
}
.contact-receiveContent{
  width: 90%;
  min-height: 1em;
  max-height: 20em;
  margin: 0 10px 0 0;
}
</style>