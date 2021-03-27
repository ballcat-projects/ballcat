<template>
  <div>
    <split-pane
      :min-percent="minPercent"
      :default-percent="defaultPercent"
      split="vertical"
      :style="splitPaneStyle"
      @resize="resize"
    >
      <template slot="paneL">
        <div class="left-pane" @contextmenu.prevent="onRightClickBox" :style="leftPaneStyle">
          <h1 align="center">右键即可创建文件或文件夹</h1>
          <a-directory-tree
            :style="directoryTreeStyle"
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
              <a-menu-item key="1" :style="menuItemStyle" @click="updateEntry">
                <a-icon type="edit" />
                编辑
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
        <div class="left-pane-leftbtn" @click="moveLeft">{{ leftHtml }}</div>
      </template>
      <template slot="paneR" style="padding:0;">
        <div ref="paneR" class="right-pane">
          <code-gen-tips v-show="showTips" :template-group-id="this.templateGroupId" />
          <div v-show="!showTips">
            <a-tabs
              v-model="activeKey"
              default-active-key="1"
              :tabBarStyle="{ margin: 0 }"
              type="editable-card"
              hide-add
              class="file-editor-tab"
              @change="handlePaneChange"
              @edit="handlePaneEdit"
            >
              <a-tab-pane v-for="([key, info], index) in templateInfoMap" :key="key" :closable="info.closable">
                <span slot="tab">
                  <a-badge status="default" v-if="info.createData" />
                  {{ info.title }}
                </span>
              </a-tab-pane>
            </a-tabs>
            <div id="codeEditor">
              <codemirror v-model="content" :options="cmOptions" style="line-height: 1.5"></codemirror>
            </div>
          </div>
        </div>
      </template>
    </split-pane>
    <remove-model ref="removeModel"></remove-model>
    <template-folder-modal-form ref="folderModalForm" @reload-page-table="treeLoad" />
    <template-file-modal-form ref="fileModalForm" @reload-page-table="treeLoad" @create-entry-file="createEntryFile" />
  </div>
</template>

<script>
import splitPane from 'vue-splitpane'
import { getList, move } from '@/api/gen/templatedirectoryentry'
import { listToTree } from '@/utils/treeUtil'
import { getObj, updateContent } from '@/api/gen/templateinfo'
import CodeGenTips from '@/views/gen/templategroup/CodeGenTips'
import { addObj } from '@/api/gen/templatedirectoryentry'

// codemirror`
import { codemirror } from 'vue-codemirror'
import 'codemirror/lib/codemirror.css'
import 'codemirror/theme/dracula.css'
import 'codemirror/mode/velocity/velocity.js'

// 全屏插件
import 'codemirror/addon/display/fullscreen.js'
import 'codemirror/addon/display/fullscreen.css'

import removeModel from './TemplateEntryRemoveModal.vue'
import TemplateFolderModalForm from '@/views/gen/templategroup/TemplateFolderModalForm'
import TemplateFileModalForm from '@/views/gen/templategroup/TemplateFileModalForm'

