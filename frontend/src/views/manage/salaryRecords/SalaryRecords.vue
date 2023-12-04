<template>
  <a-card :bordered="false" class="card-area">
    <div :class="advanced ? 'search' : null">
      <!-- 搜索区域 -->
      <a-form layout="horizontal">
        <a-row :gutter="15">
          <div :class="advanced ? null: 'fold'">
            <a-col :md="6" :sm="24">
              <a-form-item
                label="员工姓名"
                :labelCol="{span: 4}"
                :wrapperCol="{span: 18, offset: 2}">
                <a-input v-model="queryParams.staffName"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-item
                label="所属年份"
                :labelCol="{span: 4}"
                :wrapperCol="{span: 18, offset: 2}">
                <a-select v-model="queryParams.year" allowClear>
                  <a-select-option value="2019">2019</a-select-option>
                  <a-select-option value="2020">2020</a-select-option>
                  <a-select-option value="2021">2021</a-select-option>
                  <a-select-option value="2022">2022</a-select-option>
                  <a-select-option value="2023">2023</a-select-option>
                  <a-select-option value="2024">2024</a-select-option>
                  <a-select-option value="2025">2025</a-select-option>
                  <a-select-option value="2026">2026</a-select-option>
                  <a-select-option value="2027">2027</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-item
                label="所属月份"
                :labelCol="{span: 4}"
                :wrapperCol="{span: 18, offset: 2}">
                <a-select v-model="queryParams.month" allowClear>
                  <a-select-option value="1">1月</a-select-option>
                  <a-select-option value="2">2月</a-select-option>
                  <a-select-option value="3">3月</a-select-option>
                  <a-select-option value="4">4月</a-select-option>
                  <a-select-option value="5">5月</a-select-option>
                  <a-select-option value="6">6月</a-select-option>
                  <a-select-option value="7">7月</a-select-option>
                  <a-select-option value="8">8月</a-select-option>
                  <a-select-option value="9">9月</a-select-option>
                  <a-select-option value="10">10月</a-select-option>
                  <a-select-option value="11">11月</a-select-option>
                  <a-select-option value="12">12月</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </div>
          <span style="float: right; margin-top: 3px;">
            <a-button type="primary" @click="search">查询</a-button>
            <a-button style="margin-left: 8px" @click="reset">重置</a-button>
            <a-button style="margin-left: 8px" @click="exportCheck">导出</a-button>
          </span>
        </a-row>
      </a-form>
    </div>
    <div>
      <div class="operator">
        <a-button type="primary" ghost @click="add">新增</a-button>
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
        <template slot="titleShow" slot-scope="text, record">
          <template>
            <a-badge status="processing"/>
            <a-tooltip>
              <template slot="title">
                {{ record.title }}
              </template>
              {{ record.title.slice(0, 8) }} ...
            </a-tooltip>
          </template>
        </template>
        <template slot="contentShow" slot-scope="text, record">
          <template>
            <a-tooltip>
              <template slot="title">
                {{ record.remark }}
              </template>
              {{ record.remark.slice(0, 30) }} ...
            </a-tooltip>
          </template>
        </template>
        <template slot="operation" slot-scope="text, record">
          <a-icon type="bulb" theme="twoTone" twoToneColor="#4a9ff5" @click="view(record)" title="详 情" style="margin-right: 15px"></a-icon>
          <a-icon type="setting" theme="twoTone" twoToneColor="#4a9ff5" @click="edit(record)" title="修 改"></a-icon>
        </template>
      </a-table>
    </div>
    <salaryRecords-add
      v-if="salaryRecordsAdd.visiable"
      @close="handlesalaryRecordsAddClose"
      @success="handlesalaryRecordsAddSuccess"
      :salaryRecordsAddVisiable="salaryRecordsAdd.visiable">
    </salaryRecords-add>
    <salaryRecords-edit
      ref="salaryRecordsEdit"
      @close="handlesalaryRecordsEditClose"
      @success="handlesalaryRecordsEditSuccess"
      :salaryRecordsEditVisiable="salaryRecordsEdit.visiable">
    </salaryRecords-edit>
    <salaryRecords-view
      @close="handlesalaryRecordsViewClose"
      :salaryRecordsShow="salaryRecordsView.visiable"
      :salaryRecordsData="salaryRecordsView.data">
    </salaryRecords-view>
  </a-card>
</template>

