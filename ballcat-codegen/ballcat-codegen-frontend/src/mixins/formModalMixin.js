import FormMixin from './formMixin'

export default {
  mixins: [FormMixin],
  data() {
    return {
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
    show() {
      this.visible = true
      this.submitLoading = false
    },
    add() {
      this.buildCreatedForm()
      this.show()
    },
    update(record) {
      this.buildUpdatedForm(record)
      this.show()
    },
    /*eslint-disable*/
    submitSuccess(res) {
      this.$emit('reloadPageTable', false)
      this.handleClose()
    },
    /*eslint-disable*/
    handleClose(e) {
      this.visible = false
      this.submitLoading = false
    }
  }
}
