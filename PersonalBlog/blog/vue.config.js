const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
  lintOnSave: false, //禁用ESList
  transpileDependencies: true,
  devServer: {
    client: {
      overlay: {
        warnings: false,
        errors: false
      }
    }
  },
  parallel: false,  // 关闭多线程，试试解决 thread-loader 相关错误
  configureWebpack: {
    ignoreWarnings: [
      warning => true // 忽略所有警告（不过语法错误还是会报错）
    ]
  }
})
