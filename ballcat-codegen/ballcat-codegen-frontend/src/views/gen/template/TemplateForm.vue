<template>
  <a-form @submit="handleSubmit" :form="form">
    <a-form-item v-if="formAction === this.FORM_ACTION.UPDATE" style="display: none">
      <a-input v-decorator="['id']" />
    </a-form-item>

    <a-form-item label="应用ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
      <a-input placeholder="应用ID" v-decorator="['appId']" />
    </a-form-item>

    <a-form-item label="模板名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
      <a-input placeholder="模板名称" v-decorator="['name']" />
    </a-form-item>

    <a-form-item label="模板引擎" :labelCol="labelCol" :wrapperCol="wrapperCol">
      <a-input placeholder="模板引擎类型 1：velocity" v-decorator="['engineType']" />
    </a-form-item>

    <a-form-item label="描述" :labelCol="labelCol" :wrapperCol="wrapperCol">
      <a-textarea placeholder="描述" v-decorator="['remarks']" />
    </a-form-item>

    <a-form-item label="模板内容" :labelCol="labelCol" :wrapperCol="wrapperCol">
      <codemirror v-model="code" :options="cmOptions" style="line-height: 1.5"></codemirror>
    </a-form-item>

    <div v-show="formAction === this.FORM_ACTION.UPDATE">
      <a-form-item label="创建时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
        <span>{{ displayData.createTime }}</span>
      </a-form-item>
      <a-form-item label="修改时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
        <span>{{ displayData.updateTime }}</span>
      </a-form-item>
    </div>

    <a-form-item :wrapperCol="{ offset: 3 }">
      <a-button htmlType="submit" type="primary" :loading="submitLoading">提交</a-button>
      <a-button style="margin-left: 8px" @click="backToPage(false)">取消</a-button>
    </a-form-item>
  </a-form>
</template>

<script>
import { FormPageMixin } from '@/mixins'
import { addObj, putObj } from '@/api/gen/gentemplate'

// codemirror
import { codemirror } from 'vue-codemirror'
import 'codemirror/lib/codemirror.css'
import 'codemirror/theme/dracula.css'
import 'codemirror/mode/velocity/velocity.js'

export default {
  name: 'TemplateFormPage',
  mixins: [FormPageMixin],
  components: { codemirror },
  data() {
    return {
      addObj: addObj,
      putObj: putObj,
      labelCol: { lg: { span: 2 }, sm: { span: 2 } },
      wrapperCol: { lg: { span: 22 }, sm: { span: 22 } },

      // 校验配置
      decoratorOptions: {},

      code: '',
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
  },
  methods: {
    echoDataProcess(data) {
      this.code = data.content
      return data
    },
    submitDataProcess(data) {
      data.content = this.code
      return data
    }
  }
}
</script>
<style scoped>
.ant-form-item {
  margin-bottom: 8px;
}
</style>
