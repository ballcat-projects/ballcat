<template>
  <a-modal title="重命名" :visible="visible" :confirm-loading="confirmLoading" @ok="handleOk" @cancel="handleClose">
    <a-form :form="form" @submit="handleOk">
      <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="文件名">
        <a-input
          v-decorator="['title', { rules: [{ required: true, message: '重命名文件名不能为空' }] }]"
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
      rename: rename,
      confirmLoading: false
    }
  },
  methods: {
    echoDataProcess(data) {
      this.id = data.id
    },
    handleOk() {
      // 钩子函数 处理提交之前处理的事件
      if (!this.form.getFieldValue('title')) {
        return
      }
      rename(this.id, this.form.getFieldValue('title'))
        .then(res => {
          if (res.code === 200) {
            this.$message.success(res.msg)
            this.submitSuccess(res)
            this.$parent.pageLoad()
          } else {
            this.$message.error(res.msg)
          }
        })
        .catch(error => {
          this.$message.error(error.response.data.msg)
        })
        .finally(() => {
          this.submitLoading = false
        })
    }
  }
}
</script>
