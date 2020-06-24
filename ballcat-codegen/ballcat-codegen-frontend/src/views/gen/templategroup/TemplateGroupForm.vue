<template>
  <div>
    <a-row :gutter="10">
      <a-col class="gutter-row treesetting-row" :span="9">
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
          <a-menu-item key="1" :style="menuItemStyle" @click="renameModel"> <a-icon type="edit" />重命名 </a-menu-item>
          <a-menu-item key="2" :style="menuItemStyle" @click="removeTree">
            <a-icon type="delete" />
            删除
          </a-menu-item>
          <!-- <a-menu-item key="3" :style="menuItemStyle">
            <a-icon type="form" />
            编辑
          </a-menu-item>-->
        </a-menu>
      </a-col>
      <a-col class="gutter-row" :span="15">
        <a-form @submit="handleSubmit" :form="form">
          <a-form-item v-if="formAction === this.FORM_ACTION.UPDATE" style="display: none">
            <a-input v-decorator="['id']" />
          </a-form-item>

          <a-form-item label="标题" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input placeholder="模板标题" v-decorator="['title']" />
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

          <a-form-item :wrapperCol="{ offset: 3 }">
            <a-button htmlType="submit" type="primary" :loading="submitLoading">提交</a-button>
            <a-button style="margin-left: 8px" @click="backToPage(false)">取消</a-button>
          </a-form-item>
        </a-form>
      </a-col>
    </a-row>
    <rename-model ref="renameModel"></rename-model>
    <remove-model ref="removeModel"></remove-model>
  </div>
</template>

<script>
import { getList, delObj, move } from '@/api/gen/templatedirectoryentry'
import { listToTree } from '@/utils/treeUtil'
import { FormMixin } from '@/mixins'
import { putObj, getObj } from '@/api/gen/templateinfo'

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
  mixins: [FormMixin],
  components: { codemirror, renameModel, removeModel },
  data() {
    return {
      delObj: delObj,
      expandedKeys: [],
      autoExpandParent: true,
      checkedKeys: [],
      selectedKeys: [],
      selectTitle: '',
      dataRef: null,
      treeData: [],
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
      // ==================form===================
      putObj: putObj,
      labelCol: { lg: { span: 2 }, sm: { span: 2 } },
      wrapperCol: { lg: { span: 22 }, sm: { span: 22 } },
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
  mounted() {
    this.pageLoad(true)
  },
  methods: {
    pageLoad(firstInit) {
      // TODO 选择模板ID
      const templateGroupId = 1
      getList(templateGroupId).then(res => {
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
      document.body.removeEventListener('click', this.bodyClick)
    },
    ondblclick(e, node) {
      const entry = node.dataRef
      // 非文件类型不加载
      if (entry.type === 1) {
        return
      }
      // 加载详情信息
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
      this.code = data.content
      return data
    },
    submitDataProcess(data) {
      data.content = this.code
      return data
    },
    backToPage(needRefresh) {
      this.$emit('backToPage', needRefresh)
    },
    renameModel() {
      this.$refs.renameModel.update({ title: this.selectTitle, id: this.selectedKeys[0] })
    },
    removeTree() {
      /*删除树节点*/
      this.$refs.removeModel.update(this.dataRef)
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
  height: 540px;
  border: none;
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