<script>
import RangeDate from '@/components/datetime/RangeDate'
import salaryRecordsAdd from './SalaryRecordsAdd'
import salaryRecordsEdit from './SalaryRecordsEdit'
import {mapState} from 'vuex'
import moment from 'moment'
import salaryRecordsView from './SalaryRecordsView'
import { newSpread, floatForm, floatReset, saveExcel } from '@/utils/spreadJS'
moment.locale('zh-cn')

export default {
  name: 'salaryRecords',
  components: {salaryRecordsView, salaryRecordsAdd, salaryRecordsEdit, RangeDate},
  data () {
    return {
      advanced: false,
      salaryRecordsAdd: {
        visiable: false
      },
      salaryRecordsEdit: {
        visiable: false
      },
      salaryRecordsView: {
        visiable: false,
        data: null
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
      }
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    columns () {
      return [{
        title: '员工编号',
        dataIndex: 'staffCode'
      }, {
        title: '员工姓名',
        dataIndex: 'staffName'
      }, {
        title: '照片',
        dataIndex: 'images',
        customRender: (text, record, index) => {
          if (!record.images) return <a-avatar shape="square" icon="user" />
          return <a-popover>
            <template slot="content">
              <a-avatar shape="square" size={132} icon="user" src={ 'http://127.0.0.1:9527/imagesWeb/' + record.images } />
            </template>
            <a-avatar shape="square" icon="user" src={ 'http://127.0.0.1:9527/imagesWeb/' + record.images } />
          </a-popover>
        }
      }, {
        title: '职位',
        dataIndex: 'position',
        customRender: (text, row, index) => {
          switch (text) {
            case '1':
              return <a-tag>管理</a-tag>
            case '2':
              return <a-tag>药师</a-tag>
            case '3':
              return <a-tag>普通员工</a-tag>
            default:
              return '- -'
          }
        }
      }, {
        title: '发放时间',
        dataIndex: 'year',
        customRender: (text, row, index) => {
          return row.year + '年' + row.month + '月'
        }
      }, {
        title: '实发工资',
        dataIndex: 'payroll',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        }
      }, {
        title: '备注',
        dataIndex: 'remark',
        scopedSlots: {customRender: 'contentShow'}
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
    exportCheck () {
      this.$message.loading('正在生成', 0)
      this.$get('/cos/salary-records/export', { ...this.queryParams }).then((r) => {
        let newData = []
        r.data.data.forEach((item, index) => {
          newData.push([item.staffCode, item.staffName !== null ? item.staffName : '- -', item.year !== null ? item.year : '- -', item.month !== null ? item.month : '- -', item.basicWage !== null ? item.basicWage : '- -', item.postAllowance !== null ? item.postAllowance : '- -', item.performanceBonus !== null ? item.performanceBonus : '- -', item.overtimePay !== null ? item.overtimePay : '- -', item.holidayCosts !== null ? item.holidayCosts : '- -', item.pension !== null ? item.pension : '- -', item.unemployment !== null ? item.unemployment : '- -', item.medicalInsurance !== null ? item.medicalInsurance : '- -', item.tax !== null ? item.tax : '- -', item.housingFund !== null ? item.housingFund : '- -', item.payroll !== null ? item.payroll : '- -'])
        })
        let spread = newSpread('staff')
        spread = floatForm(spread, 'staff', newData)
        saveExcel(spread, '工资报表.xlsx')
        floatReset(spread, 'staff', newData.length)
        this.$message.destroy()
      })
    },
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    toggleAdvanced () {
      this.advanced = !this.advanced
    },
    view (record) {
      this.salaryRecordsView.visiable = true
      this.salaryRecordsView.data = record
    },
    add () {
      this.salaryRecordsAdd.visiable = true
    },
    handlesalaryRecordsAddClose () {
      this.salaryRecordsAdd.visiable = false
    },
    handlesalaryRecordsAddSuccess () {
      this.salaryRecordsAdd.visiable = false
      this.$message.success('新增成功')
      this.search()
    },
    edit (record) {
      this.$refs.salaryRecordsEdit.setFormValues(record)
      this.salaryRecordsEdit.visiable = true
    },
    handlesalaryRecordsEditClose () {
      this.salaryRecordsEdit.visiable = false
    },
    handlesalaryRecordsEditSuccess () {
      this.salaryRecordsEdit.visiable = false
      this.$message.success('修改成功')
      this.search()
    },
    handlesalaryRecordsViewClose () {
      this.salaryRecordsView.visiable = false
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
          that.$delete('/cos/salary-records/' + ids).then(() => {
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
      if (params.year === undefined) {
        delete params.year
      }
      if (params.month === undefined) {
        delete params.month
      }
      this.$get('/cos/salary-records/page', {
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
