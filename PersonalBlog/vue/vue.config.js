const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  publicPath: process.env.NODE_ENV === 'production'
      ? '/PersonalBlog/' // 生产环境下的基础路径，可按需修改
      : '/' // 开发环境下的基础路径
})