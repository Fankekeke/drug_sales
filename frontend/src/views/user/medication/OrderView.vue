<template>
  <a-modal v-model="show" title="订单详情" @cancel="onClose" :width="1000">
    <template slot="footer">
      <a-button key="back" @click="onClose" type="danger">
        关闭
      </a-button>
    </template>
    <div style="font-size: 13px;font-family: SimHei" v-if="orderData !== null">
      <div style="padding-left: 24px;padding-right: 24px;margin-bottom: 50px;margin-top: 50px">
        <a-steps :current="current" progress-dot size="small">
          <a-step title="待付款" />
          <a-step title="已下单" />
          <a-step title="配送中" />
          <a-step title="已收货" />
        </a-steps>
      </div>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">基础信息</span></a-col>
        <a-col :span="8"><b>工单编号：</b>
          {{ orderData.code }}
        </a-col>
        <a-col :span="8"><b>客户名称：</b>
          {{ orderData.name ? orderData.name : '- -' }}
        </a-col>
        <a-col :span="8"><b>联系方式：</b>
          {{ orderData.phone ? orderData.phone : '- -' }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">处方信息</span></a-col>
        <a-col :span="8"><b>处方单号：</b>
          {{ medicationData.code }}
        </a-col>
        <a-col :span="8"><b>病因：</b>
          {{ medicationData.cause ? medicationData.cause : '- -' }}
        </a-col>
        <a-col :span="8"><b>出具人：</b>
          {{ medicationData.checkIssuer ? medicationData.checkIssuer : '- -' }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">处方信息</span></a-col>
        <a-col :span="8"><b>发布时间：</b>
          {{ medicationData.createDate }}
        </a-col>
        <a-col :span="8"><b>出具机构：</b>
          {{ medicationData.checkAgency ? medicationData.checkAgency : '- -' }}
        </a-col>
        <a-col :span="8"><b>出具人：</b>
          <span v-if="medicationData.status == 0">未处理</span>
          <span v-if="medicationData.status == 1">已处理</span>
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>当前状态：</b>
          <span v-if="orderData.orderStatus == 0">待付款</span>
          <span v-if="orderData.orderStatus == 1">已下单</span>
          <span v-if="orderData.orderStatus == 2">配送中</span>
          <span v-if="orderData.orderStatus == 3">已收货</span>
        </a-col>
        <a-col :span="8"><b>订单金额：</b>
          {{ orderData.totalCost }} 元
        </a-col>
        <a-col :span="8"><b>下单时间：</b>
          {{ orderData.createDate }}
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
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">当前物流</span></a-col>
         <a-col :span="24">
          <a-table :columns="logisticsColumns" :data-source="logisticsList">
          </a-table>
        </a-col>
      </a-row>
    </div>
  </a-modal>
</template>

<script>
import moment from 'moment'
moment.locale('zh-cn')
function getBase64 (file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })
}
export default {
  name: 'orderView',
  props: {
    orderShow: {
      type: Boolean,
      default: false
    },
    medicationData: {
      type: Object
    }
  },
  computed: {
    show: {
      get: function () {
        return this.orderShow
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
    },
    logisticsColumns () {
      return [{
        title: '物流信息',
        dataIndex: 'remark'
      }, {
        title: '操作时间',
        dataIndex: 'createDate'
      }]
    }
  },
  data () {
    return {
      loading: false,
      fileList: [],
      previewVisible: false,
      previewImage: '',
      repairInfo: null,
      reserveInfo: null,
      durgList: [],
      logisticsList: [],
      current: 0,
      orderData: null
    }
  },
  watch: {
    orderShow: function (value) {
      if (value) {
        this.dataInit(this.medicationData.orderCode)
      }
    }
  },
  methods: {
    dataInit (orderId) {
      // 药品信息
      this.$get(`/cos/order-info/detail/${orderId}`).then((r) => {
        this.orderData = r.data.data
        this.current = this.orderData.orderStatus
        // 药品信息
        this.$get(`/cos/order-detail/detail/${this.orderData.id}`).then((r) => {
          this.durgList = r.data.data
        })
        // 物流信息
        this.$get(`/cos/logistics-info/order/${this.orderData.id}`).then((r) => {
          this.logisticsList = r.data.data
        })
      })
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
    onClose () {
      this.$emit('close')
    }
  }
}
</script>

<style scoped>

</style>
