<template>
  <a-modal v-model="show" title="订单处理" @cancel="onClose" :width="800">
    <template slot="footer">
      <a-button key="back" @click="submit" type="primary">
        发货
      </a-button>
      <a-button @click="onClose">
        关闭
      </a-button>
    </template>
    <div style="font-size: 13px;font-family: SimHei" v-if="orderAuditData !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">基础信息</span></a-col>
        <a-col :span="8"><b>工单编号：</b>
          {{ orderAuditData.code }}
        </a-col>
        <a-col :span="8"><b>客户名称：</b>
          {{ orderAuditData.userName }}
        </a-col>
        <a-col :span="8"><b>联系方式：</b>
          {{ orderAuditData.phone }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>当前状态：</b>
          <span v-if="orderAuditData.status == 0">待付款</span>
          <span v-if="orderAuditData.status == 1">已下单</span>
          <span v-if="orderAuditData.status == 2">配送中</span>
          <span v-if="orderAuditData.status == 3">已收货</span>
        </a-col>
        <a-col :span="8"><b>订单金额：</b>
          {{ orderAuditData.totalCost }} 元
        </a-col>
        <a-col :span="8"><b>下单时间：</b>
          {{ orderAuditData.createDate }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">药店信息</span></a-col>
        <a-col :span="8"><b>药店名称：</b>
            {{ orderAuditData.pharmacyName }}
          </a-col>
        <a-col :span="8"><b>药店地址：</b>
          {{ orderAuditData.address }}
        </a-col>
        <a-col :span="8"><b>联系方式：</b>
          {{ orderAuditData.pharmacyPhone }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">购买药品信息</span></a-col>
         <a-col :span="24">
          <a-table :columns="columns" :data-source="durgList">
          </a-table>
        </a-col>
      </a-row>
      <a-divider orientation="left">
        <span style="font-size: 12px;font-family: SimHei">订单发货</span>
      </a-divider>
      <a-row style="padding-left: 24px;padding-right: 24px;" :gutter="50">
        <a-col :span="24">
          <a-form-item label='物流备注' v-bind="formItemLayout">
            <a-textarea :rows="6" v-model="auditData.remark"/>
          </a-form-item>
        </a-col>
      </a-row>
    </div>
  </a-modal>
</template>

<script>
import moment from 'moment'
import {mapState} from 'vuex'
function getBase64 (file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })
}
moment.locale('zh-cn')
const formItemLayout = {
  labelCol: { span: 24 },
  wrapperCol: { span: 24 }
}
export default {
  name: 'OrderAudit',
  props: {
    orderAuditShow: {
      type: Boolean,
      default: false
    },
    orderAuditData: {
      type: Object
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.orderAuditShow
      },
      set: function () {
      }
    },
    columns () {
      return [{
        title: '药品名称',
        dataIndex: 'drugName'
      }, {
        title: '品牌',
        dataIndex: 'brand'
      }, {
        title: '数量',
        dataIndex: 'quantity'
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
        title: '单价',
        dataIndex: 'unitPrice'
      }]
    }
  },
  watch: {
    'orderAuditShow': function (value) {
      if (value) {
        this.selectOrderDetail(this.orderAuditData.id)
      }
    }
  },
  data () {
    return {
      formItemLayout,
      loading: false,
      fileList: [],
      previewVisible: false,
      previewImage: '',
      auditData: {
        remark: ''
      },
      staffList: [],
      durgList: []
    }
  },
  methods: {
    moment,
    selectOrderDetail (orderId) {
      this.$get(`/cos/order-detail/detail/${orderId}`).then((r) => {
        this.durgList = r.data.data
      })
    },
    selectStaffByProduct (productId) {
      this.$get(`/cos/staff-info/work/${productId}`).then((r) => {
        this.staffList = r.data.data
      })
    },
    onDateChange (date) {
      this.auditData.reserveDate = moment(date).format('YYYY-MM-DD')
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
    picHandleChange ({ fileList }) {
      this.fileList = fileList
    },
    imagesInit (images) {
      if (images !== null && images !== '') {
        let imageList = []
        images.split(',').forEach((image, index) => {
          imageList.push({uid: index, name: image, status: 'done', url: 'http://127.0.0.1:9527/imagesWeb/' + image})
        })
        this.fileList = imageList
      }
    },
    submit () {
      this.$get(`/cos/order-info/ship`, {
        'orderId': this.orderAuditData.id,
        'remark': this.auditData.remark
      }).then((r) => {
        this.cleanData()
        this.$emit('success')
      })
    },
    onClose () {
      this.cleanData()
      this.$emit('close')
    },
    cleanData () {
      this.auditData.remark = ''
    }
  }
}
</script>

<style scoped>

</style>
