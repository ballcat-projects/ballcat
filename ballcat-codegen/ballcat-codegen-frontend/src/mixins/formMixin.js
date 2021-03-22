import pick from 'lodash.pick'

export default {
  data() {
    return {
      // 当前表单对象
      form: this.$form.createForm(this),

      // 标签和数值框布局
      labelCol: { lg: { span: 3 }, sm: { span: 3 } },
      wrapperCol: { lg: { span: 8 }, sm: { span: 19 } },

      // v-decorator 属性
      decoratorOptions: {},

      // 只显示的表单数据
      displayData: {
        createTime: '',
        updateTime: ''
      },

      // 提交按钮防止重复提交
      submitLoading: false,

      // 表单行为
      formAction: this.FORM_ACTION.NONE,
      // 请求方法，属性key为表单行为，value为对应请求方法（返回值为一个promise对象）
      reqFunctions: {
        create: function() {},
        update: function() {}
      }
    }
  },
  methods: {
    /**
     * 构建新建型表单
     * @param argument 额外参数，用于透传到表单构建完成的回调函数中
     */
    buildCreatedForm(argument) {
      this.form.resetFields()
      this.formAction = this.FORM_ACTION.CREATE
      // 钩子函数 处理某些页面定制需求
      this.createdFormCallback(argument)
    },

    /**
     * 构建新建型表单成功后的回调方法
     * 默认无行为，组件可复写此方法 完成添加之前的事件
     */
    /*eslint-disable*/
    createdFormCallback(argument) {},

    /**
     * 表单数据回填
     * @param data 回填数据
     * @param needReset 回填前是否清空现有数据，默认不请空，增量回填
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

    /**
     * 构建 => 修改型表单
     * @param record 回显数据
     * @param argument 额外参数，用于透传到表单构建完成的回调函数中
     */
    buildUpdatedForm(record, argument) {
      let that = this
      that.formAction = that.FORM_ACTION.UPDATE
      that.echoDataProcess(record)
      this.fillFormData(record, true)
      this.updatedFormCallback(argument)
    },

    /**
     * 钩子函数，回显数据处理
     * 默认无操作，子组件可重写此方法，拿到回显数据，对回显数据做处理，或者从回显数据中取值
     * @param data 回显数据
     */
    /*eslint-disable*/
    echoDataProcess(data) {},

    /*eslint-disable*/
    updatedFormCallback(argument) {
      // 组件复写此方法 完成修改之后的事件
    },

    /**
     * 表单提交处理函数
     * @param e event
     */
    handleSubmit(e) {
      // 阻止 submit 事件的默认行为
      e.preventDefault()
      // 表单提交前事件
      this.beforeStartSubmit()
      // 根据表单行为，获取对应的请求方法
      const reqFunction = this.reqFunctions[this.formAction]
      // 表单校验，成功则进行提交
      this.form.validateFields((err, values) => {
        if (!err) {
          this.submitLoading = true
          reqFunction(this.submitDataProcess(values))
            .then(res => {
              if (res.code === 200) {
                this.$message.success(res.message)
                this.submitSuccess(res)
              } else {
                this.submitError(res)
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

    /**
     * 表单准备提交前的回调函数
     */
    beforeStartSubmit() {},

    /**
     * 表单提交数据处理函数
     * 子组件可复写此方法，在这里进行偷梁换柱
     * @param data 表单待提交数据
     * @returns {*} 真正的提交数据
     */
    submitDataProcess(data) {
      // 在此处理表单提交的数据
      return data
    },

    /**
     * 表单提交成功回调函数
     * 子组件可复写进行扩展
     * @param res 服务端返回值
     */
    /*eslint-disable*/
    submitSuccess(res) {
      // 提交表单成功的回调函数
    },

    /**
     * 表单提交失败的回调函数
     * 子组件可复写进行扩展
     * @param res 服务端返回值
     */
    submitError(res) {
      this.$message.error(res.message)
    }
  }
}
