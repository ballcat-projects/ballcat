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
        <a-button v-has="'codegen:templateproperty:add'" type="primary" icon="plus" @click="handleAdd()">新建</a-button>
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
              <a v-has="'codegen:templateproperty:edit'" @click="handleEdit(record)">编辑</a>
              <a-divider type="vertical" />
              <a-popconfirm
                v-has="'codegen:templateproperty:del'"
                title="确认要删除吗？"
                @confirm="() => handleDel(record)"
              >
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
import { getPage, delObj } from '@/api/gen/templateproperty'
import FormPage from './TemplatePropertyForm'
import { TablePageMixin } from '@/mixins'

export default {
  name: 'TemplatePropertyPage',
  mixins: [TablePageMixin],
  components: { FormPage },
  data() {
    return {
      getPage: getPage,
      delObj: delObj,

      columns: [
        {
          title: '标题',
          dataIndex: 'title'
        },
        {
          title: '属性键',
          dataIndex: 'propKey'
        },
        {
          title: '属性值',
          dataIndex: 'propValue'
        },
        {
          title: '默认值',
          dataIndex: 'defaultValue'
        },
        {
          title: '必填，1：是，0：否',
          dataIndex: 'required'
        },
        {
          title: '备注信息',
          dataIndex: 'remarks'
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
