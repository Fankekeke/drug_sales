<template>
  <a-modal v-model="show" title="工单详情" @cancel="onClose" :width="800">
    <template slot="footer">
      <a-button key="back" @click="onClose" type="danger">
        关闭
      </a-button>
    </template>
    <div style="font-size: 13px;font-family: SimHei" v-if="orderData !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">基础信息</span></a-col>
        <a-col :span="8"><b>工单名称：</b>
          {{ orderData.orderName }}
        </a-col>
        <a-col :span="8"><b>工单编号：</b>
          {{ orderData.orderCode }}
        </a-col>
        <a-col :span="8"><b>客户名称：</b>
          {{ orderData.userName }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>联系方式：</b>
          {{ orderData.phone }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>当前状态：</b>
          <span v-if="orderData.status == 0">正在对应</span>
          <span v-if="orderData.status == 1">已派发</span>
          <span v-if="orderData.status == 2">缴费</span>
          <span v-if="orderData.status == 3">正在维修</span>
          <span v-if="orderData.status == 4">维修完成</span>
          <span v-if="orderData.status == 5">已退换</span>
          <span v-if="orderData.status == 6">完成</span>
        </a-col>
        <a-col :span="8"><b>服务类型：</b>
          {{ orderData.serverTypeName }}
        </a-col>
        <a-col :span="8"><b>创建时间：</b>
          {{ orderData.createDate }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;" v-if="repairInfo !== null">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">维修信息</span></a-col>
        <a-col :span="8"><b>维修人员：</b>
          {{ repairInfo.staffName }}
        </a-col>
        <a-col :span="8"><b>产品名称：</b>
          {{ repairInfo.productName }}
        </a-col>
        <a-col :span="8"><b>产品类型：</b>
          <span v-if="repairInfo.productType == 1">标准件</span>
          <span v-if="repairInfo.productType == 2">工序外包</span>
          <span v-if="repairInfo.productType == 3">工序外购</span>
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;" v-if="repairInfo !== null">
        <a-col :span="8"><b>维修状态：</b>
          <span v-if="repairInfo.repairStatus == 0">待接收</span>
          <span v-if="repairInfo.repairStatus == 1">正在检测问题</span>
          <span v-if="repairInfo.repairStatus == 2">维修中</span>
          <span v-if="repairInfo.repairStatus == 3">维修完成</span>
          <span v-if="repairInfo.repairStatus == 4">异常退回</span>
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">备注信息</span></a-col>
        <a-col :span="24">
          {{ orderData.remark }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">工单图片</span></a-col>
        <a-col :span="24">
          <a-upload
            name="avatar"
            action="http://127.0.0.1:9527/file/fileUpload/"
            list-type="picture-card"
            :file-list="fileList"
            @preview="handlePreview"
            @change="picHandleChange"
          >
          </a-upload>
          <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
            <img alt="example" style="width: 100%" :src="previewImage" />
          </a-modal>
        </a-col>
      </a-row>
      <br/>
      <br/>
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
    orderData: {
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
    }
  },
  data () {
    return {
      loading: false,
      fileList: [],
      previewVisible: false,
      previewImage: '',
      repairInfo: null,
      reserveInfo: null
    }
  },
  watch: {
    orderShow: function (value) {
      if (value) {
        this.dataInit(this.orderData)
        if (this.orderData.images !== null && this.orderData.images !== '') {
          this.imagesInit(this.orderData.images)
        }
      }
    }
  },
  methods: {
    dataInit (orderData) {
      // 维修信息
      if (orderData.repairCode !== null && orderData.repairCode !== '') {
        this.$get(`/cos/repair-info/detail/${orderData.repairCode}`).then((r) => {
          this.repairInfo = r.data
        })
      }
      // 预约信息
      this.$get(`/cos/reserve-info/detail/${orderData.orderCode}`).then((r) => {
        this.reserveInfo = r.data.data
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
