<template>
  <a-modal :title="title" :visible="visible" :confirm-loading="submitLoading" @ok="handleOk" @cancel="handleClose">
    <a-form :form="form" @submit="handleOk">
      <a-form-item v-if="formAction === this.FORM_ACTION.UPDATE" style="display: none">
        <a-input v-decorator="['id']" />
      </a-form-item>
      <a-form-item v-if="formAction === 'copy'" :label-col="labelCol" :wrapper-col="wrapperCol" label="源模板组">
        <span>{{ resourceGroupName }}</span>
      </a-form-item>
      <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="名称">
        <a-input
          v-decorator="['name', { rules: [{ required: true, message: '模板组名称不能为空' }] }]"
          placeholder="请输入模板组名称"
        />
      </a-form-item>
      <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="备注信息">
        <a-textarea v-decorator="['remarks']" placeholder="请输入模板组备注信息" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script>
import { FormModalMixin } from '@/mixins'
import { addObj, putObj } from '@/api/gen/templategroup'

export default {
  mixins: [FormModalMixin],
  data() {
    return {
      reqFunctions: {
        create: addObj,
        update: putObj,
        copy: function(data) {
          return addObj(data)
        }
      },

      resourceGroupId: '',
      resourceGroupName: ''
    }
  },
  methods: {
    handleOk(e) {
      this.handleSubmit(e)
    },
    copy(record, title) {
      this.form.resetFields()
      this.formAction = 'copy'
      this.resourceGroupId = record.id
      this.resourceGroupName = record.name
      this.show(title)
    }
  }
}
</script>
