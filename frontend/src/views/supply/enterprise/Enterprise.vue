<template>
  <a-card :bordered="false" class="card-area">
    <div :class="advanced ? 'search' : null">
      <!-- 搜索区域 -->
      <a-form layout="horizontal">
        <a-row :gutter="15">
          <div :class="advanced ? null: 'fold'">
            <a-col :md="6" :sm="24">
              <a-form-item
                label="供应商名称"
                :labelCol="{span: 4}"
                :wrapperCol="{span: 18, offset: 2}">
                <a-input v-model="queryParams.name"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-item
                label="社会编码"
                :labelCol="{span: 4}"
                :wrapperCol="{span: 18, offset: 2}">
                <a-input v-model="queryParams.creditCode"/>
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
        <a-button type="primary" ghost @click="add">添加</a-button>
        <a-button @click="batchDelete">删除</a-button>
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
        <template slot="avatarShow" slot-scope="text, record">
          <template>
            <img alt="头像" :src="'static/avatar/' + text">
          </template>
        </template>
        <template slot="contentShow" slot-scope="text, record">
          <template>
            <a-tooltip>
              <template slot="title">
                {{ record.content }}
              </template>
              {{ record.content.slice(0, 30) }} ...
            </a-tooltip>
          </template>
        </template>
        <template slot="operation" slot-scope="text, record">
          <a-icon type="cloud" @click="handleEnterpriseViewOpen(record)" title="详 情"></a-icon>
          <a-icon type="setting" theme="twoTone" twoToneColor="#4a9ff5" @click="edit(record)" title="修 改" style="margin-left: 15px"></a-icon>
        </template>
      </a-table>
    </div>
    <enterprise-add
      :enterpriseVisiable="enterpriseAdd.visiable"
      @close="enterpriseClose"
      @success="enterpriseSuccess">
    </enterprise-add>
    <enterprise-edit
      ref="enterpriseEdit"
      @close="handleEnterpriseEditClose"
      @success="handleEnterpriseEditSuccess"
      :enterpriseEditVisiable="enterpriseEdit.visiable">
    </enterprise-edit>
    <enterprise-view
      @close="handleEnterpriseViewClose"
      :enterpriseShow="enterpriseView.visiable"
      :enterpriseData="enterpriseView.data">
    </enterprise-view>
  </a-card>
</template>

<script>
import RangeDate from '@/components/datetime/RangeDate'
import {mapState} from 'vuex'
import moment from 'moment'
import EnterpriseAdd from './EnterpriseAdd.vue'
import EnterpriseEdit from './EnterpriseEdit.vue'
import EnterpriseView from './EnterpriseView.vue'
moment.locale('zh-cn')

export default {
  name: 'User',
  components: {EnterpriseAdd, EnterpriseEdit, EnterpriseView, RangeDate},
  data () {
    return {
      enterpriseAdd: {
        visiable: false
      },
      enterpriseView: {
        visiable: false,
        data: null
      },
      enterpriseEdit: {
        visiable: false
      },
      userView: {
        visiable: false,
        data: null
      },
      advanced: false,
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
        title: '供应商名称',
        dataIndex: 'name'
      }, {
        title: '单位简称',
        dataIndex: 'abbreviation',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        }
      }, {
        title: '统一社会信用代码',
        dataIndex: 'creditCode',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        }
      }, {
        title: '单位性质',
        dataIndex: 'nature',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        }
      }, {
        title: '经营状态',
        dataIndex: 'status',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        }
      }, {
        title: '法定代表人',
        dataIndex: 'corporateRepresentative',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        }
      }, {
        title: '注册资本（万元）',
        dataIndex: 'registeredCapital',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        }
      }, {
        title: '成立日期',
        dataIndex: 'establishmentDate',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        }
      }, {
        title: '操作',
        dataIndex: 'operation',
        scopedSlots: {customRender: 'operation'}
      }]
    }
  },
  mounted () {
    this.fetch()
  },
  methods: {
    handleEnterpriseViewOpen (record) {
      this.enterpriseView.data = record
      this.enterpriseView.visiable = true
    },
    handleEnterpriseViewClose () {
      this.enterpriseView.visiable = false
    },
    handleEnterpriseEditClose () {
      this.enterpriseEdit.visiable = false
    },
    handleEnterpriseEditSuccess () {
      this.enterpriseEdit.visiable = false
      this.$message.success('修改成功')
      this.fetch()
    },
    enterpriseClose () {
      this.enterpriseAdd.visiable = false
    },
    enterpriseSuccess () {
      this.enterpriseAdd.visiable = false
      this.$message.success('导入成功')
      this.fetch()
    },
    add () {
      this.enterpriseAdd.visiable = true
    },
    edit (record) {
      this.$refs.enterpriseEdit.setFormValues(record)
      this.enterpriseEdit.visiable = true
    },
    view (row) {
      this.userView.data = row
      this.userView.visiable = true
    },
    handleUserViewClose () {
      this.userView.visiable = false
    },
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    toggleAdvanced () {
      this.advanced = !this.advanced
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
          that.$delete('/cos/enterprise-info/' + ids).then(() => {
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
      if (params.readStatus === undefined) {
        delete params.readStatus
      }
      this.$get('/cos/enterprise-info/page', {
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
