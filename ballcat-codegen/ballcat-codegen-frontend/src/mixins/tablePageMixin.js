export default {
  data() {
    return {
      // 主键 默认id
      rowKey: 'id',
      // 数据数组
      dataSource: [],
      // 数据加载动画
      loading: false,
      // 分页器设置
      pagination: {
        total: 0,
        current: 1,
        pageSize: 10,
        showSizeChanger: true,
        showTotal: (total, range) => {
          return range[0] + '-' + range[1] + ' 共' + total + '条'
        }
      },
      // 查询参数
      queryParam: {},
      // 筛选参数字段
      filters: {},
      // 默认排序字段
      sortField: null,
      // 升序 asc/降序 desc
      sortOrder: null,
      // 表格展示
      tableShow: true,
      // 高级搜索 展开/关闭
      advanced: false,
      // 获取分页数据的方法
      getPage: function() {},
      // 删除 数据的方法
      delObj: function() {},

      // 表单页的title
      cardTitle: '',
      //表单初始化标识
      formInited: false,

      // 已选中数据集合
      selectedRows: [],
      // 已选中的数据主键集合
      selectedRowKeys: [],
      // 延迟加载，created时不主动加载数据
      lazyLoad: false,

      // 需要加载的 dictSlot数据
      dictCodes: []
    }
  },
  created() {
    // this.DictPool.initDictList(this.dictCodes)
    !this.lazyLoad && this.loadData()
  },
  methods: {
    /**
     * 默认排序规则
     */
    initDefaultSort() {
      this.sortField = 'id'
      this.sortOrder = 'desc'
    },
    /**
     * 表格重新加载方法
     * 如果参数为 true, 则强制刷新到第一页
     * @param bool
     */
    reloadTable(bool = false) {
      bool && (this.pagination.current = 1)
      this.loadData()
    },
    /**
     * 合并查询参数，分页参数，排序参数，过滤参数
     * @returns {{current: number, size: number} & {sortOrders: null, sortFields: null}}
     */
    pageParams: function() {
      return Object.assign(
        this.queryParam,
        {
          current: this.pagination.current,
          size: this.pagination.pageSize
        },
        {
          // TODO 多列排序支持
          sortFields: this.sortField,
          sortOrders: this.sortOrder
        },
        { ...this.filters }
      )
    },
    /**
     * 表格数据加载方法
     */
    loadData() {
      const params = this.pageParams()

      this.loading = true
      this.getPage(params)
        .then(res => {
          if (res.code === 200) {
            const page = res.data
            // 为防止删除数据后导致页面当前页面数据长度为 0 ,自动翻页到上一页
            if (page.records.length === 0 && this.pagination.current > 1) {
              this.pagination.current--
              this.loadData()
              return
            }
            this.dataSource = page.records
            this.pagination.total = page.total
            this.onPageLoadSuccess(page)
          } else {
            this.$message.warning(res.message || 'error request')
          }
        })
        .catch(e => {
          // 未被 axios拦截器处理过，则在这里继续处理
          !e.resolved && this.$message.error(e.message || 'error request')
        })
        .finally(() => {
          this.loading = false
        })
    },
    /**
     * 分页查询成功回调
     * @param page
     */
    /* eslint-disable no-unused-vars */
    onPageLoadSuccess(page) {},
    /**
     * 分页、排序、筛选变化时进行数据更新
     * @param pagination
     * @param filters
     * @param sorter
     */
    handleTableChange(pagination, filters, sorter) {
      this.filters = filters
      if (sorter && sorter.field) {
        if (sorter.order) {
          this.sortField = sorter.field
          this.sortOrder = sorter.order === 'ascend' ? 'asc' : 'desc'
        } else {
          this.initDefaultSort()
        }
      }
      this.pagination = pagination
      this.loadData()
    },
    // 展开/关闭 搜索条件
    toggleAdvanced() {
      this.advanced = !this.advanced
    },
    // 清空搜索条件
    resetSearchForm() {
      this.queryParam = {}
    },
    // 选择
    onSelectChange(selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectedRows = selectedRows
    },
    // 清空选项
    onClearSelected() {
      this.selectedRowKeys = []
      this.selectedRows = []
    },

    // 删除
    handleDel(record) {
      this.delObj(record[this.rowKey]).then(res => {
        if (res.code === 200) {
          this.$message.success(res.message)
          this.reloadTable()
        } else {
          this.$message.error(res.message)
        }
      })
    },

    // ========== PageForm交互 ===================
    // TODO 支持 modal 形式表单
    // 切换表格/表单
    switchPage() {
      window.scrollTo(0, 0)
      this.tableShow = !this.tableShow
      if (!this.formInited) {
        this.formInited = true
      }
    },
    // 返回表格
    backToPage(needRefresh) {
      this.switchPage()
      needRefresh && this.reloadTable(false)
    },
    // 新增
    handleAdd(argument) {
      this.switchPage()
      this.cardTitle = '新增'
      this.$nextTick(function() {
        this.$refs.formPage.add(argument)
      })
    },
    // 编辑
    handleEdit(record, title) {
      this.switchPage()
      this.cardTitle = title || '修改'
      this.$nextTick(function() {
        this.$refs.formPage.update(record)
      })
    }
  }
}
