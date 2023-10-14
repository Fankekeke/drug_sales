<template>
  <a-modal v-model="show" title="采购详情" @cancel="onClose" :width="1200">
    <template slot="footer">
      <a-button key="back" @click="onClose" type="danger">
        关闭
      </a-button>
    </template>
    <div style="font-size: 13px;font-family: SimHei" v-if="purchaseData !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">基础信息</span></a-col>
        <a-col :span="8"><b>采购单号：</b>
          {{ purchaseData.code }}
        </a-col>
        <a-col :span="8"><b>采购数量：</b>
          {{ purchaseData.amount ? purchaseData.amount : '- -' }}
        </a-col>
        <a-col :span="8"><b>采购人：</b>
          {{ purchaseData.purchaser ? purchaseData.purchaser : '- -' }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>当前状态：</b>
          <span v-if="purchaseData.status == 1">运输中</span>
          <span v-if="purchaseData.status == 2">已验收</span>
        </a-col>
        <a-col :span="8"><b>采购金额：</b>
          {{ purchaseData.totalPrice }} 元
        </a-col>
        <a-col :span="8"><b>采购时间：</b>
          {{ purchaseData.createDate }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">供应商信息</span></a-col>
        <a-col :span="8"><b>供应商名称：</b>
            {{ purchaseData.name }}
          </a-col>
        <a-col :span="8"><b>统一社会信用代码：</b>
          {{ purchaseData.creditCode }}
        </a-col>
        <a-col :span="8"><b>所属行业：</b>
          {{ purchaseData.industry }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">采购药品信息</span></a-col>
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
  name: 'purchaseView',
  props: {
    purchaseShow: {
      type: Boolean,
      default: false
    },
    purchaseData: {
      type: Object
    }
  },
  computed: {
    show: {
      get: function () {
        return this.purchaseShow
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
        dataIndex: 'reserve'
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
      current: 0
    }
  },
  watch: {
    purchaseShow: function (value) {
      if (value) {
        this.dataInit(this.purchaseData.id)
      }
    }
  },
  methods: {
    dataInit (purchaseId) {
      // 药品信息
      this.$get(`/cos/purchase-info/detail/${purchaseId}`).then((r) => {
        this.durgList = r.data.drug
        this.logisticsList = r.data.logistics
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
