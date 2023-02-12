<template>
  <a-drawer
    title="添加平台订单"
    :maskClosable="false"
    width=1150
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="orderAddShow"
    style="height: calc(100% - 55px);overflow: auto;padding-bottom: 53px;">
    <a-form :form="form" layout="vertical">
      <a-row :gutter="10">
        <a-divider orientation="left">
          <span style="font-size: 13px">选择药房</span>
        </a-divider>
        <a-col :span="12">
          <a-form-item label='药店'>
            <a-select @change="pharmacyCheck" v-decorator="[
              'pharmacyId',
              { rules: [{ required: true, message: '请输入所属药店!' }] }
              ]">
              <a-select-option :value="item.id" v-for="(item, index) in pharmacyList" :key="index">{{
                  item.name
                }}
              </a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='员工'>
            <a-select v-decorator="[
              'staffCode',
              { rules: [{ required: true, message: '请输入所属员工!' }] }
              ]">
              <a-select-option :value="item.code" v-for="(item, index) in staffList" :key="index">{{
                  item.name
                }}
              </a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="10" style="font-size: 13px;font-family: SimHei" v-if="pharmacyInfo != null">
        <a-col :span="8"><b>营业时间：</b>
          {{ pharmacyInfo.businessHours }}
        </a-col>
        <a-col :span="8"><b>药店编号：</b>
          {{ pharmacyInfo.code }}
        </a-col>
        <a-col :span="8"><b>地址：</b>
          {{ pharmacyInfo.address }}
        </a-col>
      </a-row>
      <br/>
      <a-row :gutter="10">
        <a-divider orientation="left">
          <span style="font-size: 13px">药品信息</span>
        </a-divider>
        <a-col :span="24">
          <a-table :columns="columns" :data-source="dataList" :pagination="false">
            <template slot="nameShow" slot-scope="text, record">
              <a-select style="width: 100%" @change="handleChange($event, record)">
                <a-select-option v-for="(item, index) in drugList" :key="index" :value="item.drugId">{{
                    item.drugName
                  }}
                </a-select-option>
              </a-select>
            </template>
            <template slot="brandShow" slot-scope="text, record">
              <span>{{ record.brand }}</span>
            </template>
            <template slot="typeIdShow" slot-scope="text, record">
              <span v-if="record.classification == 1">中药材</span>
              <span v-if="record.classification == 2">中药饮片</span>
              <span v-if="record.classification == 3">中西成药</span>
              <span v-if="record.classification == 4">化学原料药</span>
              <span v-if="record.classification == 5">抗生素</span>
              <span v-if="record.classification == 6">生化药品</span>
              <span v-if="record.classification == 7">放射性药品</span>
              <span v-if="record.classification == 8">血清</span>
              <span v-if="record.classification == 9">诊断药品</span>
            </template>
            <template slot="dosageFormShow" slot-scope="text, record">
              <span>{{ record.dosageForm }}</span>
            </template>
            <template slot="reserveShow" slot-scope="text, record">
              <a-input-number v-model="record.quantity" :min="1" :max="record.reserve" :step="1"/>
            </template>
            <template slot="priceShow" slot-scope="text, record">
              <span>{{ record.unitPrice }}元</span>
            </template>
          </a-table>
          <a-button @click="dataAdd" type="primary" ghost style="margin-top: 10px;width: 100%">
            新增物品
          </a-button>
        </a-col>
      </a-row>
    </a-form>
    <div class="drawer-bootom-button">
      <a-popconfirm title="确定放弃编辑？" @confirm="onClose" okText="确定" cancelText="取消">
        <a-button style="margin-right: .8rem">取消</a-button>
      </a-popconfirm>
      <a-button @click="handleSubmit" type="primary" :loading="loading">提交</a-button>
    </div>
  </a-drawer>
</template>

<script>
import baiduMap from '@/utils/map/baiduMap'
import drawerMap from '@/utils/map/searchmap/drawerMap'
import {mapState} from 'vuex'

function getBase64 (file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })
}

