import FormMixin from './formMixin'

export default {
  mixins: [FormMixin],
  data() {
    return {
      // 标题
      title: '',
      visible: false,
      labelCol: {
        xs: { span: 8 },
        sm: { span: 24 },
        lg: { span: 5 }
      },
      wrapperCol: {
        xs: { span: 16 },
        sm: { span: 24 },
        lg: { span: 17 }
      }
    }
  },
  methods: {
    show(attributes) {
      this.title = attributes.title
      this.visible = true
      this.submitLoading = false
    },
    add(attributes) {
      this.buildCreatedForm()
      this.show(attributes)
    },
    update(record, attributes) {
      this.buildUpdatedForm(record, attributes)
      this.show(attributes)
    },
    /*eslint-disable*/
    submitSuccess(res) {
      this.$emit('reload-page-table', false)
      this.handleClose()
    },
    /*eslint-disable*/
    handleClose(e) {
      this.visible = false
      this.submitLoading = false
      this.form.resetFields()
    }
  }
}
