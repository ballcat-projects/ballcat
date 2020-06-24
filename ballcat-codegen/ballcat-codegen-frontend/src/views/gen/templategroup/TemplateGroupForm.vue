<template>
  <div>
    <split-pane
      v-on:resize="resize"
      :min-percent="minPercent"
      :default-percent="defaultPercent"
      split="vertical"
      :style="splitPane"
    >
      <template slot="paneL">
        <div class="treesetting-row" @contextmenu.prevent="onRightClickBox">
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
            <a-menu-item key="1" :style="menuItemStyle" @click="renameModel">
              <a-icon type="edit" />重命名
            </a-menu-item>
            <a-menu-item key="2" :style="menuItemStyle" @click="removeTree">
              <a-icon type="delete" />
              删除
            </a-menu-item>
            <a-menu-item key="3" :style="menuItemStyle" @click="createdDirectory" v-if="this.dataRef.type != 2">
              <a-icon type="diff" />
              新建文件夹
            </a-menu-item>
            <a-menu-item key="4" :style="menuItemStyle" @click="createdFile">
              <a-icon type="file-add" />
              新建文件
            </a-menu-item>
          </a-menu>
          <!---->
          <a-menu :style="menuStyle" v-if="menuVisibleFile">
            <a-menu-item key="3" :style="menuItemStyle" @click="createdDirectory">
              <a-icon type="diff" />
              新建文件夹
            </a-menu-item>
            <a-menu-item key="4" :style="menuItemStyle" @click="createdFile">
              <a-icon type="file-add" />
              新建文件
            </a-menu-item>
          </a-menu>
        </div>
      </template>
      <template slot="paneR">
        <div style="position:relative">
          <div class="treesetting-row-leftbtn" @click="moveLeft" :style="leftbtnStyle">{{ leftHtml }}</div>
          <a-form @submit="handleSubmit" :form="form">
            <template v-if="updateData">
              <a-form-item style="display: none">
                <a-input v-decorator="['directoryEntryId']" />
              </a-form-item>
              <a-form-item label="标题" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input placeholder="标题" v-decorator="['title']" />
              </a-form-item>
              <a-form-item label="引擎" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input placeholder="引擎 1：velocity" v-decorator="['engineType']" />
              </a-form-item>
              <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-textarea placeholder="备注" v-decorator="['remarks']" />
              </a-form-item>
              <a-form-item label="模板" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <codemirror v-model="code" :options="cmOptions" style="line-height: 1.5"></codemirror>
              </a-form-item>
            </template>
            <template v-else>
              <a-form-item style="display: none">
                <a-input v-decorator="['parentId']" />
              </a-form-item>
              <a-form-item label="父文件:" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <span> {{ selectTitle || '根文件' }}</span>
              </a-form-item>
              <a-form-item label="文件名" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input placeholder="请输入文件名" v-decorator="['fileName']" />
              </a-form-item>

              <template v-if="fileType === 2">
                <a-form-item label="标题" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input placeholder="标题" v-decorator="['templateInfoDTO.title']" />
                </a-form-item>
                <a-form-item label="引擎" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input placeholder="引擎 1：velocity" v-decorator="['templateInfoDTO.engineType']" />
                </a-form-item>
                <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-textarea placeholder="备注" v-decorator="['templateInfoDTO.remarks']" />
                </a-form-item>
                <a-form-item label="模板" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <codemirror v-model="code" :options="cmOptions" style="line-height: 1.5"></codemirror>
                </a-form-item>
              </template>
            </template>
            <a-form-item :wrapperCol="{ offset: 3 }">
              <a-button htmlType="submit" type="primary" :loading="submitLoading">提交</a-button>
              <a-button style="margin-left: 8px" @click="backToPage(false)">取消</a-button>
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
import { FormPageMixin } from '@/mixins'
import { putObj, getObj } from '@/api/gen/templateinfo'
import { addObj } from '@/api/gen/templatedirectoryentry'
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
  //mixins: [TablePageMixin],
  mixins: [FormPageMixin],
  components: { codemirror, renameModel, removeModel, splitPane },
  data() {
    return {
      templateGroupId: '',
      defaultPercent: 30,
      minPercent: 15,
      heightClient: 0,
      leftHtml: '<',
      delObj: delObj,
      expandedKeys: [],
      autoExpandParent: true,
      checkedKeys: [],
      selectedKeys: [],
      selectTitle: '',
      dataRef: null,
      treeData: [],
      menuVisible: false,
      menuVisibleFile: false,
      fileType: 1,
      updateData: true,
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
      splitPane: {
        height: 0
      },
      leftbtnStyle: {
        left: '-8px'
      },
      // ==================form===================
      putObj: putObj,
      addObj: addObj,
      labelCol: { lg: { span: 3 }, sm: { span: 2 } },
      wrapperCol: { lg: { span: 21 }, sm: { span: 22 } },
      decoratorOptions: {},
      code: '',
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
  watch: {
    templateGroupId() {
      this.pageLoad(true)
    }
  },
  created() {
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
        firstInit && (this.expandedKeys = this.treeData.map(item => item.id))
      })
    },
    onRightClick(e) {
      const event = e.event
      this.dataRef = e.node.dataRef
      this.selectedKeys = [e.node.dataRef.id]
      this.selectTitle = e.node.dataRef.title
      this.menuStyle.top = event.clientY + 'px'
      this.menuStyle.left = event.clientX + 'px'
      this.menuVisible = true
      document.body.addEventListener('click', this.bodyClick)
    },
    bodyClick() {
      this.menuVisible = false
      this.menuVisibleFile = false
      document.body.removeEventListener('click', this.bodyClick)
    },
    ondblclick(e, node) {
      const entry = node.dataRef
      // 非文件类型不加载
      if (entry.type === 1) {
        return
      }
      // 加载详情信息
      this.updateData = true
      getObj(entry.id).then(res => {
        this.buildUpdatedForm(res.data)
      })
    },
    onExpand(expandedKeys) {
      this.expandedKeys = expandedKeys
      this.autoExpandParent = false
    },
    onSelect(selectedKeys) {
      this.selectedKeys = selectedKeys
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
    echoDataProcess(data) {
      this.code = data.content || ''
      this.templateGroupId = data.id
    },
    beforeStartSubmit() {
      this.formAction = this.updateData ? this.FORM_ACTION.UPDATE : this.FORM_ACTION.CREATE
    },
    submitDataProcess(data) {
      if (!this.updateData) {
        // 文件
        data['groupId'] = this.templateGroupId
        data['parentId'] = this.selectedKeys[0] || 0
        data['type'] = this.fileType
        this.fileType === 2 && (data.templateInfoDTO['content'] = this.code)
      } else {
        data['directoryEntryId'] = this.selectedKeys[0]
        data.content = this.code
      }
      return data
    },
    backToPage(needRefresh) {
      this.$emit('backToPage', needRefresh)
    },
    submitSuccess() {
      this.pageLoad(true)
    },
    renameModel() {
      this.$refs.renameModel.update({ title: this.selectTitle, id: this.selectedKeys[0] })
    },
    removeTree() {
      /*删除树节点*/
      this.$refs.removeModel.update(this.dataRef)
    },
    createdDirectory() {
      this.updateData = false
      this.fileType = 1
    },
    createdFile() {
      this.updateData = false
      this.fileType = 2
    },
    resize() {},
    moveLeft() {
      if (this.defaultPercent !== 0) {
        this.minPercent = 0
        this.defaultPercent = 0
        this.leftHtml = '>'
        this.leftbtnStyle.left = '-3px'
      } else {
        this.leftHtml = '<'
        this.leftbtnStyle.left = '-8px'
        this.minPercent = 15
        this.defaultPercent = 30
      }
    },
    onRightClickBox(event) {
      if (!this.menuVisible) {
        this.menuStyle.top = event.clientY + 'px'
        this.menuStyle.left = event.clientX + 'px'
        this.menuVisibleFile = true
        this.selectedKeys = []
        this.selectTitle = ''
        document.body.addEventListener('click', this.bodyClick)
      } else {
        this.menuVisibleFile = false
      }
    }
  }
}
</script>
<style scoped>
.ant-form-item {
  margin-bottom: 8px;
}
.treesetting-row {
  overflow: auto;
  overflow-x: scroll;
  height: 100%;
  border: none;
  position: relative;
  border-right: 2px solid #ededed;
  box-sizing: border-box;
}
.treesetting-row-leftbtn {
  position: absolute;
  left: -8px;
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
  width: 8px; /*高宽分别对应横竖滚动条的尺寸*/
  height: 1px;
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
</style>
