import FormMixin from './formMixin'

export default {
  mixins: [FormMixin],
  data () {
    return {
      labelCol: { lg: { span: 3 }, sm: { span: 3 } },
      wrapperCol: { lg: { span: 8 }, sm: { span: 19 } },
    }
  },
  methods: {
    add (argument) {
      this.buildCreatedForm(argument)
    },
    update (record, argument) {
      this.buildUpdatedForm(record, argument)
    },
    submitSuccess (res){
      this.backToPage(true);
    },
    backToPage (needRefresh) {
      this.$emit('backToPage', needRefresh)
    }
  }
}