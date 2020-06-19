<template>
  <div>
    <a-card v-show="tableShow" :bordered="false">
      <!-- 查询条件 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-row :gutter="48">
            <a-col :md="8" :sm="24">
              <a-form-item label="ID">
                <a-input v-model="queryParam.id" placeholder="" />
              </a-form-item>
            </a-col>

            <!-- <template v-if="advanced">
            </template>-->
            <a-col :md="(!advanced && 8) || 24" :sm="24">
              <span
                class="table-page-search-submitButtons"
                :style="(advanced && { float: 'right', overflow: 'hidden' }) || {}"
              >
                <a-button type="primary" @click="reloadTable">查询</a-button>
                <a-button style="margin-left: 8px" @click="resetSearchForm">重置</a-button>
                <!--<a @click="toggleAdvanced" style="margin-left: 8px">
                  {{ advanced ? '收起' : '展开' }}
                  <a-icon :type="advanced ? 'up' : 'down'"/>
                </a>-->
              </span>
            </a-col>
          </a-row>
        </a-form>
      </div>

      <!-- 操作按钮区域 -->
      <div class="table-operator">
        <a-button type="primary" icon="plus" @click="handleAdd()">新建</a-button>
      </div>

      <!--数据表格区域-->
      <div class="table-wrapper">
        <a-table
          ref="table"
          size="middle"
          :rowKey="rowKey"
          :columns="columns"
          :dataSource="dataSource"
          :pagination="pagination"
          :loading="loading"
          @change="handleTableChange"
        >
          <span slot="action-slot" slot-scope="text, record">
            <template>
              <a @click="handleEdit(record)">编辑</a>
              <a-divider type="vertical" />
              <a-popconfirm title="确认要删除吗？" @confirm="() => handleDel(record)">
                <a href="javascript:;">删除</a>
              </a-popconfirm>
            </template>
          </span>
        </a-table>
      </div>
    </a-card>

    <!--表单页面-->
    <a-card v-if="formInited" :bordered="false" :title="cardTitle" v-show="!tableShow">
      <form-page ref="formPage" @backToPage="backToPage"></form-page>
    </a-card>
  </div>
</template>

<script>
import { getPage, delObj } from '@/api/gen/gentemplate'
import FormPage from './TemplateForm'
import { TablePageMixin } from '@/mixins'

export default {
  name: 'TemplatePage',
  mixins: [TablePageMixin],
  components: { FormPage },
  data() {
    return {
      getPage: getPage,
      delObj: delObj,

      columns: [
        {
          title: '#',
          dataIndex: 'id'
        },
        {
          title: '应用ID',
          dataIndex: 'appId'
        },
        {
          title: '模板名称',
          dataIndex: 'name'
        },
        {
          title: '模板引擎',
          dataIndex: 'engineType'
        },
        {
          title: '描述',
          dataIndex: 'remarks'
        },
        {
          title: '创建时间',
          dataIndex: 'createTime',
          width: '150px',
          sorter: true
        },
        {
          title: '更新时间',
          dataIndex: 'updateTime',
          width: '150px',
          sorter: true
        },
        {
          title: '操作',
          dataIndex: 'action',
          width: '150px',
          scopedSlots: { customRender: 'action-slot' }
        }
      ]
    }
  },
  methods: {}
}
</script>