const formItemLayout = {
  labelCol: {span: 24},
  wrapperCol: {span: 24}
}
export default {
  name: 'inventoryAdd',
  props: {
    orderAddShow: {
      default: false
    }
  },
  components: {
    drawerMap
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.orderAddShow
      },
      set: function () {
      }
    },
    columns () {
      return [{
        title: '药品名称',
        dataIndex: 'drugName',
        scopedSlots: {customRender: 'nameShow'}
      }, {
        title: '数量',
        dataIndex: 'quantity',
        scopedSlots: {customRender: 'reserveShow'}
      }, {
        title: '所属品牌',
        dataIndex: 'brand',
        scopedSlots: {customRender: 'brandShow'}
      }, {
        title: '药品类别',
        dataIndex: 'classification',
        scopedSlots: {customRender: 'typeIdShow'}
      }, {
        title: '剂型',
        dataIndex: 'dosageForm',
        scopedSlots: {customRender: 'dosageFormShow'}
      }, {
        title: '单价',
        dataIndex: 'unitPrice',
        scopedSlots: {customRender: 'priceShow'}
      }]
    }
  },
  watch: {
    'orderAddShow': function (value) {
      if (value) {
        this.dataList = []
      }
    }
  },
  data () {
    return {
      formItemLayout,
      form: this.$form.createForm(this),
      loading: false,
      fileList: [],
      previewVisible: false,
      previewImage: '',
      localPoint: {},
      stayAddress: '',
      childrenDrawer: false,
      pharmacyList: [],
      pharmacyInfo: null,
      dataList: [],
      drugList: [],
      staffList: [],
      staffCode: ''
    }
  },
  mounted () {
    this.getPharmacy()
    this.getStaff()
  },
  methods: {
    handleChange (value, record) {
      console.log(value)
      if (value) {
        this.drugList.forEach(e => {
          if (e.drugId === value) {
            record.brand = e.brand
            record.classification = e.classification
            record.dosageForm = e.dosageForm
            record.unitPrice = e.unitPrice
            record.reserve = e.reserve
            record.drugId = e.drugId
            console.log(record)
          }
        })
      }
    },
    pharmacyCheck (value) {
      if (value) {
        this.pharmacyList.forEach(e => {
          if (e.id === value) {
            this.getDrug(e.id)
            this.pharmacyInfo = e
          }
        })
      }
    },
    dataAdd () {
      this.dataList.push({drugId: null, quantity: 1, brand: '', classification: '', dosageForm: '', unitPrice: ''})
    },
    getStaff (pharmacyId) {
      this.$get(`/cos/staff-info/list`).then((r) => {
        this.staffList = r.data.data
      })
    },
    getDrug (pharmacyId) {
      this.$get(`/cos/pharmacy-inventory/detail/pharmacy/${pharmacyId}`).then((r) => {
        this.drugList = r.data.data
      })
    },
    getPharmacy () {
      this.$get('/cos/pharmacy-info/list').then((r) => {
        this.pharmacyList = r.data.data
      })
    },
    handlerClosed (localPoint) {
      this.childrenDrawer = false
      if (localPoint !== null && localPoint !== undefined) {
        this.localPoint = localPoint
        console.log(this.localPoint)
        let address = baiduMap.getAddress(localPoint)
        address.getLocation(localPoint, (rs) => {
          if (rs != null) {
            if (rs.address !== undefined && rs.address.length !== 0) {
              this.stayAddress = rs.address
            }
            if (rs.surroundingPois !== undefined) {
              if (rs.surroundingPois.address !== undefined && rs.surroundingPois.address.length !== 0) {
                this.stayAddress = rs.surroundingPois.address
              }
            }
            let obj = {}
            obj['address'] = this.stayAddress
            obj['longitude'] = localPoint.lng
            obj['latitude'] = localPoint.lat
            this.form.setFieldsValue(obj)
          }
        })
      }
    },
    showChildrenDrawer () {
      this.childrenDrawer = true
    },
    onChildrenDrawerClos () {
      this.childrenDrawer = false
    },
    handleCancel () {
      this.previewVisible = false
    },
    async handlePreview (file) {
      if (!file.url && !file.preview) {
        file.preview = await getBase64(file.originFileObj)
      }
      this.previewImage = file.url || file.preview
      this.previewVisible = true
    },
    picHandleChange ({fileList}) {
      this.fileList = fileList
    },
    reset () {
      this.loading = false
      this.form.resetFields()
    },
    onClose () {
      this.reset()
      this.$emit('close')
    },
    handleSubmit () {
      this.form.validateFields((err, values) => {
        values.orderDetailList = JSON.stringify(this.dataList)
        if (!err) {
          this.loading = true
          this.$post('/cos/order-info/platform', {
            ...values
          }).then((r) => {
            this.reset()
            this.$emit('success')
          }).catch(() => {
            this.loading = false
          })
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
