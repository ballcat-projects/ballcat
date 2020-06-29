<template>
  <a-modal title="节点删除" :visible="visible" :confirm-loading="confirmLoading" @ok="handleOk" @cancel="handleClose">
    <a-form :form="form" @submit="handleOk">
      <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="删除">
        <a-radio-group v-decorator="['mode', { initialValue: 1, rules: [{ required: true, message: '必填内容' }] }]">
          <a-radio :value="1">
            删除本身(子节点向上移动)
          </a-radio>
          <a-radio :value="2">
            删除所有
          </a-radio>
        </a-radio-group>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script>
import { FormModalMixin } from '@/mixins'
import { delObj } from '@/api/gen/templatedirectoryentry'
export default {
  name: 'TemplatePropertyPage',
  mixins: [FormModalMixin],
  data() {
    return {
      id: '',
      confirmLoading: false
    }
  },
  methods: {
    echoDataProcess(data) {
      this.id = data.id
    },
    handleOk() {
      // 钩子函数 处理提交之前处理的事件
      delObj(this.id, this.form.getFieldValue('mode'))
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
