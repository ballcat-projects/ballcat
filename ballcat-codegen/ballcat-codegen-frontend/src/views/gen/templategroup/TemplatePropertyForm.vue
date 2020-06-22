<template>
  <a-form @submit="handleSubmit" :form="form">
    <a-form-item v-if="formAction === this.FORM_ACTION.UPDATE" style="display: none">
      <a-input v-decorator="['id']" />
    </a-form-item>

    <a-form-item v-if="formAction === this.FORM_ACTION.CREATE" style="display: none">
      <a-input v-decorator="['groupId']" />
    </a-form-item>

    <a-form-item label="标题" :labelCol="labelCol" :wrapperCol="wrapperCol">
      <a-input placeholder="标题" v-decorator="['title']" />
    </a-form-item>

    <a-form-item label="属性键" :labelCol="labelCol" :wrapperCol="wrapperCol">
      <a-input placeholder="属性键" v-decorator="['propKey']" />
    </a-form-item>

    <a-form-item label="默认值" :labelCol="labelCol" :wrapperCol="wrapperCol">
      <a-input placeholder="默认值" v-decorator="['defaultValue']" />
    </a-form-item>

    <a-form-item label="是否必填" :labelCol="labelCol" :wrapperCol="wrapperCol">
      <a-select placeholder="必填，1：是，0：否" v-decorator="['required']">
        <a-select-option :value="1">是</a-select-option>
        <a-select-option :value="0">否</a-select-option>
      </a-select>
    </a-form-item>

    <a-form-item label="备注信息" :labelCol="labelCol" :wrapperCol="wrapperCol">
      <a-textarea placeholder="备注信息" v-decorator="['remarks']" />
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
import { addObj, putObj } from '@/api/gen/templateproperty'

export default {
  name: 'TemplatePropertyFormPage',
  mixins: [FormPageMixin],
  data() {
    return {
      addObj: addObj,
      putObj: putObj,

      // 校验配置
      decoratorOptions: {}
    }
  },
  methods: {
    createdFormCallback(argument) {
      if (this.formAction === this.FORM_ACTION.CREATE) {
        this.$nextTick(function() {
          this.form.setFieldsValue({ groupId: argument })
        })
      }
    }
  }
}
</script>
<style scoped>
.ant-form-item {
  margin-bottom: 8px;
}
</style>
