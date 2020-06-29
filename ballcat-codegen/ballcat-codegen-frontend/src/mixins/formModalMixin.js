import FormMixin from './formMixin'

export default {
  mixins: [FormMixin],
  data() {
    return {
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
    show(title) {
      this.visible = true
      this.submitLoading = false

      this.title = title
    },
    add(title) {
      this.buildCreatedForm()
      this.show(title)
    },
    update(record, title) {
      this.buildUpdatedForm(record)
      this.show(title)
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
