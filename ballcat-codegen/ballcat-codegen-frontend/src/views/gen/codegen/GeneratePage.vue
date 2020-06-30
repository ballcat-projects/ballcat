<template>
  <div>
    <a-card v-show="tableShow" :bordered="false">
      <!-- 查询条件 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-row :gutter="48">
            <a-col :md="8" :sm="24">
              <a-form-item label="数据源">
                <a-select v-model="dsName" style="width: 100%">
                  <a-select-option key="master" value="master">master</a-select-option>
                  <a-select-option v-for="item in dataSourceSelectData" :key="item.value">
                    {{ item.name }}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24">
              <a-form-item label="表名">
                <a-input v-model="queryParam.tableName" placeholder="" />
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
        <a-button type="primary" icon="download" @click="multiGenerate()">批量生成</a-button>
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
          :rowSelection="{ onChange: onSelectChange, selectedRowKeys: selectedRowKeys }"
        >
          <span slot="action-slot" slot-scope="text, record">
            <template>
              <a @click="singleGenerate(record)">生成</a>
            </template>
          </span>
        </a-table>
      </div>
    </a-card>

    <generate-modal
      ref="generateModal"
      :dsName="dsName"
      :bodyStyle="{
        padding: '12px 24px'
      }"
    ></generate-modal>
  </div>
</template>

<script>
import { TablePageMixin } from '@/mixins'
import { getTableInfoPage } from '@/api/gen/generate'
import { getSelectData } from '@/api/gen/datasourceconfig'
import GenerateModal from './GenerateModal'

export default {
  name: 'GeneratePage',
  components: { GenerateModal },
  mixins: [TablePageMixin],
  data() {
    return {
      getPage: queryParam => {
        return getTableInfoPage(this.dsName, queryParam)
      },
      rowKey: 'tableName',

      queryParam: {
        dsName: 'master'
      },

      columns: [
        {
          title: '表名',
          dataIndex: 'tableName',
          width: '250px'
        },
        {
          title: 'Engine',
          dataIndex: 'engine'
        },
        {
          title: '表备注',
          dataIndex: 'tableComment'
        },
        {
          title: '创建时间',
          dataIndex: 'createTime',
          width: '150px',
          sorter: true
        },
        {
          title: '操作',
          dataIndex: 'action',
          width: '150px',
          scopedSlots: { customRender: 'action-slot' }
        }
      ],

      dataSourceSelectData: [],
      dsName: 'master'
    }
  },
  mounted() {
    getSelectData().then(res => {
      this.dataSourceSelectData = res.data
    })
  },
  watch: {
    dsName() {
      this.reloadTable(true)
    }
  },
  methods: {
    singleGenerate(record) {
      this.$refs.generateModal.show([record.tableName])
      //this.handleGenerate([record.tableName])
    },
    multiGenerate() {
      const tableNames = this.selectedRowKeys
      if (tableNames && tableNames.length > 0) {
        this.$refs.generateModal.show(tableNames)
      } else {
        this.$message.warning('至少选中一张表')
      }
    }
  }
}
</script>
