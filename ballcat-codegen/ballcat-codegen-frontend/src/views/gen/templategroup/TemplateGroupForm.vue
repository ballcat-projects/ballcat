<template>
  <div>
    <split-pane
      :min-percent="minPercent"
      :default-percent="defaultPercent"
      split="vertical"
      :style="splitPane"
      @resize="resize"
    >
      <template slot="paneL">
        <div class="treesetting-row" @contextmenu.prevent="onRightClickBox" :style="treesettingRow">
          <a-directory-tree
            v-model="checkedKeys"
            :expanded-keys="expandedKeys"
            :auto-expand-parent="autoExpandParent"
            :selected-keys="selectedKeys"
            :tree-data="treeData"
            :showIcon="true"
            :draggable="true"
            @expand="onExpand"
            @select="onSelect"
            @drop="onDrop"
            @dblclick="ondblclick"
            @rightClick="onRightClick"
          >
          </a-directory-tree>
          <a-menu :style="menuStyle" v-if="menuVisible">
            <template v-if="this.selectedNode">
              <a-menu-item key="1" :style="menuItemStyle" @click="renameModel">
                <a-icon type="edit" />
                重命名
              </a-menu-item>
              <a-menu-item key="2" :style="menuItemStyle" @click="removeEntry">
                <a-icon type="delete" />
                删除
              </a-menu-item>
            </template>
            <template v-if="!this.selectedNode || this.selectedNode.dataRef.type !== 2">
              <a-menu-item key="3" :style="menuItemStyle" @click="createdEntry(1)">
                <a-icon type="diff" />
                新建文件夹
              </a-menu-item>
              <a-menu-item key="4" :style="menuItemStyle" @click="createdEntry(2)">
                <a-icon type="file-add" />
                新建文件
              </a-menu-item>
            </template>
          </a-menu>
        </div>
      </template>
      <template slot="paneR" style="padding:0;">
        <div class="treesetting-row-leftbtn" @click="moveLeft" :style="leftbtnStyle">{{ leftHtml }}</div>
        <div class="treesetting-paneR">
          <div v-show="showTips">
            <a-descriptions title="使用说明">
              <a-descriptions-item label="文件名占位" :span="3">
                使用 {} 占位，使用时会替换为实际属性
              </a-descriptions-item>
              <a-descriptions-item label="文件夹合并" :span="3">
                多级文件夹可以合并为一个目录项, 使用 / 或者. 作为分隔符，例如 com.test
              </a-descriptions-item>
              <a-descriptions-item label="模板引擎" :span="3">
                目前只支持 Velocity，具体语法请参看其官网
              </a-descriptions-item>
            </a-descriptions>
            <a-descriptions title="默认属性">
              <a-descriptions-item label="tablePrefix">
                表前缀，代码生成时截取此前缀后再生成类名
              </a-descriptions-item>
            </a-descriptions>
            <a-descriptions title="自定义属性">
              <template v-for="item in this.properties">
                <a-descriptions-item :label="item.propKey">
                  {{ item.remarks ? item.title + '，' + item.remarks : item.title }}
                </a-descriptions-item>
              </template>
            </a-descriptions>
          </div>
          <a-form v-show="!showTips" @submit="handleSubmit" :form="form">
            <div class="template-form-title">{{ formInfo.formTitle }}</div>
            <template v-if="!formInfo.updateFlag">
              <a-form-item style="display: none">
                <a-input v-decorator="['groupId']" />
              </a-form-item>
              <a-form-item style="display: none">
                <a-input v-decorator="['parentId']" />
              </a-form-item>
              <a-form-item style="display: none">
                <a-input v-decorator="['type']" />
              </a-form-item>
            </template>

            <a-form-item label="父目录:" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <span> {{ formInfo.parentFileName }}</span>
            </a-form-item>
            <a-form-item label="文件名" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-if="!formInfo.updateFlag" placeholder="请输入文件名" v-decorator="['fileName']" />
              <span v-else>{{ formInfo.fileName }}</span>
            </a-form-item>
            <template v-if="formInfo.fileType === 2">
              <a-form-item v-if="formInfo.updateFlag" style="display: none">
                <a-input v-decorator="['directoryEntryId']" />
              </a-form-item>
              <a-form-item label="标题" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input placeholder="标题" v-decorator="formInfo.updateFlag ? ['title'] : ['templateInfoDTO.title']" />
              </a-form-item>
              <a-form-item label="引擎" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-select
                  v-decorator="[formInfo.updateFlag ? 'engineType' : 'templateInfoDTO.engineType', { initialValue: 1 }]"
                >
                  <a-select-option :value="1">velocity</a-select-option>
                </a-select>
              </a-form-item>
              <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-textarea
                  placeholder="备注"
                  v-decorator="formInfo.updateFlag ? ['remarks'] : ['templateInfoDTO.remarks']"
                />
              </a-form-item>
              <a-form-item label="模板" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <codemirror v-model="formInfo.content" :options="cmOptions" style="line-height: 1.5"></codemirror>
              </a-form-item>
            </template>
            <a-form-item :wrapperCol="{ span: 24 }" style="text-align: center">
              <a-button htmlType="submit" type="primary" :loading="submitLoading">提交</a-button>
              <a-button style="margin-left: 8px" @click="() => (this.showTips = true)">取消</a-button>
            </a-form-item>
          </a-form>
        </div>
      </template>
    </split-pane>
    <rename-model ref="renameModel"></rename-model>
    <remove-model ref="removeModel"></remove-model>
  </div>
