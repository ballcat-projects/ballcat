<template>
  <div>
    <codemirror v-model="code" :options="cmOptions"></codemirror>
  </div>
</template>

<script>
// require component
import { codemirror } from 'vue-codemirror'
// require styles
import 'codemirror/lib/codemirror.css'
import 'codemirror/theme/dracula.css'
import 'codemirror/mode/velocity/velocity.js'

export default {
  name: 'TemplateEdit',
  components: {
    codemirror
  },
  data() {
    return {
      code: 'const a = 10',
      cmOptions: {
        // codemirror options
        tabSize: 4,
        mode: 'velocity',
        theme: 'dracula',
        lineNumbers: true,
        line: true
        // more codemirror options, 更多 codemirror 的高级配置...
      }
    }
  }
}
</script>

<style>
.CodeMirror {
  height: 600px !important;
}
</style>