export default {
  name: 'TemplateDirectoryEntryPage',
  components: {
    TemplateFolderModalForm,
    TemplateFileModalForm,
    splitPane,
    codemirror,
    removeModel,
    CodeGenTips
  },
  props: {
    templateGroupId: Number
  },
  data() {
    return {
      // 属性
      showTips: true,

      // tree
      treeData: [],
      expandedKeys: [],
      autoExpandParent: true,
      checkedKeys: [],
      selectedKeys: [],
      selectedNode: null,

      // 文件列表
      templateInfoMap: new Map(),
      activeKey: null,

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
      splitPaneStyle: {
        height: '800px'
      },
      leftbtnStyle: {
        left: '0'
      },
      leftPaneStyle: {
        padding: '10px'
      },
      directoryTreeStyle: {
        width: '100%'
      },
      defaultPercent: 30,
      minPercent: 15,
      heightClient: 0,
      leftHtml: '<',

      // ==================form===================
      content: '',
      cmOptions: {
        // codemirror options
        tabSize: 4,
        mode: 'velocity',
        theme: 'dracula',
        lineNumbers: true,
        line: true,
        extraKeys: {
          // 全屏
          F11: function(cm) {
            cm.setOption('fullScreen', !cm.getOption('fullScreen'))
          },
          // 保存
          'Ctrl-S': () => {
            this.saveTemplateContent()
          }
        }
        // more codemirror options, 更多 codemirror 的高级配置...
      }
    }
  },
  watch: {
    templateGroupId() {
      this.initPage()
    }
  },
  created() {
    this.initPage()
  },
  methods: {
    handlePaneChange(activeKey) {
      const data = this.templateInfoMap.get(activeKey)
      data && (this.content = data.content)
    },
    handlePaneEdit(targetKey, action) {
      this[action](targetKey)
    },
    remove(targetKey) {
      // 是否关闭当前标签
      const closeCurrent = this.activeKey === targetKey
      // 获取关闭标签的前一个标签
      let preKey = null
      if (closeCurrent) {
        for (let key of this.templateInfoMap.keys()) {
          if (key !== targetKey) {
            preKey = key
          } else {
            break
          }
        }
      }
      // 删除标签
      this.templateInfoMap.delete(targetKey)
      // 当全部标签删除时，显示提示
      if (this.templateInfoMap.size === 0) {
        this.showTips = true
        this.activeKey = null
      } else if (closeCurrent) {
        // 当关闭标签为第一个的时候，默认打开现在的第一个标签
        this.activeKey = preKey ? preKey : this.templateInfoMap.keys().next().value
      }
      // Vue2 Map 双向绑定有点问题，需要强制刷新
      this.$forceUpdate()
    },
    saveTemplateContent() {
      const createData = this.templateInfoMap.get(this.activeKey).createData
      // 新建模板信息
      if (createData) {
        createData.templateInfo.content = this.content
        addObj(createData).then(res => {
          if (res.code === 200) {
            this.$message.success(res.message)
            this.templateInfoMap.delete(this.activeKey)
            const entryId = res.data
            this.templateInfoMap.set(entryId, {
              content: this.content,
              title: createData.fileName
            })
            this.activeKey = entryId
            this.treeLoad()
          } else {
            this.$message.error(res.message)
          }
        })
      } else {
        // 修改模板文件内容
        const formData = { directoryEntryId: this.activeKey, content: this.content }
        updateContent(formData).then(res => {
          if (res.code === 200) {
            this.templateInfoMap.get(this.activeKey).content = this.content
            this.$message.success(res.message)
          } else {
            this.$message.error(res.message)
          }
        })
      }
    },
    // 创建文件目录项
    createEntryFile(data) {
      this.showTips = false
      const content = ''
      this.content = content
      const key = new Date().getTime()
      this.activeKey = key
      this.templateInfoMap.set(key, {
        content: content,
        title: data.fileName,
        createData: data
      })
    },

    /**
     * 页面初始化
     * 1. 更新 Entry Tree
     * 2. 更新自定义属性展示
     */
    initPage() {
      this.treeLoad(true)
      this.showTips = true
      // 代码编辑模块置空
      this.templateInfoMap = new Map()
      this.content = ''
      this.activeKey = null
      // 页面高度
      this.heightClient = document.documentElement.clientHeight || document.body.clientHeight
      this.heightClient = this.heightClient - 165
      this.splitPaneStyle.height = this.heightClient + 'px'
    },
    /**
     * 加载 Entry Tree
     */
    treeLoad(firstInit) {
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
        const targetKey = entry.id
        // 显示表单
        this.showTips = false
        this.activeKey = targetKey
        if (this.templateInfoMap.has(targetKey)) {
          this.content = this.templateInfoMap.get(targetKey).content
          return
        }

        // 远程加载模板文件详情信息
        getObj(targetKey).then(res => {
          const templateInfo = res.data
          const content = templateInfo.content || ''
          this.templateInfoMap.set(targetKey, {
            content: content,
            title: entry.fileName
          })
          this.content = content
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
            this.treeLoad()
          } else {
            this.$message.error(res.message)
          }
        })
        .catch(error => {
          this.$message.error(error.message)
        })
    },

    /**
     * 删除
     */
    removeEntry() {
      if (this.selectedNode && this.selectedNode.dataRef) {
        this.$refs.removeModel.show(this.selectedNode.dataRef)
      } else {
        this.$message.warning('请选择一个目录项')
      }
    },
    /**
     * 新建
     * @param entryType 文件类型
     */
    createdEntry(entryType) {
      let parentId = 0
      let parentFileName = '根目录'
      if (this.selectedNode && this.selectedNode.dataRef) {
        const dataRef = this.selectedNode.dataRef
        parentFileName = dataRef.fileName
        parentId = dataRef.id
      }

      const defaultFormData = {
        groupId: this.templateGroupId,
        parentId: parentId,
        type: entryType
      }

      if (entryType === 1) {
        this.$refs.folderModalForm.add({
          title: '新建文件夹',
          parentFileName: parentFileName,
          formData: defaultFormData
        })
      } else {
        this.$refs.fileModalForm.add({
          title: '新建模板文件',
          parentFileName: parentFileName,
          formData: defaultFormData
        })
      }
    },
    /* 更新目录项 */
    updateEntry() {
      if (this.selectedNode && this.selectedNode.dataRef) {
        const entry = this.selectedNode.dataRef
        // 文件夹类型
        if (entry.type === 1) {
          this.$refs.folderModalForm.update(entry, { title: '编辑文件夹信息' })
        } else {
          // 文件类型
          this.$refs.fileModalForm.update(entry, { title: '编辑文件信息' })
        }
      } else {
        this.$message.warning('请选择一个目录项')
      }
    },
    resize(val) {
      if (val < 24) {
        this.directoryTreeStyle.width = document.querySelector('.vue-splitter-container').offsetWidth / 3 + 'px'
      } else {
        this.directoryTreeStyle.width = '100%'
      }
      if (val > 1) {
        this.leftHtml = '<'
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
        this.leftPaneStyle.padding = 0
        this.leftHtml = '>'
      } else {
        this.leftHtml = '<'
        this.leftPaneStyle.padding = '10px'
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

.right-pane {
  overflow-y: auto;
  height: 100%;
  border: none;
  position: relative;
  box-sizing: border-box;
}

.left-pane {
  overflow: auto;
  height: 100%;
  border: none;
  position: relative;
  box-sizing: border-box;
}

.left-pane-leftbtn {
  position: absolute;
  left: 0;
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

.left-pane-leftbtn:hover {
  background: #1da57a;
}

.left-pane::-webkit-scrollbar {
  /*滚动条整体样式*/
  width: 5px; /*高宽分别对应横竖滚动条的尺寸*/
  height: 5px;
}

.left-pane::-webkit-scrollbar-thumb {
  /*滚动条里面小方块*/
  border-radius: 8px;
  box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
  background: #969696;
}

.left-pane::-webkit-scrollbar-track {
  /*滚动条里面轨道*/
  box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
  border-radius: 8px;
  background: #ededed;
}

.splitter-pane.vertical.splitter-paneR {
  padding: 0 !important;
}

.splitter-paneL {
  padding-right: 0 !important;
}

#codeEditor >>> .CodeMirror {
  min-height: 500px !important;
  height: 100% !important;
}
#codeEditor >>> .CodeMirror-scroll {
  min-height: 500px !important;
}

.file-editor-tab >>> .ant-tabs-tab {
  height: 32px !important;
  line-height: 32px !important;
  padding: 0 12px !important;
}
.file-editor-tab >>> .ant-tabs-nav-container {
  height: 32px !important;
}
.file-editor-tab >>> .ant-tabs-tab-active {
  height: 32px !important;
  line-height: 30px !important;
}
</style>
