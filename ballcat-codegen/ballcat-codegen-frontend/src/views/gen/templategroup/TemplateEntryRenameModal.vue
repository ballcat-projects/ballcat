<template>
  <a-modal title="重命名" :visible="visible" :confirm-loading="submitLoading" @ok="handleSubmit" @cancel="handleClose">
    <a-form :form="form">
      <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="文件名">
        <a-input
          v-decorator="['fileName', { rules: [{ required: true, message: '重命名文件名不能为空' }] }]"
          placeholder="请输入你的重命名文件名"
        />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script>
import { FormModalMixin } from '@/mixins'
import { rename } from '@/api/gen/templatedirectoryentry'

export default {
  name: 'TemplatePropertyPage',
  mixins: [FormModalMixin],
  data() {
    return {
      id: '',
      formAction: 'rename',
      reqFunctions: {
        rename: this.renameFunction
      }
    }
  },
  methods: {
    renameFunction: function(data) {
      return rename(this.id, data.fileName)
    },
    submitSuccess() {
      // 提交表单成功的回调函数
      this.$parent.treeLoad()
      this.handleClose()
    },
    show(data) {
      this.id = data.id
      this.visible = true
      this.submitLoading = false

      this.fillFormData({ fileName: data.fileName })
    }
  }
}
</script>
