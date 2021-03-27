<template>
  <a-modal
    title="节点删除"
    :visible="visible"
    :confirm-loading="submitLoading"
    @ok="handleSubmit"
    @cancel="handleClose"
  >
    <a-form :form="form">
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
import { PopUpFormMixin } from '@/mixins'
import { delObj } from '@/api/gen/templatedirectoryentry'
export default {
  name: 'TemplatePropertyPage',
  mixins: [PopUpFormMixin],
  data() {
    return {
      id: '',
      formAction: 'delete',
      reqFunctions: {
        delete: this.deleteFunction
      }
    }
  },
  methods: {
    deleteFunction: function(data) {
      return delObj(this.id, data.mode)
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
    }
  }
}
</script>
