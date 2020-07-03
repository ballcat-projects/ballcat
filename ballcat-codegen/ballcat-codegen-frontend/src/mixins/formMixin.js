import pick from 'lodash.pick'

export default {
  data() {
    return {
      form: this.$form.createForm(this),

      formAction: this.FORM_ACTION.NONE,
      labelCol: { lg: { span: 3 }, sm: { span: 3 } },
      wrapperCol: { lg: { span: 8 }, sm: { span: 19 } },
      decoratorOptions: {},
      displayData: {
        createTime: '',
        updateTime: ''
      },
      // 提交按钮防止重复提交
      submitLoading: false,

      reqFunctions: {
        create: function() {},
        update: function() {}
      }
    }
  },
  methods: {
    // ============ 添加 ======================
    buildCreatedForm(argument) {
      this.form.resetFields()
      this.formAction = this.FORM_ACTION.CREATE
      // 钩子函数 处理某些页面定制需求
      this.createdFormCallback(argument)
    },
    /*eslint-disable*/
    createdFormCallback(argument) {
      // 组件复写此方法 完成添加之前的事件
    },
    /**
     * ==============表单数据回显===============
     * @param data 回显数据
     * @param needReset 回显前是否清空现有数据，变更部分数据时传递 false
     */
    fillFormData: function(data, needReset = false) {
      // 延迟加载 避免隐藏展示元素时出现的bug
      setTimeout(() => {
        // 获取仅展示元素
        this.displayData = pick(data, Object.keys(this.displayData))
        // 移除所有不用的元素，否则会抛出异常
        const fromData = pick(data, Object.keys(this.form.getFieldsValue()))
        this.$nextTick(function() {
          needReset && this.form.resetFields()
          this.form.setFieldsValue(fromData)
        })
      }, 0)
    },
    // ============ 修改 ======================
    buildUpdatedForm(record, argument) {
      let that = this
      that.formAction = that.FORM_ACTION.UPDATE
      that.echoDataProcess(record)
      this.fillFormData(record, true)
      this.updatedFormCallback(argument)
    },
    echoDataProcess(data) {
      // 对准备回显的数据做预处理
      return data
    },
    /*eslint-disable*/
    updatedFormCallback(argument) {
      // 组件复写此方法 完成修改之后的事件
    },

    // ============ 提交 ======================
    handleSubmit(e) {
      // 阻止 submit 事件的默认行为
      e.preventDefault()
      // 钩子函数 处理提交之前处理的事件
      this.beforeStartSubmit()
      const reqFunction = this.reqFunctions[this.formAction]
      this.form.validateFields((err, values) => {
        if (!err) {
          this.submitLoading = true
          reqFunction(this.submitDataProcess(values))
            .then(res => {
              if (res.code === 200) {
                this.$message.success(res.msg)
                this.submitSuccess(res)
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
      })
    },
    /*eslint-disable*/
    beforeStartSubmit(record) {
      // 组件复写此方法 提交之前处理的事件
    },
    submitDataProcess(data) {
      // 在此处理表单提交的数据
      return data
    },
    /*eslint-disable*/
    submitSuccess(res) {
      // 提交表单成功的回调函数
    },
    /*eslint-disable*/
    submitError(res) {
      // 提交表单失败的回调函数
    }
  }
}
