/*const SERVER_URL = 'http://ballcat-admin:7777'*/
const SERVER_URL = 'http://192.168.1.253:7777'
const vueConfig = {
  css: {
    loaderOptions: {
      less: {
        javascriptEnabled: true,
        modifyVars: {
          'primary-color': '#1DA57A',
          'link-color': '#1DA57A',
          'border-radius-base': '2px'
        }
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
