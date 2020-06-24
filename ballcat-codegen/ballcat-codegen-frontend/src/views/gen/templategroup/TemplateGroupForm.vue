<template>
  <div>
    <split-pane :min-percent="minPercent" :default-percent="defaultPercent" split="vertical" :style="splitPane">
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
            <template v-if="this.rightClickEntry">
              <a-menu-item key="1" :style="menuItemStyle" @click="renameModel">
                <a-icon type="edit" />重命名
              </a-menu-item>
              <a-menu-item key="2" :style="menuItemStyle" @click="removeEntry">
                <a-icon type="delete" />
                删除
              </a-menu-item>
            </template>
            <template v-if="!this.rightClickEntry || this.rightClickEntry.type !== 2">
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
      <template slot="paneR">
        <div style="position:relative">
          <div class="treesetting-row-leftbtn" @click="moveLeft" :style="leftbtnStyle">{{ leftHtml }}</div>
          <a-form @submit="handleSubmit" :form="form" style="padding: 12px 12px 12px 0px">
            <template v-if="!updateData">
              <a-form-item style="display: none">
                <a-input v-decorator="['groupId']" />
              </a-form-item>
              <a-form-item style="display: none">
                <a-input v-decorator="['parentId']" />
              </a-form-item>
              <a-form-item style="display: none">
                <a-input v-decorator="['type']" />
              </a-form-item>
              <a-form-item label="父目录:" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <span> {{ (rightClickEntry && rightClickEntry.fileName) || '根目录' }}</span>
              </a-form-item>
              <a-form-item label="文件名" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input placeholder="请输入文件名" v-decorator="['fileName']" />
              </a-form-item>
            </template>

            <template v-if="fromFileType === 2">
              <a-form-item v-if="updateData" style="display: none">
                <a-input v-decorator="updateData ? ['directoryEntryId'] : ['templateInfoDTO.directoryEntryId']" />
              </a-form-item>
              <a-form-item label="标题" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input placeholder="标题" v-decorator="updateData ? ['title'] : ['templateInfoDTO.title']" />
              </a-form-item>
              <a-form-item label="引擎" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input
                  placeholder="引擎 1：velocity"
                  v-decorator="updateData ? ['engineType'] : ['templateInfoDTO.engineType']"
                />
              </a-form-item>
              <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-textarea placeholder="备注" v-decorator="updateData ? ['remarks'] : ['templateInfoDTO.remarks']" />
              </a-form-item>
              <a-form-item label="模板" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <codemirror v-model="code" :options="cmOptions" style="line-height: 1.5"></codemirror>
              </a-form-item>
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
import { FormMixin } from '@/mixins'
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
  mixins: [FormMixin],
  components: { codemirror, renameModel, removeModel, splitPane },
  props: {
    templateGroupId: Number
  },
  data() {
    return {
      defaultPercent: 30,
      minPercent: 15,
      heightClient: 0,
      leftHtml: '<',
      delObj: delObj,
      expandedKeys: [],
      autoExpandParent: true,
      checkedKeys: [],
      selectedKeys: [],
      rightClickEntry: null,
      treeData: [],
      menuVisible: false,
      updateData: true,
      fromFileType: 0,
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
  created() {
    this.pageLoad(true)
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
      const dataRef = e.node.dataRef
      this.rightClickEntry = dataRef
      this.selectedKeys = [dataRef.id]

      // 防止冒泡
      event.cancelBubble = true

      this.menuVisible = true
      this.menuStyle.top = event.clientY + 'px'
      this.menuStyle.left = event.clientX + 'px'
      document.body.addEventListener('click', this.bodyClick)
    },
    onRightClickBox(event) {
      // 清空选中数据
      this.rightClickEntry = null
      this.selectedKeys = []

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
      const entry = node.dataRef
      // 非文件类型不加载
      if (entry.type === 1) {
        return
      }
      // 加载详情信息
      this.updateData = true
      this.rightClickEntry = entry
      this.fromFileType = entry.type

      getObj(entry.id).then(res => {
        const templateInfo = res.data
        this.code = templateInfo.content || ''
        this.buildUpdatedForm(templateInfo)
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
    /**
     * 表单提交前更新formAction，用于切换请求方法
     */
    beforeStartSubmit() {
      this.formAction = this.updateData ? this.FORM_ACTION.UPDATE : this.FORM_ACTION.CREATE
    },
    /**
     * 表单提交时的数据处理
     */
    submitDataProcess(data) {
      if (this.updateData) {
        data.content = this.code
      } else if (data.type === 2) {
        data.templateInfoDTO.content = this.code
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
      this.$refs.renameModel.update({ title: this.rightClickEntry.fileName, id: this.selectedKeys[0] })
    },
    /**
     * 删除
     */
    removeEntry() {
      this.$refs.removeModel.update(this.rightClickEntry)
    },
    /**
     * 新建
     * @param fileType 文件类型
     */
    createdEntry(fileType) {
      this.code = ''
      this.updateData = false
      this.fromFileType = fileType
      setTimeout(() => {
        this.$nextTick(function() {
          this.form.setFieldsValue({
            groupId: this.templateGroupId,
            parentId: this.rightClickEntry.id,
            type: fileType
          })
        })
      })
    },
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
    }
  }
}
</script>
<style scoped>
.ant-form-item {
  margin-bottom: 8px;
}
.treesetting-row {
  padding: 10px;
  overflow: auto;
  height: 100%;
  border: none;
  position: relative;
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
  width: 5px; /*高宽分别对应横竖滚动条的尺寸*/
  height: 6px;
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
