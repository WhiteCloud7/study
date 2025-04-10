<template>
  <a href="https://github.com/WhiteCloud7/study" class="href">
    GitHub地址：<em>https://github.com/WhiteCloud7/study</em>
  </a><br>
  <div class="contact-info">
    <span class="contact-item">QQ:2334646904</span>
    <span class="contact-item" style="margin-left: 30px;">WeChat:cCloud7777777</span>
  </div>
</template>

<style>
.href {
  text-decoration: none;
  color: #3a79d0;
  font-weight: 500;
  transition: color 0.3s ease, transform 0.3s ease;
}
.href:hover {
  color: #1e4d92;
  transform: scale(1.05);
  text-decoration: underline;
}

.href em {
  font-style: italic;
  color: #666;
  transition: color 0.3s ease;
}

.href:hover em {
  color: #1e4d92;
}

.contact-info {
  display: inline;
}

.contact-item {
  color: #3434c5;
  font-weight: 500;
  transition: color 0.3s ease;
}

.contact-item:hover {
  color: #1e4d92;
}
</style>
