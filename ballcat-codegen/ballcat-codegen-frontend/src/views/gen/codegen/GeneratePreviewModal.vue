<template>
  <a-modal
    :title="title"
    :width="950"
    :visible="visible"
    :footer="null"
    :centered="true"
    :bodyStyle="{ padding: 0 }"
    class="preview-modal"
    @cancel="handleClose"
  >
    <a-tabs>
      <a-tab-pane v-for="(value, key) in data" :key="key" :tab="key">
        <div style="padding-left: 8px; height: 800px; overflow-y: auto">
          <pre><code class="hljs" v-html="highlightedCode(value,key)"></code></pre>
        </div>
      </a-tab-pane>
    </a-tabs>
  </a-modal>
</template>
<script>
import hljs from 'highlight.js'
import 'highlight.js/styles/github-gist.css'

hljs.registerLanguage('java', require('highlight.js/lib/languages/java'))
hljs.registerLanguage('xml', require('highlight.js/lib/languages/xml'))
hljs.registerLanguage('html', require('highlight.js/lib/languages/xml'))
hljs.registerLanguage('vue', require('highlight.js/lib/languages/xml'))
hljs.registerLanguage('javascript', require('highlight.js/lib/languages/javascript'))
hljs.registerLanguage('sql', require('highlight.js/lib/languages/sql'))

export default {
  name: 'GeneratePreviewModal',
  data() {
    return {
      // 预览参数
      title: '代码预览',
      visible: false,
      data: {},
      defaultActiveKey: null
    }
  },
  methods: {
    /** 高亮显示 */
    highlightedCode(code, key) {
      const language = key.substring(key.lastIndexOf('.') + 1)
      const result = hljs.highlight(code || '', {
        language: language,
        ignoreIllegals: true
      })
      return result.value || '&nbsp;'
    },
    show(data) {
      this.visible = true
      this.data = data
    },
    handleClose() {
      this.visible = false
      this.data = {}
    }
  }
}
</script>

<style scoped>
.preview-modal >>> .ant-modal-header {
  padding: 8px 24px !important;
}
.preview-modal >>> .ant-modal-close-x {
  height: 40px;
  line-height: 40px;
}
.preview-modal >>> .ant-tabs-bar {
  margin: 0 !important;
}
.preview-modal >>> .ant-tabs-bar {
  margin: 0 !important;
}
.preview-modal >>> .ant-tabs-nav-container {
  line-height: 1;
}
.preview-modal >>> .ant-tabs-nav .ant-tabs-tab {
  margin: 0;
}

.preview-modal >>> .ant-tabs {
  line-height: 1.25;
}
</style>
