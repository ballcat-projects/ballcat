<template>
  <a-modal
    title="属性配置"
    ok-text="确认"
    cancel-text="取消"
    :visible="visible"
    :confirm-loading="submitLoading"
    @ok="handleOk"
    @cancel="handleClose"
    width="60%"
  >
    <a-form :form="form" @submit="handleOk">
      <a-row :gutter="16">
        <a-col :span="8">
          <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="模板组">
            <a-select
              @change="onTemplateGroupChange"
              v-decorator="[
                'templateGroupId',
                {
                  initialValue: defaultTemplateGroupId,
                  rules: [{ required: true, message: '必须选择一个模板组' }]
                }
              ]"
            >
              <a-select-option v-for="item in templateGroupSelectData" :key="Number(item.value)">
                {{ item.name }}
              </a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="16">
          <a-divider orientation="left">
            系统属性
          </a-divider>
          <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="表前缀">
            <a-input v-decorator="['tablePrefix']" placeholder="填写则会将表名的前缀截取后，再生成类名" />
          </a-form-item>
          <a-divider orientation="left">
            自定义属性
          </a-divider>
          <a-form-item v-for="item in properties" :label-col="labelCol" :wrapper-col="wrapperCol" :label="item.propKey">
            <a-input
              v-decorator="[
                'genProperties.' + item.propKey,
                { rules: [{ required: item.required === 1, message: item.title + '不能为空' }] }
              ]"
              :placeholder="'请输入' + item.title"
            />
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>

<script>
import { FormModalMixin } from '@/mixins'
import { getSelectData } from '@/api/gen/templategroup'
import { getProperties } from '@/api/gen/templateproperty'
import { generate } from '@/api/gen/generate'

export default {
  name: 'GenerateModal',
  mixins: [FormModalMixin],
  props: {
    dsName: String
  },
  data() {
    return {
      labelCol: {
        xs: { span: 12 },
        sm: { span: 24 },
        lg: { span: 6 }
      },
      wrapperCol: {
        xs: { span: 12 },
        sm: { span: 24 },
        lg: { span: 18 }
      },
      defaultTemplateGroupId: 1,
      tableNames: [],
      templateGroupSelectData: [],
      properties: []
    }
  },
  mounted() {
    getSelectData().then(res => {
      this.templateGroupSelectData = res.data
    })
    this.onTemplateGroupChange(this.defaultTemplateGroupId)
  },
  methods: {
    show(tableNames) {
      this.visible = true
      this.submitLoading = false

      this.tableNames = tableNames
    },
    onTemplateGroupChange(templateGroupId) {
      getProperties(templateGroupId).then(res => {
        this.properties = res.data
      })
    },
    handleOk() {
      // 钩子函数 处理提交之前处理的事件
      this.form.validateFields((err, values) => {
        if (!err) {
          this.submitLoading = true
          generate(this.dsName, this.submitDataProcess(values))
            .then(fileBlob => {
              const blob = new Blob([fileBlob])
              const fileName = 'BallCat-CodeGen.zip'
              const eLink = document.createElement('a')
              if ('download' in eLink) {
                // 非IE下载
                eLink.download = fileName
                eLink.style.display = 'none'
                eLink.href = URL.createObjectURL(blob)
                document.body.appendChild(eLink)
                eLink.click()
                URL.revokeObjectURL(eLink.href) // 释放URL 对象
                document.body.removeChild(eLink)
              } else {
                // IE10+下载
                navigator.msSaveBlob(blob, fileName)
              }
            })
            .catch(() => {
              this.$message.error('代码生成异常')
            })
            .finally(() => {
              this.submitLoading = false
            })
        }
      })
    },
    submitDataProcess(data) {
      data.tableNames = this.tableNames
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
