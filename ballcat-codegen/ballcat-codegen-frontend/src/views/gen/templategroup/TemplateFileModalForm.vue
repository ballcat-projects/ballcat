<template>
  <a-modal
    :title="title"
    :visible="visible"
    :confirm-loading="submitLoading"
    :ok-text="formAction === this.FORM_ACTION.CREATE ? '填写文件内容' : '保存'"
    @ok="handleOk"
    @cancel="handleClose"
  >
    <a-form :form="form" :labelCol="labelCol" :wrapperCol="wrapperCol">
      <template v-if="formAction === this.FORM_ACTION.CREATE">
        <a-form-item style="display: none">
          <a-input v-decorator="['groupId']" />
        </a-form-item>
        <a-form-item style="display: none">
          <a-input v-decorator="['parentId']" />
        </a-form-item>
        <a-form-item style="display: none">
          <a-input v-decorator="['type']" />
        </a-form-item>
        <a-form-item label="父目录">
          <span> {{ parentFileName }}</span>
        </a-form-item>
      </template>
      <a-form-item v-if="formAction === this.FORM_ACTION.UPDATE" style="display: none">
        <a-input v-decorator="['id']" />
      </a-form-item>

      <a-form-item label="文件名">
        <a-input placeholder="请输入文件名" v-decorator="['fileName']" />
      </a-form-item>
      <a-form-item label="标题" :labelCol="labelCol" :wrapperCol="wrapperCol">
        <a-input placeholder="标题" v-decorator="['templateInfo.title']" />
      </a-form-item>
      <a-form-item label="引擎" :labelCol="labelCol" :wrapperCol="wrapperCol">
        <a-select v-decorator="['templateInfo.engineType', { initialValue: 1 }]">
          <a-select-option :value="1">velocity</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
        <a-textarea placeholder="备注" v-decorator="['templateInfo.remarks']" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script>
import { PopUpFormMixin } from '@/mixins'
import { addObj, putObj } from '@/api/gen/templatedirectoryentry'
import { getObj } from '@/api/gen/templateinfo'

export default {
  name: 'TemplateEntryAddModalForm',
  mixins: [PopUpFormMixin],
  data() {
    return {
      reqFunctions: {
        create: addObj,
        update: putObj
      },

      parentFileName: ''
    }
  },
  methods: {
    add(attributes) {
      this.buildCreatedForm()
      this.parentFileName = attributes.parentFileName
      this.fillFormData(attributes.formData)
      this.show(attributes)
    },
    update(record, attributes) {
      getObj(record.id).then(res => {
        if (res.code === 200) {
          const fileInfo = res.data
          const formData = {
            id: record.id,
            fileName: record.fileName,
            templateInfo: {
              title: fileInfo.title,
              engineType: fileInfo.engineType,
              remarks: fileInfo.remarks
            }
          }
          this.buildUpdatedForm(formData, attributes)
          this.show(attributes)
        }
      })
    },
    handleOk(e) {
      if (this.formAction === this.FORM_ACTION.UPDATE) {
        // 如果是修改，则直接提交表单数据
        this.handleSubmit(e)
      } else {
        let data = {}
        this.form.validateFields((err, values) => {
          if (!err) {
            data = this.submitDataProcess(values)
          }
        })
        // 如果是新建，则提交表单数据到父组件，用于编写文件内容
        this.$emit('create-entry-file', data)
        this.handleClose()
      }
    }
  }
}
</script>

<style scoped></style>
