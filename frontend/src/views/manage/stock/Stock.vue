<template>
  <a-card :bordered="false" class="card-area">
    <div :class="advanced ? 'search' : null">
      <!-- 搜索区域 -->
      <a-form layout="horizontal">
        <a-row :gutter="15">
          <div :class="advanced ? null: 'fold'">
            <a-col :md="6" :sm="24">
              <a-form-item
                label="药品名称"
                :labelCol="{span: 5}"
                :wrapperCol="{span: 18, offset: 1}">
                <a-input v-model="queryParams.drugName"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-item
                label="药房名称"
                :labelCol="{span: 5}"
                :wrapperCol="{span: 18, offset: 1}">
                <a-input v-model="queryParams.pharmacyName"/>
              </a-form-item>
            </a-col>
          </div>
          <span style="float: right; margin-top: 3px;">
            <a-button type="primary" @click="search">查询</a-button>
            <a-button style="margin-left: 8px" @click="reset">重置</a-button>
          </span>
        </a-row>
      </a-form>
    </div>
    <div>
      <div class="operator">
        <a-button type="primary" ghost @click="add">添加库存</a-button>
      </div>
      <!-- 表格区域 -->
      <a-table ref="TableInfo"
               :columns="columns"
               :rowKey="record => record.id"
               :dataSource="dataSource"
               :pagination="pagination"
               :loading="loading"
               :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
               :scroll="{ x: 900 }"
               @change="handleTableChange">
        <template slot="addressShow" slot-scope="text, record">
          <template>
            <a-tooltip>
              <template slot="title">
                {{ record.address }}
              </template>
              {{ record.address.slice(0, 8) }} ...
            </a-tooltip>
          </template>
        </template>
        <template slot="operation" slot-scope="text, record">
          <a-icon type="setting" theme="twoTone" twoToneColor="#4a9ff5" @click="showModal(record)" title="更新保质期" style="margin-right: 10px"></a-icon>
          <a-icon v-if="record.shelfStatus == 1" type="caret-down" @click="audit(record.id, 2)" title="取 消" style="margin-right: 10px"></a-icon>
          <a-icon v-if="record.shelfStatus == null || record.shelfStatus == 2" type="caret-up" @click="audit(record.id, 1)" title="设 置" style="margin-right: 10px"></a-icon>
        </template>
      </a-table>
    </div>
    <inventory-add
      v-if="inventoryAdd.visiable"
      @close="handleinventoryAddClose"
      @success="handleinventoryAddSuccess"
      :inventoryAddVisiable="inventoryAdd.visiable">
    </inventory-add>
    <a-modal v-model="dateVisible" title="更新保质期" @ok="handleOk">
      <a-range-picker @change="onChange" style="width: 100%"/>
    </a-modal>
    <a-modal v-model="downVisible" title="下架备注" @ok="handleDownOk">
      <a-textarea
        style="width: 100%"
        v-model="remark"
        placeholder="Controlled autosize"
        :auto-size="{ minRows: 3, maxRows: 5 }"
      />
    </a-modal>
  </a-card>
</template>

<script>
import RangeDate from '@/components/datetime/RangeDate'
import {mapState} from 'vuex'
import moment from 'moment'
import inventoryAdd from './InventoryAdd'
moment.locale('zh-cn')

