import Vue from 'vue'
import {
  Button,
  Layout,
  Icon,
  Menu,
  Table,
  Form,
  Card,
  Row,
  Input,
  Col,
  Divider,
  Popconfirm,
  Select,
  Tree,
  Result,
  Dropdown,
  message,
  Breadcrumb,
  Modal,
  Radio,
  Descriptions,
  Checkbox,
  Tooltip,
  Tabs
} from 'ant-design-vue'
import App from './App.vue'
import router from './router'
import store from './store'

Vue.config.productionTip = false

Vue.use(Button)
Vue.use(Layout)
Vue.use(Icon)
Vue.use(Menu)
Vue.use(Table)
Vue.use(Form)
Vue.use(Card)
Vue.use(Row)
Vue.use(Input)
Vue.use(Col)
Vue.use(Divider)
Vue.use(Popconfirm)
Vue.use(Select)
Vue.use(Tree)
Vue.use(Result)
Vue.use(Dropdown)
Vue.use(Breadcrumb)
Vue.use(Modal)
Vue.use(Radio)
Vue.use(Checkbox)
Vue.use(Descriptions)
Vue.use(Tooltip)
Vue.use(Tabs)
Vue.prototype.$message = message

Vue.prototype.FORM_ACTION = {
  NONE: '',
  CREATE: 'create',
  UPDATE: 'update'
}

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
