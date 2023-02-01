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
                <a-input v-model="queryParams.name"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-item
                label="药品编号"
                :labelCol="{span: 5}"
                :wrapperCol="{span: 18, offset: 1}">
                <a-input v-model="queryParams.code"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-item
                label="品牌"
                :labelCol="{span: 5}"
                :wrapperCol="{span: 18, offset: 1}">
                <a-input v-model="queryParams.brand"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-item
                label="所属分类"
                :labelCol="{span: 5}"
                :wrapperCol="{span: 18, offset: 1}">
                <a-select v-model="queryParams.category" allowClear>
                  <a-select-option value="1">可卡因</a-select-option>
                  <a-select-option value="2">维生素制剂</a-select-option>
                  <a-select-option value="3">鱼肝油</a-select-option>
                  <a-select-option value="4">药物饮料</a-select-option>
                  <a-select-option value="5">膳食纤维</a-select-option>
                </a-select>
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
            <a-tooltip>
              <template slot="title">
                {{ record.title }}
              </template>
              {{ record.title.slice(0, 8) }} ...
            </a-tooltip>
          </template>
        </template>
        <template slot="operation" slot-scope="text, record">
          <a-icon type="cloud" @click="handleDrugViewOpen(record)" title="详 情"></a-icon>
          <a-icon type="setting" theme="twoTone" twoToneColor="#4a9ff5" @click="edit(record)" title="修 改" style="margin-left: 15px"></a-icon>
        </template>
      </a-table>
    </div>
    <drug-add
      v-if="drugAdd.visiable"
      @close="handledrugAddClose"
      @success="handledrugAddSuccess"
      :drugAddVisiable="drugAdd.visiable">
    </drug-add>
    <drug-edit
      ref="drugEdit"
      @close="handledrugEditClose"
      @success="handledrugEditSuccess"
      :drugEditVisiable="drugEdit.visiable">
    </drug-edit>
    <drug-view
      @close="handleDrugViewClose"
      :drugShow="drugView.visiable"
      :drugData="drugView.data">
    </drug-view>
  </a-card>
</template>

<script>
import RangeDate from '@/components/datetime/RangeDate'
import drugAdd from './DrugAdd'
import drugEdit from './DrugEdit'
import drugView from './DrugView'
import {mapState} from 'vuex'
import moment from 'moment'
moment.locale('zh-cn')

export default {
  name: 'drug',
  components: {drugAdd, drugEdit, drugView, RangeDate},
  data () {
    return {
      advanced: false,
      drugAdd: {
        visiable: false
      },
      drugEdit: {
        visiable: false
      },
      drugView: {
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
        title: '药品编号',
        dataIndex: 'code'
      }, {
        title: '药品名称',
        dataIndex: 'name'
      }, {
        title: '所属品牌',
        dataIndex: 'brand'
      }, {
        title: '药品类别',
        dataIndex: 'classification',
        customRender: (text, row, index) => {
          switch (text) {
            case 1:
              return <a-tag>中药材</a-tag>
            case 2:
              return <a-tag>中药饮片</a-tag>
            case 3:
              return <a-tag>中西成药</a-tag>
            case 4:
              return <a-tag>化学原料药</a-tag>
            case 5:
              return <a-tag>抗生素</a-tag>
            case 6:
              return <a-tag>生化药品</a-tag>
            case 7:
              return <a-tag>放射性药品</a-tag>
            case 8:
              return <a-tag>血清</a-tag>
            case 9:
              return <a-tag>诊断药品</a-tag>
            default:
              return '- -'
          }
        }
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
      }, {
        title: '通用名',
        dataIndex: 'commonName',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        }
      }, {
        title: '剂型',
        dataIndex: 'dosageForm',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        }
      }, {
        title: '用法',
        dataIndex: 'usages',
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
    handleDrugViewOpen (row) {
      this.drugView.data = row
      this.drugView.visiable = true
    },
    handleDrugViewClose () {
      this.drugView.visiable = false
    },
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    toggleAdvanced () {
      this.advanced = !this.advanced
    },
    add () {
      this.drugAdd.visiable = true
    },
    handledrugAddClose () {
      this.drugAdd.visiable = false
    },
    handledrugAddSuccess () {
      this.drugAdd.visiable = false
      this.$message.success('新增药品成功')
      this.search()
    },
    edit (record) {
      this.$refs.drugEdit.setFormValues(record)
      this.drugEdit.visiable = true
    },
    handledrugEditClose () {
      this.drugEdit.visiable = false
    },
    handledrugEditSuccess () {
      this.drugEdit.visiable = false
      this.$message.success('修改药品成功')
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
          that.$delete('/cos/drug-info/' + ids).then(() => {
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
      this.$get('/cos/drug-info/page', {
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