</template>

<script>
import splitPane from 'vue-splitpane'
import { getList, delObj, move } from '@/api/gen/templatedirectoryentry'
import { listToTree } from '@/utils/treeUtil'
import { FormMixin } from '@/mixins'
import { putObj, getObj } from '@/api/gen/templateinfo'
import { addObj } from '@/api/gen/templatedirectoryentry'
import { getProperties } from '@/api/gen/templateproperty'

// codemirror
import { codemirror } from 'vue-codemirror'
import 'codemirror/lib/codemirror.css'
import 'codemirror/theme/dracula.css'
import 'codemirror/mode/velocity/velocity.js'
//import { TablePageMixin } from '@/mixins'
import renameModel from './TemplateGroupRenameModel.vue'
import removeModel from './TemplateGroupRemoveTree.vue'

export default {
  name: 'TemplateDirectoryEntryPage',
  mixins: [FormMixin],
  components: { codemirror, renameModel, removeModel, splitPane },
  props: {
    templateGroupId: Number
  },
  data() {
    return {
      // 属性
      showTips: true,
      properties: [],

      // tree
      treeData: [],
      expandedKeys: [],
      autoExpandParent: true,
      checkedKeys: [],
      selectedKeys: [],
      selectedNode: null,

      // ========== 右键菜单样式 ==============
      menuVisible: false,
      menuItemStyle: {
        height: '31px',
        lineHeight: '31px',
        fontSize: '13px',
        marginBottom: '0'
      },
      menuStyle: {
        boxShadow: '0 2px 4px rgba(0, 0, 0, 0.12), 0 0 2px rgba(0, 0, 0, 0.04)',
        borderRadius: '2px',
        fontFamily: 'arial',
        zIndex: 999,
        padding: '2px 0 8px 0',
        position: 'fixed',
        top: '0',
        left: '0',
        border: '1px solid #eee'
      },

      // ========== split样式 ==============
      splitPane: {
        height: 0
      },
      leftbtnStyle: {
        left: '-3px'
      },
      treesettingRow: {
        padding: '10px'
      },
      defaultPercent: 30,
      minPercent: 15,
      heightClient: 0,
      leftHtml: '<',

      // ==================form===================
      putObj: putObj,
      addObj: addObj,
      delObj: delObj,
      formInfo: {
        formTitle: '',
        fileType: 0,
        fileName: '',
        parentFileName: '',
        content: '',
        updateFlag: true
      },
      labelCol: { lg: { span: 3 }, sm: { span: 3 } },
      wrapperCol: { lg: { span: 20 }, sm: { span: 20 } },
      decoratorOptions: {},
      cmOptions: {
        // codemirror options
        tabSize: 4,
        mode: 'velocity',
        theme: 'dracula',
        lineNumbers: true,
        line: true
        // more codemirror options, 更多 codemirror 的高级配置...
      }
    }
  },
  created() {
    this.pageLoad(true)
    getProperties(this.templateGroupId).then(res => {
      this.properties = res.data
    })
    this.heightClient = document.documentElement.clientHeight || document.body.clientHeight
    this.heightClient = this.heightClient - 210
    this.splitPane.height = this.heightClient + 'px'
  },
  methods: {
    pageLoad(firstInit) {
      getList(this.templateGroupId).then(res => {
        this.treeData = listToTree(res.data, 0, (treeNode, item) => {
          treeNode.isLeaf = item.type === 2
          treeNode.title = item.fileName
        })
        // 只在第一次加载时默认展开第一级的文件，防止用户提交表单后tree被折叠
        firstInit && (this.expandedKeys = this.treeData.map(item => item.id))
      })
    },
    onRightClick(e) {
      const event = e.event
      const node = e.node
      const dataRef = node.dataRef
      // 设置选中数据
      this.selectedKeys = [dataRef.id]
      this.selectedNode = node

      // 防止冒泡
      event.cancelBubble = true

      // 右键菜单属性
      this.menuVisible = true
      this.menuStyle.top = event.clientY + 'px'
      this.menuStyle.left = event.clientX + 'px'
      document.body.addEventListener('click', this.bodyClick)
    },
    onRightClickBox(event) {
      // 清空选中数据
      this.selectedKeys = []
      this.selectedNode = null

      this.menuVisible = true
      this.menuStyle.top = event.clientY + 'px'
      this.menuStyle.left = event.clientX + 'px'
      document.body.addEventListener('click', this.bodyClick)
    },
    bodyClick() {
      this.menuVisible = false
      document.body.removeEventListener('click', this.bodyClick)
    },
    ondblclick(e, node) {
      // 非文件类型不加载
      const entry = node.dataRef
      if (entry.type === 2) {
        // 显示表单
        this.showTips = false
        // 修改表单信息
        this.formInfo.formTitle = '修改模板文件'
        this.formInfo.updateFlag = true
        this.formInfo.fileType = entry.type
        this.formInfo.fileName = entry.fileName
        const parentDataRef = node.$parent.dataRef
        this.formInfo.parentFileName = parentDataRef ? parentDataRef.fileName : '/'
        // 远程加载模板文件详情信息
        getObj(entry.id).then(res => {
          const templateInfo = res.data
          this.formInfo.content = templateInfo.content || ''
          this.buildUpdatedForm(templateInfo)
        })
      }
    },
    onExpand(expandedKeys) {
      this.expandedKeys = expandedKeys
      this.autoExpandParent = false
    },
    onSelect(selectedKeys, e) {
      this.selectedKeys = selectedKeys
      this.selectedNode = e.node
    },
    onDrop(info) {
      // 被移动的目录项
      const entry = info.dragNode.dataRef
      // 目标目录项
      const targetEntry = info.node.dataRef
      // 是否移动到其子节点，否则是平级
      const horizontalMove = info.dropToGap
      // 无需移动
      const parentId = horizontalMove ? targetEntry.parentId : targetEntry.id
      if (entry.parentId === parentId) {
        return
      }
      if (!horizontalMove && targetEntry.type !== 1) {
        this.$message.error('不能移动到非文件夹目标内部')
        return
      }
      move(entry.id, targetEntry.id, horizontalMove)
        .then(res => {
          if (res.code === 200) {
            this.$message.success('移动成功！')
            this.pageLoad()
          } else {
            this.$message.error(res.msg)
          }
        })
        .catch(error => {
          this.$message.error(error.message)
        })
    },
    /**
     * 表单提交前更新formAction，用于切换请求方法
     */
    beforeStartSubmit() {
      this.formAction = this.formInfo.updateFlag ? this.FORM_ACTION.UPDATE : this.FORM_ACTION.CREATE
    },
    /**
     * 表单提交时的数据处理
     */
    submitDataProcess(data) {
      if (this.formInfo.updateFlag) {
        data.content = this.formInfo.content
      } else if (data.type === 2) {
        data.templateInfoDTO.content = this.formInfo.content
      }
      return data
    },
    /**
     * 表单提交后进行重新load（不更新展开的文件）
     */
    submitSuccess() {
      this.pageLoad()
    },
    backToPage(needRefresh) {
      this.$emit('backToPage', needRefresh)
    },
    /**
     * 重命名
     */
    renameModel() {
      if (this.selectedNode && this.selectedNode.dataRef) {
        const dataRef = this.selectedNode.dataRef
        this.$refs.renameModel.update({ title: dataRef.fileName, id: dataRef.id })
      } else {
        this.$message.warning('请选择一个目录项')
      }
    },
    /**
     * 删除
     */
    removeEntry() {
      if (this.selectedNode && this.selectedNode.dataRef) {
        this.$refs.removeModel.update(this.selectedNode.dataRef)
      } else {
        this.$message.warning('请选择一个目录项')
      }
    },
    /**
     * 新建
     * @param fileType 文件类型
     */
    createdEntry(fileType) {
      // 显示表单
      this.showTips = false

      let parentId = 0
      let parentFileName = '/'
      if (this.selectedNode && this.selectedNode.dataRef) {
        const dataRef = this.selectedNode.dataRef
        parentFileName = dataRef.fileName
        parentId = dataRef.parentId
      }

      this.formInfo.formTitle = fileType === 1 ? '新建文件夹' : '新建模板文件'
      this.formInfo.content = ''
      this.formInfo.updateFlag = false
      this.formInfo.fileType = fileType
      this.formInfo.parentFileName = parentFileName

      setTimeout(() => {
        this.$nextTick(function() {
          this.form.setFieldsValue({
            groupId: this.templateGroupId,
            parentId: parentId,
            type: fileType
          })
        })
      })
    },
    resize(val) {
      /* 监听面板的拖动*/
      if (val > 1) {
        this.leftHtml = '<'
        this.leftbtnStyle.left = '-3px'
        this.defaultPercent = 30
      } else {
        this.leftHtml = '>'
        this.defaultPercent = 0
      }
    },
    moveLeft() {
      if (this.defaultPercent !== 0) {
        this.minPercent = 0
        this.defaultPercent = 0
        this.treesettingRow.padding = 0
        this.leftHtml = '>'
        this.leftbtnStyle.left = '0px'
      } else {
        this.leftHtml = '<'
        this.leftbtnStyle.left = '-3px'
        this.treesettingRow.padding = '10px'
        this.minPercent = 15
        this.defaultPercent = 30
      }
    }
  }
}
</script>
<style scoped>
.ant-form-item {
  margin-bottom: 8px;
}
.treesetting-paneR {
  overflow-y: auto;
  width: 96%;
  height: 100%;
  border: none;
  position: relative;
  box-sizing: border-box;
  padding: 2%;
}
.treesetting-row {
  overflow: auto;
  height: 100%;
  border: none;
  position: relative;
  box-sizing: border-box;
  border-right: 1px solid #f0f2f5;
}

