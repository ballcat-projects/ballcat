const SERVER_URL = 'http://ballcat-admin:7777'

const vueConfig = {
  css: {
    loaderOptions: {
      less: {
        javascriptEnabled: true
      }
    }
  },
  devServer: {
    port: 8000,
    proxy: {
      '^/api': {
        target: SERVER_URL,
        changeOrigin: true
      }
    }
  },

  // Change build paths to make them Maven compatible
  // see https://cli.vuejs.org/config/
  outputDir: 'target/dist',
  assetsDir: 'static'
}

module.exports = vueConfig
