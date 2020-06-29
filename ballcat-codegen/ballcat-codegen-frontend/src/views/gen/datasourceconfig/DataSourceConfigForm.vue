<template>
  <a-form @submit="handleSubmit" :form="form">
    <a-form-item v-if="formAction === this.FORM_ACTION.UPDATE" style="display: none">
      <a-input v-decorator="['id']" />
    </a-form-item>

    <a-form-item label="数据源名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
      <a-input placeholder="数据源名称" v-decorator="['name']" />
    </a-form-item>

    <a-form-item label="用户名" :labelCol="labelCol" :wrapperCol="wrapperCol">
      <a-input placeholder="用户名" v-decorator="['username']" />
    </a-form-item>

    <a-form-item
      label="原密码"
      :labelCol="labelCol"
      :wrapperCol="wrapperCol"
      v-if="formAction === this.FORM_ACTION.UPDATE"
    >
      <a-input v-model="password" disabled />
    </a-form-item>

    <a-form-item label="密码" :labelCol="labelCol" :wrapperCol="wrapperCol">
      <template #extra v-if="formAction === this.FORM_ACTION.UPDATE">
        <p style="color: red">注意：如果需要修改密码则填写此处，不修改请置空</p>
      </template>
      <a-input placeholder="密码" v-decorator="['pass']" />
    </a-form-item>

    <a-form-item label="连接地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
      <a-textarea placeholder="连接地址" v-decorator="['url']" :rows="4" />
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
import { addObj, putObj } from '@/api/gen/datasourceconfig'

export default {
  name: 'DataSourceConfigFormPage',
  mixins: [FormPageMixin],
  data() {
    return {
      reqFunctions: {
        create: addObj,
        update: putObj
      },

      // 校验配置
      decoratorOptions: {},

      password: ''
    }
  },
  methods: {
    echoDataProcess(data) {
      this.password = data.password
      return data
    }
  }
}
</script>
