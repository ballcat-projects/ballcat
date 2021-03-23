<template>
  <a-modal
    title="属性配置"

    cancel-text="取消"
    :visible="visible"
    :confirm-loading="submitLoading"
    :width="900"
    @cancel="handleClose"
  >
    <template slot="footer">
      <a-button @click="handleClose">
        取消
      </a-button>
      <a-button @click="previewCode" v-if="'single'===this.type" >
        预览
      </a-button>
      <a-button type="primary" @click="handleOk">
        确认
      </a-button>
    </template>
    <a-modal
        :title="preview.title"
        width="50%"
        :visible="preview.open"
        :footer="null"
        @cancel="closePreviewCode"

    >
      <a-tabs
          default-active-key="1"
          tab-position="left"
      >
        <a-tab-pane v-for="(value,key) in preview.data" :key="key" :tab="key">
          <pre><code class="hljs" v-html="highlightedCode(value,key)"></code></pre>
        </a-tab-pane>
      </a-tabs>
    </a-modal>


    <a-form :form="form" @submit="handleOk">
      <a-row :gutter="6">
        <a-col :span="10">
          <a-form-item :label-col="labelCol" :wrapper-col="wrapperCol" label="模板组">
            <a-select
              @change="onTemplateGroupChange"
              v-decorator="[
                'templateGroupId',
                {
                  initialValue: templateGroupIdInitValue,
                  rules: [{ required: true, message: '必须选择一个模板组' }]
                }
              ]"
            >
              <a-select-option v-for="item in templateGroupSelectData" :key="Number(item.value)">
                {{ item.name }}
              </a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item label="生成文件选择">
            <div :style="{ borderBottom: '1px solid #E9E9E9' }">
              <a-checkbox :indeterminate="indeterminate" :checked="checkAll" @change="onCheckAllChange">
                Check all
              </a-checkbox>
            </div>
            <a-checkbox-group
              v-decorator="['templateFileIds', { initialValue: templateFileIds }]"
              @change="onTemplateFileIdsChange"
              style="width: 100%;"
            >
              <a-row>
                <template v-for="item in templateFiles">
                  <a-col :span="12">
                    <a-checkbox :value="item.directoryEntryId">
                      {{ item.title }}
                    </a-checkbox>
                  </a-col>
                </template>
              </a-row>
            </a-checkbox-group>
          </a-form-item>
        </a-col>
        <a-col :span="14">
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
                {
                  rules: [{ required: item.required === 1, message: item.title + '不能为空' }],
                  initialValue: item.defaultValue
                }
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
import { getList as getTemplateFiles } from '@/api/gen/templateinfo'
import { preview,generate } from '@/api/gen/generate'

import hljs from "highlight.js"
import "highlight.js/styles/github-gist.css";

hljs.registerLanguage("java", require("highlight.js/lib/languages/java"));
hljs.registerLanguage("xml", require("highlight.js/lib/languages/xml"));
hljs.registerLanguage("html", require("highlight.js/lib/languages/xml"));
hljs.registerLanguage("vue", require("highlight.js/lib/languages/xml"));
hljs.registerLanguage("javascript", require("highlight.js/lib/languages/javascript"));
hljs.registerLanguage("sql", require("highlight.js/lib/languages/sql"));

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
      tableNames: [],
      templateGroupSelectData: [],
      templateGroupIdInitValue: null,

      properties: [],

      templateFiles: [],
      templateFileIds: [],
      checkedList: [],
      indeterminate: false,
      checkAll: true,
      // 预览参数
      preview: {
        open: false,
        title: "代码预览",
        data: {},
      },
      type:''
    }
  },
  mounted() {
    getSelectData().then(res => {
      const data = res.data
      this.templateGroupSelectData = res.data
      if (data && data.length > 0) {
        this.templateGroupIdInitValue = Number(data[0].value)
      }
    })
  },
  methods: {
    show(tableNames,type) {
      this.visible = true
      this.submitLoading = false
      this.type=type
      this.tableNames = tableNames
    },
    onTemplateGroupChange(templateGroupId) {
      getProperties(templateGroupId).then(res => {
        this.properties = res.data
      })
      getTemplateFiles(templateGroupId).then(res => {
        this.templateFiles = res.data
        this.templateFileIds = this.templateFiles.map(x => x.directoryEntryId)
      })
    },
    /** 高亮显示 */
    highlightedCode(code, key) {
      var language =key.substring(key.lastIndexOf(".")+1)
      const result = hljs.highlight(language, code || "", true);
      return result.value || '&nbsp;';
    },

    previewCode(){
        this.form.validateFields((err,values)=>{
          if (!err){
            this.submitLoading = true
            preview(this.dsName,this.submitDataProcess(values)).then(res=>{
              this.preview.data=res.data;
              this.preview.open=true;
            }).catch(()=>{
              this.$message.error('代码生成异常')
            }).finally(()=>{
              this.submitLoading = false
            })
          }
        })
    },
    closePreviewCode(){
      this.preview.open=false;
      this.preview.data={}
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
    },
    onTemplateFileIdsChange(checkedList) {
      this.indeterminate = !!checkedList.length && checkedList.length < this.templateFileIds.length
      this.checkAll = checkedList.length === this.templateFileIds.length
    },
    onCheckAllChange(e) {
      this.indeterminate = false
      this.checkAll = e.target.checked
      this.fillFormData({ templateFileIds: this.checkAll ? this.templateFileIds : [] })
    }
  }
}
</script>
<style scoped>
.ant-form-item {
  margin-bottom: 8px;
}
</style>