export default {
  name: 'inventory',
  components: {inventoryAdd, RangeDate},
  data () {
    return {
      dateList: [],
      thisAuditData: {
        id: null,
        status: null
      },
      thisData: null,
      dateVisible: false,
      remark: '',
      downVisible: false,
      advanced: false,
      inventoryAdd: {
        visiable: false
      },
      inventoryEdit: {
        visiable: false
      },
      queryParams: {},
      filteredInfo: null,
      sortedInfo: null,
      paginationInfo: null,
      dataSource: [],
      selectedRowKeys: [],
      loading: false,
      pagination: {
        pageSizeOptions: ['10', '20', '30', '40', '100'],
        defaultCurrent: 1,
        defaultPageSize: 10,
        showQuickJumper: true,
        showSizeChanger: true,
        showTotal: (total, range) => `显示 ${range[0]} ~ ${range[1]} 条记录，共 ${total} 条记录`
      },
      userList: []
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    columns () {
      return [{
        title: '状态',
        dataIndex: 'shelfStatus',
        customRender: (text, row, index) => {
          switch (text) {
            case 1:
              return <a-tag color="green">上架</a-tag>
            case 2:
              return <a-tag color="red">下架</a-tag>
            default:
              return '- -'
          }
        }
      }, {
        title: '药店名称',
        dataIndex: 'pharmacyName'
      }, {
        title: '药店编号',
        dataIndex: 'pharmacyCode'
      }, {
        title: '药品名称',
        dataIndex: 'drugName'
      }, {
        title: '品牌',
        dataIndex: 'brand'
      }, {
        title: '药品图片',
        dataIndex: 'images',
        customRender: (text, record, index) => {
          if (!record.images) return <a-avatar shape="square" icon="user" />
          return <a-popover>
            <template slot="content">
              <a-avatar shape="square" size={132} icon="user" src={ 'http://127.0.0.1:9527/imagesWeb/' + record.images.split(',')[0] } />
            </template>
            <a-avatar shape="square" icon="user" src={ 'http://127.0.0.1:9527/imagesWeb/' + record.images.split(',')[0] } />
          </a-popover>
        }
      }, {
        title: '数量',
        dataIndex: 'reserve',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text + '件'
          } else {
            return '- -'
          }
        }
      }, {
        title: '所属分类',
        dataIndex: 'category',
        customRender: (text, row, index) => {
          switch (text) {
            case 1:
              return <a-tag>可卡因</a-tag>
            case 2:
              return <a-tag>维生素制剂</a-tag>
            case 3:
              return <a-tag>鱼肝油</a-tag>
            case 4:
              return <a-tag>药物饮料</a-tag>
            case 5:
              return <a-tag>膳食纤维</a-tag>
            default:
              return '- -'
          }
        }
      }]
    }
  },
  mounted () {
    this.fetch()
  },
  methods: {
    onChange (date, dateString) {
      this.dateList = dateString
    },
    showModal (row) {
      this.thisData = row
      this.dateVisible = true
    },
    handleDownOk () {
      this.$get('/cos/pharmacy-inventory/audit', {id: this.thisAuditData.id, status: this.thisAuditData.status, remark: this.remark}).then((r) => {
        this.downVisible = false
        this.$message.success('修改成功')
        this.search()
      })
    },
    handleOk (e) {
      console.log(this.dateList[0])
      console.log(this.dateList[1])
      this.$put('/cos/pharmacy-inventory/date/put', {
        id: this.thisData.id,
        startDate: this.dateList[0],
        endDate: this.dateList[1]
      }).then((r) => {
        if (r.data.data <= 5 && r.data.data > 0) {
          this.$message.warn('该药品即将到期请尽快下架')
        }
        if (r.data.data <= 0) {
          this.$message.warn('该药品已经到期请下架')
        }
      })
      this.dateVisible = false
      this.search()
    },
    audit (id, status) {
      if (status === 2) {
        this.thisAuditData.id = id
        this.thisAuditData.status = status
        this.downVisible = true
      } else {
        this.$get('/cos/pharmacy-inventory/audit', {id, status}).then((r) => {
          this.$message.success('修改成功')
          this.search()
        })
      }
    },
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    toggleAdvanced () {
      this.advanced = !this.advanced
    },
    add () {
      this.inventoryAdd.visiable = true
    },
    handleinventoryAddClose () {
      this.inventoryAdd.visiable = false
    },
    handleinventoryAddSuccess () {
      this.inventoryAdd.visiable = false
      this.$message.success('新增库存成功')
      this.search()
    },
    edit (record) {
      this.$refs.inventoryEdit.setFormValues(record)
      this.inventoryEdit.visiable = true
    },
    handleinventoryEditClose () {
      this.inventoryEdit.visiable = false
    },
    handleinventoryEditSuccess () {
      this.inventoryEdit.visiable = false
      this.$message.success('修改库存成功')
      this.search()
    },
    handleDeptChange (value) {
      this.queryParams.deptId = value || ''
    },
    batchDelete () {
      if (!this.selectedRowKeys.length) {
        this.$message.warning('请选择需要删除的记录')
        return
      }
      let that = this
      this.$confirm({
        title: '确定删除所选中的记录?',
        content: '当您点击确定按钮后，这些记录将会被彻底删除',
        centered: true,
        onOk () {
          let ids = that.selectedRowKeys.join(',')
          that.$delete('/cos/pharmacy-inventory/' + ids).then(() => {
            that.$message.success('删除成功')
            that.selectedRowKeys = []
            that.search()
          })
        },
        onCancel () {
          that.selectedRowKeys = []
        }
      })
    },
    search () {
      let {sortedInfo, filteredInfo} = this
      let sortField, sortOrder
      // 获取当前列的排序和列的过滤规则
      if (sortedInfo) {
        sortField = sortedInfo.field
        sortOrder = sortedInfo.order
      }
      this.fetch({
        sortField: sortField,
        sortOrder: sortOrder,
        ...this.queryParams,
        ...filteredInfo
      })
    },
    reset () {
      // 取消选中
      this.selectedRowKeys = []
      // 重置分页
      this.$refs.TableInfo.pagination.current = this.pagination.defaultCurrent
      if (this.paginationInfo) {
        this.paginationInfo.current = this.pagination.defaultCurrent
        this.paginationInfo.pageSize = this.pagination.defaultPageSize
      }
      // 重置列过滤器规则
      this.filteredInfo = null
      // 重置列排序规则
      this.sortedInfo = null
      // 重置查询参数
      this.queryParams = {}
      this.fetch()
    },
    handleTableChange (pagination, filters, sorter) {
      // 将这三个参数赋值给Vue data，用于后续使用
      this.paginationInfo = pagination
      this.filteredInfo = filters
      this.sortedInfo = sorter

      this.fetch({
        sortField: sorter.field,
        sortOrder: sorter.order,
        ...this.queryParams,
        ...filters
      })
    },
    fetch (params = {}) {
      // 显示loading
      this.loading = true
      if (this.paginationInfo) {
        // 如果分页信息不为空，则设置表格当前第几页，每页条数，并设置查询分页参数
        this.$refs.TableInfo.pagination.current = this.paginationInfo.current
        this.$refs.TableInfo.pagination.pageSize = this.paginationInfo.pageSize
        params.size = this.paginationInfo.pageSize
        params.current = this.paginationInfo.current
      } else {
        // 如果分页信息为空，则设置为默认值
        params.size = this.pagination.defaultPageSize
        params.current = this.pagination.defaultCurrent
      }
      if (params.type === undefined) {
        delete params.type
      }
      this.$get('/cos/pharmacy-inventory/page', {
        ...params
      }).then((r) => {
        let data = r.data.data
        const pagination = {...this.pagination}
        pagination.total = data.total
        this.dataSource = data.records
        this.pagination = pagination
        // 数据加载完毕，关闭loading
        this.loading = false
      })
    }
  },
  watch: {}
}
</script>
<style lang="less" scoped>
@import "../../../../static/less/Common";
</style>