.treesetting-row-leftbtn {
  position: absolute;
  left: 1px;
  cursor: pointer;
  width: 12px;
  line-height: 28px;
  height: 30px;
  background: #ededed;
  border-radius: 0 4px 4px 0;
  text-align: center;
  color: #ffffff;
  top: 45%;
  z-index: 1;
}

.treesetting-row-leftbtn:hover {
  background: #1da57a;
}

.treesetting-row::-webkit-scrollbar {
  /*滚动条整体样式*/
  width: 5px; /*高宽分别对应横竖滚动条的尺寸*/
  height: 6px;
}
.treesetting-paneR::-webkit-scrollbar {
  /*滚动条整体样式*/
  width: 5px; /*高宽分别对应横竖滚动条的尺寸*/
  height: 6px;
}
.treesetting-paneR::-webkit-scrollbar-thumb {
  /*滚动条里面小方块*/
  border-radius: 8px;
  box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
  background: #969696;
}
.treesetting-paneR::-webkit-scrollbar-track {
  /*滚动条里面轨道*/
  box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
  border-radius: 8px;
  background: #ededed;
}
.treesetting-row::-webkit-scrollbar-thumb {
  /*滚动条里面小方块*/
  border-radius: 8px;
  box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
  background: #969696;
}

.treesetting-row::-webkit-scrollbar-track {
  /*滚动条里面轨道*/
  box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
  border-radius: 8px;
  background: #ededed;
}
.splitter-pane.vertical.splitter-paneR {
  padding: 0 !important;
}
.template-form-title {
  color: rgba(0, 0, 0, 0.85);
  font-weight: 600;
  font-size: 18px;
  line-height: 32px;
  text-align: center;
}
</style>
